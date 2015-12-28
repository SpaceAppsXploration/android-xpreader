/*
 * Copyright 2014-2015 Project Chronos and Pramantha Ltd
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package uk.projectchronos.xplorationreader.activites;

import android.content.ComponentName;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.customtabs.CustomTabsCallback;
import android.support.customtabs.CustomTabsClient;
import android.support.customtabs.CustomTabsIntent;
import android.support.customtabs.CustomTabsService;
import android.support.customtabs.CustomTabsServiceConnection;
import android.support.customtabs.CustomTabsSession;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import uk.projectchronos.xplorationreader.BuildConfig;
import uk.projectchronos.xplorationreader.R;
import uk.projectchronos.xplorationreader.adapters.SectionsPagerAdapter;
import uk.projectchronos.xplorationreader.api.ProjectChronosService;
import uk.projectchronos.xplorationreader.dialogs.SectionPreferenceDialog;
import uk.projectchronos.xplorationreader.fragments.ArticleListFragment;
import uk.projectchronos.xplorationreader.interfaces.OnDialogClosedListener;
import uk.projectchronos.xplorationreader.models.Article;
import uk.projectchronos.xplorationreader.utils.BaseActivity;
import uk.projectchronos.xplorationreader.utils.ConnectionHelper;
import uk.projectchronos.xplorationreader.utils.ConnectionReceiver;
import uk.projectchronos.xplorationreader.utils.CustomTabsHelper;

public class MainActivity extends BaseActivity implements ConnectionHelper.Callbacks, TabLayout.OnTabSelectedListener, OnDialogClosedListener {

    /**
     * Base url for API service.
     */
    private static final String BASE_URL = "http://semantics.projectchronos.eu/";

    /**
     * List of feed Articles.
     */
    public static List<Article> feedList = new ArrayList<>();

    /**
     * List of movie Articles.
     */
    public static List<Article> movieList = new ArrayList<>();

    /**
     * List of media Articles.
     */
    public static List<Article> mediaList = new ArrayList<>();

    /**
     * List of link Articles.
     */
    public static List<Article> linkList = new ArrayList<>();

    /**
     * List of tweet Articles.
     */
    public static List<Article> tweetList = new ArrayList<>();

    /**
     * List of fb Articles.
     */
    public static List<Article> fbPostList = new ArrayList<>();

    /**
     * List of paper Articles.
     */
    public static List<Article> paperList = new ArrayList<>();

    /**
     * List of pdf Articles.
     */
    public static List<Article> pdfList = new ArrayList<>();

    /**
     * Custom Tabs Session.
     */
    private static CustomTabsSession customTabsSession;

    /**
     * Custom Tabs Client.
     */
    private static CustomTabsClient customTabsClient;

    /**
     * Rootview of activity.
     */
    @Bind(R.id.main_content)
    protected View rootView;

    /**
     * Toolbar.
     */
    @Bind(R.id.toolbar)
    protected Toolbar toolbar;

    /**
     * ViewPager that will host the section contents.
     */
    @Bind(R.id.container)
    protected ViewPager viewPager;

    /**
     * Tab layout that allow navigation throw sections.
     */
    @Bind(R.id.tabs)
    protected TabLayout tabLayout;

    /**
     * SectionsPagerAdapter.
     */
    private SectionsPagerAdapter sectionsPagerAdapter;

    /**
     * Retrofit service of the app.
     */
    private ProjectChronosService projectChronosService;

    /**
     * Custom Tabs Service Connection.
     */
    private CustomTabsServiceConnection customTabsServiceConnection;

    /**
     * Snackbar for connectivity errors.
     */
    private Snackbar connectionSnackbar;

    /**
     * Brodcast receiver for connectivity change.
     */
    private ConnectionReceiver connectionReceiver;

    /**
     * Gets the actual CustomTabsSession.
     * If one is ready it returns it, otherwise it creates one with a navigation event callback.
     *
     * @return null in case of there is not a CustomTabsClient, customTabsSession oterwhise.
     */
    private CustomTabsSession getSession() {
        if (customTabsClient == null) {
            customTabsSession = null;
        } else if (customTabsSession == null) {
            customTabsSession = customTabsClient.newSession(new CustomTabsCallback() {
                @Override
                public void onNavigationEvent(int navigationEvent, Bundle extras) {
                    switch (navigationEvent) {
                        case 1:
                            if (BuildConfig.DEBUG) Log.i(TAG, "Navigation started");
                            break;
                        case 2:
                            if (BuildConfig.DEBUG) Log.i(TAG, "Navigation finished");
                            // TODO: maybe we can set article read after this event
                            break;
                        case 3:
                            if (BuildConfig.DEBUG) Log.i(TAG, "Navigation failed");
                            break;
                        case 4:
                            if (BuildConfig.DEBUG) Log.i(TAG, "Navigation aborted");
                            break;
                        case 5:
                            if (BuildConfig.DEBUG) Log.i(TAG, "Tab shown");
                            break;
                        case 6:
                            if (BuildConfig.DEBUG) Log.i(TAG, "Tab hidden");
                            break;
                        default:
                            break;
                    }
                }
            });
        }

        return customTabsSession;
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Make sure this is before calling super.onCreate in order to set the right theme and remove the launcher theme
        setTheme(R.style.AppThemeNoActionBar);

        super.onCreate(savedInstanceState);

        // Binds views
        ButterKnife.bind(this);

        // Prepares Snackbar for error
        prepareSnackBar();

        // Prepares custom tab
        prepareCustomTab();

        // Sets action bar
        setSupportActionBar(toolbar);

        // Creates service
        getProjectChronosService();

        // Creates BroadcastReceiver for connectivity checking
        connectionReceiver = new ConnectionReceiver();

        // Creates the adapter that will return a fragment for each sections of the activity
        sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());

        // Sets tab mode
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        // Sets up the ViewPager with the sections adapter and overrides OnTabSelectedListener
        viewPager.setAdapter(sectionsPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setOnTabSelectedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflates the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            // Opens search pop-up
            case R.id.menu_search:
                // TODO: see #10
                return true;

            // Shows add section dialog
            case R.id.menu_add_section:
                SectionPreferenceDialog dialog = new SectionPreferenceDialog();
                dialog.setOnDialogClosedListener(this);
                dialog.show(getSupportFragmentManager(), "SectionPreferenceDialog");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Starts connectivity checker
        connectionReceiver.register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Stops connectivity checker
        connectionReceiver.unRegister(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Releases custom tab service
        unBindCustomTabsService();
    }

    /**
     * Prepares snackbar with style and callback.
     */
    private void prepareSnackBar() {
        // Creates snackbar
        connectionSnackbar = Snackbar.make(rootView, R.string.app_name, Snackbar.LENGTH_INDEFINITE);
        // Sets callback for dismiss event
        connectionSnackbar.setCallback(new Snackbar.Callback() {
            @Override
            public void onDismissed(Snackbar snackbar, int event) {
                super.onDismissed(snackbar, event);

                //getArticles(next);  //TODO manage in better way otherwise it will get articles that are not requested yet
            }
        });

        // Decorates snackbar
        View snackBarView = connectionSnackbar.getView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            snackBarView.setBackgroundColor(getColor(R.color.primary));
        } else {
            //noinspection deprecation
            snackBarView.setBackgroundColor(getResources().getColor(R.color.primary));
        }
    }

    @Override
    public void onConnectionChanged(boolean isConnected, boolean isOnline) {
        Log.i(TAG, String.format("Connection changed, now is connected %s and is online %s", isConnected, isOnline));

        if (!isConnected) {
            if (!connectionSnackbar.isShown()) {
                // Shows no connection error
                connectionSnackbar.setText(R.string.connection_error).show();
            }
        } else if (!isOnline) {
            if (!connectionSnackbar.isShown()) {
                // Shows captive error
                connectionSnackbar.setText(R.string.captive_error).show();
            }
        } else {
            if (connectionSnackbar.isShown()) {
                // Dismisses snackbar
                connectionSnackbar.dismiss();

                // Prepares for the future
                prepareSnackBar();
            }
        }
    }

    /**
     * Creates Retrofit's ProjectChronosService.
     */
    private void createProjectChronosService() {
        // Set data format
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .create();

        // Create Retrofit service with our KeywordAdapter
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        projectChronosService = retrofit.create(ProjectChronosService.class);
    }

    /**
     * Singleton for Project Chronos Service.
     *
     * @return the projectChronosService instance.
     */
    public ProjectChronosService getProjectChronosService() {
        if (projectChronosService == null) {
            createProjectChronosService();
        }

        return projectChronosService;
    }

    /**
     * Allocates resources for CustomTabs.
     */
    private void bindCustomTabsService() {
        if (customTabsClient != null) {
            return;
        }

        // Gets package name to use in binding
        String packageNameToBind = CustomTabsHelper.getPackageNameToUse(this);
        if (packageNameToBind == null) {
            return;
        }

        // Creates new CustomTabsService
        customTabsServiceConnection = new CustomTabsServiceConnection() {
            @Override
            public void onCustomTabsServiceConnected(ComponentName name, CustomTabsClient client) {
                if (BuildConfig.DEBUG) Log.i(TAG, "Custom Tabs Service connected!");
                customTabsClient = client;
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                if (BuildConfig.DEBUG) Log.i(TAG, "Custom Tabs Service disconnected!");
                customTabsClient = null;
            }
        };

        // Binds it!
        if (!CustomTabsClient.bindCustomTabsService(this, packageNameToBind, customTabsServiceConnection)) {
            customTabsServiceConnection = null;
        }
    }

    /**
     * Releases all resources used for CustomTabs.
     */
    private void unBindCustomTabsService() {
        if (customTabsServiceConnection == null) {
            return;
        }

        unbindService(customTabsServiceConnection);
        customTabsClient = null;
        customTabsSession = null;
    }

    /**
     * Sets all UI of our CustomTabs and after it launches the page of the requested URI.
     *
     * @param uri the URI to open into our CustomTabs.
     */
    public void launchCustomTabs(Uri uri) {
        CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder(getSession())
                .setToolbarColor(ContextCompat.getColor(this, R.color.primary))
                .setCloseButtonIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_arrow_back))
                .setStartAnimations(this, R.anim.slide_in_right, R.anim.slide_out_left)
                .setExitAnimations(this, R.anim.slide_in_left, R.anim.slide_out_right)
                .setShowTitle(true)
                .build();

        CustomTabsHelper.addKeepAliveExtra(this, customTabsIntent.intent);
        customTabsIntent.launchUrl(this, uri);
    }

    /**
     * Prepares all the stuffs in order to use an optimized CustomTabs.
     */
    private void prepareCustomTab() {
        // Binds to the custom tab service
        bindCustomTabsService();

        // Warmup the browser process
        if (customTabsClient != null) {
            customTabsClient.warmup(0);
        }
    }

    /**
     * Prefetches contents of articles shown in the list.
     *
     * @param articleList list of article to prefetch.
     */
    public void prefetchArticles(List<Article> articleList) {
        // Gets actual session and prefetch some contents
        customTabsSession = getSession();
        if (customTabsClient != null) {

            Uri firstUri = Uri.parse("http://www.projectchronos.eu/"); // Only inizialization
            List<Bundle> bundleList = new ArrayList<>();
            // Gets all article url
            for (Article article : articleList) {

                String url = article.getUrl();
                if (bundleList.size() == 0) {
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(CustomTabsService.KEY_URL, Uri.parse(url));
                    bundleList.add(bundle);
                } else {
                    firstUri = Uri.parse(url);
                }
            }
            // Tells to the browser of a likely future navigation to a URL.
            customTabsSession.mayLaunchUrl(firstUri, null, bundleList);
        }
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        // Same as default for TabLayout
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
        // No-op
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
        // Scrolls to top
        ((ArticleListFragment) sectionsPagerAdapter.getCurrentFragment()).scrollToTop();
    }

    @Override
    public void onDialogClosed(boolean positiveResult) {
        if (positiveResult) {
            // Recreates the adapter in order to have the new secction
            sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());

            // Resets up the ViewPager with the new sections adapter and reoverrides OnTabSelectedListener
            viewPager.setAdapter(sectionsPagerAdapter);
            tabLayout.setupWithViewPager(viewPager);
            tabLayout.setOnTabSelectedListener(this);

            // Goes to the newly created section
            viewPager.setCurrentItem(sectionsPagerAdapter.getCount());
        }
    }
}