package com.example.natan.backgroundtasks.BackGroundTasks;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;

import com.example.natan.backgroundtasks.AsyncTaskLoader.MainActivityAsyncLoader;
import com.example.natan.backgroundtasks.MainActivity;
import com.example.natan.backgroundtasks.R;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by natan on 2/7/2018.
 */

public class Notif {

    private NotificationManager mNotifyManager;
    private static final int NOTIFICATION_ID = 0;


    public static void notify(Context context) {

        // on pressing the Notification
        Intent notificationIntent = new Intent(context, MainActivityAsyncLoader.class);

        PendingIntent notificationPendingIntent = PendingIntent.getActivity(context,
                NOTIFICATION_ID, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);


        // For displaying big picture in notification

        Bitmap androidImage = BitmapFactory
                .decodeResource(context.getResources(),R.drawable.goku);


        NotificationManager mNotifyManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

        NotificationCompat.Builder notifyBuilder = new NotificationCompat.Builder(context)
                .setContentTitle("You've been notified!")
                .setContentText("This is your notification text.")
                // for big picture notification
                .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(androidImage))

                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setSmallIcon(R.drawable.ic_stat_name_notif)
                //setting content for when the notification will be pressed it will launch the app
                .setContentIntent(notificationPendingIntent);
        Notification myNotification = notifyBuilder.build();
        mNotifyManager.notify(NOTIFICATION_ID, myNotification);

    }


}
