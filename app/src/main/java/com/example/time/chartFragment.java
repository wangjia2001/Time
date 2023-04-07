package com.example.time;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;


public class chartFragment extends Fragment {
    private static final SimpleDateFormat dateFormat  = new SimpleDateFormat("MM-dd");
    public static final long DAY_IN_MILLIS = 24 * 60 * 60 * 1000;
    private ArrayList total=new ArrayList<>();
    private LineChart mLineChart;
    double sum = 0;
    double avg = 0;

    String weektime[] = new String[]{"7-11", "7-12", "7-13", "7-14", "7-15", "7-16", "7-17"};//横坐标
    String data[] =  new String[7];//纵坐标

    public chartFragment(ArrayList total){
        this.total=total;
    }

    private  void getTime(){   //获取使用时间
        long count=0;
        int j=0;
        int i=0;
        for(i=0;i<7;i++){            //数据
            data[i]=total.get(i).toString();
        }
        for(i=total.size();i<7;i++){
            data[i]="0";
        }
        for(i = 0 ; i < 7 ; i++){           //横坐标
            long time = System.currentTimeMillis() - i * DAY_IN_MILLIS;
            weektime[i]=dateFormat.format(time);
        }
        for(i = 0; i < data.length; i++) {
            sum = Float.parseFloat(data[i]);
        }
        avg = sum/data.length;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chart_fragment, container, false);
        mLineChart = view.findViewById(R.id.mLine);
        getTime();

        mLineChart.SetInfo(weektime, // X轴刻度
                data, // 数据
                "总时间："+sum,"平均值："+avg);
        return view;
    }

}