package com.example.time;

import android.app.usage.UsageEvents;
import android.content.pm.PackageManager;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class UseTimeEveryDetailAdapter extends RecyclerView.Adapter {

    private ArrayList<UsageEvents.Event> mOneTimeDetailEventInfoList;
    private PackageManager packageManager;

    public UseTimeEveryDetailAdapter(ArrayList<UsageEvents.Event> mOneTimeDetailEventInfoList) {
        this.mOneTimeDetailEventInfoList = mOneTimeDetailEventInfoList;
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
        return mOneTimeDetailEventInfoList.size()/2;
    }

}
