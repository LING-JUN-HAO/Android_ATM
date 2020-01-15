package com.example.mynavdrawer.ui.home;

import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.mynavdrawer.R;

public class HomeFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.home_2, container, false);

        final TextView button1_1 = (TextView) view.findViewById(R.id.button1_1text);
        button1_1.setMovementMethod(LinkMovementMethod.getInstance());

        final TextView button1_2 = (TextView) view.findViewById(R.id.button1_2_text);
        button1_2.setMovementMethod(LinkMovementMethod.getInstance());

        final TextView button1_3 = (TextView) view.findViewById(R.id.button1_3_text);
        button1_3.setMovementMethod(LinkMovementMethod.getInstance());

        final TextView button2_1 = (TextView) view.findViewById(R.id.button2_1_text);
        button2_1.setMovementMethod(LinkMovementMethod.getInstance());

        final TextView button2_2 = (TextView) view.findViewById(R.id.button2_2_text);
        button2_2.setMovementMethod(LinkMovementMethod.getInstance());

        final TextView button2_3 = (TextView) view.findViewById(R.id.button2_3_text);
        button2_3.setMovementMethod(LinkMovementMethod.getInstance());

        final TextView button3_1 = (TextView) view.findViewById(R.id.button3_1_text);
        button3_1.setMovementMethod(LinkMovementMethod.getInstance());

        final TextView button3_2 = (TextView) view.findViewById(R.id.button3_2_text);
        button3_2.setMovementMethod(LinkMovementMethod.getInstance());

        final TextView button3_3 = (TextView) view.findViewById(R.id.button3_3_text);
        button3_3.setMovementMethod(LinkMovementMethod.getInstance());

        final TextView button4_1 = (TextView) view.findViewById(R.id.button4_1_text);
        button4_1.setMovementMethod(LinkMovementMethod.getInstance());

        final TextView button4_2 = (TextView) view.findViewById(R.id.button4_2_text);
        button4_2.setMovementMethod(LinkMovementMethod.getInstance());

        final TextView button4_3 = (TextView) view.findViewById(R.id.button4_3_text);
        button4_3.setMovementMethod(LinkMovementMethod.getInstance());

        final TextView button5_1 = (TextView) view.findViewById(R.id.button5_1_text);
        button5_1.setMovementMethod(LinkMovementMethod.getInstance());

        final TextView button5_2 = (TextView) view.findViewById(R.id.button5_2_text);
        button5_2.setMovementMethod(LinkMovementMethod.getInstance());

        final TextView button5_3 = (TextView) view.findViewById(R.id.button5_3_text);
        button5_3.setMovementMethod(LinkMovementMethod.getInstance());
        return view;

    }
}