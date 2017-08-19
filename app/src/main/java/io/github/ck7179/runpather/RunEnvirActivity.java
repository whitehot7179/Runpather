package io.github.ck7179.runpather;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

public class RunEnvirActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private Button button_next;
    private FloatingActionButton fab;
    Activity thisActivity = null;

    private void findViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        button_next = (Button) findViewById(R.id.button_next);
        fab = (FloatingActionButton) findViewById(R.id.fab);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run_envir);
        thisActivity = this;
        findViews();
        set_toolbar();;
        set_fabClick();
        setNextButton();
        setWebView();
    }

    //設定toolbar
    public void set_toolbar(){
        //顯示返回鍵
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_clear);
        //設定返回鍵按下的處裡
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lastpage();
            }
        });
    }

    //設定按下fab浮動按鈕
    public void set_fabClick(){
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //.setAction("Action", null).show();
            }
        });
    }

    //設定底部攔的下一步按鈕
    public void setNextButton(){
        button_next.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                nextpage();
            }
        });
    }

    //設定webview
    public void setWebView(){
        final WebView webview_run_envir = (WebView)findViewById(R.id.webview_run_envir);
        webview_run_envir.setBackgroundColor(Color.WHITE);
        webview_run_envir.setWebViewClient(new MyWebViewClient());
        webview_run_envir.getSettings().setJavaScriptEnabled(true);
        webview_run_envir.getSettings().setAppCacheEnabled(true);
        webview_run_envir.getSettings().setDomStorageEnabled(true);
        webview_run_envir.getSettings().setDatabaseEnabled(true);
        //webview_run_envir.loadUrl("https://ck7179.github.io/Web-Design/HW_4");
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
    }

    //上一頁intent
    public void lastpage(){
        Intent intent = new Intent(this,MainActivity.class);
        //finish在此activity之上層(包含此層)的activity
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //不另create新的activity，只把目標activity移到上層
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        //設定activity頁面跳轉動畫(新頁面,現有頁面)
        overridePendingTransition(R.transition.slide_none, R.transition.slide_to_right);
    }

    //下一頁intent
    public void nextpage(){
        Intent intent = new Intent(this,RunMileActivity.class);
        startActivity(intent);
        //設定activity頁面跳轉動畫(新頁面,現有頁面)
        overridePendingTransition(R.transition.slide_from_right, R.transition.slide_none);
    }

    //返回鍵監聽
    @Override
    public void onBackPressed() {
        //以finishpage()處理使動畫一致
        lastpage();
        super.onBackPressed();
    }
}
