package com.example.mynavdrawer.ui.record;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.mynavdrawer.Login;
import com.example.mynavdrawer.R;
import com.example.mynavdrawer.Transaction_record;
import com.example.mynavdrawer.UserDatabase;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class RecordFragment extends Fragment {

//    private LinearLayout liner = (LinearLayout)getActivity().findViewById(R.id.Liner);

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //fragment_pyamentdetail的畫面(root) 最後會回傳
        View root = inflater.inflate(R.layout.fragment_pyamentdetail, container, false);

        String username = getActivity().getSharedPreferences("username", MODE_PRIVATE)
                .getString("username", "");

        new record(this).execute(username);

        return root;
    }

    public void success(List<Transaction_record> data){

        TextView record = (TextView) getActivity().findViewById(R.id.paymentdetail);
        String msg;

        String username = getActivity().getSharedPreferences("username", MODE_PRIVATE)
                .getString("username", "");

        String userbalance = getActivity().getSharedPreferences("userbalance", MODE_PRIVATE)
                .getString("userbalance", "");

        record.setText(username + "您目前帳戶餘額   " + userbalance + '\n');

        for(Transaction_record transaction_record:data){
            record.setMovementMethod(ScrollingMovementMethod.getInstance());
            msg = '\n' +"date:" + transaction_record.getDate() +'\n' + "type:" + transaction_record.getType() + '\n' +"money:" + transaction_record.getMoney() + '\n' + "note:" + transaction_record.getNote() + '\n';
            record.append(msg);
        }


    }

    private class record extends AsyncTask<String , Integer , String> {

        public RecordFragment activity;

        private List<Transaction_record> data;

        public record(RecordFragment Record)
        {
            this.activity = Record;
        }

        @Override
        protected String doInBackground(String... params) {

            String username = params[0];

            data = UserDatabase
                    .getInstance(getActivity().getApplicationContext())
                    .getTransactionDao()
                    .getdata(username);

            return null;
        }
        protected void onPostExecute(String string) {
            super.onPostExecute(string);

            success(data);

        }
    }
}