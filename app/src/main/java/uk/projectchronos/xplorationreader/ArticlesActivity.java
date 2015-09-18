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

package uk.projectchronos.xplorationreader;

import android.content.ComponentName;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.customtabs.CustomTabsCallback;
import android.support.customtabs.CustomTabsClient;
import android.support.customtabs.CustomTabsIntent;
import android.support.customtabs.CustomTabsServiceConnection;
import android.support.customtabs.CustomTabsSession;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;
import uk.projectchronos.xplorationreader.api.KeywordAdapterFactory;
import uk.projectchronos.xplorationreader.api.ProjectChronosService;
import uk.projectchronos.xplorationreader.model.Article;
import uk.projectchronos.xplorationreader.model.Keyword;
import uk.projectchronos.xplorationreader.model.ResponseArticlesList;
import uk.projectchronos.xplorationreader.utils.BaseActivityWithToolbar;
import uk.projectchronos.xplorationreader.utils.CustomTabsHelper;
import uk.projectchronos.xplorationreader.utils.HTTPUtil;

/**
 * Main Activty with articles list.
 */
public class ArticlesActivity extends BaseActivityWithToolbar {

    /**
     * Base url for API service.
     */
    private static final String BASE_URL = "http://hypermedia.projectchronos.eu/";

    /**
     * List of articles.
     */
    private List<Article> articleList = new ArrayList<>();

    /**
     * Last next page url.
     */
    private String next;

    /**
     * Custom Tabs Session.
     */
    private CustomTabsSession customTabsSession;

    /**
     * Custom Tabs Client.
     */
    private CustomTabsClient customTabsClient;

    /**
     * Custom Tabs Service Connection.
     */
    private CustomTabsServiceConnection customTabsServiceConnection;

    /**
     * ProjectChronosService that allow to access to articles and keywords API.
     */
    private ProjectChronosService projectChronosService;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_articles;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Make sure this is before calling super.onCreate in order to set the right theme and remove the launcher theme
        setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);

        // Disable default home as up
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setTitle(R.string.title_activity_articles);

        // Create service
        createProjectChronosService();

        // Get articles
        getArticles(null);

        // Prepare custom tab
        prepareCustomTab();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_articles, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        // TODO: remove after
        switch (id) {

            case R.id.action_settings:
                return true;

            case R.id.action_try_tab:
                Uri uri = Uri.parse("http://www.esa.int/Our_Activities/Launchers/Launcher_Technology/Materials_structure_and_stages");
                launchCustomTabs(uri);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onDestroy() {
        // Release service
        unbindCustomTabsService();
        super.onDestroy();
    }

    /**
     *
     */
    private void createProjectChronosService() {
        // Create new Gson throw GsonBuilder in order to extend it and parse a String[] of keywords
        // into a List<Keyword> thanks to KeywordAdapter
        Gson gson = new GsonBuilder()
                .registerTypeAdapterFactory(new KeywordAdapterFactory())
                .create();

        // Create Retrofit service with our KeywordAdapter
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        projectChronosService = retrofit.create(ProjectChronosService.class);
    }

    /**
     * Get articles with Retrofit service from
     * http://hypermedia.projectchronos.eu/visualize/articles/?api=true and set them and next page
     * in class's private variables.
     * <p/>
     * If any bookmark is passed, it opens it, otherwise opens the base page.
     * It always returns 25 articles or less with the next page to other 25 articles.
     *
     * @param bookmark the bookmark to get. If it is null it retrieves the base page.
     */
    private void getArticles(String bookmark) {
        // Create asynchronous call
        Call<ResponseArticlesList> articles = projectChronosService.getArticles(bookmark);
        articles.enqueue(new Callback<ResponseArticlesList>() {
            @Override
            public void onResponse(Response<ResponseArticlesList> response) {
                if (response.isSuccess() && response.body() != null) {
                    // Get response's body
                    ResponseArticlesList responseArticlesList = response.body();

                    // Add all articles retrieved
                    articleList.addAll(responseArticlesList.getArticles());

                    // Get next page from next url
                    String nextUrl = responseArticlesList.getNext();
                    try {
                        if (BuildConfig.DEBUG)
                            Log.i(TAG, String.format("Total articles downloaded: %d\nArticles: %s", articleList.size(), articleList.toString()));
                        if (BuildConfig.DEBUG) Log.i(TAG, String.format("NextUrl: %s", nextUrl));

                        next = HTTPUtil.splitQuery(new URL(nextUrl)).get("bookmark").get(0);

                        // For all articles get associated keywords list
                        for (Article article : articleList) {
                            getKeywords(article, HTTPUtil.splitQuery(new URL(article.getKeywordsUrl())).get("url").get(0));
                        }

                    } catch (MalformedURLException e) {
                        Log.e(TAG, String.format("%s could not be parsed as a URL", nextUrl), e);
                        //TODO: last call return a null next parameter, manage in order to avoid silly connections
                    }
                } else {
                    // TODO: manage in better way error
                    try {
                        Log.e(TAG, String.format("Response not succeed in getArticles: %s", response.errorBody().string()));
                    } catch (IOException e) {
                        Log.e(TAG, "onResponse in getArticle", e);
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                // TODO: manage in better way error
                Log.e(TAG, "Error in onFailure in getArticle", t);
            }
        });
    }

    private void getKeywords(final Article article, String url) {
        // Create asynchronous call
        Call<List<Keyword>> keywords = projectChronosService.getKeywords(url);
        keywords.enqueue(new Callback<List<Keyword>>() {
            @Override
            public void onResponse(Response<List<Keyword>> response) {
                if (response.isSuccess()) {
                    if (BuildConfig.DEBUG) Log.i(TAG, String.valueOf(response.body()));
                    // Get response's body
                    article.setKeywordList(response.body());

                    if (BuildConfig.DEBUG) Log.i(TAG, article.toString());

                } else {
                    // TODO: manage in better way error
                    try {
                        Log.e(TAG, String.format("Response not succeed in getKeywords: %s", response.errorBody().string()));
                    } catch (IOException e) {
                        Log.e(TAG, "onResponse in getKeywords", e);
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                // TODO: manage in better way error
                Log.e(TAG, "Error in onFailure in getKeywords", t);
            }
        });
    }


    /**
     * Prepare all the stuffs in order to use an optimezed CustomTabs.
     */
    private void prepareCustomTab() {
        // Bind to the custom tab service
        bindCustomTabsService();

        // Warmup the browser process
        if (customTabsClient != null) {
            customTabsClient.warmup(0);
        }

        // Get actual session and prefetch some contents
        customTabsSession = getSession();
        if (customTabsClient != null) {
            // TODO: pass the entire list of url fetched
            customTabsSession.mayLaunchUrl(Uri.parse("http://www.esa.int/Our_Activities/Launchers/Launcher_Technology/Materials_structure_and_stages"), null, null);
        }
    }

    /**
     * This method sets all UI of our CustomTabs and after launch the page of the requested URI.
     *
     * @param uri the URI to open into our custom CustomTabs.
     */
    private void launchCustomTabs(Uri uri) {
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
     * Gets the actual CustomTabsSession.
     * If one is ready returns it, otherwise creates one with a navigation event callback.
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

    /**
     * Allocate resources for CustomTabs.
     */
    private void bindCustomTabsService() {
        if (customTabsClient != null) {
            return;
        }

        // Get package name to use in binding
        String packageNameToBind = CustomTabsHelper.getPackageNameToUse(this);
        if (packageNameToBind == null) {
            return;
        }

        // Create new CustomTabsService
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

        // Bind it!
        if (!CustomTabsClient.bindCustomTabsService(this, packageNameToBind, customTabsServiceConnection)) {
            customTabsServiceConnection = null;
        }
    }

    /**
     * Release all resources used for CustomTabs.
     */
    private void unbindCustomTabsService() {
        if (customTabsServiceConnection == null) {
            return;
        }

        unbindService(customTabsServiceConnection);
        customTabsClient = null;
        customTabsSession = null;
    }
}