package io.github.ck7179.runpather;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    private FloatingActionButton fab;
    private ImageView img;
    private DrawerLayout drawer;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();//抓取view

        //設定以toolbar支援actionbar
        setSupportActionBar(toolbar);

        //設定fab浮動按鈕的onclick監聽
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //設定按下按鈕後出現底部的提示訊息
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
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

        //圖片

    }

    //抓取view函式
    private void findViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        img = (ImageView) findViewById(R.id.imageView);
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

    //intent 設定往下一頁
    public void nextpage(){
        Intent intent = new Intent(this,SignInActivity.class);
        startActivity(intent);
    }
}
