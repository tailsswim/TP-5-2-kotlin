
package com.example.networkapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.example.tp52.R;

public class NetworkReceiver extends BroadcastReceiver {

    private static final String CHANNEL_ID = "network_channel";

    @Override
    public void onReceive(Context context, Intent intent) {

        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(
                        Context.CONNECTIVITY_SERVICE);

        boolean connected = false;

        if (cm != null) {
            Network network = cm.getActiveNetwork();

            if (network != null) {
                NetworkCapabilities capabilities =
                        cm.getNetworkCapabilities(network);

                connected = capabilities != null &&
                        capabilities.hasCapability(
                                NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                        capabilities.hasCapability(
                                NetworkCapabilities.NET_CAPABILITY_VALIDATED);
            }
        }

        String title;
        String message;

        if (connected) {
            title = "Connexion réseau";
            message = "Vous êtes connecté à Internet.";
        } else {
            title = "Connexion réseau";
            message = "Vous êtes déconnecté d'Internet.";
        }

        createNotificationChannel(context);

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context, CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_notification)
                        .setContentTitle(title)
                        .setContentText(message)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setAutoCancel(true);

        NotificationManager manager =
                (NotificationManager) context.getSystemService(
                        Context.NOTIFICATION_SERVICE);

        if (manager != null) {
            manager.notify(100, builder.build());
        }
    }

    private void createNotificationChannel(Context context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel =
                    new NotificationChannel(
                            CHANNEL_ID,
                            "Network Notifications",
                            NotificationManager.IMPORTANCE_DEFAULT
                    );

            NotificationManager manager =
                    context.getSystemService(NotificationManager.class);

            manager.createNotificationChannel(channel);
        }
    }
}
