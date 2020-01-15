package com.example.mynavdrawer;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.os.HandlerThread;
import android.service.autofill.UserData;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    boolean login = false;
    public static final int FUNC_LOGIN = 1;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //當今天login = false 跳轉到Login activity 並且給予一個參數 FUNC_LOGIN
        if(!login){
            Intent intent = new Intent(this, Login.class);
            startActivityForResult(intent, FUNC_LOGIN);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            String channelId  = "default_notification_channel_id";
            String channelName = "default_notification_channel_name";
            NotificationManager notificationManager =
                    getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(new NotificationChannel(channelId,
                    channelName, NotificationManager.IMPORTANCE_LOW));
        }

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();

                        // Log and toast
                        String msg = token;
                        Log.d(TAG, msg);
//                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });

        //app_bar_main.xml
        Toolbar toolbar = findViewById(R.id.toolbar);
        //使用ToolBar控件替代ActionBar控件
        setSupportActionBar(toolbar);

        //右下角郵件圖示
        FloatingActionButton fab = findViewById(R.id.fab);
        //設定fab的onclick的監聽---->點擊之後要處理的事情
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //郵件功能
                //subject(主旨) Save your happiness  傳送對象(to) 0624063@nkust.edu.tw
                Intent mailIntent = new Intent(Intent.ACTION_VIEW);
                Uri data = Uri.parse("mailto:?subject=" + "Save your happiness"+ "&to=" + "0624063@nkust.edu.tw");
                mailIntent.setData(data);
                startActivity(Intent.createChooser(mailIntent, "Send mail..."));
            }
        });
        //DrawerLayout切出nav_view 跟 右邊內容區域activity_main->app_bar_main(toorbar,fab,<include>)->nav_host_fragment
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        //側邊選單_(activity_main)j物件
        NavigationView navigationView = findViewById(R.id.nav_view);
        //AppBarConfiguration 是 NavigationUI 的導覽
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_deposit, R.id.nav_withdrawal,
                R.id.nav_balance, R.id.nav_payment,R.id.nav_transfer,R.id.nav_logout,R.id.nav_paymentdetail)
                .setDrawerLayout(drawer)
                .build();
        //Navigation flows and destinations are determined by the navigation graph owned by the controller
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        //設置Toolbar與一起使用的NavController
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


    //處理activity_main原生的manu_item物件
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_Web_earch:
                Uri webpage = Uri.parse("https://www.nkust.edu.tw/p/422-1000-1001.php");
                Intent webIntent = new Intent(Intent.ACTION_VIEW, webpage);
                startActivity(webIntent);
                return true;
            case R.id.action_settings:
                Toast.makeText(this, "Setting", Toast.LENGTH_LONG).show();
                return true;
            case R.id.action_about:
                Toast.makeText(this, "About", Toast.LENGTH_LONG).show();
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    //這裡處理從Login activity回傳的值
    //requestCode 傳過去的值 resultCode 回傳值
    //比對成功才可進去主畫面
    //否則結束主畫面
  protected void onActivityResult(int requestCode,
                                    int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FUNC_LOGIN){
            if (resultCode == RESULT_OK){
                //這裡只是把 uid 跟 pw 從Login activity那裡回傳回來 並顯示在左下角Logcat上 方便觀察
                String uid = data.getStringExtra("LOGIN_USERID");
                String pw = data.getStringExtra("LOGIN_PASSWD");
                Log.d("RESULT", uid + "/" + pw);
            }else{
                finish();
            }
        }
    }
}



