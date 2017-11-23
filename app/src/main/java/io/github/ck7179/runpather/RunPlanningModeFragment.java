package io.github.ck7179.runpather;


import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.CompoundButton;

public class RunPlanningModeFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private CheckBox checkbox_safe;
    private CheckBox checkbox_quiet;
    private CheckBox checkbox_health;
    private CheckBox checkbox_scene;
    private CheckBox checkbox_list[];
    private ConstraintLayout mode_layout_1;
    private ConstraintLayout mode_layout_2;
    private ConstraintLayout mode_layout_3;
    private ConstraintLayout mode_layout_4;
    private ConstraintLayout mode_layout[];
    private int mode = 0;
    private boolean needZero;//判斷是否需在第一次checkbox打勾的時候將checkbox取消打勾
    private static RunPlanningModeFragment instance;

    private void findviews(View view){
        checkbox_safe = (CheckBox) view.findViewById(R.id.checkBox_safe);
        checkbox_quiet = (CheckBox) view.findViewById(R.id.checkBox_quiet);
        checkbox_health = (CheckBox) view.findViewById(R.id.checkBox_health);
        checkbox_scene = (CheckBox) view.findViewById(R.id.checkBox_scene);
        mode_layout_1 = (ConstraintLayout) view.findViewById(R.id.mode_layout_1);
        mode_layout_2 = (ConstraintLayout) view.findViewById(R.id.mode_layout_2);
        mode_layout_3 = (ConstraintLayout) view.findViewById(R.id.mode_layout_3);
        mode_layout_4 = (ConstraintLayout) view.findViewById(R.id.mode_layout_4);
    }

    public RunPlanningModeFragment() {
    }

    public static RunPlanningModeFragment newInstance(String param1, String param2) {
        //避免重複呼叫
        if(instance == null){
            instance = new RunPlanningModeFragment();
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
        View view = inflater.inflate(R.layout.fragment_run_planning_mode, container, false);
        findviews(view);
        setLayout(view);
        setCheckBox();
        return  view;
    }

    @Override
    public void onDestroy() {
        if(mode != 0) {
            needZero = true;//需要將checkbox歸零
            mode = 0;//將目前選取的模式歸零
        }
        super.onDestroy();
    }

    //設定checkbox
    private void setCheckBox(){
            //輸入陣列
            checkbox_list = new CheckBox[]{checkbox_safe,checkbox_quiet,checkbox_health,checkbox_scene};
            //為所有checkbox建立監聽
            for(int i=1;i<=checkbox_list.length;i++) {
                checkBoxListener(i);
            }
        if(mode != 0 && !needZero){//進入下一頁後再回來此頁須將狀態回復
            checkbox_list[mode-1].setChecked(true);//預設&更新
        }
    }

    //checkbox的監聽
    private void checkBoxListener(final int a){
        checkbox_list[a-1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkbox_list[a-1].isChecked()) {//判斷是打勾
                    mode = a;
                    for (int i = 1; i <= checkbox_list.length; i++) {
                        if (a != i) {//除了自己以外全部取消打勾
                            checkbox_list[i - 1].setChecked(false);
                        }
                    }
                }else if (!checkbox_list[a-1].isChecked() && mode == a){//取消打勾時同時將mode也歸零
                    mode = 0;
                }
            }
        });
        checkbox_list[a - 1].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(needZero){//確認需要歸零
                    checkbox_safe.setChecked(false);
                    checkbox_quiet.setChecked(false);
                    checkbox_health.setChecked(false);
                    checkbox_scene.setChecked(false);
                    needZero = false;
                }
            }
        });
    }

    //設定layout
    private void setLayout(View view) {
        mode_layout = new ConstraintLayout[]{mode_layout_1,mode_layout_2,mode_layout_3,mode_layout_4};
        if(mode == 0){//初次載入
            for(int i=1;i<=4;i++){
                //動畫
                Animation am = AnimationUtils.loadAnimation(this.getContext(),R.anim.slide_from_right);
                am.setDuration(200+100*i);
                mode_layout[i-1].setAnimation(am);
                am.startNow();
            }
        }else{//上一步載入
            for(int i=1;i<=4;i++){
                //動畫
                Animation am = AnimationUtils.loadAnimation(this.getContext(),R.anim.slide_from_left);
                am.setDuration(300+100*i);
                mode_layout[i-1].setAnimation(am);
                am.startNow();
            }
        }

    }

    //===API(由 RunPlanningActivity call)===
    public int getMode(){
        return mode;
    }
    public boolean isNextEnabled(){
        if(mode == 0){
            return false;
        }else{
            return  true;
        }
    }
    //===API===
}
