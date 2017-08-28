package io.github.ck7179.runpather;

import android.content.Intent;
import android.content.res.Resources;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class RunPlanningActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView toolbar_title;
    private Button button_next;
    private ProgressBar progressBar;
    private FragmentManager fragManager;
    private RunPlanningMileFragment runPlanningMileFragment;
    private RunPlanningModeFragment runPlanningModeFragment;
    private RunPlanningPathoverviewFragment runPlanningPathoverviewFragment;
    private RunPlanningPathcheckFragment runPlanningPathcheckFragment;
    public Fragment Frag_list[];
    public String Frag_title_list[];
    public int currentFrag = 0;

    private void findViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        button_next = (Button) findViewById(R.id.button_next);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run_planning);
        findViews();
        set_toolbar();
        setProgressBar(0,1);
        setFrag();
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
                onBackPressed();
            }
        });
    }

    //初始化frag
    public void setFrag(){
        fragManager = getSupportFragmentManager();
        runPlanningMileFragment = RunPlanningMileFragment.newInstance("a","b");
        runPlanningModeFragment = RunPlanningModeFragment.newInstance("a","b");
        runPlanningPathoverviewFragment = RunPlanningPathoverviewFragment.newInstance("a","b");
        runPlanningPathcheckFragment = RunPlanningPathcheckFragment.newInstance("a","b");
        Frag_list = new Fragment[]{runPlanningMileFragment,runPlanningModeFragment,runPlanningPathoverviewFragment,runPlanningPathcheckFragment};
        Frag_title_list = new String[]{getString(R.string.rp_mile_title),getString(R.string.rp_mode_title),getString(R.string.rp_pathoverview_title),getString(R.string.rp_pathcheck_title)};
        defaultFrag();
    }

    //設定底部攔的下一步按鈕
    public void setNextButton(){
        button_next.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                btn_controler();
            }
        });
    }

    //button控制
    public void btn_controler() {
        if (currentFrag < Frag_list.length - 1){//確認尚未到達最後一頁fragment
            switch(currentFrag){//按下下一步按鈕後開始擷取當頁的資訊
                case 0:
                    if(frag_mile_do()){
                        nextFrag(currentFrag+1);//擷取完當頁資訊後即切換到下一個fragment
                    }
                    break;
                case 1:
                    if(frag_mode_do()){
                        nextFrag(currentFrag+1);//擷取完當頁資訊後即切換到下一個fragment
                    }
                    break;
                case 2:
                    frag_pathoverview_do();
                    break;
                case 3:
                    frag_pathcheck_do();
                    break;
            }

        }else{
            Toast.makeText(this,"已經到最後一頁", Toast.LENGTH_SHORT).show();
        }
    }

    //切換到下一頁frag
    public void nextFrag(int next_num){
        FragmentTransaction trans = fragManager.beginTransaction();
        //設定切換動畫，(下一頁:新畫面，下一頁:舊畫面，上一頁:新畫面，上一頁:舊畫面)
        trans.setCustomAnimations(R.anim.slide_from_right, R.anim.slide_to_left,R.anim.slide_from_left, R.anim.slide_to_right);
        //以新的fragment替換現有的fragment
        trans.replace(R.id.fragment_container,Frag_list[next_num]);
        //將現有的fragment存入stack
        trans.addToBackStack(Integer.toString(next_num-1));
        trans.commit();
        currentFrag = next_num;//更新目前頁數
        toolbar_title.setText(Frag_title_list[next_num]);//切換title
        setProgressBar(next_num,next_num+1);//更新progressbar進度
    }

    //預設的第一個frag
    public void defaultFrag(){
        FragmentTransaction trans = fragManager.beginTransaction();
        trans.setCustomAnimations(R.anim.slide_from_right, R.anim.slide_to_left,R.anim.slide_from_left, R.anim.slide_to_right);
        trans.add(R.id.fragment_container,Frag_list[0]);//新增fragment
        trans.commit();
    }

    //切換到上一個frag
    public void lastFrag(int last_num){
        //從stack裡pop一個fragment出來
        fragManager.popBackStack(Integer.toString(last_num), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        currentFrag = last_num;//更新目前頁數
        toolbar_title.setText(Frag_title_list[last_num]);//切換title
        setProgressBar(last_num+2,last_num+1);//更新progressbar進度
    }

    public boolean frag_mile_do(){//第1頁資訊擷取
        if(!runPlanningMileFragment.isNextEnabled()){
            Toast.makeText(this,"請選取適當的里程數", Toast.LENGTH_SHORT).show();
            return false;
        }else{
            //取得資訊
            String unit = "";
            if(runPlanningMileFragment.isBtn_unit()){
                unit = "公里";
            }else{
                unit = "公尺";
            }
            Toast.makeText(this,runPlanningMileFragment.getPath_num()+unit, Toast.LENGTH_SHORT).show();
            return true;
        }
    }

    public boolean frag_mode_do(){//第2頁資訊擷取
        if(!runPlanningModeFragment.isNextEnabled()){
            Toast.makeText(this,"請選取情境模式", Toast.LENGTH_SHORT).show();
            return false;
        }else{
            //取得資訊
            String mode ="";
            switch(runPlanningModeFragment.getMode()){
                case 1:
                    mode = "安全優先";
                    break;
                case 2:
                    mode = "寧靜優先";
                    break;
                case 3:
                    mode = "健康優先";
                    break;
                case 4:
                    mode = "景點優先";
            }
            Toast.makeText(this,mode, Toast.LENGTH_SHORT).show();
            return true;
        }
    }

    public void frag_pathoverview_do(){//第3頁資訊擷取

    }

    public void frag_pathcheck_do(){//第4頁資訊擷取

    }

    //設定progressbar的進度
    public void setProgressBar(int from,int to){
        //XML中設定最大值為100，這裡切5等分，每等分為20
        ProgressBarAnimation anim = new ProgressBarAnimation(progressBar, 20*from, 20*to);
        anim.setDuration(1000);//動畫速度
        progressBar.startAnimation(anim);
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
        RunPlanningActivity.this.finish();
    }

    //下一頁intent
    public void nextpage(){
        Intent intent = new Intent(this,RunPlanningActivity.class);
        startActivity(intent);
        //設定activity頁面跳轉動畫(新頁面,現有頁面)
        overridePendingTransition(R.transition.slide_from_right, R.transition.slide_none);
    }

    //返回鍵監聽
    @Override
    public void onBackPressed() {
        if(currentFrag != 0){//非第一頁
            lastFrag(currentFrag-1);
        }else {//第一頁
            //以lastpage()處理使動畫一致
            lastpage();
            super.onBackPressed();
        }
    }

    //progressbar的動態進度設置
    public class ProgressBarAnimation extends Animation {
        private ProgressBar progressBar;
        private float from;
        private float  to;

        public ProgressBarAnimation(ProgressBar progressBar, float from, float to) {
            super();
            this.progressBar = progressBar;
            this.from = from;
            this.to = to;
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            super.applyTransformation(interpolatedTime, t);
            float value = from + (to - from) * interpolatedTime;
            progressBar.setProgress((int) value);
        }

    }
}
