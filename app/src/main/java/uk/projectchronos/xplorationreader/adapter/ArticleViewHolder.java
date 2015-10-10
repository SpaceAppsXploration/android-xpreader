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
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.greenfrvr.hashtagview.HashtagView;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import uk.projectchronos.xplorationreader.R;
import uk.projectchronos.xplorationreader.model.Article;

/**
 *
 */
public class ArticleViewHolder extends RecyclerView.ViewHolder {

    /**
     * Title TextView.
     */
    @Bind(R.id.title_text_view)
    protected TextView titleTextView;
    /**
     * Date TextView.
     */
    @Bind(R.id.date_text_view)
    protected TextView dateTextView;
    /**
     * Abstract TextView.
     */
    @Bind(R.id.abstract_text_view)
    protected TextView abstractTextView;
    /**
     * Agency ImageView.
     */
    @Bind(R.id.agency_image_view)
    protected ImageView agencyImageView;
    /**
     * Keywords HashTagView.
     */
    @Bind(R.id.keywords_hashtag)
    protected HashtagView keywordsHashTagView;
    /**
     * OnCardClickListener.
     */
    private OnCardClickListener onCardClickListener;
    /**
     * Article.
     */
    private Article article;

    /**
     * Contructor for the ViewHoler.
     *
     * @param view                the view to fill with data.
     * @param onCardClickListener the click listener to set.
     */
    public ArticleViewHolder(View view, OnCardClickListener onCardClickListener) {
        super(view);

        // Binds views
        ButterKnife.bind(this, view);

        // Sets the clickListener
        this.onCardClickListener = onCardClickListener;
    }

    // OnCardClick listener called
    @OnClick({R.id.article_card_view, R.id.title_text_view, R.id.date_text_view, R.id.abstract_text_view, R.id.agency_image_view})
    void onClick(View view) {
        onCardClickListener.onClick(view, article);
    }

    /**
     * Binds Article to the view.
     *
     * @param context the context.
     * @param article the article to bind to the view.
     */
    public void bind(Context context, final Article article) {

        // Sets article
        this.article = article;

        // Sets title
        titleTextView.setText(article.getTitle());

        // Gets default date format using locale
        DateFormat simpleDateFormat = DateFormat.getDateInstance(DateFormat.DEFAULT, Locale.getDefault());

        // Gets stored and published date and then sets them
        Date storedDate = article.getStored();
        Date publishedDate = article.getPublished();

        if (publishedDate != null) {
            dateTextView.setText(String.format("%s (%s %s)", simpleDateFormat.format(publishedDate), context.getResources().getString(R.string.stored), simpleDateFormat.format(storedDate)));
        } else {
            dateTextView.setText(String.format("%s %s", context.getResources().getString(R.string.stored), simpleDateFormat.format(storedDate)));
        }

        // Sets abstract
        abstractTextView.setText(article.get_abstract());
        //Linkify.addLinks(abstractTextView, Linkify.ALL);

        // Sets image view
        Picasso.with(context)
                .load("http://icons.better-idea.org/api/icons?url=" + article.getUrl() + "&i_am_feeling_lucky=yes")
                .placeholder(R.drawable.ic_earh)    // TODO: randomize
                .fit()
                .centerInside()
                .into(agencyImageView);
    }
}