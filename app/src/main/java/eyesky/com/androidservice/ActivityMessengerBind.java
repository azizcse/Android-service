package eyesky.com.androidservice;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * *************************************************************
 * * Created by Md. Azizul Islam on 3/22/2018 at 06:13 AM.
 * * Email: imazizul@gmail.com
 * *
 * * Company: W3engineers Ltd
 * * Last edited by : Md. Azizul Islam on 3/22/2018.
 * *
 * * Last Reviewed by : <Reviewer Name> on <mm/dd/yy>
 * *************************************************************
 */


public class ActivityMessengerBind extends AppCompatActivity {
    /** Messenger for communicating with the service. */
    Messenger mService = null;

    /** Flag indicating whether we have called bind on the service. */
    boolean mBound;

    /**
     * Class for interacting with the main interface of the service.
     */
    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            mService = new Messenger(service);
            mBound = true;
            keepServiceRunningInForeground();
        }

        public void onServiceDisconnected(ComponentName className) {
            mService = null;
            mBound = false;
        }
    };

    public void sayHello(View v) {
        if (!mBound) return;
        Message msg = Message.obtain(null, MessageBindService.MSG_SAY_HELLO, 0, 0);
        try {
            mService.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_bind);
    }

    @Override
    protected void onStart() {
        super.onStart();
        keepServiceRunningInForeground();
        serviceBindAndStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        keepServiceRunningInBackground();
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }
    }


    private void serviceBindAndStart(){
        Intent intent = new Intent(this, MessageBindService.class);
        bindService(intent, mConnection,Context.BIND_AUTO_CREATE);
        startService(intent);
    }

    private void keepServiceRunningInBackground(){
        Message msg = Message.obtain(null, MessageBindService.RUN_BACKGROUND, 0, 0);
        try {
            mService.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    private void keepServiceRunningInForeground(){
        Message msg = Message.obtain(null, MessageBindService.RUN_FOREGROUND, 0, 0);
        try {
            if(mService != null)
                mService.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
