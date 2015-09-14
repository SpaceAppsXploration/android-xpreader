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
