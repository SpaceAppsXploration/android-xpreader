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

package uk.projectchronos.xplorationreader.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import java.util.Set;

import uk.projectchronos.xplorationreader.dialogs.SectionPreferenceDialog;
import uk.projectchronos.xplorationreader.fragments.ArticleListFragment;
import uk.projectchronos.xplorationreader.models.Article;

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

    /**
     * The TAG used for logging.
     */
    private static final String TAG = "SectionsPagerAdapter";

    /**
     *
     */
    private SharedPreferences preference;

    /**
     *
     */
    private String[] sectionNames;

    /**
     *
     */
    private Fragment currentFragment;

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        // Gets preference
        preference = context.getSharedPreferences(SectionPreferenceDialog.SECTION_PREFERENCE_NAME, Context.MODE_PRIVATE);

        // Gets all sections name (unordered)
        Set<String> sections = preference.getStringSet(SectionPreferenceDialog.SECTIONS_NAME, SectionPreferenceDialog.SECTIONS_DEFAULT_SET);

        // Creates array that collects all sections name in an ordered way
        sectionNames = new String[sections.size()];

        for (String section : sections) {
            // Puts section name into sectionNames in right position
            sectionNames[preference.getInt("#" + section, getDefaultIndex(section))] = section;
        }
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a ArticleListFragment (defined as a static inner class below).

        // Gets section name
        String sectionName = sectionNames[position];

        // Gets section types string
        Set<String> types = preference.getStringSet(sectionName, getDefaultTypes(sectionName));

        // Creates array that collects all type of this particular section
        Article.Type[] typesArray = new Article.Type[types.size()];

        // Converts into Article.Type and it puts them into typesArray
        int counter = 0;
        for (String type : types) {
            typesArray[counter] = Article.Type.values()[Integer.parseInt(type)];
            counter++;
        }

        return ArticleListFragment.newInstance(typesArray);
    }

    /**
     * @param section
     * @return
     */
    private Set<String> getDefaultTypes(String section) {
        switch (section) {
            case "All":
                return SectionPreferenceDialog.ALL_DEFAULT_SET;
            case "Social":
                return SectionPreferenceDialog.SOCIAL_DEFAULT_SET;
            case "Media":
                return SectionPreferenceDialog.MEDIA_DEFAULT_SET;
            default:
                return null;
        }
    }

    /**
     * @param section
     * @return
     */
    private int getDefaultIndex(String section) {
        switch (section) {
            case "All":
                return 2;
            case "Social":
                return 1;
            case "Media":
                return 0;
            default:
                return -1;
        }
    }

    @Override
    public int getCount() {
        return sectionNames.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return sectionNames[position];
    }

    /**
     * Gets the current fragment shown.
     *
     * @return the current fragment instance shown by the adapter.
     */
    public Fragment getCurrentFragment() {
        return currentFragment;
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        if (getCurrentFragment() != object) {
            currentFragment = ((Fragment) object);
        }
        super.setPrimaryItem(container, position, object);
    }
}
