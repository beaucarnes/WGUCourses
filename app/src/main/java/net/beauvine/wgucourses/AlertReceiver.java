package net.beauvine.wgucourses;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

public class AlertReceiver extends BroadcastReceiver{

    // Called when a broadcast is made targeting this class
    @Override
    public void onReceive(Context context, Intent intent) {

        createNotification(context, "Important date!", intent.getStringExtra("time") + " date for " + intent.getStringExtra("event"), "Alert");

    }

    private void createNotification(Context context, String msg, String msgText, String msgAlert){

        // Define an Intent and an action to perform with it by another application
        PendingIntent notificIntent = PendingIntent.getActivity(context, 0,
                new Intent(context, MainActivity.class), 0);

        // Builds a notification
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setContentTitle(msg)
                        .setTicker(msgAlert)
                        .setContentText(msgText);

        // Defines the Intent to fire when the notification is clicked
        mBuilder.setContentIntent(notificIntent);

        // Set the default notification option

        mBuilder.setDefaults(Notification.DEFAULT_SOUND);

        // Auto cancels the notification when clicked on in the task bar
        mBuilder.setAutoCancel(true);

        // Gets a NotificationManager which is used to notify the user of the background event
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Post the notification
        mNotificationManager.notify(1, mBuilder.build());

    }
}