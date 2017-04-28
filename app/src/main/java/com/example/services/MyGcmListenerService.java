package com.example.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.bean.PushResponse;
import com.example.ogadrive.FragmentHistory;
import com.google.android.gms.gcm.GcmListenerService;

/**
 * Created by Footkar on 7/16/2015.
 */
public class MyGcmListenerService extends GcmListenerService {

    String TAG = "C2DM";
    @Override
    public void onMessageReceived(String from, Bundle data) {
        super.onMessageReceived(from, data);

      final  String message = data.getString("payload");
        final  String userID = data.getString("UserID");
        final PushResponse pushResponse = new PushResponse();
        pushResponse.setMessage(message);
        pushResponse.setUserId(userID);

        Log.d(TAG, "From: " + from);
        Log.d(TAG, "Message: " + message);

        Handler handler = new Handler(Looper.getMainLooper());

        handler.post(new Runnable() {

            @Override
            public void run() {
              //  Toast.makeText(getApplicationContext(), "Push Received : "+message, Toast.LENGTH_SHORT).show();
                FragmentHistory.addPush(pushResponse);

            }
        });

        /**
         * Production applications would usually process the message here.
         * Eg: - Syncing with server.
         *     - Store message in local database.
         *     - Update UI.
         */

        /**
         * In some cases it may be useful to show a notification indicating to the user
         * that a message was received.
         */
       // sendNotification(message);

    }


}
