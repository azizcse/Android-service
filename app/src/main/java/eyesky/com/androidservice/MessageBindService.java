package eyesky.com.androidservice;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

/**
 * *************************************************************
 * * Created by Md. Azizul Islam on 3/22/2018 at 06:10 AM.
 * * Email: imazizul@gmail.com
 * *
 * * Company: W3engineers Ltd
 * * Last edited by : Md. Azizul Islam on 3/22/2018.
 * *
 * * Last Reviewed by : <Reviewer Name> on <mm/dd/yy>
 * *************************************************************
 */


public class MessageBindService extends Service {
    /**
     * Command to the service to display a message
     */
    static final int MSG_SAY_HELLO = 1;
    static final int RUN_FOREGROUND = 2;
    static final int RUN_BACKGROUND = 3;

    public static final String START_FOREGROUND_ACTION = "io.left.meshim.action.startforeground";
    public static final String STOP_FOREGROUND_ACTION = "io.left.meshim.action.stopforeground";
    public static final int FOREGROUND_SERVICE_ID = 101;

    public static final String CHANNEL_NAME = "meshim";
    public static final String CHANNEL_ID = "notification_channel";

    private Notification mServiceNotification;
    private boolean mIsForeground = false;
    private int mVisibleActivities = 0;


    @Override
    public void onCreate() {
        super.onCreate();

        Intent stopForegroundIntent = new Intent(this, MessageBindService.class);
        stopForegroundIntent.setAction(STOP_FOREGROUND_ACTION);
        PendingIntent pendingIntent
                = PendingIntent.getService(this, 0, stopForegroundIntent, 0);
        NotificationCompat.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager mNotificationManager = (NotificationManager) getApplicationContext()
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT);
            mNotificationManager.createNotificationChannel(channel);
            builder = new NotificationCompat.Builder(this, CHANNEL_ID);
        } else {
            //noinspection deprecation
            builder = new NotificationCompat.Builder(this);
        }
        Resources resources = getResources();
        mServiceNotification = builder.setAutoCancel(false)
                .setTicker(resources.getString(R.string.app_name))
                .setContentTitle("Service")
                .setContentText("Service is Running")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)
                .setOngoing(true)
                .setNumber(100)
                .build();
    }

    /**
     * Handler of incoming messages from clients.
     */
    class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_SAY_HELLO:
                    Toast.makeText(getApplicationContext(), "hello!", Toast.LENGTH_SHORT).show();
                    break;
                case RUN_BACKGROUND:
                    startInForeground();
                    break;
                case RUN_FOREGROUND:
                    stopForeground(true);
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        String action = null;
        if (intent != null) {
            action = intent.getAction();
        }
        if (action != null && action.equals(STOP_FOREGROUND_ACTION)) {
            if (mIsForeground) {
                stopForeground(true);
                mIsForeground = false;
            }
            stopSelf();

        } else if (action != null && action.equals(START_FOREGROUND_ACTION)) {
            startInForeground();
        }
        return START_STICKY;
    }

    private void startInForeground() {
        startForeground(FOREGROUND_SERVICE_ID, mServiceNotification);
    }

    /**
     * Target we publish for clients to send messages to IncomingHandler.
     */
    final Messenger mMessenger = new Messenger(new IncomingHandler());

    /**
     * When binding to the service, we return an interface to our messenger
     * for sending messages to the service.
     */
    @Override
    public IBinder onBind(Intent intent) {
        Toast.makeText(getApplicationContext(), "binding", Toast.LENGTH_SHORT).show();
        return mMessenger.getBinder();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        android.os.Process.killProcess(android.os.Process.myPid());
    }
}
