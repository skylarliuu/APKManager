package com.skylar.apkmanager.adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.skylar.apkmanager.R;
import com.skylar.apkmanager.activity.AppActivity;
import com.skylar.apkmanager.activity.AppInfo;

import java.util.ArrayList;

/**
 * Created by Skylar on 2017/9/26.
 */

public class AppAdapter extends RecyclerView.Adapter<AppAdapter.AppViewHolder> {

    private Context mContext;
    private ArrayList<AppInfo> mAppList;

    public AppAdapter(ArrayList<AppInfo> appList,Context context) {
        mAppList = appList;
        mContext = context;
    }

    @Override
    public AppViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.app_item,parent,false);
        AppViewHolder holder = new AppViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final AppViewHolder holder, final int position) {
        AppInfo appInfo = mAppList.get(position);
        holder.name.setText(appInfo.getName());
        holder.packageName.setText(appInfo.getPackageName());
        holder.icon.setImageDrawable(appInfo.getIcon());

        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //分享
            }
        });

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Activity activity = (Activity) mContext;
                Intent intent = new Intent(activity, AppActivity.class);
                AppInfo app = mAppList.get(position);

                intent.putExtra("name",app.getName());
                BitmapDrawable bd = (BitmapDrawable) app.getIcon();
                intent.putExtra("icon",bd.getBitmap());
                intent.putExtra("version",app.getVersion());
                intent.putExtra("packageName",app.getPackageName());
                intent.putExtra("path",app.getPath());
                intent.putExtra("size",app.getSize());
                intent.putExtra("isSystem",app.isSystem());
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                    String transitionName = mContext.getString(R.string.transition_app_icon);
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(activity,holder.icon,transitionName);
                    mContext.startActivity(intent,options.toBundle());
                }else{

                    mContext.startActivity(intent);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mAppList.size();
    }

    public void clear(){
        mAppList.clear();
        notifyDataSetChanged();
    }

    class AppViewHolder extends RecyclerView.ViewHolder{

        public TextView name,packageName,share;
        public ImageView icon;
        public CardView cardView;

        public AppViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            packageName = itemView.findViewById(R.id.packageName);
            share = itemView.findViewById(R.id.share);
            icon = itemView.findViewById(R.id.icon);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}
