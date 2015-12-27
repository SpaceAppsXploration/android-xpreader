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

package uk.projectchronos.xplorationreader.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import uk.projectchronos.xplorationreader.R;
import uk.projectchronos.xplorationreader.activites.MainActivity;
import uk.projectchronos.xplorationreader.adapters.ArticleAdapter;
import uk.projectchronos.xplorationreader.interfaces.OnCardClickListener;
import uk.projectchronos.xplorationreader.models.Article;
import uk.projectchronos.xplorationreader.models.ResponseArticlesList;
import uk.projectchronos.xplorationreader.models.ResponseKeywordsList;
import uk.projectchronos.xplorationreader.utils.BaseFragment;
import uk.projectchronos.xplorationreader.utils.EndlessRecyclerOnScrollListener;
import uk.projectchronos.xplorationreader.utils.HTTPUtil;
import uk.projectchronos.xplorationreader.views.RecyclerViewEmptySupport;

/**
 * Article list fragment containing the list of article by types.
 */
public class ArticleListFragment extends BaseFragment {

    /**
     * The types of the article to show.
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

    /**
     * Reference to the MainActivity.
     */
    private MainActivity mainActivity;

    /**
     * Adapter for RecyclerView.
     */
    private ArticleAdapter articleAdapter;

    /**
     * Indicates if the user could load other articles.
     * <p/>
     * If is in notLoadMoreMode the app does not load more results.
     * //TODO add footer with message that explain
     * //TODO add progress bar when load more is called
     */
    private boolean notLoadMoreMode = false;

    /**
     * Last next page URL.
     */
    private String next = null;

    /**
     * Array of Articles types to show in this fragment.
     */
    private Article.Type[] types;

    /**
     * Hashmap that remembers which types are displayed.
     */
    private HashMap<Article.Type, Boolean> filteredTypes = new HashMap<>();

    /**
     * List of articles of fragment.
     */
    private List<Article> articleList = new ArrayList<>();

    /**
     * Default constructor.
     */
    public ArticleListFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given types.
     */
    public static ArticleListFragment newInstance(Article.Type[] types) {
        ArticleListFragment fragment = new ArticleListFragment();

        Bundle args = new Bundle();
        args.putParcelableArray(ARG_TYPE, types);
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
        // Sets true in order to notify that fragment would like to partecipate in options menu
        // manipulation
        setHasOptionsMenu(true);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Gets reference to activity
        mainActivity = ((MainActivity) this.getActivity());

        // Binds views
        ButterKnife.bind(this, view);

        // Gets types of articles to show
        types = (Article.Type[]) getArguments().getParcelableArray(ARG_TYPE);

        // Generates the filteredTypes array that remembers which types are displayed
        if (types != null) {
            for (Article.Type type : types) {
                filteredTypes.put(type, Boolean.TRUE);
            }
        }

        // Overrides TAG in order to have a more comfortable string to read
        TAG = String.format("FragmentType=%s", Arrays.toString(types));

        // Prepares list view
        prepareRecyclerView();

        // Gets articles by type
        for (Article.Type type : types) {
            try {
                if (getList(type).size() > 0) {
                    showArticles();
                } else {
                    getArticlesBy(type, null, next);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {

            case R.id.menu_filter:

                View view = getActivity().findViewById(R.id.menu_filter); // SAME ID AS MENU ID
                final PopupMenu popupMenu = new PopupMenu(getActivity(), view);
                for (Article.Type type : types) {
                    popupMenu.getMenu()
                            .add(Menu.NONE, type.getId(), Menu.NONE, type.getStringResourceId())
                            .setCheckable(true)
                            .setChecked(filteredTypes.get(type));
                }

                // process popup clicks as appropriate
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        Article.Type type = Article.Type.values()[item.getItemId()];

                        item.setChecked(!item.isChecked());
                        filteredTypes.put(type, !filteredTypes.get(type));

                        articleRecyclerView.getLayoutManager().scrollToPosition(0);
                        articleAdapter.animateTo(filterArticles());

                        popupMenu.show();
                        return true;
                    }
                });

                popupMenu.show();

                return true;

            // Shows articles ordered in ascending published date
            case R.id.ascending_order_date:

                List<Article> sortedList = new ArrayList<>(articleAdapter.getArticleList());
                // Sorts articles
                Collections.sort(sortedList, new Comparator<Article>() {
                    @Override
                    public int compare(Article lhs, Article rhs) {
                        return lhs.getPublished().compareTo(rhs.getPublished());
                    }
                });
                articleRecyclerView.getLayoutManager().scrollToPosition(0);
                articleAdapter.animateTo(sortedList);
                // Sets not load more to false
                notLoadMoreMode = true;

                // Checks or not item
                item.setChecked(!item.isChecked());

                return true;
            // Shows articles ordered in desceding published date
            case R.id.descending_order_date:

                sortedList = new ArrayList<>(articleAdapter.getArticleList());
                // Sorts articles
                Collections.sort(sortedList, new Comparator<Article>() {
                    @Override
                    public int compare(Article lhs, Article rhs) {
                        return rhs.getPublished().compareTo(lhs.getPublished());
                    }
                });
                articleRecyclerView.getLayoutManager().scrollToPosition(0);
                articleAdapter.animateTo(sortedList);
                // Sets not load more to false
                notLoadMoreMode = true;

                // Checks or not item
                item.setChecked(!item.isChecked());

                return true;

            // Shows articles without order
            case R.id.no_order_date:

                articleRecyclerView.getLayoutManager().scrollToPosition(0);
                articleAdapter.animateTo(filterArticles());
                // Sets not load more to true
                notLoadMoreMode = false;

                // Checks or not item
                item.setChecked(!item.isChecked());

                return true;

            default:

                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * @return
     */
    private List<Article> filterArticles() {
        List<Article> filteredArticles = new ArrayList<>();

        for (Map.Entry<Article.Type, Boolean> articleType : filteredTypes.entrySet()) {
            Article.Type type = articleType.getKey();
            Boolean value = articleType.getValue();

            if (value) {
                filteredArticles.addAll(filter(type));
            }
        }

        return filteredArticles;
    }

    /**
     * @param type
     * @return
     */
    private List<Article> filter(Article.Type type) {
        // TODO improve using filter interface, see #22
        final List<Article> filteredArticleList = new ArrayList<>();
        for (Article article : articleList) {
            final Article.Type articleType = article.getType();

            if (articleType.equals(type)) {
                filteredArticleList.add(article);
            }
        }

        // Sets not load more to true
        notLoadMoreMode = true;

        return filteredArticleList;
    }

    /**
     * Prepares recycler view view with empty view and listeners.
     */
    private void prepareRecyclerView() {
        // Sets layout manager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        articleRecyclerView.setLayoutManager(linearLayoutManager);

        // Adds onScrollListener in order to download other artiles when user arrives to the bottom
        articleRecyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(linearLayoutManager) {
            @Override
            public boolean onLoadMore() {
                if (!notLoadMoreMode) {
                    // Gets articles by type
                    for (Article.Type type : types) {
                        getArticlesBy(type, null, next);
                    }
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

    /**
     * @param type
     * @param keyword
     * @param bookmark
     */
    private void getArticlesBy(final Article.Type type, String keyword, final String bookmark) {
        Log.i(TAG, "getArticlesBy called");
        // Creates asynchronous call
        Call<ResponseArticlesList> articles = mainActivity.getProjectChronosService().getArticlesBy(type.getValue(), keyword, bookmark);
        articles.enqueue(new Callback<ResponseArticlesList>() {
            @Override
            public void onResponse(Response<ResponseArticlesList> response) {
                if (response.isSuccess() && response.body() != null) {
                    // Gets response's body
                    ResponseArticlesList responseArticlesList = response.body();
                    List<Article> articleListFetched = responseArticlesList.getArticles();

                    try {
                        getList(type).addAll(articleListFetched);

                        showArticles();
                    } catch (Exception e) {
                        Log.e(TAG, "Error in type");
                    }

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
                        if (nextUrl != null) {
                            next = HTTPUtil.splitQuery(new URL(nextUrl)).get("bookmark").get(0);
                        } else {
                            //TODO add footer for last element?
                        }
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
     * @param type
     * @return
     * @throws Exception
     */
    private List<Article> getList(Article.Type type) throws Exception {
        switch (type) {
            case FEED:
                return MainActivity.feedList;
            case TWEET:
                return MainActivity.tweetList;
            case MEDIA:
                return MainActivity.mediaList;
            case LINK:
                return MainActivity.linkList;
            case PDF:
                return MainActivity.pdfList;
            case PAPER:
                return MainActivity.paperList;
            case FB_POST:
                return MainActivity.fbPostList;
            case MOVIE:
                return MainActivity.movieList;
            default:
                throw new Exception("Not right type");
        }
    }

    /**
     * @throws Exception
     */
    private void showArticles() throws Exception {
        for (Article.Type type : types) {
            articleList.addAll(getList(type));
        }

        articleAdapter.addArticles(articleList);
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
        Call<ResponseKeywordsList> keywords = mainActivity.getProjectChronosService().getKeywords(url);
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
                Log.e(TAG, "Error in onFailure in getKeywords", t);
            }
        });
    }

    /**
     *
     */
    public void scrollToTop() {
        articleRecyclerView.scrollToPosition(0);
    }
}
