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

    private String TAG = getClass().getName();
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i("CodeRunner", "Received message in receiver");
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onBindService(View view){
        Intent intent = new Intent(this, BindActivity.class);
        startActivity(intent);
    }

    public void onIntentService(View view) {
        MyIntentService.startActionFoo(MainActivity.this, "Paran1", "param2");
    }


    public void onStartAidlService(View view){
        Intent intent = new Intent(this, ActivityAidl.class);
        startActivity(intent);
    }

    public void onStartJobService(View view) {
        /*JobScheduler jobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        JobInfo jobInfo = new JobInfo.Builder(101,new ComponentName(this, MyJobService.class))
                .setMinimumLatency(0).build();
        jobScheduler.schedule(jobInfo);*/

        ComponentName componentName = new ComponentName(this, MyJobService.class);
        JobInfo jobInfo = new JobInfo.Builder(12, componentName)
                .setRequiresCharging(true)
                //.setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                .setMinimumLatency(0)
                .build();

        JobScheduler jobScheduler = (JobScheduler)getSystemService(JOB_SCHEDULER_SERVICE);
        int resultCode = jobScheduler.schedule(jobInfo);
        if (resultCode == JobScheduler.RESULT_SUCCESS) {
            Log.d(TAG, "Job scheduled!");
        } else {
            Log.d(TAG, "Job not scheduled");
        }
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
