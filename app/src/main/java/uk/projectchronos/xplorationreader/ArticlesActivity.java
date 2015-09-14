package uk.projectchronos.xplorationreader;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;
import uk.projectchronos.xplorationreader.api.ProjectChronosService;
import uk.projectchronos.xplorationreader.model.ResponseArticlesList;
import uk.projectchronos.xplorationreader.utils.BaseActivityWithToolbar;

public class ArticlesActivity extends BaseActivityWithToolbar {

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


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://hypermedia.projectchronos.eu/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ProjectChronosService service = retrofit.create(ProjectChronosService.class);

        Call<ResponseArticlesList> articles = service.articles();
        articles.enqueue(new Callback<ResponseArticlesList>() {
            @Override
            public void onResponse(Response<ResponseArticlesList> response) {
                if (response.isSuccess()) {
                    Log.i(TAG, response.body().toString());
                    Log.i(TAG, response.body().getArticles().get(9).toString());
                } else {
                    Log.e(TAG, response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(TAG, t.getMessage());
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
