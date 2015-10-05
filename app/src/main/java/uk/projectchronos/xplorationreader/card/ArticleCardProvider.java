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

package uk.projectchronos.xplorationreader.card;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dexafree.materialList.card.Card;
import com.dexafree.materialList.card.provider.CardProvider;
import com.greenfrvr.hashtagview.HashtagView;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import uk.projectchronos.xplorationreader.R;
import uk.projectchronos.xplorationreader.model.Article;
import uk.projectchronos.xplorationreader.model.Keyword;

/**
 * Custom CardProvider that allows us to create own Article Card.
 */
public class ArticleCardProvider extends CardProvider {

    private static final String TAG = "ArticleCardProvider";

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
     * Keywords HashtagView.
     */
    @Bind(R.id.keywords_hashtag)
    protected HashtagView keywordHashtagView;

    /**
     * Agency's ImageView.
     */
    @Bind(R.id.agency_image_view)
    protected ImageView agencyImageView;

    @Override
    public int getLayout() {
        return R.layout.card_article;
    }

    @Override
    public void render(@NonNull View view, @NonNull Card card) {
        super.render(view, card);

        // Binds views
        ButterKnife.bind(this, view);

        // Gets article
        Article article = (Article) card.getTag();
        if (article != null) {
            // Sets title
            titleTextView.setText(article.getTitle());

            // Gets default date format using locale
            DateFormat simpleDateFormat = DateFormat.getDateInstance(DateFormat.DEFAULT, Locale.getDefault());

            // Gets stored and published date and then sets them
            Date storedDate = article.getStored();
            Date publishedDate = article.getPublished();

            if (publishedDate != null) {
                dateTextView.setText(String.format("%s (%s %s)", simpleDateFormat.format(publishedDate), getContext().getResources().getString(R.string.stored), simpleDateFormat.format(storedDate)));
            } else {
                dateTextView.setText(String.format("%s %s", getContext().getResources().getString(R.string.stored), simpleDateFormat.format(storedDate)));
            }

            // Sets abstract
            abstractTextView.setText(article.get_abstract());

            // Sets keywords
            keywordHashtagView.setData(article.getKeywordsList(), new HashtagView.DataTransform<Keyword>() {
                @Override
                public CharSequence prepare(Keyword keyword) {
                    return keyword.getValue();  // In this way the tag shows the keywords' value
                }
            });

            // Sets image view
            Picasso.with(getContext())
                    .load("http://icons.better-idea.org/api/icons?url=" + article.getUrl() + "&i_am_feeling_lucky=yes")
                    .placeholder(R.drawable.ic_earh)    // TODO: randomize
                    .fit()
                    .centerInside()
                    .noFade()
                    .into(agencyImageView);
        }
    }
}