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

package uk.projectchronos.xplorationreader.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import uk.projectchronos.xplorationreader.R;
import uk.projectchronos.xplorationreader.model.Article;

/**
 * Adapter for Articles.
 */
public class ArticleAdapter extends RecyclerView.Adapter<ArticleViewHolder> {

    /**
     * Layout inflater.
     */
    private final LayoutInflater layoutInflater;

    /**
     * Articles list binded to the
     */
    private final List<Article> articleList;

    /**
     * Context.
     */
    private Context context;

    /**
     * OnCardClickListener.
     */
    private OnCardClickListener onCardClickListener;

    public ArticleAdapter(Context context) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        // Create a new article list, use addArticles() or addArticle() in order to add article in the adapter.
        this.articleList = new ArrayList<>();
    }

    /**
     * Sets onClickListener of the card.
     *
     * @param onCardClickListener the onCardClickListener to set.
     */
    public void setOnCardClickListener(OnCardClickListener onCardClickListener) {
        this.onCardClickListener = onCardClickListener;
    }

    @Override
    public ArticleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View articleCard = layoutInflater.inflate(R.layout.card_article, parent, false);
        return new ArticleViewHolder(articleCard, onCardClickListener);
    }

    @Override
    public void onBindViewHolder(ArticleViewHolder holder, int position) {
        final Article article = articleList.get(position);
        holder.bind(context, article);
    }

    @Override
    public int getItemCount() {
        return articleList.size();
    }

    /**
     * It will animate between the List of objects currently displayed in the Adapter to the
     * filtered List.
     *
     * @param newArticleList the new articles list.
     */
    public void animateTo(List<Article> newArticleList) {
        applyAndAnimateRemovals(newArticleList);
        applyAndAnimateAdditions(newArticleList);
        applyAndAnimateMovedArticles(newArticleList);
    }

    /**
     * This method iterates through the internal List of the Adapter backwards and checks if each
     * item is contained in the new filtered List. If it is not it calls removeItem(). The reason
     * we iterate backwards is to avoid having to keep track of an offset. If you remove an item all
     * items below it move up. If you iterate through to the List from the bottom up then only items
     * which you have already iterated over are moved.
     *
     * @param newArticleList the new articles list.
     */
    private void applyAndAnimateRemovals(List<Article> newArticleList) {
        for (int i = articleList.size() - 1; i >= 0; i--) {
            final Article model = articleList.get(i);
            if (!newArticleList.contains(model)) {
                removeArticle(i);
            }
        }
    }

    /**
     * This method iterating through the internal List of the Adapter it iterates through the
     * filtered List and checks if the item exists in the internal List.
     * If it does not it calls addItem().
     *
     * @param newArticleList the new articles list.
     */
    private void applyAndAnimateAdditions(List<Article> newArticleList) {
        for (int i = 0, count = newArticleList.size(); i < count; i++) {
            final Article article = newArticleList.get(i);
            if (!articleList.contains(article)) {
                addArticle(i, article);
            }
        }
    }

    /**
     * This method is essentially a combination of applyAndAnimateRemovals() and
     * applyAndAnimateAdditions() but with a twist. What does is it iterates through the filtered
     * List backwards and looks up the index of each item in the internal List.
     * If it detects a difference in the index it calls moveItem() to bring the internal List of the
     * Adapter in line with the filtered List.
     * <p/>
     * To call after applyAndAnimateRemovals() and applyAndAnimateAdditions().
     *
     * @param newArticleList the new articles list.
     */
    private void applyAndAnimateMovedArticles(List<Article> newArticleList) {
        for (int toPosition = newArticleList.size() - 1; toPosition >= 0; toPosition--) {
            final Article model = newArticleList.get(toPosition);
            final int fromPosition = articleList.indexOf(model);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveArticle(fromPosition, toPosition);
            }
        }
    }

    /**
     * Removes item from a certain position.
     *
     * @param position the position in which there is the article to remove.
     * @return the article removed.
     */
    public Article removeArticle(int position) {
        final Article model = articleList.remove(position);
        notifyItemRemoved(position);
        return model;
    }

    /**
     * Adds Article to a certain position.
     *
     * @param position position in which add Article.
     * @param article  Article to add.
     */
    public void addArticle(int position, Article article) {
        articleList.add(position, article);
        notifyItemInserted(position);
    }

    /**
     * Adds Articles to adaper.
     *
     * @param articleList the list to add.
     */
    public void addArticles(List<Article> articleList) {
        this.articleList.addAll(articleList);
        notifyItemRangeInserted(this.articleList.size() - articleList.size(), articleList.size());
    }

    /**
     * Moves article from a position to another one.
     *
     * @param fromPosition position where to get the article.
     * @param toPosition   position where to put the article.
     */
    public void moveArticle(int fromPosition, int toPosition) {
        final Article model = articleList.remove(fromPosition);
        articleList.add(toPosition, model);
        notifyItemMoved(fromPosition, toPosition);
    }
}