package com.example.mynavdrawer.ui.logout;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.mynavdrawer.Login;
import com.example.mynavdrawer.MainActivity;
import com.example.mynavdrawer.R;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

public class LogoutFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //fragment_logout的畫面(root) 最後會回傳
        View root = inflater.inflate(R.layout.fragment_logout, container, false);

        final ImageView image = (ImageView) root.findViewById(R.id.imagelogout);

        final TextView logout_text = (TextView) root.findViewById(R.id. text_logout);
        //產生dialog對話框 設定標題(LOGOUT) 顯示訊息-->您確定要登出嗎 設定兩個按鈕 確認 & 取消
        //當點擊確認後跳轉到Mainactivity並結束程式 並且跳出浮動訊息告知使用者 登出成功 請重新登入
        new AlertDialog.Builder(getActivity())
                .setTitle("LOGOUT")
                .setIcon(R.drawable.dialog)
                .setMessage("您確定要登出嗎?")
                .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        logout_text.setText("感謝您的支持");
                        image.setImageResource(R.drawable.logout);
                        image.setVisibility(View.VISIBLE);
                    }
                })
                .setNegativeButton("確定",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        startActivity(intent);
                        Toast.makeText(getActivity(), "登出成功 請重新登入", Toast.LENGTH_LONG).show();
                        getActivity().finish();
                    }
                })
                .show();
        return root;
    }
}