package eyesky.com.androidservice;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    MyBindService myBindService;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i("CodeRunner", "Received message in receiver");
        }
    };
    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            MyBindService.ServiceBinder serviceBinder = (MyBindService.ServiceBinder) iBinder;
            myBindService = serviceBinder.getService();
            Log.i("CodeRunner", "onServiceConnected");
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            myBindService = null;
            Log.i("CodeRunner", "onServiceDisconnected");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onIntentService(View view) {
        MyIntentService.startActionFoo(MainActivity.this, "Paran1", "param2");

    }

    public void onBindService(View view) {

    }

    public void onStartService(View view) {

    }
    public void onStartJobService(View view) {
        JobScheduler jobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        JobInfo jobInfo = new JobInfo.Builder(101,new ComponentName(this, MyJobService.class))
                .setMinimumLatency(0).build();
        jobScheduler.schedule(jobInfo);
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

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter("intent_service");
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();

        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
    }


}
