package com.example.mynavdrawer.ui.withdrawal;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.icu.util.Calendar;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.mynavdrawer.Login;
import com.example.mynavdrawer.R;
import com.example.mynavdrawer.Transaction_record;
import com.example.mynavdrawer.UserDatabase;

import static android.content.Context.MODE_PRIVATE;

public class WithdrawalFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //fragment_withdrawal的畫面(root) 最後會回傳
        View root = inflater.inflate(R.layout.fragment_withdrawal, container, false);

        final ImageView image = (ImageView) root.findViewById(R.id.imagewithdrawal);

        final TextView withdrawal_text = (TextView) root.findViewById(R.id. text_withdrawal);

        //宣告一個EditText的物件
        final EditText input = new EditText(getActivity());
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        //產生dialog對話框 設定標題(WITHDRAWAL) 顯示的畫面(把EditText放入) 顯示訊息-->請輸入提款金額 設定兩個按鈕 確定 & 取消
        //確定之後-----> 使用SharedPreferences讀取資料 把設定檔(test)裡面資料標籤(BALANCE)取出來 並且限定本APP使用(MODE_PRIVATE)
        //WITHDRAWAL--->代表Edittext輸入的金額
        //WITHDRAWAL.matches("")-->確認輸入金額不可為空白
        //BANLANCE 減掉 本次提款金額(WITHDRAWAL)
        //BANLANCE------>把新的餘額利用SharedPreferences記錄到設定檔test資料標籤BANLANCE裡面 讓目前餘額能隨時更新
        //使用SharedPreferences讀取資料 把設定檔(test)裡面資料標籤(RECORD取出來 並且限定本APP使用(MODE_PRIVATE)---->這裡的RECORD是代表目前為止的交易紀錄
        //detail-----使用者自行提款 & 金額
        //RECORD------>把新的一筆交易紀錄利用SharedPreferences記錄到設定檔test資料標籤RECORD裡面 讓交易紀錄能隨時更新
        new AlertDialog.Builder(getActivity())
                .setTitle("WITHDRAWAL")
                .setView(input)
                .setIcon(R.drawable.dialog)
                .setMessage("請輸入提款金額")
                .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        withdrawal_text.setText("省了一筆！");
                        image.setImageResource(R.drawable.save2);
                        image.setVisibility(View.VISIBLE);
                    }
                })
                .setNegativeButton("確定",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String username = getActivity().getSharedPreferences("username", MODE_PRIVATE)
                                .getString("username", "");

                        String userbalance = getActivity().getSharedPreferences("userbalance", MODE_PRIVATE)
                                .getString("userbalance", "");

                        String WITHDRAWAL = input.getText().toString();
                        if (!WITHDRAWAL.matches("")) {
                            if(Integer.parseInt(WITHDRAWAL)< Integer.parseInt(userbalance)) {
                                Calendar mCal = Calendar.getInstance();
                                CharSequence date = DateFormat.format("yyyy-MM-dd kk:mm:ss", mCal.getTime());

                                String diff = String.valueOf(Integer.parseInt(userbalance) - Integer.parseInt(WITHDRAWAL));

                                SharedPreferences balance = getActivity().getSharedPreferences("userbalance", MODE_PRIVATE);
                                balance.edit()
                                        .putString("userbalance", diff)
                                        .commit();

                                new withdrawlmoney().execute(username, WITHDRAWAL, String.valueOf(date),diff,WITHDRAWAL);

                                withdrawal_text.setText("提款成功！");
                                image.setImageResource(R.drawable.happy);
                                image.setVisibility(View.VISIBLE);

                            }else{
                                withdrawal_text.setText("提款失敗！");
                                image.setImageResource(R.drawable.shy);
                                image.setVisibility(View.VISIBLE);
                                Toast.makeText(getActivity(),"操作失敗！提款金額大於目前存款",Toast.LENGTH_LONG).show();
                            }

                        }else{
                            withdrawal_text.setText("提款失敗！");
                            image.setImageResource(R.drawable.failure);
                            image.setVisibility(View.VISIBLE);
                            Toast.makeText(getActivity(),"存款金額空白請重新操作",Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .show();
        return root;
    }


    private class withdrawlmoney extends AsyncTask<String , Integer , String> {
        @Override
        protected String doInBackground(String... params) {
            String username = params[0];
            String withdrawmoney = params[1];
            String date = params[2];
            String balance = params[3];

            Transaction_record transaction_record=new Transaction_record();
            transaction_record.setUsername(username);
            transaction_record.setDate(date);
            transaction_record.setTotal(balance);
            transaction_record.setNote("自行提款");
            transaction_record.setType("withdrawal");
            transaction_record.setMoney(withdrawmoney);
            UserDatabase
                    .getInstance(getActivity().getApplicationContext())
                    .getTransactionDao()
                    .insert(transaction_record);
            return null;
        }
    }
}