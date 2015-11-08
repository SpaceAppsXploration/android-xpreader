package uk.projectchronos.xplorationreader;

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
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;
import uk.projectchronos.xplorationreader.adapter.ArticleAdapter;
import uk.projectchronos.xplorationreader.adapter.OnCardClickListener;
import uk.projectchronos.xplorationreader.adapter.RecyclerViewEmptySupport;
import uk.projectchronos.xplorationreader.api.ProjectChronosService;
import uk.projectchronos.xplorationreader.model.Article;
import uk.projectchronos.xplorationreader.model.ResponseArticlesList;
import uk.projectchronos.xplorationreader.model.ResponseKeywordsList;
import uk.projectchronos.xplorationreader.utils.BaseActivity;
import uk.projectchronos.xplorationreader.utils.BaseFragment;
import uk.projectchronos.xplorationreader.utils.ConnectionHelper;
import uk.projectchronos.xplorationreader.utils.ConnectionReceiver;
import uk.projectchronos.xplorationreader.utils.CustomTabsHelper;
import uk.projectchronos.xplorationreader.utils.EndlessRecyclerOnScrollListener;
import uk.projectchronos.xplorationreader.utils.HTTPUtil;


public class MainActivity extends BaseActivity implements ConnectionHelper.Callbacks {

    /**
     * Base url for API service.
     */
    private static final String BASE_URL = "http://hypermedia.projectchronos.eu/";//"http://rdfendpoints.appspot.com/";
    private static List<Article> feedList = new ArrayList<>();
    private static List<Article> movieList = new ArrayList<>();
    private static List<Article> mediaList = new ArrayList<>();
    private static List<Article> tweetList = new ArrayList<>();
    private static List<Article> fbPostList = new ArrayList<>();
    private static List<Article> paperList = new ArrayList<>();
    private static List<Article> pdfList = new ArrayList<>();
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
    @Bind(R.id.toolbar)
    protected Toolbar toolbar;
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    @Bind(R.id.container)
    protected ViewPager viewPager;
    @Bind(R.id.tabs)
    protected TabLayout tabLayout;
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter sectionsPagerAdapter;
    private ProjectChronosService projectChronosService;
    /**
     * Custom Tabs Service Connection.
     */
    private CustomTabsServiceConnection customTabsServiceConnection;

    /**
     * Snackbar for connectivity errors.
     */
    private Snackbar connectionSnackbar;

    private ConnectionReceiver connectionReceiver;

    /**
     * Gets the actual CustomTabsSession.
     * If one is ready it returns it, otherwise it creates one with a navigation event callback.
     *
     * @return null in case of there is not a CustomTabsClient, customTabsSession oterwhise.
     */
    private static CustomTabsSession getSession() {
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
        setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);

        // Binds views
        ButterKnife.bind(this);

        // Prepares Snackbar for error
        prepareSnackBar();

        // Prepares custom tab
        prepareCustomTab();

        // Set action bar
        setSupportActionBar(toolbar);

        // Creates service
        createProjectChronosService();

        // Create BroadcastReceiver for connectivity checking
        connectionReceiver = new ConnectionReceiver();

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        viewPager.setAdapter(sectionsPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {

            // Filters medias
            case R.id.medias_filter:
                // Not implemented here
                return false;

            // Filters links
            case R.id.links_filter:
                // Not implemented here
                return false;

            // Filters Facebook posts
            case R.id.fb_post_filter:
                // Not implemented here
                return false;

            // Filters tweets
            case R.id.tweets_filter:
                // Not implemented here
                return false;

            // Filters feeds
            case R.id.feeds_filter:
                // Not implemented here
                return false;

            // Filters PDF
            case R.id.pdf_filter:
                // Not implemented here
                return false;

            // Filters papers
            case R.id.papers_filter:
                // Not implemented here
                return false;

            // Filters feeds
            case R.id.movies_filter:
                // Not implemented here
                return false;

            // Shows all articles
            case R.id.all_filter:
                // Not implemented here
                return false;

            case R.id.menu_search:
                // TODO: see #10
                Log.i(TAG, "search");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Start connectivity checker
        connectionReceiver.register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Stop connectivity checker
        connectionReceiver.unRegister(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Releases service
        unBindCustomTabsService();
    }

    /**
     * Prepares snackbar with style and callback
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
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        projectChronosService = retrofit.create(ProjectChronosService.class);
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
     */
    private void prefetchArticles(List<Article> articleList) {
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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class ArticleListFragment extends BaseFragment {

        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_TYPE = "type";
        /**
         * Articles RecyclerView.
         */
        @Bind(R.id.article_recycler_view)
        protected RecyclerViewEmptySupport articleRecyclerView;

        /**
         * Empty LinearLayout.
         */
        @Bind(R.id.empty_article_view)
        protected LinearLayout emptyArticleView;
        private MainActivity mainActivity;
        private ArticleAdapter articleAdapter;

        /**
         * Indicates if the user is viewing all the articles or just of a certain kind.
         * <p/>
         * If is in filterMode the app does not load more results.
         */
        private boolean filterMode = false;
        private String next;
        private String type;

        public ArticleListFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static ArticleListFragment newInstance(String type) {
            ArticleListFragment fragment = new ArticleListFragment();
            Bundle args = new Bundle();
            args.putString(ARG_TYPE, type);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        protected int getLayoutResourceId() {
            return R.layout.fragment_main;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            setHasOptionsMenu(true);
        }

        @Override
        public void onViewCreated(View view, Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);

            mainActivity = ((MainActivity) this.getActivity());

            // Binds views
            ButterKnife.bind(this, view);

            type = getArguments().getString(ARG_TYPE);

            // Prepares list view
            prepareRecylerView();

            // Shows articles
            if (feedList.size() != 0) {
                articleAdapter.animateTo(feedList);
            } else {
                getArticlesBy(type, null, next);
            }
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            // Handle action bar item clicks here. The action bar will
            // automatically handle clicks on the Home/Up button, so long
            // as you specify a parent activity in AndroidManifest.xml.
            switch (item.getItemId()) {

                // Filters medias
                case R.id.medias_filter:
                    articleRecyclerView.getLayoutManager().scrollToPosition(0);
                    articleAdapter.animateTo(filter(Article.Type.MEDIA));

                    return true;

                // Filters links
                case R.id.links_filter:
                    articleRecyclerView.getLayoutManager().scrollToPosition(0);
                    articleAdapter.animateTo(filter(Article.Type.LINK));

                    return true;

                // Filters Facebook posts
                case R.id.fb_post_filter:
                    articleRecyclerView.getLayoutManager().scrollToPosition(0);
                    articleAdapter.animateTo(filter(Article.Type.FB_POST));

                    return true;

                // Filters tweets
                case R.id.tweets_filter:
                    articleRecyclerView.getLayoutManager().scrollToPosition(0);
                    articleAdapter.animateTo(filter(Article.Type.TWEET));

                    return true;

                // Filters feeds
                case R.id.feeds_filter:
                    articleRecyclerView.getLayoutManager().scrollToPosition(0);
                    articleAdapter.animateTo(filter(Article.Type.FEED));

                    return true;

                // Filters PDF
                case R.id.pdf_filter:
                    articleRecyclerView.getLayoutManager().scrollToPosition(0);
                    articleAdapter.animateTo(filter(Article.Type.PDF));

                    return true;

                // Filters papers
                case R.id.papers_filter:
                    articleRecyclerView.getLayoutManager().scrollToPosition(0);
                    articleAdapter.animateTo(filter(Article.Type.PAPER));

                    return true;

                // Filters feeds
                case R.id.movies_filter:
                    articleRecyclerView.getLayoutManager().scrollToPosition(0);
                    articleAdapter.animateTo(filter(Article.Type.MOVIE));

                    return true;

                // Shows all articles
                case R.id.all_filter:
                    articleRecyclerView.getLayoutManager().scrollToPosition(0);
                    articleAdapter.animateTo(feedList);

                    // Sets filter mode to false
                    filterMode = false;

                    return true;

                case R.id.menu_search:
                    // Not implemented here
                    return false;
                default:
                    return super.onOptionsItemSelected(item);
            }
        }

        private List<Article> filter(Article.Type type) {
            // TODO improve using filter interface, see #22
            final List<Article> filteredArticleList = new ArrayList<>();
            for (Article article : feedList) {
                final Article.Type articleType = article.getType();

                if (articleType.equals(type)) {
                    filteredArticleList.add(article);
                }
            }

            // Sets filter mode to true
            filterMode = true;

            return filteredArticleList;
        }

        /**
         * Prepares material list view with empty view and listeners.
         */
        private void prepareRecylerView() {
            // Sets layout manager
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
            articleRecyclerView.setLayoutManager(linearLayoutManager);

            // Adds onScrollListener in order to download other artiles when user arrives to the bottom
            articleRecyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(linearLayoutManager) {
                @Override
                public boolean onLoadMore() {
                    if (!filterMode) {
                        getArticlesBy(type, null, next);
                        return true;
                    } else {
                        return false;
                    }
                }
            });

            // Sets empty view
            articleRecyclerView.setEmptyView(emptyArticleView);

            // Sets adapter
            articleAdapter = new ArticleAdapter(getContext());
            articleRecyclerView.setAdapter(articleAdapter);

            // Sets onCardClickListener
            articleAdapter.setOnCardClickListener(new OnCardClickListener() {
                @Override
                public void onClick(View view, Article article) {
                    String url = article.getUrl();
                    Uri uri = Uri.parse(url);

                    // Launch custom tab
                    mainActivity.launchCustomTabs(uri);
                }
            });
        }

        private void getArticlesBy(String type, String keyword, final String bookmark) {
            Log.i(TAG, "getArticlesBy called");
            // Creates asynchronous call
            Call<ResponseArticlesList> articles = mainActivity.projectChronosService.getArticlesBy(type, keyword, bookmark);
            articles.enqueue(new Callback<ResponseArticlesList>() {
                @Override
                public void onResponse(Response<ResponseArticlesList> response) {
                    if (response.isSuccess() && response.body() != null) {
                        // Gets response's body
                        ResponseArticlesList responseArticlesList = response.body();
                        List<Article> articleListFetched = responseArticlesList.getArticles();

                        // Add newly articles fetched
                        feedList.addAll(articleListFetched);
                        articleAdapter.animateTo(articleListFetched);
                        articleAdapter.addArticles(articleListFetched);

                        // Prefetches articles
                        mainActivity.prefetchArticles(articleListFetched);

                        // Gets next page from next url
                        String nextUrl = responseArticlesList.getNext();

                        // For all articles
                        for (Article article : articleListFetched) {
                            // Gets all keywords associated
                            getKeywords(article);
                        }

                        try {
                            next = HTTPUtil.splitQuery(new URL(nextUrl)).get("bookmark").get(0);
                        } catch (MalformedURLException e) {
                            Log.e(TAG, String.format("%s could not be parsed as a URL", nextUrl), e);
                            //TODO: Issue #11
                        }
                    } else {
                        // TODO: manage in better way error
                        try {
                            Log.e(TAG, String.format("Response not succeed in getArticlesBy: %s", response.errorBody().string()));
                        } catch (IOException e) {
                            Log.e(TAG, "onResponse in getArticlesBy", e);
                        }
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    // TODO: manage in better way error
                    Log.e(TAG, "Error in onFailure in getArticlesBy", t);
                }
            });
        }

        /**
         * Gets keywords of an article with Retrofit service from
         * http://hypermedia.projectchronos.eu/articles/v04/url=URL and sets them into
         * the article passed.
         *
         * @param article the Article in wich put keywords.
         */
        private void getKeywords(final Article article) {
            String url = null;
            try {
                url = HTTPUtil.splitQuery(new URL(article.getKeywordsUrl())).get("url").get(0);
            } catch (MalformedURLException exception) {
                Log.e(TAG, "error in getKeywords", exception);
            }

            // Creates asynchronous call
            Call<ResponseKeywordsList> keywords = mainActivity.projectChronosService.getKeywords(url);
            keywords.enqueue(new Callback<ResponseKeywordsList>() {
                @Override
                public void onResponse(Response<ResponseKeywordsList> response) {
                    if (response.isSuccess()) {
                        // Gets response's body
                        ResponseKeywordsList responseKeywordsList = response.body();

                        // Sets article's keywords list
                        article.setKeywordsList(responseKeywordsList.getKeywords());
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
                    //Log.e(TAG, "Error in onFailure in getKeywords", t);
                }
            });
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a ArticleListFragment (defined as a static inner class below).
            return ArticleListFragment.newInstance("feed");
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SECTION 1";
                case 1:
                    return "SECTION 2";
                case 2:
                    return "SECTION 3";
            }
            return null;
        }
    }
}
