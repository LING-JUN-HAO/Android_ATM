package com.example.mynavdrawer;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }
    //edUserid(使用者帳號) &  edPasswd(使用者密碼)
    //比對字串確認成功才登入
    //登入成功------->
    //BANLANCE------>把存款餘額利用SharedPreferences記錄到設定檔test資料標籤BANLANCE裡面 預設10000
    //RECORD------>把交易紀錄利用SharedPreferences記錄到設定檔test資料標籤RECORD裡面 預設空白
    //回傳給Mainactivity 一個RESULT_OK
    //finish結束Login activity
    //登入失敗------->
    //產生dialog對話框 設定標題(高科大校園 Atm) 顯示訊息-->登入失敗 設定確定按鈕
    //取消按鈕------->
    //finish結束Login activity  因為沒有回傳值 所以Mainacvitivy會直接結束
    //清除按鈕-------->
    //edUserid(使用者帳號) &  edPasswd(使用者密碼) 輸入框清空
    public void login(View v){

        EditText edUserid = (EditText) findViewById(R.id.userid);
        EditText edPasswd = (EditText) findViewById(R.id.registration_password);

        String uid = edUserid.getText().toString();
        String pw = edPasswd.getText().toString();

        new Searchuser(this).execute(uid,pw);

        }

    public void cancel(View v){
        finish();
    }

    public void clear(View v){
        EditText edUserid = (EditText) findViewById(R.id.userid);
        EditText edPasswd = (EditText) findViewById(R.id.registration_password);
        edUserid.setText("");
        edPasswd.setText("");
    }

    public void registered(View v){
        EditText edUserid = (EditText) findViewById(R.id.userid);
        EditText edPasswd = (EditText) findViewById(R.id.registration_password);
        edUserid.setText("");
        edPasswd.setText("");
        Intent intent = new Intent(this, Registered.class);
        startActivity(intent);
    }

    public void success(String username){
        EditText edUserid = (EditText) findViewById(R.id.userid);
        EditText edPasswd = (EditText) findViewById(R.id.registration_password);
        String uid = edUserid.getText().toString();
        String pw = edPasswd.getText().toString();


        Toast.makeText(this, username + " 歡迎您的使用", Toast.LENGTH_LONG).show();
        getIntent().putExtra("LOGIN_USERID",uid);
        getIntent().putExtra("LOGIN_PASSWD",pw);

        SharedPreferences user = getSharedPreferences("username", MODE_PRIVATE);
        user.edit()
                .putString("username", username)
                .commit();

        new Gettotal(this).execute(username);

        setResult(RESULT_OK,getIntent());
        finish();
    }

    public void total(String total){
        if(total == null){
            SharedPreferences userbalance = getSharedPreferences("userbalance", MODE_PRIVATE);
            userbalance.edit()
                    .putString("userbalance", "0")
                    .commit();
        }else{
            SharedPreferences userbalance = getSharedPreferences("userbalance", MODE_PRIVATE);
            userbalance.edit()
                    .putString("userbalance", total)
                    .commit();
        }
    }

    public void failure(){
        EditText edUserid = (EditText) findViewById(R.id.userid);
        EditText edPasswd = (EditText) findViewById(R.id.registration_password);
        String uid = edUserid.getText().toString();
        String pw = edPasswd.getText().toString();

        edUserid.setText("");
        edPasswd.setText("");
        new AlertDialog.Builder(this)
                .setTitle("登入失敗")
                .setMessage("使用者帳號或密碼錯誤")
                .setPositiveButton("OK", null)
                .show();
    }


    private class Searchuser extends AsyncTask<String , Integer , Bitmap> {

        public Login activity;

        private int value;
        private String username;

        public Searchuser(Login Loginactivity)
        {
            this.activity = Loginactivity;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            String uid = params[0];
            String pwd = params[1];
            value = UserDatabase
                    .getInstance(getApplicationContext())
                    .getUserDao()
                    .getDataCount(uid,pwd);

            username = UserDatabase
                    .getInstance(getApplicationContext())
                    .getUserDao()
                    .getusername(uid,pwd);

            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if(value == 1){
                success(username);
            }else{
                failure();
            }
        }
    }

    private class Gettotal extends AsyncTask<String , Integer , String> {

        public Login activity;

        public Gettotal(Login Loginactivity)
        {
            this.activity = Loginactivity;
        }

        private String total;
        @Override
        protected String doInBackground(String... params) {

            String username = params[0];

            total = UserDatabase
                    .getInstance(getApplicationContext())
                    .getTransactionDao()
                    .gettotal(username);

            return null;
        }

        @Override
        protected void onPostExecute(String String) {
            super.onPostExecute(String);
            total(total);
        }

    }
}
