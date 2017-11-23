package io.github.ck7179.runpather;


import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.triggertrap.seekarc.SeekArc;

public class RunPlanningMileFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private SeekArc seekArc;
    private TextView textview_seekbar_num ;
    private int path_num = 110;//里程數
    private int seekbar_num = 0;//進度條數字
    private Button btn_kilometer;
    private Button btn_meter;
    private TextView textView_unit;
    private TextView textView_alert;
    private boolean btn_unit = false;//辨識目前單位為何，(false為公尺，true為公里)
    private static RunPlanningMileFragment instance;

    private void findviews(View view){
        seekArc = (SeekArc) view.findViewById(R.id.seekArc);
        textview_seekbar_num = (TextView) view.findViewById(R.id.textView_seekbar_num);
        btn_kilometer = (Button) view.findViewById(R.id.btn_kilometer);
        btn_meter = (Button) view.findViewById(R.id.btn_meter);
        textView_unit = (TextView) view.findViewById(R.id.textView_unit);
        textView_alert = (TextView) view.findViewById(R.id.textView_alert);
    }

    public RunPlanningMileFragment() {
    }

    public static RunPlanningMileFragment newInstance(String param1, String param2) {
        //避免重複呼叫
        if(instance == null){
            instance = new RunPlanningMileFragment();
        }
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        instance.setArguments(args);
        return instance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_run_planning_mile, container, false);
        findviews(view);
        setSeekArc();
        setBtn();
        return  view;
    }

    @Override
    public void onDestroy() {
        path_num = 0;
        seekbar_num = 0;
        btn_unit = false;
        super.onDestroy();
    }

    //里程數單位按鈕設定
    private void setBtn(){
        if(!btn_unit){//目前為公尺時
            btn_meter.setTextColor(ContextCompat.getColor(getContext(),R.color.colorPrimary));
            btn_meter.setEnabled(false);//取消按鈕作用
            textView_unit.setText("m");
        }else{//目前為公里時
            btn_kilometer.setTextColor(ContextCompat.getColor(getContext(),R.color.colorPrimary));
            btn_kilometer.setEnabled(false);
            textView_unit.setText("km");
        }

        //公尺按鈕監聽
        btn_meter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (btn_unit) {//目前為公里時
                    btn_meter.setTextColor(ContextCompat.getColor(getContext(),R.color.colorPrimary));
                    btn_kilometer.setTextColor(ContextCompat.getColor(getContext(),R.color.colorTextBlack3));
                    btn_meter.setEnabled(false);
                    btn_kilometer.setEnabled(true);
                    btn_unit = false;
                    textView_unit.setText("m");
                    //setZeroSeekbar();
                    //動畫
                    SeekArcAnimation am = new SeekArcAnimation(seekArc,0,11);
                    am.setDuration(600);//動畫速度
                    seekArc.startAnimation(am);
                }
            }
        });
        //公里按鈕監聽
        btn_kilometer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!btn_unit) {//目前為公尺時
                    btn_kilometer.setTextColor(ContextCompat.getColor(getContext(),R.color.colorPrimary));
                    btn_meter.setTextColor(ContextCompat.getColor(getContext(),R.color.colorTextBlack3));
                    btn_kilometer.setEnabled(false);
                    btn_meter.setEnabled(true);
                    btn_unit = true;
                    textView_unit.setText("km");
                    //setZeroSeekbar();
                    //動畫
                    SeekArcAnimation am = new SeekArcAnimation(seekArc,0,2);
                    am.setDuration(200);//動畫速度
                    seekArc.startAnimation(am);
                }
            }
        });
    }

    //設定seekarc
    private void setSeekArc(){
        //===預設&更新===
        textview_seekbar_num.setText(Integer.toString(path_num));
        setInitSeekArcAnim();
        //===預設&更新===

        //seekarc數值監聽
        seekArc.setOnSeekArcChangeListener(new SeekArc.OnSeekArcChangeListener() {
            @Override
            public void onProgressChanged(SeekArc seekArc, int i, boolean b) {
                seekbar_num = i ;//擷取進度條目前刻度
                if(!btn_unit){//目前為公尺時
                    path_num = i * 10;//使最大值為1000
                    if(path_num<=100){//判斷距離是否過短
                        showMileAlert(true);
                    }else{
                        showMileAlert(false);
                    }
                }else{//目前為公里時
                    float p1 = (float)i;
                    float p2 = p1 * 42 / 100;//使最大值為42
                    path_num = Math.round(p2);//Math.round:取浮點數的最接近整數
                    if(path_num == 0){//判斷距離是否過短
                        showMileAlert(true);
                    }else {
                        showMileAlert(false);
                    }
                }
                textview_seekbar_num.setText(Integer.toString(path_num));//動態顯示里程數
            }
            @Override
            public void onStartTrackingTouch(SeekArc seekArc) {
            }
            @Override
            public void onStopTrackingTouch(SeekArc seekArc) {
            }
        });
    }

    //設定初始載入時的seekarc動畫顯示
    public void setInitSeekArcAnim(){
        if(seekbar_num!=0){//已有紀錄
            if(!btn_unit) {//目前為公尺
                if(seekbar_num-11 >= 5) {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            //動畫
                            SeekArcAnimation am = new SeekArcAnimation(seekArc, 11, seekbar_num);
                            am.setDuration(Math.round(500 + 300 * ((double) seekbar_num / 100)));//動畫速度
                            seekArc.startAnimation(am);
                        }
                    }, 200);//動畫延遲
                }else{//數值過低則不進行動畫
                    seekArc.setProgress(seekbar_num);
                }
            }else{//目前為公里
                if(seekbar_num-2 >= 12) {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            //動畫
                            SeekArcAnimation am = new SeekArcAnimation(seekArc,2,seekbar_num);
                            am.setDuration(Math.round(500+300*((double)seekbar_num/100)));//動畫速度
                            seekArc.startAnimation(am);
                        }
                    }, 300);//動畫延遲
                }else{//數值過低則不進行動畫
                    seekArc.setProgress(seekbar_num);
                }
            }

        }else{//初次載入
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    //動畫
                    SeekArcAnimation am = new SeekArcAnimation(seekArc,0,11);
                    am.setDuration(600);//動畫速度
                    seekArc.startAnimation(am);
                }
            }, 300);//動畫延遲
        }
    }

    //seekArc的動態進度設置
    public class SeekArcAnimation extends Animation {
        private SeekArc seekArc;
        private float from;
        private float  to;

        public SeekArcAnimation(SeekArc seekArc, float from, float to) {
            super();
            this.seekArc = seekArc;
            this.from = from;
            this.to = to;
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            super.applyTransformation(interpolatedTime, t);
            float value = from + (to - from) * interpolatedTime;
            Log.i("seekk",Float.toString(value));
            seekArc.setProgress((int) value);
            if(value == 100.0){//最後做歸零
                seekArc.setProgress(0);
            }
        }

    }

    //顯示里程數過短警告
    private void showMileAlert(boolean a){
        if(a){
            seekArc.setProgressColor(ContextCompat.getColor(getContext(),R.color.colorAccent));//進度條變色
            textview_seekbar_num.setTextColor(ContextCompat.getColor(getContext(),R.color.colorAccent));//里程數數字變色
            textView_alert.setText(R.string.mile_alert);//進度條下方顯示警告訊息
        }else{
            seekArc.setProgressColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryLight));
            textview_seekbar_num.setTextColor(ContextCompat.getColor(getContext(), R.color.colorTextBlack2));
            textView_alert.setText("");
        }
    }

    //進度條及里程數歸零
    private void setZeroSeekbar(){
        seekbar_num = 0;
        path_num = 0;
        seekArc.setProgress(seekbar_num);
    }

    //===API===
    //取得目前里程數
    public int getPath_num(){
        return path_num;
    }
    //取得進度條目前刻度
    public int getSeekbar_num(){
        return  seekbar_num;
    }
    //取得目前單位
    public boolean isBtn_unit(){
        return btn_unit;
    }
    //取得是否能進入下一步
    public boolean isNextEnabled(){
        if((!btn_unit && path_num<=100) || (btn_unit && path_num==0)){//公尺且小於100或公里且等於0
            return false;
        }else{
            return true;
        }
    }
    //===API===
}
