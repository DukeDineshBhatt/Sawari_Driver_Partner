package com.dinesh.sawaridriverpartner;


import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.text.Html;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMessagingServ";

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);

        Log.e("NEW_TOKEN", s);

        //sendRegistrationToServer(refreshedToken);
    }


   /* private void sendRegistrationToServer(String refreshedToken) {
        Log.d("TOKEN ", refreshedToken.toString());
    }*/

    Target target = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            sendNotification(bitmap);
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    };

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

           if (remoteMessage.getData() != null)
            getImage(remoteMessage);

        Log.d("asdasd", remoteMessage.getData().toString());

       /* JSONObject object = null;
        object = new JSONObject(remoteMessage.getData());

        try {


            handleNotification(object.getString("title"));
            handleNotification(object.getString("body"));
        } catch (JSONException e) {
            e.printStackTrace();
        }*/

    }

    private void sendNotification(Bitmap bitmap) {

        NotificationCompat.BigPictureStyle style = new NotificationCompat.BigPictureStyle();
        style.bigPicture(bitmap);

        String idChannel = "101";
        Intent mainIntent;

        mainIntent = new Intent(Bean.getContext(), Splash.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(Bean.getContext(), 0, mainIntent, 0);

        NotificationManager mNotificationManager = (NotificationManager) Bean.getContext().getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationChannel mChannel = null;
        // The id of the channel.

        int importance = NotificationManager.IMPORTANCE_HIGH;

        NotificationCompat.Builder builder;

        if (isAppRunning(this)) {
            builder = new NotificationCompat.Builder(Bean.getContext(), idChannel);
            builder.setContentTitle(Bean.getContext().getString(R.string.app_name))
                    .setSmallIcon(R.drawable.sawarilogo)
                    .setAutoCancel(true)
                    .setSound(Uri.parse("android.resource://"
                            + getPackageName() + "/" + R.raw.sound))
                    .setContentIntent(pendingIntent)
                    .setStyle(style)
                    .setContentText(Config.body);
        } else {
            builder = new NotificationCompat.Builder(Bean.getContext(), idChannel);
            builder.setContentTitle(Bean.getContext().getString(R.string.app_name))
                    .setSmallIcon(R.drawable.sawarilogo)
                    .setContentIntent(pendingIntent)
                    .setSound(Uri.parse("android.resource://"
                            + getPackageName() + "/" + R.raw.sound))
                    .setStyle(style)
                    .setAutoCancel(true)
                    .setContentText(Config.body);
        }


       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            @SuppressLint("WrongConstant") NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "Notification", NotificationManager.IMPORTANCE_MAX);

            //Configure Notification Channel
            notificationChannel.setDescription("Game Notifications");
            notificationChannel.enableLights(true);
            notificationChannel.setVibrationPattern(new long[]{1000, 1000, 1000, 1000});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);


            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .setContentTitle(Config.title)
                    .setAutoCancel(true)
                    .setSound(defaultSound)
                    .setContentText(Config.body)
                    .setContentIntent(pendingIntent)
                    .setStyle(style)
                    .setLargeIcon(bitmap)
                    .setWhen(System.currentTimeMillis())
                    .setPriority(Notification.PRIORITY_MAX);


            notificationManager.notify(1, notificationBuilder.build());


        }*/

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            Uri soundUri = Uri.parse(
                    "android.resource://" +
                            getApplicationContext().getPackageName() +
                            "/" +
                            R.raw.sound);

            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .build();

            mChannel = new NotificationChannel(idChannel, Bean.getContext().getString(R.string.app_name), importance);
            // Configure the notification channel.
            mChannel.setDescription(Bean.getContext().getString(R.string.alarm_notification));
            mChannel.enableLights(true);
            mChannel.setSound(soundUri, audioAttributes);
            mChannel.setLightColor(Color.RED);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            if (mNotificationManager != null) {
                mNotificationManager.createNotificationChannel(mChannel);
            }
        } else {
            builder.setContentTitle(Bean.getContext().getString(R.string.app_name))
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setColor(ContextCompat.getColor(Bean.getContext(), R.color.colorPrimary))
                    .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400})
                    .setLights(Color.YELLOW, 500, 5000)
                    .setSound(Uri.parse("android.resource://"
                            + getPackageName() + "/" + R.raw.sound))
                    .setAutoCancel(true);
        }
        if (mNotificationManager != null) {
            mNotificationManager.notify(1, builder.build());
        }

    }

    private void getImage(final RemoteMessage remoteMessage) {

        Map<String, String> data = remoteMessage.getData();
        Config.title = data.get("title");
        Config.body = data.get("body");
        Config.icon = data.get("icon");
        //Create thread to fetch image from notification
        if (remoteMessage.getData() != null) {

            Handler uiHandler = new Handler(Looper.getMainLooper());
            uiHandler.post(new Runnable() {
                @Override
                public void run() {
                    // Get image from data Notification
                    Picasso.with(getApplicationContext())
                            .load(Config.icon)
                            .into(target);
                }
            });
        }
    }


    /*private void handleNotification(String message) {

       // Log.d("notificationData", message);
        String idChannel = "101";
        Intent mainIntent;

        mainIntent = new Intent(Bean.getContext(), Splash.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(Bean.getContext(), 0, mainIntent, 0);

        NotificationManager mNotificationManager = (NotificationManager) Bean.getContext().getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationChannel mChannel = null;
        // The id of the channel.

        int importance = NotificationManager.IMPORTANCE_HIGH;

        NotificationCompat.Builder builder;

        if (isAppRunning(this)) {
            builder = new NotificationCompat.Builder(Bean.getContext(), idChannel);
            builder.setContentTitle(Bean.getContext().getString(R.string.app_name))
                    .setSmallIcon(R.drawable.logo1)
                    .setAutoCancel(true)
                    .setSound(Uri.parse("android.resource://"
                            + getPackageName() + "/" + R.raw.sound))
                    .setContentIntent(pendingIntent)
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText(Html.fromHtml(message)))
                    .setContentText(Html.fromHtml(message));
        } else {
            builder = new NotificationCompat.Builder(Bean.getContext(), idChannel);
            builder.setContentTitle(Bean.getContext().getString(R.string.app_name))
                    .setSmallIcon(R.drawable.logo1)
                    .setContentIntent(pendingIntent)
                    .setSound(Uri.parse("android.resource://"
                            + getPackageName() + "/" + R.raw.sound))
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText(Html.fromHtml(message)))
                    .setAutoCancel(true)
                    .setContentText(Html.fromHtml(message));
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            Uri soundUri = Uri.parse(
                    "android.resource://" +
                            getApplicationContext().getPackageName() +
                            "/" +
                            R.raw.sound);

            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .build();

            mChannel = new NotificationChannel(idChannel, Bean.getContext().getString(R.string.app_name), importance);
            // Configure the notification channel.
            mChannel.setDescription(Bean.getContext().getString(R.string.alarm_notification));
            mChannel.enableLights(true);
            mChannel.setSound(soundUri, audioAttributes);
            mChannel.setLightColor(Color.RED);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            if (mNotificationManager != null) {
                mNotificationManager.createNotificationChannel(mChannel);
            }
        } else {
            builder.setContentTitle(Bean.getContext().getString(R.string.app_name))
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setColor(ContextCompat.getColor(Bean.getContext(), R.color.colorPrimary))
                    .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400})
                    .setLights(Color.YELLOW, 500, 5000)
                    .setSound(Uri.parse("android.resource://"
                            + getPackageName() + "/" + R.raw.sound))
                    .setAutoCancel(true);
        }
        if (mNotificationManager != null) {
            mNotificationManager.notify(1, builder.build());
        }


    }*/


    public static boolean isAppRunning(Context context) {
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
                if (appProcess.importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_PERCEPTIBLE) {
                    return true;
                }
            }
        }
        return false;
    }


}