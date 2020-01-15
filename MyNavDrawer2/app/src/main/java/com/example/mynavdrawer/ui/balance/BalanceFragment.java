package com.example.mynavdrawer.ui.balance;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.mynavdrawer.R;

import static android.content.Context.MODE_PRIVATE;

public class BalanceFragment extends Fragment {


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        //fragment_balance的畫面(root) 最後會回傳
        View root = inflater.inflate(R.layout.fragment_balance, container, false);

        final ImageView image = (ImageView) root.findViewById(R.id.imagebalance);

        final TextView withdrawal_text = (TextView) root.findViewById(R.id. text_balance);

        //使用SharedPreferences讀取資料 把設定檔(test)裡面資料標籤(BALANCE)取出來 並且限定本APP使用(MODE_PRIVATE)
        String userbalance = getActivity().getSharedPreferences("userbalance", MODE_PRIVATE)
                .getString("userbalance", "");

        //產生dialog對話框 設定標題(BALANCE) 顯示訊息-->這裡代表金額(設定檔BANLANCE資料) 設定單一按鈕確定
        new AlertDialog.Builder(getActivity())
                .setTitle("BALANCE")
                .setIcon(R.drawable.dialog)
                .setMessage(userbalance)
                .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        withdrawal_text.setText("越來越多錢");
                        image.setImageResource(R.drawable.balance2);
                        image.setVisibility(View.VISIBLE);
                    }
                })
                .show();
        return root;
    }
}