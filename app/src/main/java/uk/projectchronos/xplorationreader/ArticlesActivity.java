package uk.projectchronos.xplorationreader;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

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
import uk.projectchronos.xplorationreader.api.ProjectChronosService;
import uk.projectchronos.xplorationreader.model.Article;
import uk.projectchronos.xplorationreader.model.ResponseArticlesList;
import uk.projectchronos.xplorationreader.utils.BaseActivityWithToolbar;
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

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_articles;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Disable default home as up
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setTitle(R.string.title_activity_articles);

        // Get articles
        getArticles(null);
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
        // Create Retrofit service
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ProjectChronosService service = retrofit.create(ProjectChronosService.class);

        // Create asynchronous call
        Call<ResponseArticlesList> articles = service.getArticles(bookmark);
        articles.enqueue(new Callback<ResponseArticlesList>() {
            @Override
            public void onResponse(Response<ResponseArticlesList> response) {
                if (response.isSuccess()) {
                    // Get response's body
                    ResponseArticlesList responseArticlesList = response.body();

                    // Add all articles retrieved
                    articleList.addAll(responseArticlesList.getArticles());

                    // Get next page from next url
                    String nextUrl = responseArticlesList.getNext();
                    try {
                        Log.i(TAG, String.format("Total articles downloaded: %d\nArticles: %s", articleList.size(), articleList.toString()));
                        Log.i(TAG, String.format("NextUrl: %s", nextUrl));

                        next = HTTPUtil.splitQuery(new URL(nextUrl)).get("bookmark").get(0);

                        //getArticles(next); // TODO: to be removed
                    } catch (MalformedURLException e) {
                        Log.e(TAG, String.format("%s could not be parsed as a URL", nextUrl), e);
                        //TODO: last call return a null next parameter, manage in order to avoid silly connections
                    }
                } else {
                    // TODO: manage in better way error
                    try {
                        Log.e(TAG, response.errorBody().string());
                    } catch (IOException e) {
                        Log.e(TAG, "onResponse", e);
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                // TODO: manage in better way error
                Log.e(TAG, "Error in onFailure", t);
            }
        });
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}