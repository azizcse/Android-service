package eyesky.com.androidservice;

import android.app.Service;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class MyJobService extends JobService {
    public MyJobService() {
    }

    @Override
    public boolean onStartJob(final JobParameters jobParameters) {
        jobFinished(jobParameters, false);
        Log.i("CodeRunner", "onStartJob");
        Runnable r = new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Log.i("CodeRunner", "Complete");
                jobFinished(jobParameters, false);
            }
        };

        new Thread(r).start();
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        Log.i("CodeRunner", "onStopJob");
        return false;
    }
}
