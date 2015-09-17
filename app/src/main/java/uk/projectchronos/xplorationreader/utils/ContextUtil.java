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

import android.content.Context;

/**
 * ContextUtil class.
 *
 * @author pincopallino93
 * @version 1.1
 */
public class ContextUtil {

    /**
     * Static context.
     */
    private static Context context = null;

    /**
     * Method that allow to retrieve the context "stored".
     *
     * @return the context "stored".
     */
    public static Context getContext() {
        return ContextUtil.context;
    }

    /**
     * Method that allow to set context with the actual context.
     *
     * @param context the context to be "store".
     */
    public static void setContext(Context context) {
        ContextUtil.context = context;
    }
}
