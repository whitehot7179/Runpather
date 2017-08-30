package io.github.ck7179.runpather;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    private static FloatingActionButton fab;
    private ImageView img;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private TextView toolbar_title;
    static Activity thisActivity = null;
    //抓取view函式
    private void findViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        img = (ImageView) findViewById(R.id.imageView);
        toolbar_title =(TextView) findViewById(R.id.toolbar_title);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        thisActivity = this;
        findViews();//抓取view

        //設置toolbar不顯示預設title
        toolbar.setTitle("");
        //設定以toolbar支援actionbar
        setSupportActionBar(toolbar);

        //設定fab浮動按鈕的onclick監聽
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //設定按下按鈕後出現底部的提示訊息
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //       .setAction("Action", null).show();
                //設定intent nextpage()
                nextpage();
            }
        });

        //菜單設定
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        //菜單內容監聽
        navigationView.setNavigationItemSelectedListener(this);

        //tabbed宣告
        ViewPager vp_pages= (ViewPager) findViewById(R.id.vp_pages);
        FragmentAdapter pagerAdapter=new FragmentAdapter(getSupportFragmentManager());
        vp_pages.setAdapter(pagerAdapter);
        vp_pages.setOffscreenPageLimit(2);
        TabLayout tbl_pages= (TabLayout) findViewById(R.id.tbl_pages);
        tbl_pages.setupWithViewPager(vp_pages);
        tab_setting(tbl_pages);

        //設定viewpager與fab關聯
        viewpager_change_fab(vp_pages);


    }

    //切換viewpager時隱藏fab與顯示fab
    public void viewpager_change_fab(ViewPager v){
        v.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                switch (position+1) {
                    case 1:
                        fab.show();
                        break;
                    case 2:
                        fab.hide();
                        break;
                    case 3:
                        fab.hide();
                        break;
                    default:
                        break;
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    //tab切換設定
    public void tab_setting(TabLayout tbl_pages){
        //view的宣告，每個view對應到各自的drawable，也就是圖示
        final View view1 = getLayoutInflater().inflate(R.layout.custom_tab, null);
        view1.findViewById(R.id.icon).setBackgroundResource(R.drawable.ic_person_white);
        final View view2 = getLayoutInflater().inflate(R.layout.custom_tab, null);
        view2.findViewById(R.id.icon).setBackgroundResource(R.drawable.ic_location);
        final View view3 = getLayoutInflater().inflate(R.layout.custom_tab, null);
        view3.findViewById(R.id.icon).setBackgroundResource(R.drawable.ic_public);

        //設定每個tab的view
        tbl_pages.getTabAt(0).setCustomView(view1);
        tbl_pages.getTabAt(1).setCustomView(view2);
        tbl_pages.getTabAt(2).setCustomView(view3);

        //針對tab的切換做監聽
        tbl_pages.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            //當tab被selected時的處理
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getCustomView() == view1){
                    view1.findViewById(R.id.icon).setBackgroundResource(R.drawable.ic_person_white);
                    toolbar_title_setting(1);
                }else if(tab.getCustomView() == view2){
                    view2.findViewById(R.id.icon).setBackgroundResource(R.drawable.ic_location_white);
                    toolbar_title_setting(2);
                }else if(tab.getCustomView() == view3){
                    view3.findViewById(R.id.icon).setBackgroundResource(R.drawable.ic_public_white);
                    toolbar_title_setting(3);
                }
            }
            //當tab沒被selected時的處理
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                if(tab.getCustomView() == view1){
                    view1.findViewById(R.id.icon).setBackgroundResource(R.drawable.ic_person);
                }else if(tab.getCustomView() == view2){
                    view2.findViewById(R.id.icon).setBackgroundResource(R.drawable.ic_location);
                }else if(tab.getCustomView() == view3){
                    view3.findViewById(R.id.icon).setBackgroundResource(R.drawable.ic_public);
                }
            }
            //當tab再次被selected時的處理
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                onTabSelected(tab);
            }
        });
    }

    //設定toolbar上的顯示字
    public void toolbar_title_setting(int a){
        //由第幾個tab來判定顯示什麼字
        switch(a){
            case 1:
                toolbar_title.setText(getString(R.string.tab_overall));
                break;
            case 2:
                toolbar_title.setText(getString(R.string.tab_location));
                break;
            case 3:
                toolbar_title.setText(getString(R.string.tab_public));
                break;
        }
    }

    //toolbar上的選單(settings)建立
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    //選單內的item點擊事件監聽
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.setting_about) {
            return true;
        }else if (id == R.id.setting_contact) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //drawer內item的事件監聽
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.menu_route_fav) {
            // Handle the camera action
        } else if (id == R.id.menu_route_share) {

        } else if (id == R.id.menu_voice_setting) {
            return false;//取消點擊
        } else if (id == R.id.menu_screen_setting) {
            return false;//取消點擊
        } else if (id == R.id.menu_account_signout) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //fragment設定
    public static class PlaceholderFragment extends Fragment {
        //The fragment argument representing the section number for this fragment.
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        //Returns a new instance of this fragment for the given section number.
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView ;
            //宣告每個view對應到的fragment.xml
            switch(getArguments().getInt(ARG_SECTION_NUMBER)){
                case 1:
                    rootView = inflater.inflate(R.layout.fragment_main_overall, container, false);
                    setFrag_overall(rootView);
                    return rootView;
                case 2:
                    rootView = inflater.inflate(R.layout.fragment_main_location, container, false);
                    setFrag_location(rootView);
                    return rootView;
                case 3:
                    rootView = inflater.inflate(R.layout.fragment_main_public, container, false);
                    setFrag_public(rootView);
                    return rootView;
                default:
                    rootView = inflater.inflate(R.layout.fragment_main_overall, container, false);
                    setFrag_overall(rootView);
                    return rootView;
            }
        }
    }

    //tab及fragmnt的對應設定
    class FragmentAdapter extends FragmentPagerAdapter {

        public FragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return null;
        }
    }

    //第1頁fragment內容設定
    public static void setFrag_overall(View rootView){
        TextView tx = (TextView)rootView.findViewById(R.id.section_label);
        tx.setText(R.string.large_text);
    }

    //第2頁fragment內容設定
    public static void setFrag_location(final View rootView){
        final WebView webview_location = (WebView)rootView.findViewById(R.id.webview_location);
        WebviewSetting(webview_location,"setFrag_location");

        //向下滑使頁面重新整理
        final SwipeRefreshLayout mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                webview_location.reload();//webview重新整理
                //確認webview是否已載入
                webview_location.setWebViewClient(new WebViewClient() {
                    @Override
                    public void onPageFinished(WebView view, String url) {
                        mSwipeRefreshLayout.setRefreshing(false);//一旦重新整理即停止refreshing
                        //toast顯示
                        Toast.makeText(thisActivity, R.string.page_reload_finish, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    //第3頁fragment內容設定
    public static void setFrag_public(View rootView){
        final WebView webview_public = (WebView)rootView.findViewById(R.id.webview_public);
        WebviewSetting(webview_public,"setFrag_public");

        //向下滑使頁面重新整理
        final SwipeRefreshLayout mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                webview_public.reload();//webview重新整理
                //確認webview是否已載入
                webview_public.setWebViewClient(new WebViewClient() {
                    @Override
                    public void onPageFinished(WebView view, String url) {
                        mSwipeRefreshLayout.setRefreshing(false);//一旦重新整理即停止refreshing
                        //toast顯示
                        Toast.makeText(thisActivity, R.string.page_reload_finish, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    //設定WebView
    private static void WebviewSetting(WebView webview,String setFrag){
        webview.setBackgroundColor(Color.WHITE);
        webview.setWebViewClient(new MyWebViewClient());
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setAppCacheEnabled(true);
        webview.getSettings().setDomStorageEnabled(true);
        webview.getSettings().setDatabaseEnabled(true);
        switch(setFrag){
            case "setFrag_location":
                webview.loadUrl("https://ck7179.github.io/Web-Design/HW_4");
                break;
            case "setFrag_public":
                webview.loadUrl("https://hypixel.net");
                break;
            default:
                webview.loadUrl("https://www.google.com.tw");
                break;
        }
    }

    //webview的詳細設定
    private static class MyWebViewClient extends WebViewClient {
        //強制不跳出網頁瀏覽器顯示網頁，在app裡顯示
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return super.shouldOverrideUrlLoading(view, url);
        }
        @Override
        public void onPageStarted(WebView w, String url, Bitmap f){
        }
        @Override
        public void onPageFinished(WebView w,String url){
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            view.loadUrl("file:///android_asset/loadError.html");
            //super.onReceivedError(view, errorCode, description, failingUrl);
            //Log.i("webb","errorCode"+errorCode+"/"+"description"+description);
        }
    }

    //intent 設定往下一頁
    public void nextpage(){
        Intent intent = new Intent(this,RunEnvirActivity.class);
        startActivity(intent);
        //設定activity頁面跳轉動畫(新頁面,現有頁面)
        overridePendingTransition(R.transition.slide_from_right, R.transition.slide_none);
    }

    //返回鍵監聽
    @Override
    public void onBackPressed() {
        //設定當drawer打開時的返回鍵按下的處裡
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
