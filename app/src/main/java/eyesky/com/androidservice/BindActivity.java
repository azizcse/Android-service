package eyesky.com.androidservice;

/*
*  ****************************************************************************
*  * Created by : Md. Azizul Islam on 3/21/2018 at 3:10 PM.
*  * Email : azizul@w3engineers.com
*  * 
*  * Last edited by : Md. Azizul Islam on 3/21/2018.
*  * 
*  * Last Reviewed by : <Reviewer Name> on <mm/dd/yy>  
*  ****************************************************************************
*/


import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class BindActivity extends AppCompatActivity {

    MyBindService myBindService;


    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            MyBindService.ServiceBinder serviceBinder = (MyBindService.ServiceBinder) iBinder;
            myBindService = serviceBinder.getService();
            Log.i("CodeRunner", "onServiceConnected");
            myBindService.bindActivity = BindActivity.this;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            myBindService = null;
            Log.i("CodeRunner", "onServiceDisconnected");
        }
    };

    TextView timeTv;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bindservice);
        timeTv = findViewById(R.id.text_view);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, MyBindService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        Log.i("CodeRunner", "Service bind");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("CodeRunner", "Service Unbind");
        unbindService(serviceConnection);

    }

    public void onIntentService(View view){
        myBindService.updateTime();
    }

    public void updateTimeLog(long currentTime) {
        timeTv.setText("Current ime ="+currentTime);
    }
}
