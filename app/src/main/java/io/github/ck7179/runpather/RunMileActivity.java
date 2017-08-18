package io.github.ck7179.runpather;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class RunMileActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private Button button_next;

    private void findViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        button_next = (Button) findViewById(R.id.button_next);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run_mile);
        findViews();
        set_toolbar();;
        setNextButton();
    }

    //設定toolbar
    public void set_toolbar(){
        //顯示返回鍵
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lastpage();
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

    //上一頁intent
    public void lastpage(){
        Intent intent = new Intent(this,RunEnvirActivity.class);
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
