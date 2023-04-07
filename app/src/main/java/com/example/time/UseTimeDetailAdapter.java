package com.example.time;

import android.content.pm.PackageManager;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class UseTimeDetailAdapter extends RecyclerView.Adapter {

    private ArrayList<OneTimeDetails> mOneTimeDetailInfoList;
    private PackageManager packageManager;

    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    //define interface
    public  interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, OneTimeDetails details);
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public UseTimeDetailAdapter(ArrayList<OneTimeDetails> mOneTimeDetailInfoList) {
        this.mOneTimeDetailInfoList = mOneTimeDetailInfoList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }


    public int getItemCount() {
        return mOneTimeDetailInfoList.size();
    }

}
