package com.surine.homeme.UI;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.donkingliang.labels.LabelsView;
import com.github.rubensousa.floatingtoolbar.FloatingToolbar;
import com.surine.homeme.Adapter.Recycleview.MainRecycleviewAdapter;
import com.surine.homeme.InitApp.BaseActivity;
import com.surine.homeme.JavaBean.Ctrl;
import com.surine.homeme.MQTT.Client;
import com.surine.homeme.R;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    String band,name,end;
    FloatingToolbar mFloatingToolbar;

    private List<Ctrl> mCtrlList =new ArrayList<>();
    //电视
    final String[] tv = new String[] { "TCl","海信","长虹","小米" };
    //空调
    final String[] cn = new String[] { "格力","美的","海尔","其他" };
    MainRecycleviewAdapter adpter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 激活Activity元素中的过度效果，一定要写在setContentView()方法之前
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tb_info);
        setSupportActionBar(toolbar);

        new Thread(new Runnable() {
            @Override
            public void run() {
                //启动mqtt
                Client client = new Client();
                client.start();
            }
        }).start();
        //创建数据库
        Connector.getDatabase();

        mFloatingToolbar = (FloatingToolbar) findViewById(R.id.floatingToolbar);
        mFloatingToolbar.attachFab((FloatingActionButton) findViewById(R.id.fab));

        mFloatingToolbar.setClickListener(new FloatingToolbar.ItemClickListener() {
            @Override
            public void onItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.tb_tv:
                        showMyDialog(tv,1);
                        break;
                    case R.id.tb_cn:
                        showMyDialog(cn,2);
                        break;
                    case R.id.tb_more:
                        Toast.makeText(MainActivity.this,"更多期待",Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onItemLongClick(MenuItem item) {

            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //初始化遥控器列表
        initData();

        initRec();

        TextView home_temperature = (TextView) findViewById(R.id.home_temperature);
        home_temperature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,InfoActivity.class));
            }
        });


    }

    private void initRec() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rec_main);
        adpter = new MainRecycleviewAdapter(mCtrlList,MainActivity.this,this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adpter);

        //绑定rec列表，滑动时候隐藏toolbar
        mFloatingToolbar.attachRecyclerView(recyclerView);

    }


    //添加品牌
    private void showMyDialog(final String[] choose, final int ii) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("请选择品牌");
        builder.setItems(choose, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //TODO:储存选择的品牌
                SharedPreferences sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE); //私有数据
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(String.valueOf(ii), choose[i]);
                editor.commit();//提交修改

                showNameDialog(ii);
            }
        });
        builder.setPositiveButton(R.string.dialog_giveup,null);
        builder.setCancelable(false);
        builder.show();
    }

    private void showNameDialog(final int ii) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.dialog_input_name);
        View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog_add_name,null);
        TextView end_string = (TextView) view.findViewById(R.id.end_string);
        if(ii==1){
            end_string.setText(R.string.the_tv);
        }else{
            end_string.setText(R.string.the_cn);
        }
        final EditText input = (EditText) view.findViewById(R.id.input_name);
        LabelsView labelsView = (LabelsView) view.findViewById(R.id.labels);
        ArrayList<String> label = new ArrayList<>();
        label.add("寝室的");
        label.add("客厅的");
        label.add("萌萌哒");
        label.add("开不开机的");
        label.add("搞笑的");
        label.add("充满活力的");
        label.add("年迈的");
        label.add("酷酷哒");
        labelsView.setLabels(label); //直接设置一个字符串数组就可以了。
        SharedPreferences sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE); //私有数据
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        //标签的点击监听
        labelsView.setOnLabelClickListener(new LabelsView.OnLabelClickListener() {
            @Override
            public void onLabelClick(View label, String labelText, int position) {
                //label是被点击的标签，labelText是标签的文字，position是标签的位置。
                 input.setText(labelText);
                //移动光标到末尾
                input.setSelection(input.getText().length());
            }
        });
            //标签的选中监听
        labelsView.setOnLabelSelectChangeListener(new LabelsView.OnLabelSelectChangeListener() {
            @Override
            public void onLabelSelectChange(View label, String labelText, boolean isSelect, int position) {
                //label是被点击的标签，labelText是标签的文字，isSelect是是否选中，position是标签的位置。
            }
        });
        builder.setNeutralButton("上一步", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (ii == 1) {
                    showMyDialog(tv,ii);
                }else{
                    showMyDialog(cn,ii);
                }
            }
        });
        builder.setCancelable(false);
        builder.setView(view);
        builder.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //ok
                if (input.getText().toString().equals("")) {
                    Toast.makeText(MainActivity.this, R.string.dialog_input_nothing,Toast.LENGTH_SHORT).show();
                    showNameDialog(ii);
                } else {
                    editor.putString(ii + "_label", input.getText().toString());
                    editor.commit();//提交修改
                    showSuccessDialog(ii);
                }
            }
        });
        builder.show();
    }

    private void showSuccessDialog(final int ii) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("添加成功");
        View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog_add_success,null);
        ImageView img = (ImageView) view.findViewById(R.id.img_info);
        TextView tv = (TextView) view.findViewById(R.id.tv_info);
        SharedPreferences share=getSharedPreferences("data", Context.MODE_PRIVATE);
        if(ii == 1){
            Glide.with(this).load(R.drawable.item_tv).into(img);
            band = share.getString("1",null);
            name = share.getString("1_label",null);
            end = "我是"+name+band+"电视";
            tv.setText(end);
        }else{
            Glide.with(this).load(R.drawable.item_airconditioning).into(img);
            band = share.getString("2",null);
            name = share.getString("2_label",null);
            end = "我是"+name+band+"空调";
            tv.setText(end);
        }


        builder.setNeutralButton(R.string._step, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
              showNameDialog(ii);
            }
        });
        builder.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(MainActivity.this,"添加成功",Toast.LENGTH_SHORT).show();
                Ctrl ctrl = new Ctrl();
                ctrl.setImage_id(ii);
                if(ii == 1){
                    ctrl.setName(name+band+"电视");
                }else{
                    ctrl.setName(name+band+"空调");
                }
                ctrl.save();
                initData();
            }
        });
        builder.setCancelable(false);
        builder.setView(view);
        builder.show();
    }

    private void initData() {
          mCtrlList = DataSupport.findAll(Ctrl.class);
          initRec();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            refresh();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //刷新
    private void refresh() {

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_test) {
            startActivity(new Intent(MainActivity.this,TestActivity.class));
        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_about) {
            showAboutDialog();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void showAboutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("柠檬团队");
        builder.setMessage("\n原来复杂的世界里，有柠檬就够了。");
                builder.setPositiveButton("嗯",null);
        builder.show();
    }

}
