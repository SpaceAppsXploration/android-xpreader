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
import android.widget.TextView;

import com.dexafree.materialList.card.Card;
import com.dexafree.materialList.card.provider.CardProvider;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import uk.projectchronos.xplorationreader.R;
import uk.projectchronos.xplorationreader.model.Article;

/**
 *
 */
public class ArticleCardProvider extends CardProvider {

    private static final String TAG = "ArticleCardProvider";

    /**
     *
     */
    @Bind(R.id.title_text_view)
    TextView titleTextView;

    /**
     *
     */
    @Bind(R.id.date_text_view)
    TextView dateTextView;

    /**
     *
     */
    @Bind(R.id.abstract_text_view)
    TextView abstractTextView;

    @Override
    public int getLayout() {
        return R.layout.card_article;
    }

    @Override
    public void render(@NonNull View view, @NonNull Card card) {
        super.render(view, card);

        // Binds views
        ButterKnife.bind(this, view);

        // Get article
        Article article = (Article) card.getTag();
        if (article != null) {
            titleTextView.setText(article.getTitle());

            DateFormat simpleDateFormat = DateFormat.getDateInstance(DateFormat.DEFAULT, Locale.getDefault());

            Date storedDate = article.getStored();
            Date publishedDate = article.getPublished();

            if (publishedDate != null) {
                dateTextView.setText(String.format("%s (%s %s)", simpleDateFormat.format(publishedDate), getContext().getResources().getString(R.string.stored), simpleDateFormat.format(storedDate)));
            } else {
                dateTextView.setText(String.format("%s %s", getContext().getResources().getString(R.string.stored), simpleDateFormat.format(storedDate)));
            }

            abstractTextView.setText(article.get_abstract());
        }
    }
}