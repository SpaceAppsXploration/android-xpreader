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

import android.view.View;

import uk.projectchronos.xplorationreader.model.Article;

/**
 * Interface for card click.
 */
public interface OnCardClickListener {

    /**
     * OnClick method.
     *
     * @param view    the view clicked.
     * @param article the article binded to view clicked.
     */
    void onClick(View view, Article article);
}
