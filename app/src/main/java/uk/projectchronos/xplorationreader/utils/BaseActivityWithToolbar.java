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

package uk.projectchronos.xplorationreader.utils;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;

import uk.projectchronos.xplorationreader.R;

/**
 * Base App Compact Activity Action Bar from Toolbar.
 *
 * @author pincopallino93
 * @version 1.3
 */
public abstract class BaseActivityWithToolbar extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Init toolbar
        initToolbar();
    }

    /**
     * This method initializes a Toolbar in order to use such a ActionBar.
     */
    private void initToolbar() {
        // Get toolbar
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // Set as Action Bar and get it
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            // Set the Up arrow in Action Bar
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
}