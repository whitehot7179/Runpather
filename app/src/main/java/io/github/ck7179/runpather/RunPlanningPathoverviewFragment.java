package io.github.ck7179.runpather;


import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

public class RunPlanningPathoverviewFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private ConstraintLayout layout_1;
    private ConstraintLayout layout_2;
    private ConstraintLayout layout[];
    private ImageView imageView_hand;
    private  ImageView imageView_auto;
    private int mode = 0;

    private static RunPlanningPathoverviewFragment instance;

    public void findviews(View view){
        layout_1 = (ConstraintLayout) view.findViewById(R.id.layout_1);
        layout_2 = (ConstraintLayout) view.findViewById(R.id.layout_2);
        imageView_hand = (ImageView) view.findViewById(R.id.imageView1);
        imageView_auto = (ImageView) view.findViewById(R.id.imageView2);
    }

    public RunPlanningPathoverviewFragment() {
    }

    public static RunPlanningPathoverviewFragment newInstance(String param1, String param2) {
        //避免重複呼叫
        if(instance == null){
            instance = new RunPlanningPathoverviewFragment();
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
        View view = inflater.inflate(R.layout.fragment_run_planning_pathoverview, container, false);
        findviews(view);
        setLayout(view);
        ImageViewListener();
        recovery();
        return  view;
    }

    @Override
    public void onDestroy() {
        if(mode != 0) {
            //needZero = true;//需要將checkbox歸零
            mode = 0;//將目前選取的模式歸零
        }
        super.onDestroy();
    }

    //恢復模式選擇
    private void recovery(){
        switch(mode){
            case 0:
                //do nothing
                break;
            case 1:
                imageView_hand.setImageResource(R.drawable.hand_clicked);
                break;
            case 2:
                imageView_auto.setImageResource(R.drawable.auto_clicked);
                break;
        }
    }

    //偵測圖片點擊
    private void ImageViewListener(){
        //使用者click手動
        imageView_hand.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(mode == 1) {//取消選取
                mode = 0;
                imageView_hand.setImageResource(R.drawable.hand);
            }else if(mode == 2){//已經選其他的
                mode = 1;//模式設為1
                imageView_auto.setImageResource(R.drawable.auto);//把auto變回原本的
                imageView_hand.setImageResource(R.drawable.hand_clicked);
            }else{//選取
                mode = 1;//模式設為1
                imageView_hand.setImageResource(R.drawable.hand_clicked);
            }
            //Toast.makeText(v.getContext(),Integer.toString(mode),Toast.LENGTH_SHORT).show();
        }
        });
        //使用者click自動
        imageView_auto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mode == 2) {//取消選取
                    mode = 0;
                    imageView_auto.setImageResource(R.drawable.auto);
                }else if(mode == 1){//已經選其他的
                    mode = 2;//模式設為2
                    imageView_hand.setImageResource(R.drawable.hand);//把hand變回原本的
                    imageView_auto.setImageResource(R.drawable.auto_clicked);
                }else{//選取
                    mode = 2;//模式設為2
                    imageView_auto.setImageResource(R.drawable.auto_clicked);
                }
                //Toast.makeText(v.getContext(),Integer.toString(mode),Toast.LENGTH_SHORT).show();
            }
        });
    }

    //設定layout動畫載入
    private void setLayout(View view) {
        layout = new ConstraintLayout[]{layout_1,layout_2};
        if(mode == 0){//初次載入
            for(int i=1;i<=2;i++){
                //動畫
                Animation am = AnimationUtils.loadAnimation(this.getContext(),R.anim.slide_from_right);
                am.setDuration(300+100*i);
                layout[i-1].setAnimation(am);
                am.startNow();
            }
        }else{//上一步載入
            for(int i=1;i<=2;i++){
                //動畫
                Animation am = AnimationUtils.loadAnimation(this.getContext(),R.anim.slide_from_left);
                am.setDuration(300+100*i);
                layout[i-1].setAnimation(am);
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
