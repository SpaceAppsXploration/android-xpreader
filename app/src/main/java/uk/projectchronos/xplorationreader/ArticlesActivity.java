/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Claudio Pastorini
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package uk.projectchronos.xplorationreader;

import android.content.ComponentName;
import android.database.sqlite.SQLiteConstraintException;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.customtabs.CustomTabsCallback;
import android.support.customtabs.CustomTabsClient;
import android.support.customtabs.CustomTabsIntent;
import android.support.customtabs.CustomTabsService;
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
import uk.projectchronos.xplorationreader.model.KeywordToArticles;
import uk.projectchronos.xplorationreader.model.ResponseArticlesList;
import uk.projectchronos.xplorationreader.model.ResponseKeywordsList;
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
     * Last next page URL.
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
     * ProjectChronosService that allows to access to articles and keywords API.
     */
    private ProjectChronosService projectChronosService;

    /**
     * Application useful in order to call singletons.
     */
    private App application;

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

        // Get application
        application = ((App) getApplication());

        // Create service
        createProjectChronosService();

        // Get articles
        getArticles(null);

        // Prepare custom tab
        prepareCustomTab();

        Log.v(TAG, String.format("Articles from db: %s", String.valueOf(application.getArticleDao().loadAll())));
        Log.v(TAG, String.format("Keywords from db: %s", String.valueOf(application.getKeywordDao().loadAll())));
        Log.v(TAG, String.format("KeywordToArticles from db: %s", String.valueOf(application.getKeywordToArticlesDao().loadAll())));

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
        // Releases service
        unbindCustomTabsService();
        super.onDestroy();
    }

    /**
     * Creates Retrofit's ProjectChronosService with custom typeAdapter.
     */
    private void createProjectChronosService() {
        // Create new Gson throw GsonBuilder in order to extend it and parse a String[] of keywords
        // into a List<Keyword> thanks to KeywordAdapter
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
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
     * Gets articles with Retrofit service from
     * http://hypermedia.projectchronos.eu/articles/?api=true and sets them and next page
     * in private variables.
     * <p/>
     * If any bookmark is passed, it opens it, otherwise opens the base page.
     * It always returns 25 articles or less with the next page to other articles.
     *
     * @param bookmark the bookmark to get. If it is null it retrieves the base page.
     */
    private void getArticles(final String bookmark) {
        // Creates asynchronous call
        Call<ResponseArticlesList> articles = projectChronosService.getArticles(bookmark);
        articles.enqueue(new Callback<ResponseArticlesList>() {
            @Override
            public void onResponse(Response<ResponseArticlesList> response) {
                if (response.isSuccess() && response.body() != null) {
                    // Gets response's body
                    ResponseArticlesList responseArticlesList = response.body();

                    // Adds all articles retrieved
                    articleList.addAll(responseArticlesList.getArticles());

                    // Prefetches articles
                    prefetchArticles();

                    // Gets next page from next url
                    String nextUrl = responseArticlesList.getNext();
                    try {
                        if (BuildConfig.DEBUG)
                            Log.v(TAG, String.format("Total articles downloaded: %d\nArticles: %s", articleList.size(), articleList.toString()));
                        if (BuildConfig.DEBUG) Log.v(TAG, String.format("NextUrl: %s", nextUrl));

                        next = HTTPUtil.splitQuery(new URL(nextUrl)).get("bookmark").get(0);

                        // For all articles
                        for (Article article : articleList) {
                            // Saves it into databases (or get it)
                            long articleId = application.getArticleDao().getOrInsert(article);

                            // Gets all keywords associated
                            String url = HTTPUtil.splitQuery(new URL(article.getKeywordsUrl())).get("url").get(0);
                            getKeywords(articleId, url);
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

    /**
     * Gets keywords of an article with Retrofit service from
     * http://hypermedia.projectchronos.eu/articles/?api=true&url= and sets them into
     * the article passed.
     *
     * @param articleId the article id where sets the keywords list downloaded.
     * @param url       the URL where gets the keywords.
     */
    private void getKeywords(final long articleId, String url) {
        // Creates asynchronous call
        Call<ResponseKeywordsList> keywords = projectChronosService.getKeywords(url);
        keywords.enqueue(new Callback<ResponseKeywordsList>() {
            @Override
            public void onResponse(Response<ResponseKeywordsList> response) {
                if (response.isSuccess()) {
                    // Gets response's body
                    ResponseKeywordsList responseKeywordsList = response.body();

                    // Gets keywords list
                    List<Keyword> keywordList = responseKeywordsList.getKeywords();

                    Log.i(TAG, keywordList.toString());

                    // For all keywords
                    for (Keyword keyword : keywordList) {
                        try {
                            // Saves it into database
                            long keywordId = application.getKeywordDao().getOrInsert(keyword);
                            Log.v(TAG, String.format("keyword has id: %d", keywordId));

                            // Associates it with article
                            KeywordToArticles keywordToArticles = new KeywordToArticles();
                            keywordToArticles.setKeywordId(keywordId);
                            keywordToArticles.setArticleId(articleId);

                            // Saves it into databases
                            long keywordToArticlesId = application.getKeywordToArticlesDao().getOrInsert(keywordToArticles);

                            Log.v(TAG, String.format("keywordToArticles has id: %d", keywordToArticlesId));
                        } catch (SQLiteConstraintException exception) {
                            Log.e(TAG, "Error in onFailure in getKeywords", exception);
                        }
                    }

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
     * Prepares all the stuffs in order to use an optimized CustomTabs.
     * */
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
    private void prefetchArticles() {
        // Gets actual session and prefetch some contents
        customTabsSession = getSession();
        if (customTabsClient != null) {

            Uri firstUri = Uri.parse("http://www.projectchronos.eu/"); // Only inizialization
            List<Bundle> bundleList = new ArrayList<>();
            // Gets all article url
            for (Article article : articleList) {

                String url = article.getUrl();
                Log.v(TAG, String.format("Url to add to bundle: %s", url));
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
     * Sets all UI of our CustomTabs and after it launches the page of the requested URI.
     *
     * @param uri the URI to open into our CustomTabs.
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
    private void unbindCustomTabsService() {
        if (customTabsServiceConnection == null) {
            return;
        }

        unbindService(customTabsServiceConnection);
        customTabsClient = null;
        customTabsSession = null;
    }
}