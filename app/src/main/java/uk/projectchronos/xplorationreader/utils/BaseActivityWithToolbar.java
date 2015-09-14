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