package eyesky.com.androidservice;

/*
*  ****************************************************************************
*  * Created by : Md. Azizul Islam on 3/21/2018 at 6:05 PM.
*  * Email : azizul@w3engineers.com
*  * 
*  * Last edited by : Md. Azizul Islam on 3/21/2018.
*  * 
*  * Last Reviewed by : <Reviewer Name> on <mm/dd/yy>  
*  ****************************************************************************
*/


import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

public class ActivityAidl extends AppCompatActivity {
    IMyAidlInterface mService;

    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = IMyAidlInterface.Stub.asInterface(service);
            try {
                mService.registerActivityCallback(mCallback);
                mService.setForeground(false);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            if(mService != null){
                mService = null;
            }
        }
    };

    IActivity.Stub mCallback = new IActivity.Stub() {
        @Override
        public void onUiUpdate() throws RemoteException {

        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        connectToService();
    }

    @Override
    protected void onResume() {
        super.onResume();
        connectToService();
    }

    @Override
    protected void onPause() {
        super.onPause();
        disconnectFromService();
    }

    private void connectToService() {
        Intent serviceIntent = new Intent(this, BindServiceWithAidl.class);
        startService(serviceIntent);
        bindService(serviceIntent, serviceConnection, BIND_AUTO_CREATE);

    }

    private void disconnectFromService() {
        if (mService != null) {
            try {
                mService.setForeground(true);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            unbindService(serviceConnection);
            mService = null;
        }
    }


}
