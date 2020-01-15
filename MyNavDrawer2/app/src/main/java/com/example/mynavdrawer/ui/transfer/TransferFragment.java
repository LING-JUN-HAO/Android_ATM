package com.example.mynavdrawer.ui.transfer;

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
import com.example.mynavdrawer.ui.payment.PaymentFragment;

import static android.content.Context.MODE_PRIVATE;

public class TransferFragment extends Fragment {


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //fragment_transfer的畫面(root) 最後會回傳
        View root = inflater.inflate(R.layout.fragment_transfer, container, false);

        final ImageView image = (ImageView) root.findViewById(R.id.imagetransfer);

        final TextView transfer_text = (TextView) root.findViewById(R.id. text_transfer);


        //fragment_payment_dialog--->dialog的畫面layout檔案(裡面包含spinner下拉式選單 & 兩個Textview(郵局帳號 & 輸入金額) & 兩個個Edittext(使用者輸入帳號 & 使用者輸入金額))
        final View fragment_transfer_dialog = inflater.inflate(R.layout.fragment_transfer_dialog, null);

        //產生dialog對話框 設定標題(TRANSFER) 顯示的畫面(把fragment_transfer_dialog放入)  設定兩個按鈕 確定 & 取消
        //宣告bank_username 物件(Edittext)-->使用者輸入帳號
        //宣告money 物件(Edittext)-->使用者輸入金額
        //確定之後-----> 使用SharedPreferences讀取資料 把設定檔(test)裡面資料標籤(BALANCE)取出來 並且限定本APP使用(MODE_PRIVATE)
        //BANK_NAME--->代表Edittext輸入的郵局帳號
        //BANKMONEY--->代表Edittext輸入的金額
        //BANK_NAME.matches("") & BANKMONEY.matches("")-->確認輸入金額不可為空白
        //BANLANCE 減掉 本次轉帳金額(BANKMONEY)
        //BANLANCE------>把新的餘額利用SharedPreferences記錄到設定檔test資料標籤BANLANCE裡面 讓目前餘額能隨時更新
        //使用SharedPreferences讀取資料 把設定檔(test)裡面資料標籤(RECORD取出來 並且限定本APP使用(MODE_PRIVATE)---->這裡的RECORD是代表目前為止的交易紀錄
        //spinner(R.id.bankspinner)-->下拉式選單  selectedItem-->下拉式選單選到的值
        //detail-----轉帳銀行( selectedItem) & 郵局帳號(BANK_NAME) & 郵局金額(BANKMONEY)
        //RECORD------>把新的一筆交易紀錄利用SharedPreferences記錄到設定檔test資料標籤RECORD裡面 讓交易紀錄能隨時更新
        new AlertDialog.Builder(getActivity())
                .setTitle("TRANSFER")
                .setView(fragment_transfer_dialog)
                .setIcon(R.drawable.dialog)
                .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        transfer_text.setText("省了一筆");
                        image.setImageResource(R.drawable.save2);
                        image.setVisibility(View.VISIBLE);
                    }
                })
                .setNegativeButton("確定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        EditText bank_username = (EditText) (fragment_transfer_dialog.findViewById(R.id.bank_username));

                        EditText money = (EditText) (fragment_transfer_dialog.findViewById(R.id.money_input));

                        String username = getActivity().getSharedPreferences("username", MODE_PRIVATE)
                                .getString("username", "");

                        String userbalance = getActivity().getSharedPreferences("userbalance", MODE_PRIVATE)
                                .getString("userbalance", "");


                        String BANK_NAME = bank_username.getText().toString();
                        String BANKMONEY = money.getText().toString();

                        if(!BANK_NAME.matches("") && !BANKMONEY.matches("")) {
                            int Total = Integer.parseInt(userbalance) - Integer.parseInt(BANKMONEY);

                            if(Total > 0) {
                                Calendar mCal = Calendar.getInstance();
                                CharSequence date = DateFormat.format("yyyy-MM-dd kk:mm:ss", mCal.getTime());

                                String diff = String.valueOf(Integer.parseInt(userbalance) - Integer.parseInt(BANKMONEY));

                                SharedPreferences balance = getActivity().getSharedPreferences("userbalance", MODE_PRIVATE);
                                balance.edit()
                                        .putString("userbalance", diff)
                                        .commit();

                                Spinner spinner = (Spinner) fragment_transfer_dialog.findViewById(R.id.bankspinner);
                                String selectedItem = (String) spinner.getSelectedItem();


                                new transfermoney().execute(username, BANKMONEY, String.valueOf(date),diff,selectedItem + " : " + BANK_NAME);

                                transfer_text.setText("轉帳成功！");
                                image.setImageResource(R.drawable.happy);
                                image.setVisibility(View.VISIBLE);

                            }else{
                                transfer_text.setText("轉帳失敗！");
                                image.setImageResource(R.drawable.shy);
                                image.setVisibility(View.VISIBLE);
                                Toast.makeText(getActivity(),"操作失敗！轉帳金額大於目前存款",Toast.LENGTH_LONG).show();
                            }
                        }else{
                            transfer_text.setText("轉帳失敗！");
                            image.setImageResource(R.drawable.failure);
                            image.setVisibility(View.VISIBLE);
                            Toast.makeText(getActivity(),"付款金額或郵局帳號空白請重新操作",Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .show();
        return root;
    }

    private class transfermoney extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... params) {
            String username = params[0];
            String bankmoney = params[1];
            String date = params[2];
            String balance = params[3];
            String note = params[4];


            Transaction_record transaction_record = new Transaction_record();
            transaction_record.setUsername(username);
            transaction_record.setDate(date);
            transaction_record.setTotal(balance);
            transaction_record.setNote(note);
            transaction_record.setType("transfer");
            transaction_record.setMoney(bankmoney);
            UserDatabase
                    .getInstance(getActivity().getApplicationContext())
                    .getTransactionDao()
                    .insert(transaction_record);
            return null;
        }
    }
}