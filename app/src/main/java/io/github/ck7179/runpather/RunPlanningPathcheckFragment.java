package io.github.ck7179.runpather;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class RunPlanningPathcheckFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private static RunPlanningPathcheckFragment instance;

    public void findviews(View view){

    }

    public RunPlanningPathcheckFragment() {
    }

    public static RunPlanningPathcheckFragment newInstance(String param1, String param2) {
        //避免重複呼叫
        if(instance == null){
            instance = new RunPlanningPathcheckFragment();
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
        View view = inflater.inflate(R.layout.fragment_run_planning_pathcheck, container, false);
        findviews(view);
        return  view;
    }

}
