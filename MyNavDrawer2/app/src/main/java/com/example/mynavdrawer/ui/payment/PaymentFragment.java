package com.example.mynavdrawer.ui.payment;

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

import com.example.mynavdrawer.R;
import com.example.mynavdrawer.Transaction_record;
import com.example.mynavdrawer.UserDatabase;
import com.example.mynavdrawer.ui.withdrawal.WithdrawalFragment;

import static android.content.Context.MODE_PRIVATE;

public class PaymentFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //fragment_paymentt的畫面(root) 最後會回傳
        View root = inflater.inflate(R.layout.fragment_payment, container, false);

        final ImageView image = (ImageView) root.findViewById(R.id.imagepayment);

        final TextView payment_text = (TextView) root.findViewById(R.id.text_payment);

        //fragment_payment_dialog--->dialog的畫面layout檔案(裡面包含spinner下拉式選單 & 一個Textview(輸入金額) & 一個Edittext(使用者輸入))
        final View payment_layout = inflater.inflate(R.layout.fragment_payment_dialog, null);

        //產生dialog對話框 設定標題(PAYMENT) 顯示的畫面(把fragment_payment_dialog放入)  設定兩個按鈕 確定 & 取消
        //確定之後-----> 使用SharedPreferences讀取資料 把設定檔(test)裡面資料標籤(BALANCE)取出來 並且限定本APP使用(MODE_PRIVATE)
        //PAYMENT--->代表Edittext輸入的金額
        //PAYMENT.matches("")-->確認輸入金額不可為空白
        //使用SharedPreferences讀取資料 把設定檔(test)裡面資料標籤(RECORD取出來 並且限定本APP使用(MODE_PRIVATE)---->這裡的RECORD是代表目前為止的交易紀錄
        //spinner-->下拉式選單  selectedItem-->下拉式選單選到的值
        //detail-----付款項目( selectedItem) & 金額
        //RECORD------>把新的一筆交易紀錄利用SharedPreferences記錄到設定檔test資料標籤RECORD裡面 讓交易紀錄能隨時更新
        new AlertDialog.Builder(getActivity())
                .setTitle("PAYMENT")
                .setView(payment_layout)
                .setIcon(R.drawable.dialog)
                .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        payment_text.setText("省了一筆");
                        image.setImageResource(R.drawable.save2);
                        image.setVisibility(View.VISIBLE);
                    }
                })
                .setNegativeButton("確定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        EditText editText = (EditText) (payment_layout.findViewById(R.id.pyamentedit));

                        String username = getActivity().getSharedPreferences("username", MODE_PRIVATE)
                                .getString("username", "");

                        String userbalance = getActivity().getSharedPreferences("userbalance", MODE_PRIVATE)
                                .getString("userbalance", "");

                        String PAYMENT = editText.getText().toString();
                        if (!PAYMENT.matches("")) {
                            int Total = Integer.parseInt(userbalance) - Integer.parseInt(PAYMENT);
                            if (Total > 0) {

                                Calendar mCal = Calendar.getInstance();
                                CharSequence date = DateFormat.format("yyyy-MM-dd kk:mm:ss", mCal.getTime());

                                String diff = String.valueOf(Integer.parseInt(userbalance) - Integer.parseInt(PAYMENT));

                                SharedPreferences balance = getActivity().getSharedPreferences("userbalance", MODE_PRIVATE);
                                balance.edit()
                                        .putString("userbalance", diff)
                                        .commit();

                                Spinner spinner = (Spinner) payment_layout.findViewById(R.id.spinner);
                                String selectedItem = (String) spinner.getSelectedItem();

                                new paymentmoney().execute(username, PAYMENT, String.valueOf(date),diff,selectedItem);

                                payment_text.setText("付款成功！");
                                image.setImageResource(R.drawable.happy);
                                image.setVisibility(View.VISIBLE);
                            } else {
                                payment_text.setText("付款失敗！");
                                image.setImageResource(R.drawable.shy);
                                image.setVisibility(View.VISIBLE);
                                Toast.makeText(getActivity(), "操作失敗！付款金額大於目前存款", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            payment_text.setText("付款失敗！");
                            image.setImageResource(R.drawable.failure);
                            image.setVisibility(View.VISIBLE);
                            Toast.makeText(getActivity(), "操作失敗！付款金額空白請重新操作", Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .show();

        return root;
    }

    private class paymentmoney extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... params) {
            String username = params[0];
            String paymentmoney = params[1];
            String date = params[2];
            String balance = params[3];
            String note = params[4];


            Transaction_record transaction_record = new Transaction_record();
            transaction_record.setUsername(username);
            transaction_record.setDate(date);
            transaction_record.setTotal(balance);
            transaction_record.setNote(note);
            transaction_record.setType("payment");
            transaction_record.setMoney(paymentmoney);
            UserDatabase
                    .getInstance(getActivity().getApplicationContext())
                    .getTransactionDao()
                    .insert(transaction_record);
            return null;
        }
    }
}