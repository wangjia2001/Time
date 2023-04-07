package com.example.time;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class countFragment extends Fragment {
    private TextView timeUsing;//今日使用总时间
    private ArrayList<Week> lists = new ArrayList<Week>();
    private PieChart mPieChart;
    private List<PieData> pieData;
    public static final int COUNT = 1;//连续绘制
    List<String> appName = new ArrayList<String>();
    List<String> appTime = new ArrayList<String>();


    public countFragment(ArrayList<Week> lists){
        this.lists=lists;
    }

    private  void getTime(){   //获取今日使用时间
        double a=0;
        for(int i = 0; i < appTime.size(); i++){
            a =Float.parseFloat(appTime.get(i));
        }
        timeUsing.setText(""+a);   //设置今日使用总时间
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        for(int i=0;i<5;i++){
            appName.add(lists.get(i).getApp());
            String str=lists.get(i).getTime() + "";
            appTime.add(str);
        }

        View view = inflater.inflate(R.layout.count_fragment, container, false);
        timeUsing = (TextView) view.findViewById(R.id.time_using);
        getTime(); //获取使用时间

        mPieChart = view.findViewById(R.id.mPieChart);
        pieData = new ArrayList<>();
        for (int i = 0; i < appName.size(); i++) {
            pieData.add(new PieData(appName.get(i), Float.parseFloat(appTime.get(i))));//读入应用名称 ，使用时间
        }
        mPieChart.setData(pieData, COUNT);
        return view;
    }

}
