// The MIT License (MIT)
//
// Copyright (c) 2015 Claudio Pastorini
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in all
// copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
// SOFTWARE.

package uk.projectchronos.xplorationreader.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

/**
 * \.
 *
 * @author pincopallino93
 * @version 1.0
 */
public class ConnectionHelper {

    /**
     * The TAG used for logging.
     */
    private static final String TAG = "ConnectionHelper";

    /**
     * URL to web page used by Apple in order to verify if the device is really connected or not.
     */
    private static final String APPLE_CAPTIVE_URL = "http://captive.apple.com/";

    /**
     * Default response of http://captive.apple.com/ used in order to verify if there is some
     * redirect or not. If the content is disegual from this would mean that the device is connected
     * to a captive network.
     */
    private static final String APPLE_CAPTIVE_RESPONSE = "<HTML><HEAD><TITLE>Success</TITLE></HEAD><BODY>Success</BODY></HTML>";

    /**
     * Last time in milliseconds when there is no connection.
     */
    public static long lastNoConnectionTs = -1;

    /**
     * True if it is connected to some network, false otherwise.
     */
    public static boolean isConnected = false;

    /**
     * True if the device is connected and it can navigate on the web, false otherwise.
     */
    public static boolean isOnline = false;

    /**
     * Indicates whether network connectivity exists.
     *
     * @param context the context in which checks connectivity.
     * @return true if network connectivity exists, false otherwise.
     */
    public static boolean isConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();

        return activeNetwork != null && activeNetwork.isConnected();
    }

    /**
     * Indicates whether network connectivity exists or is in the process of being established.
     *
     * @param context the context in which checks connectivity.
     * @return true if network connectivity exists or is in the process of being established,
     * false otherwise.
     */
    public static boolean isConnectedOrConnecting(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();

        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    /**
     * Checks if the device can navigate into the web.
     *
     * @return true if can navigate, false otherwise.
     */
    public static boolean isOnline() {

        boolean isOnline = false;
        try {
            isOnline = new CheckConnectionTask().execute().get();
        } catch (InterruptedException | ExecutionException exception) {
            Log.e(TAG, "Error in isOnline", exception);
        }

        return isOnline;
    }

    /**
     * Connection callbacks.
     */
    public interface Callbacks {

        /**
         * This callback must be implemented in the activity in which we want to check network with
         * a ConnectionReceiver.
         *
         * @param isConnected true if the device is connected to some network, false otherwise.
         * @param isOnline    true if the device can navigate on the web, false otherwise.
         */
        void onConnectionChanged(boolean isConnected, boolean isOnline);
    }

    /**
     * Using the same approach of Apple's devices, it tries to connect to a static and simple page
     * and verify if the response body is the same such as the default.
     * If is equal to default we are on the web, otherwise not and we can suppose that the device
     * has to sign in into a captive portal.
     */
    private static class CheckConnectionTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            String responseString = null;

            HttpURLConnection httpURLConnection = null;
            try {
                // Connects to Apple's captive page
                URL url = new URL(APPLE_CAPTIVE_URL);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setInstanceFollowRedirects(false);
                httpURLConnection.setReadTimeout(10000 /* milliseconds */);
                httpURLConnection.setConnectTimeout(15000 /* milliseconds */);
                httpURLConnection.setUseCaches(false);
                // Starts the query
                httpURLConnection.connect();

                // Gets the response string
                InputStream in = new BufferedInputStream(httpURLConnection.getInputStream());
                Scanner scanner = new java.util.Scanner(in).useDelimiter("\\A");
                responseString = scanner.hasNext() ? scanner.next() : "";
            } catch (IOException exception) {
                Log.e(TAG, "Error in CheckConnectionTask", exception);
            } finally {
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
            }

            // Check if the response string is the same as Apple
            return responseString != null && responseString.equals(APPLE_CAPTIVE_RESPONSE);
        }
    }
}