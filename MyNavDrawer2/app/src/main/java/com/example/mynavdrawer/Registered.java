package com.example.mynavdrawer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Registered extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registered);
    }

    public void registration_success(View v){

        EditText username = (EditText) findViewById(R.id.username_input);
        EditText edUserid = (EditText) findViewById(R.id.userid);
        EditText edPasswd = (EditText) findViewById(R.id.password_input);
        EditText checkpwd = (EditText) findViewById(R.id.checkpwd_input);
        EditText email = (EditText) findViewById(R.id.email_input);
        EditText phone = (EditText) findViewById(R.id.phone_input);

        String Username = username.getText().toString();
        String uid = edUserid.getText().toString();
        String pw = edPasswd.getText().toString();
        String Check = checkpwd.getText().toString();
        String Email = email.getText().toString();
        String Phone = phone.getText().toString();

        if(pw.equals(Check)){
            new Getregister().execute(Username,uid,pw,Email,Phone);
            this.finish();

        }else{

            Toast.makeText(this,"密碼跟確認密碼不一致!!請重新輸入",Toast.LENGTH_LONG).show();
        }
    }
    public void registration_cancel(View v){
        finish();
    }

    private class Getregister extends AsyncTask<String , Integer , String> {

        @Override
        protected String doInBackground(String... params) {

            String username = params[0];
            String uid = params[1];
            String pwd = params[2];
            String Email = params[3];
            String Phone = params[4];

            User user=new User();
            user.setUsername(username);
            user.setUserID(uid);
            user.setPwd(pwd);
            user.setEmail(Email);
            user.setCellphone(Phone);
            UserDatabase
                    .getInstance(getApplicationContext())
                    .getUserDao()
                    .insert(user);
            return null;
        }
    }
}


