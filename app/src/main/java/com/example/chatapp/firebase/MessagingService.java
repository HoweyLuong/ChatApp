package com.example.chatapp.firebase;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
/**
 * This class represents a custom Firebase Messaging Service for handling
 * Firebase Cloud Messaging (FCM) events, such as receiving new tokens and messages.
 */

public class MessagingService extends FirebaseMessagingService {
    /**
     * Called when a new FCM token is generated for the device. This token can be used
     * to send targeted messages to the device. Logs the new token for debugging purposes.
     *
     * @param token The new FCM token generated for the device.
     */

    @Override

    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        Log.d("FCM","Token: " +token);
    }
    /**
     * Called when a new FCM message is received. Logs the message body for debugging
     * purposes, which can be used to handle notifications or data messages.
     *
     * @param message The message received from FCM, containing notification data.
     */


    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);
        Log.d("FCM","460 Message: " + message.getNotification().getBody());
    }
}
