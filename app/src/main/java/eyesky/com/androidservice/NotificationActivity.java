package eyesky.com.androidservice;

/*
*  ****************************************************************************
*  * Created by : Md. Azizul Islam on 3/21/2018 at 6:37 PM.
*  * Email : azizul@w3engineers.com
*  * 
*  * Last edited by : Md. Azizul Islam on 3/21/2018.
*  * 
*  * Last Reviewed by : <Reviewer Name> on <mm/dd/yy>  
*  ****************************************************************************
*/


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class NotificationActivity extends AppCompatActivity {
    private NotificationUtil notificationUtil;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        notificationUtil = new NotificationUtil();
    }

    public void onDefaultNotification(View view){
        notificationUtil.showStandardNotification(this);
    }

    public void onHeadsUpNotification(View view){
        notificationUtil.showStandardHeadsUpNotification(this);
    }

    public void onShowCustomNotification(View view){
        notificationUtil.showCustomContentViewNotification(this);
    }
}
