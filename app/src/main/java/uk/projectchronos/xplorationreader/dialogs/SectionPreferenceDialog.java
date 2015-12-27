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

package uk.projectchronos.xplorationreader.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import uk.projectchronos.xplorationreader.R;
import uk.projectchronos.xplorationreader.interfaces.OnDialogClosedListener;
import uk.projectchronos.xplorationreader.models.Article;

/**
 *
 */
public class SectionPreferenceDialog extends DialogFragment {
    /**
     *
     */
    public static final String SECTION_PREFERENCE_NAME = "section_preference";
    /**
     *
     */
    public static final String SECTIONS_NAME = "sections";
    /**
     *
     */
    public static final String[] SECTION_DEFAULT_VALUES = new String[]{"Social", "Media", "All"};
    public static final Set<String> SECTIONS_DEFAULT_SET = new HashSet<>(Arrays.asList(SECTION_DEFAULT_VALUES));
    /**
     *
     */
    public static final String[] SOCIAL_DEFAULT_VALUES = new String[]{Integer.toString(Article.Type.FB_POST.getId()), Integer.toString(Article.Type.TWEET.getId())};
    public static final Set<String> SOCIAL_DEFAULT_SET = new HashSet<>(Arrays.asList(SOCIAL_DEFAULT_VALUES));
    /**
     *
     */
    public static final String[] MEDIA_DEFAULT_VALUES = new String[]{Integer.toString(Article.Type.MEDIA.getId()), Integer.toString(Article.Type.PDF.getId()), Integer.toString(Article.Type.MOVIE.getId())};
    public static final Set<String> MEDIA_DEFAULT_SET = new HashSet<>(Arrays.asList(MEDIA_DEFAULT_VALUES));
    /**
     *
     */
    public static final String[] ALL_DEFAULT_VALUES = new String[]{Integer.toString(Article.Type.FEED.getId()), Integer.toString(Article.Type.TWEET.getId()), Integer.toString(Article.Type.MEDIA.getId()), Integer.toString(Article.Type.LINK.getId()), Integer.toString(Article.Type.PDF.getId()), Integer.toString(Article.Type.PAPER.getId()), Integer.toString(Article.Type.FB_POST.getId()), Integer.toString(Article.Type.MOVIE.getId())};
    public static final Set<String> ALL_DEFAULT_SET = new HashSet<>(Arrays.asList(ALL_DEFAULT_VALUES));
    /**
     *
     */
    private static final String TAG = "SectionPreferenceDialog";
    /**
     *
     */
    private EditText sectionNameEditText;

    /**
     *
     */
    private String sectionName;

    /**
     *
     */
    private HashMap<Article.Type, Boolean> types = new HashMap<>();

    /**
     *
     */
    private SharedPreferences preference;

    /**
     *
     */
    private OnDialogClosedListener onDialogClosedListener;

    // TODO manages savedInstanceState
    // TODO checks for # first char
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        // Gets layout to set to dialog
        View view = getActivity().getLayoutInflater().inflate(R.layout.section_preference_dialog, null, false);

        // Clears types
        types.clear();

        // Gets preferences
        preference = getContext().getSharedPreferences(SECTION_PREFERENCE_NAME, Context.MODE_PRIVATE);

        // Gets section name edit text
        sectionNameEditText = (EditText) view.findViewById(R.id.section_name_edit_text);

        // Gets list layout
        LinearLayout typeList = (LinearLayout) view.findViewById(R.id.type_list_linear_layout);

        // Adds all types as checboxs
        for (final Article.Type type : Article.Type.values()) {

            // Creates checkbox
            CheckBox checkBox = new CheckBox(getContext());
            checkBox.setText(type.getStringResourceId());
            checkBox.setId(type.getId());
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    // Puts type into sectionTypes
                    types.put(type, isChecked);
                }
            });

            // Adds newly created view
            typeList.addView(checkBox);
        }

        // Creates the dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Sets title
        builder.setTitle(R.string.pref_title_add_section)
                // Sets view
                .setView(view)
                // Sets buttons behaviour
                .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Gets editor
                        SharedPreferences.Editor editor = preference.edit();

                        // Gets newly section name
                        sectionName = sectionNameEditText.getText().toString();

                        // Gets old sections
                        Set<String> sections = new HashSet<>(preference.getStringSet(SECTIONS_NAME, SECTIONS_DEFAULT_SET));

                        // Gets index of the new section name
                        int sectionIndex = sections.size();

                        // Adds the new one
                        if (!sections.add(sectionName)) {
                            // TODO show error section name already used or maybe checks before
                            Log.e(TAG, "Section name already used");
                        }
                        editor.putStringSet(SECTIONS_NAME, sections);
                        editor.putInt("#" + sectionName, sectionIndex);

                        // Gets the string value of types of the new section
                        Set<String> typesString = new HashSet<>();
                        for (Article.Type type : types.keySet()) {
                            typesString.add(Integer.toString(type.getId()));
                        }

                        // Puts them into preference binded to new section
                        editor.putStringSet(sectionName, typesString);

                        // Saves the result
                        editor.apply();

                        // Calls interface
                        if (onDialogClosedListener != null) {
                            onDialogClosedListener.onDialogClosed(true);
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Calls interface
                        if (onDialogClosedListener != null) {
                            onDialogClosedListener.onDialogClosed(false);
                        }
                        dialog.cancel();
                    }
                });

        return builder.create();
    }

    /**
     * @param onDialogClosedListener
     */
    public void setOnDialogClosedListener(OnDialogClosedListener onDialogClosedListener) {
        this.onDialogClosedListener = onDialogClosedListener;
    }
}