package com.surine.homeme.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.surine.homeme.R;

/**
 * Created by surine on 2017/5/20.
 */

public class TV_Ctrl_Fragment extends Fragment {
    private static final String ARG_ = "TV_Ctrl_Fragment";

    public static TV_Ctrl_Fragment getInstance(String title) {
        TV_Ctrl_Fragment fra = new TV_Ctrl_Fragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_, title);
        fra.setArguments(bundle);
        return fra;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tv_ctrl, container, false);
        return v;
    }
}
