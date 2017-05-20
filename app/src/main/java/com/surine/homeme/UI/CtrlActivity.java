package com.surine.homeme.UI;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.view.MenuItem;

import com.surine.homeme.Fragment.CN_ctrl_fragment;
import com.surine.homeme.Fragment.TV_Ctrl_Fragment;
import com.surine.homeme.InitApp.BaseActivity;
import com.surine.homeme.JavaBean.Ctrl;
import com.surine.homeme.R;

import org.litepal.crud.DataSupport;

public class CtrlActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ctrl);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        getWindow().setEnterTransition(new Explode().setDuration(300));
            getWindow().setExitTransition(new Explode().setDuration(300));
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.tb_ctrl);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = getIntent();
        Ctrl ctrl = DataSupport.find(Ctrl.class,intent.getIntExtra("ctrl_id",1));
        setTitle(ctrl.getName());

        if(ctrl.getImage_id()==1){
            replaceFragment(TV_Ctrl_Fragment.getInstance("TV"));
        }else{
            replaceFragment(CN_ctrl_fragment.getInstance("CN"));
        }
    }

    //load the fragment
    private void replaceFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction tran = fm.beginTransaction();
        tran.replace(R.id.ctrl_fragment, fragment);
        tran.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
