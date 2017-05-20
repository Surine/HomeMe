package com.surine.homeme.UI;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.surine.homeme.Adapter.Viewpager.SimpleFragmentPagerAdapter;
import com.surine.homeme.Fragment.CO_Fragment_three;
import com.surine.homeme.Fragment.Humidity_Fragment;
import com.surine.homeme.Fragment.Temperature_Fragment;
import com.surine.homeme.InitApp.BaseActivity;
import com.surine.homeme.R;

import java.util.ArrayList;
import java.util.List;

public class InfoActivity extends BaseActivity {

    private TabLayout tab;
    private ViewPager viewpager;
    SimpleFragmentPagerAdapter pagerAdapter;
    private List<Fragment> fragments =new ArrayList<>();
    private List<String> titles =new ArrayList<>();
    private int Position_page = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tb_info);
        setSupportActionBar(toolbar);
        setTitle("我家");

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        initViewPager();

    }

    private void initViewPager() {
        //1.实例化viewpager和tablayout
        viewpager = (ViewPager)findViewById(R.id.viewpager);
        tab = (TabLayout)findViewById(R.id.tabs);

        //2.使用fragment 的list集合管理碎片
        fragments.add(Temperature_Fragment.getInstance("1"));
        fragments.add(Humidity_Fragment.getInstance("2"));
        fragments.add(CO_Fragment_three.getInstance("3"));

        //3.使用string的list集合来添加标题
        titles.add("温度");
        titles.add("湿度");
        titles.add("CO");


        //4.初始化适配器（传入参数：FragmentManager，碎片集合，标题）
        pagerAdapter = new SimpleFragmentPagerAdapter
                (getSupportFragmentManager(), fragments, titles);
        //5.设置viewpager适配器
        viewpager.setAdapter(pagerAdapter);

        //6.设置缓存
        /*
        *  * 注意：设置缓存的原因
        * 在加载Tab-A时会实例化Tab-B中fragment，依次调用：onAttach、
        * onCreate、onCreateView、onActivityCreated、onStart和onResume。
        * 同样切换到Tab-B时也会初始化Tab-C中的fragment。（Viewpager预加载）
        * 但是fragment中的数据(如读取的服务器数据)没有相应清除，导致重复加载数据。
        *
        *
        * 注意：ps:我们在使用viewpager时会定义一个适配器adapter，其中实例化了一个fragment列表，
        * 所以在tab切换时fragment都是已经实例化好的，所以在切换标签页时是不会重新实例化fragment
        * 对象的，因而在fragment中定义的成员变量是不会被重置的。所以为列表初始化数据需要注意这个问题。
        *
        * 参考网址：https://my.oschina.net/buobao/blog/644699
*/
        viewpager.setOffscreenPageLimit(3);
        //7.关联viewpager
        tab.setupWithViewPager(viewpager);


        //8.viewpager的监听器
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            //滚动监听器
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            //页卡选中监听器
            @Override
            public void onPageSelected(int position) {
                if(position==0)
                {
                    Position_page = 0;
                }
                else if (position==1)
                {
                    Position_page = 1;
                }
                else if(position==2)
                {
                    Position_page = 2;
                }
            }
            //滚动状态变化监听器
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

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
