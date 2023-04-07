package com.example.time;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class appTitleFragment extends Fragment {  //count界面表格
    public appTitleFragment(){
    }
    FragmentCallBack fragmentCallBack =null;
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        fragmentCallBack = ( MainActivity )activity;
    }
    ArrayList<Week> f=new ArrayList<>();
    ArrayList<String> lists=new ArrayList<>();
    List<String> name = new ArrayList<String>();
    List<String> time = new ArrayList<String>();
    List<Drawable> img = new ArrayList<Drawable>();
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,
                             Bundle savedInstanceState) {
        f=fragmentCallBack.callbackFun1();
        for(int i =0; i<f.size(); i++) {
            name.add(f.get(i).getApp());
            String str=f.get(i).getTime()+ "";
            time.add(str);
            img.add(f.get(i).getIcon());
        }

        View view = inflater.inflate(R.layout.app_title_fragment,container, false);
        RecyclerView appTitleRecyclerView = view.findViewById(R.id.app_title_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        appTitleRecyclerView.setLayoutManager(layoutManager);
        AppAdapter appAdapter = new AppAdapter(getApp());
        appTitleRecyclerView.setAdapter(appAdapter);
        return view;
    }

    class AppAdapter extends RecyclerView.Adapter<AppAdapter.ViewHolder>{
        private List<App> mAppList;

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
            final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.app, parent, false);
            final ViewHolder holder = new ViewHolder(view);
            return holder;
        }

        public AppAdapter(List<App> mAppList) {
            this.mAppList = mAppList;
        }


        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            App app = mAppList.get(position);
            holder.appImage.setImageDrawable(app.getImage());
            holder.appName.setText(app.getName());
            holder.appTime.setText(app.getTime());
        }

        @Override
        public int getItemCount() {
            return mAppList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            ImageView appImage;
            TextView appName;
            TextView appTime;
            View appView;

            public ViewHolder(View itemView) {
                super(itemView);
                appView = itemView;
                appImage = itemView.findViewById(R.id.app_image);
                appName = itemView.findViewById(R.id.app_name);
                appTime = itemView.findViewById(R.id.app_time);
            }
        }

    }

    private List<App> getApp() {
        List<App> appList = new ArrayList<>();
        int j = name.size();//软件数目
        for (int i = 0; i <j; i++) {//i<count:软件数目
            App[] app = new App[j];
//            app[i] = new App(name.get(i), R.drawable.ic_launcher_background, "20");
            app[i] = new App(name.get(i), img.get(i), time.get(i));
            appList.add(app[i]);
//
        }
        return appList;
    }



}

