package eyesky.com.androidservice;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.RemoteInput;
import android.support.v4.content.ContextCompat;
import android.widget.RemoteViews;

public class NotificationUtil {

    public static final int REPLY_INTENT_ID = 0;
    public static final int ARCHIVE_INTENT_ID = 1;

    public static final int REMOTE_INPUT_ID = 1247;

    public static final String LABEL_REPLY = "Reply";
    public static final String LABEL_ARCHIVE = "Archive";
    public static final String REPLY_ACTION = "com.hitherejoe.notifi.util.ACTION_MESSAGE_REPLY";
    public static final String KEY_PRESSED_ACTION = "KEY_PRESSED_ACTION";
    public static final String KEY_TEXT_REPLY = "KEY_TEXT_REPLY";
    private static final String KEY_NOTIFICATION_GROUP = "KEY_NOTIFICATION_GROUP";


    public NotificationUtil() {
    }

    public void showStandardNotification(Context context) {
        Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.me);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Standard Notification")
                .setContentText("This is just a standard notification!")
                .setLargeIcon(largeIcon)
                .setColor(ContextCompat.getColor(context, R.color.colorAccent))
                .setAutoCancel(true);
        builder.setPriority(Notification.PRIORITY_HIGH);
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(0, builder.build());
    }

    public void showStandardHeadsUpNotification(Context context) {

        PendingIntent archiveIntent = PendingIntent.getActivity(context,
                ARCHIVE_INTENT_ID,
                getMessageReplyIntent(LABEL_ARCHIVE), PendingIntent.FLAG_UPDATE_CURRENT);


        NotificationCompat.Action replyAction =
                new NotificationCompat.Action.Builder(android.R.drawable.sym_def_app_icon,
                        LABEL_REPLY, archiveIntent)
                        .build();
        NotificationCompat.Action archiveAction =
                new NotificationCompat.Action.Builder(android.R.drawable.sym_def_app_icon,
                        LABEL_ARCHIVE, archiveIntent)
                        .build();

        NotificationCompat.Builder notificationBuider = createNotificationBuider(
                context, "Heads up!", "This is a normal heads up notification");
        notificationBuider.setPriority(Notification.PRIORITY_HIGH).setVibrate(new long[0]);
        notificationBuider.addAction(replyAction);
        notificationBuider.addAction(archiveAction);

        Intent push = new Intent();
        push.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        push.setClass(context, MainActivity.class);

        PendingIntent fullScreenPendingIntent = PendingIntent.getActivity(context, 0,
                push, PendingIntent.FLAG_CANCEL_CURRENT);
        notificationBuider.setFullScreenIntent(fullScreenPendingIntent, true);
        showNotification(context, notificationBuider.build(), 0);
    }

    public void showCustomContentViewNotification(Context context) {
        RemoteViews remoteViews = createRemoteViews(context, R.layout.notification_custom_content);

        Notification.Builder builder = new Notification.Builder(context)
                                           .setSmallIcon(R.mipmap.ic_launcher)
                                           .setAutoCancel(true);
        if(Build.VERSION.SDK_INT >= 24) {
            builder.setCustomContentView(remoteViews).setStyle(new Notification.DecoratedCustomViewStyle());
        }else {
            builder.setContent(remoteViews);
        }
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(0, builder.build());
    }

    private RemoteViews createRemoteViews(Context mContext, int layoutId) {
        RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), layoutId);
        remoteViews.setTextViewText(R.id.text_time, "10:03");

        String flareString = "Active message";
        String flareModeString = "Custom message";
        remoteViews.setTextViewText(R.id.text_flare_current_status, flareString);
        remoteViews.setTextViewText(R.id.text_message, flareModeString);
        remoteViews.setImageViewResource(R.id.image_end, R.mipmap.ic_launcher);

        return remoteViews;
    }

    public static final String START_FOREGROUND_ACTION = "io.left.meshim.action.startforeground";
    public static final String STOP_FOREGROUND_ACTION = "io.left.meshim.action.stopforeground";
    public static final int FOREGROUND_SERVICE_ID = 101;

    public static final String CHANNEL_NAME = "meshim";
    public static final String CHANNEL_ID = "notification_channel";

    public void notificationChannel(Context mContext){
        NotificationCompat.Builder builder;
        Notification mServiceNotification;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager mNotificationManager = (NotificationManager)mContext.getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);

            mNotificationManager.createNotificationChannel(channel);

            builder = new NotificationCompat.Builder(mContext, CHANNEL_ID);
        } else {
            //noinspection deprecation
            builder = new NotificationCompat.Builder(mContext);
        }
        Resources resources = mContext.getResources();
        mServiceNotification = builder.setAutoCancel(false)
                .setTicker(resources.getString(R.string.app_name))
                .setContentTitle(resources.getString(R.string.app_name))
                .setContentText(resources.getString(R.string.app_name))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setOngoing(true)
                .setNumber(100)
                .build();

        NotificationManager mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(0, mServiceNotification);
    }


    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder mBuilder;
    public static final String NOTIFICATION_CHANNEL_ID = "10001";
    public void createNotification(Context mContext, String title, String message)
    {
        /**Creates an explicit intent for an Activity in your app**/
        Intent resultIntent = new Intent(mContext , MainActivity.class);
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent resultPendingIntent = PendingIntent.getActivity(mContext,
                0 /* Request code */, resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder = new NotificationCompat.Builder(mContext);
        mBuilder.setSmallIcon(R.mipmap.ic_launcher);
        mBuilder.setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(false)
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .setContentIntent(resultPendingIntent);

        mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
        {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "NOTIFICATION_CHANNEL_NAME", importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            assert mNotificationManager != null;
            mBuilder.setChannelId(NOTIFICATION_CHANNEL_ID);
            mNotificationManager.createNotificationChannel(notificationChannel);
        }
        assert mNotificationManager != null;
        mNotificationManager.notify(0, mBuilder.build());
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void directInput(Context context){
        // Add the reply button
         /*final String KEY_TEXT_REPLY = "key_text_reply";

        String replyLabel = context.getResources().getString(R.string.reply_label);
        RemoteInput remoteInput = new RemoteInput.Builder(KEY_TEXT_REPLY)
                .setLabel(replyLabel)
                .build();

// Build a PendingIntent for the reply action to trigger.
        PendingIntent replyPendingIntent = PendingIntent.getBroadcast(context.getApplicationContext(),
                        0,
                        getMessageReplyIntent("Replay"),
                        PendingIntent.FLAG_UPDATE_CURRENT);

        // Create the reply action and add the remote input.
        NotificationCompat.Action action =   new NotificationCompat.Action.Builder(R.drawable.circle_view_green,
                        context.getString(R.string.reply_label), replyPendingIntent)
                        .addRemoteInput(remoteInput)
                        .build();

        // Build the notification and add the action.
        Notification newMessageNotification = new Notification.Builder(mContext, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_message)
                .setContentTitle(getString(R.string.title))
                .setContentText(getString(R.string.content))
                .addAction(action)
                .build();

// Issue the notification.
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(1, newMessageNotification);*/

    }


    /*public void showBundledNotifications(Context context) {

        PendingIntent archiveIntent = PendingIntent.getActivity(context,
                ARCHIVE_INTENT_ID,
                getMessageReplyIntent(LABEL_ARCHIVE),
                PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Action replyAction =
                new NotificationCompat.Action.Builder(android.R.drawable.sym_def_app_icon,
                        LABEL_REPLY, archiveIntent)
                        .build();
        NotificationCompat.Action archiveAction =
                new NotificationCompat.Action.Builder(android.R.drawable.sym_def_app_icon,
                        LABEL_ARCHIVE, archiveIntent)
                        .build();

        NotificationCompat.Builder first = createNotificationBuider(
                context, "First notification", "This is the first bundled notification");
        first.setGroupSummary(true).setGroup(KEY_NOTIFICATION_GROUP);

        NotificationCompat.Builder second = createNotificationBuider(
                context, "Second notification", "Here's the second one");
        second.setGroup(KEY_NOTIFICATION_GROUP);

        NotificationCompat.Builder third = createNotificationBuider(
                context, "Third notification", "And another for luck!");
        third.setGroup(KEY_NOTIFICATION_GROUP);
        third.addAction(replyAction);
        third.addAction(archiveAction);

        NotificationCompat.Builder fourth = createNotificationBuider(
                context, "Fourth notification", "This one sin't a part of our group");
        third.setGroup(KEY_NOTIFICATION_GROUP);

        showNotification(context, first.build(), 0);
        showNotification(context, second.build(), 1);
        showNotification(context, third.build(), 2);
        showNotification(context, fourth.build(), 3);
    }

    public void showStandardHeadsUpNotification(Context context) {

        PendingIntent archiveIntent = PendingIntent.getActivity(context,
                ARCHIVE_INTENT_ID,
                getMessageReplyIntent(LABEL_ARCHIVE), PendingIntent.FLAG_UPDATE_CURRENT);



        NotificationCompat.Action replyAction =
                new NotificationCompat.Action.Builder(android.R.drawable.sym_def_app_icon,
                        LABEL_REPLY, archiveIntent)
                        .build();
        NotificationCompat.Action archiveAction =
                new NotificationCompat.Action.Builder(android.R.drawable.sym_def_app_icon,
                        LABEL_ARCHIVE, archiveIntent)
                        .build();

        NotificationCompat.Builder notificationBuider = createNotificationBuider(
                context, "Heads up!", "This is a normal heads up notification");
        notificationBuider.setPriority(Notification.PRIORITY_HIGH).setVibrate(new long[0]);
        notificationBuider.addAction(replyAction);
        notificationBuider.addAction(archiveAction);

        Intent push = new Intent();
        push.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        push.setClass(context, MainActivity.class);

        PendingIntent fullScreenPendingIntent = PendingIntent.getActivity(context, 0,
                push, PendingIntent.FLAG_CANCEL_CURRENT);
        notificationBuider.setFullScreenIntent(fullScreenPendingIntent, true);
        showNotification(context, notificationBuider.build(), 0);
    }
*/
    /*public void showCustomLayoutHeadsUpNotification(Context context) {

        RemoteViews remoteViews = createRemoteViews(context,
                R.layout.notification_custom_content, R.drawable.ic_phonelink_ring_primary_24dp,
                "Heads up!", "This is a custom heads-up notification",
                R.drawable.ic_priority_high_primary_24dp);

        Intent notificationIntent = new Intent(Intent.ACTION_VIEW);
        notificationIntent.setData(Uri.parse("http://www.hitherejoe.com"));
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);

        Notification.Builder builder = createCustomNotificationBuilder(context);
        if(Build.VERSION.SDK_INT >= 24) {
            builder.setCustomContentView(remoteViews)
                    .setStyle(new Notification.DecoratedCustomViewStyle())
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setVibrate(new long[0])
                    .setContentIntent(contentIntent);
        }else {
                 builder.setContent(remoteViews).setStyle(null)
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setVibrate(new long[0])
                    .setContentIntent(contentIntent);
        }

        Intent push = new Intent();
        push.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        push.setClass(context, MainActivity.class);

        PendingIntent fullScreenPendingIntent = PendingIntent.getActivity(context, 0,
                push, PendingIntent.FLAG_CANCEL_CURRENT);
        builder.setFullScreenIntent(fullScreenPendingIntent, true);

        showNotification(context, builder.build(), 0);
    }*/

    /*public void showRemoteInputNotification(Context context) {
        RemoteInput remoteInput = new RemoteInput.Builder(KEY_TEXT_REPLY)
                .setLabel(context.getString(R.string.text_label_reply))
                .build();

        PendingIntent replyIntent = PendingIntent.getActivity(context,
                REPLY_INTENT_ID,
                getMessageReplyIntent(LABEL_REPLY),
                PendingIntent.FLAG_UPDATE_CURRENT);

        PendingIntent archiveIntent = PendingIntent.getActivity(context,
                ARCHIVE_INTENT_ID,
                getMessageReplyIntent(LABEL_ARCHIVE),
                PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Action replyAction =
                new NotificationCompat.Action.Builder(android.R.drawable.sym_def_app_icon,
                        LABEL_REPLY, replyIntent)
                        .addRemoteInput(remoteInput)
                        .build();

        NotificationCompat.Action archiveAction =
                new NotificationCompat.Action.Builder(android.R.drawable.sym_def_app_icon,
                        LABEL_ARCHIVE, archiveIntent)
                        .build();

        NotificationCompat.Builder builder =
                createNotificationBuider(context, "Remote input", "Try typing some text!");
        builder.addAction(replyAction);
        builder.addAction(archiveAction);

        showNotification(context, builder.build(), REMOTE_INPUT_ID);
    }

    public void showCustomContentViewNotification(Context context) {
        RemoteViews remoteViews = createRemoteViews(context,
                R.layout.notification_custom_content, R.drawable.ic_phonelink_ring_primary_24dp,
                "Custom notification", "This is a custom layout",
                R.drawable.ic_priority_high_primary_24dp);

        Notification.Builder builder = createCustomNotificationBuilder(context);
        if(Build.VERSION.SDK_INT >= 24) {
            builder.setCustomContentView(remoteViews).setStyle(new Notification.DecoratedCustomViewStyle());
        }else {
            builder.setContent(remoteViews);
        }

        showNotification(context, builder.build(), 0);
    }

    public void showCustomBigContentViewNotification(Context context) {
        RemoteViews remoteViews = createRemoteViews(context,
                R.layout.notification_custom_big_content, R.drawable.ic_phonelink_ring_primary_24dp,
                "Custom notification", "This one is a little bigger!",
                R.drawable.ic_priority_high_primary_24dp);

        Notification.Builder builder = createCustomNotificationBuilder(context);
        if(Build.VERSION.SDK_INT >= 24) {
            builder.setCustomBigContentView(remoteViews)
                    .setStyle(new Notification.DecoratedCustomViewStyle());
        }else {
            builder.setContent(remoteViews);
        }
        showNotification(context, builder.build(), 0);
    }

    public void showCustomBothContentViewNotification(Context context) {
        RemoteViews remoteViews = createRemoteViews(context, R.layout.notification_custom_content,
                R.drawable.ic_phonelink_ring_primary_24dp, "Custom notification",
                "This is a custom layout", R.drawable.ic_priority_high_primary_24dp);

        RemoteViews bigRemoteView = createRemoteViews(context,
                R.layout.notification_custom_big_content, R.drawable.ic_phonelink_ring_primary_24dp,
                "Custom notification", "This one is a little bigger",
                R.drawable.ic_priority_high_primary_24dp);

        Notification.Builder builder = createCustomNotificationBuilder(context);
        if(Build.VERSION.SDK_INT >= 24) {
            builder.setCustomContentView(remoteViews)
                    .setCustomBigContentView(bigRemoteView)
                    .setStyle(new Notification.DecoratedCustomViewStyle());
        }else {
            builder.setContent(remoteViews);
        }

        showNotification(context, builder.build(), 0);
    }

    public void showCustomMediaViewNotification(Context context) {
        RemoteViews remoteViews = createRemoteViews(context, R.layout.notification_custom_content,
                R.drawable.ic_phonelink_ring_primary_24dp, "Custom media notification",
                "This is a custom media layout", R.drawable.ic_play_arrow_primary_24dp);

        Notification.Builder builder = createCustomNotificationBuilder(context);
        if(Build.VERSION.SDK_INT >= 24) {
            builder.setCustomContentView(remoteViews)
                    .setStyle(new Notification.DecoratedMediaCustomViewStyle());
        }else {
            builder.setContent(remoteViews);
        }
        showNotification(context, builder.build(), 0);
    }*/

    /*private RemoteViews createRemoteViews(Context context, int layout, int iconResource,
                                          String title, String message, int imageResource) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), layout);
        remoteViews.setImageViewResource(R.id.image_icon, iconResource);
        remoteViews.setTextViewText(R.id.text_title, title);
        remoteViews.setTextViewText(R.id.text_message, message);
        remoteViews.setImageViewResource(R.id.image_end, imageResource);

        return remoteViews;
    }

    public Notification.Builder createCustomNotificationBuilder(Context context) {
        return new Notification.Builder(context)
                .setSmallIcon(R.drawable.ic_phonelink_ring_primary_24dp)
                .setAutoCancel(true);
    }*/


    public NotificationCompat.Builder createNotificationBuider(Context context,
                                                               String title, String message) {
        Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.me);
        return new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(message)
                .setLargeIcon(largeIcon)
                .setColor(ContextCompat.getColor(context, R.color.colorAccent))
                .setAutoCancel(true);
    }

    private Intent getMessageReplyIntent(String label) {
        return new Intent()
                .addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES)
                .setAction(REPLY_ACTION)
                .putExtra(KEY_PRESSED_ACTION, label);
    }

    private void showNotification(Context context, Notification notification, int id) {
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(id, notification);
    }
}