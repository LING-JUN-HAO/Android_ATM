package com.example.mynavdrawer.ui.deposit;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.icu.util.Calendar;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.text.SpannableString;
import android.text.format.DateFormat;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.mynavdrawer.Login;
import com.example.mynavdrawer.MainActivity;
import com.example.mynavdrawer.R;
import com.example.mynavdrawer.Transaction_record;
import com.example.mynavdrawer.User;
import com.example.mynavdrawer.UserDatabase;
import com.example.mynavdrawer.ui.balance.BalanceFragment;

import static android.content.Context.MODE_PRIVATE;

public class DepositFragment extends Fragment {

    public View onCreateView(@NonNull final LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //fragment_deposit的畫面(root) 最後會回傳
        View root = inflater.inflate(R.layout.fragment_deposit, container, false);

        //宣告一個EditText的物件
        final EditText input = new EditText(getActivity());
        input.setInputType(InputType.TYPE_CLASS_NUMBER);

        final ImageView image = (ImageView) root.findViewById(R.id.imagedeposit);

        final TextView deposit_text = (TextView) root.findViewById(R.id. text_deposit);

        //產生dialog對話框 設定標題(DEPOSIT) 顯示的畫面(把EditText放入) 顯示訊息-->請輸入存款金額 設定兩個按鈕 確定 & 取消
        //確定之後-----> 使用SharedPreferences讀取資料 把設定檔(test)裡面資料標籤(BALANCE)取出來 並且限定本APP使用(MODE_PRIVATE)
        //DEPOSIT--->代表Edittext輸入的金額
        //DEPOSIT.matches("")-->確認輸入金額不可為空白
        //BANLANCE 加上 本次存款金額(DEPOSIT)
        //BANLANCE------>把新餘額利用SharedPreferences記錄到設定檔test資料標籤BANLANCE裡面 讓目前餘額能隨時更新
        //使用SharedPreferences讀取資料 把設定檔(test)裡面資料標籤(RECORD取出來 並且限定本APP使用(MODE_PRIVATE)---->這裡的RECORD是代表目前為止的交易紀錄
        //detail-----使用者自行存款  & 金額
        //RECORD------>把新的一筆交易紀錄利用SharedPreferences記錄到設定檔test資料標籤RECORD裡面 讓交易紀錄能隨時更新

        new AlertDialog.Builder(getActivity())
                .setTitle("DEPOSIT")
                .setView(input)
                .setIcon(R.drawable.dialog)
                .setMessage("請輸入存款金額")
                .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        deposit_text.setText("荷包空空！");
                        image.setImageResource(R.drawable.cancel2);
                        image.setVisibility(View.VISIBLE);
                    }
                })
                .setNegativeButton("確定",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        String username = getActivity().getSharedPreferences("username", MODE_PRIVATE)
                                .getString("username", "");

                        String userbalance = getActivity().getSharedPreferences("userbalance", MODE_PRIVATE)
                                .getString("userbalance", "");

                        String DEPOSIT = input.getText().toString();

                        if(!DEPOSIT.matches("")){
                            userbalance = String.valueOf(Integer.parseInt(userbalance) + Integer.parseInt(DEPOSIT)) ;

                            SharedPreferences balance = getActivity().getSharedPreferences("userbalance", MODE_PRIVATE);
                            balance.edit()
                                    .putString("userbalance", userbalance)
                                    .commit();

                            Calendar mCal = Calendar.getInstance();
                            CharSequence date = DateFormat.format("yyyy-MM-dd kk:mm:ss", mCal.getTime());

                            new depositmoney().execute(username,DEPOSIT,String.valueOf(date),userbalance);

                            deposit_text.setText("存款成功！");
                            image.setImageResource(R.drawable.happy);
                            image.setVisibility(View.VISIBLE);

                        }else{
                            deposit_text.setText("存款失敗！");
                            image.setImageResource(R.drawable.failure);
                            image.setVisibility(View.VISIBLE);
                            Toast.makeText(getActivity(),"存款金額空白請重新操作",Toast.LENGTH_LONG).show();
                        }
                        }
                    })
                .show();
        return root;
    }

    private class depositmoney extends AsyncTask<String , Integer , String> {

        private int value;

        @Override
        protected String doInBackground(String... params) {

            String username = params[0];
            String depositmoney = params[1];
            String date = params[2];
            String balance = params[3];

            Transaction_record transaction_record=new Transaction_record();
            transaction_record.setUsername(username);
            transaction_record.setDate(date);
            transaction_record.setTotal(balance);
            transaction_record.setNote("自行存款");
            transaction_record.setType("deposit");
            transaction_record.setMoney(depositmoney);
            UserDatabase
                    .getInstance(getActivity().getApplicationContext())
                    .getTransactionDao()
                    .insert(transaction_record);
            return null;
        }
        protected void onPostExecute(String string) {
            super.onPostExecute(string);

        }
    }
}