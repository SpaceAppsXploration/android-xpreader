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

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.util.Log;

import uk.projectchronos.xplorationreader.BuildConfig;

/**
 * BroadcastReceiver useful in order to listen for connection changes.
 * <p/>
 * Initialize in onCreate() and register into onResume() and unRegister into onPause().
 * <p/>
 * You have to implement ConnectionHelper.Callbacks in order to receive updates otherwise a
 * IllegalStateException is raised.
 *
 * @author pincopallino93
 * @version 1.0
 */
public class ConnectionReceiver extends BroadcastReceiver {

    /**
     * The TAG used for logging.
     */
    private final String TAG = this.getClass().getCanonicalName();

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (!(context instanceof ConnectionHelper.Callbacks)) {
            throw new IllegalStateException("Activity must implement ConnectionHelper.Callbacks.");
        }

        // Binds to callbacks
        ConnectionHelper.Callbacks callbacks = (ConnectionHelper.Callbacks) context;

        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(action)) {

            if (BuildConfig.DEBUG) Log.i(TAG, "Connection is changed!");

            // Checks internet connection
            if (!ConnectionHelper.isConnectedOrConnecting(context)) {
                boolean show = false;
                // First time
                if (ConnectionHelper.lastNoConnectionTs == -1) {
                    show = true;
                    ConnectionHelper.lastNoConnectionTs = System.currentTimeMillis();
                } else {
                    // Checks after a second
                    if (System.currentTimeMillis() - ConnectionHelper.lastNoConnectionTs > 1000) {
                        show = true;
                        ConnectionHelper.lastNoConnectionTs = System.currentTimeMillis();
                    }
                }

                if (show && ConnectionHelper.isConnected) {
                    ConnectionHelper.isConnected = false;
                }
            } else {
                ConnectionHelper.isConnected = true;
            }

            // Checks if is online
            ConnectionHelper.isOnline = ConnectionHelper.isConnected && ConnectionHelper.isOnline();

            // Changes connection status using callback
            callbacks.onConnectionChanged(ConnectionHelper.isConnected, ConnectionHelper.isOnline);
        }
    }

    /**
     * Register a ConnectionReceiver to be run in the main activity thread matching changes in
     * network connectivity.
     *
     * @param context the application context.
     */
    public void register(Context context) {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        context.registerReceiver(this, filter);
        Log.d(TAG, "ConnectionReceiver registered!");
    }

    /**
     * Unregister a previously registered ConnectionReceiver.
     *
     * @param context the application context in wich BroadcastReceiver was registered.
     */
    public void unRegister(Context context) {
        context.unregisterReceiver(this);
        Log.d(TAG, "ConnectionReceiver unregistered!");
    }
}
