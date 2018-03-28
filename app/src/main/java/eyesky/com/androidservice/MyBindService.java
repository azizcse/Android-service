package eyesky.com.androidservice;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class MyBindService extends Service {
    public static final String TAG = MyBindService.class.getName();

    private final Binder mBinder = new ServiceBinder();
    BindActivity bindActivity;

    public MyBindService() {
        Log.i(TAG, "Service created");
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "onBind");
        return mBinder;
    }

    public void updateTime() {
        if(bindActivity != null){
            bindActivity.updateTimeLog(System.currentTimeMillis());
        }
    }

    class ServiceBinder extends Binder {
        MyBindService getService() {
            return MyBindService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.i(TAG, "onUnbind");
        return super.onUnbind(intent);
    }
}
