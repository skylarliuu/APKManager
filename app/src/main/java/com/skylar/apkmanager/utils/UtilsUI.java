package com.skylar.apkmanager.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.holder.BadgeStyle;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.skylar.apkmanager.R;
import com.skylar.apkmanager.adapter.AppAdapter;

/**
 * Created by Skylar on 2017/9/26.
 */

public class UtilsUI {

    public static Drawer setDrawer(Activity act, Context context, Toolbar toolbar, final AppAdapter appAdapter, final AppAdapter systemAdapter, final AppAdapter favoriteAdapter, final RecyclerView recyclerView){
        String lable = "...";
        String appCount,systemCount,favoriteCount;

        //得到各类APP的数量
        if(appAdapter!=null){
            appCount = Integer.toString(appAdapter.getItemCount());
        }else{
            appCount = lable;
        }

        if(systemAdapter!=null){
            systemCount = Integer.toString(systemAdapter.getItemCount());
        }else{
            systemCount = lable;
        }

        if(favoriteAdapter!=null){
            favoriteCount = Integer.toString(favoriteAdapter.getItemCount());
        }else{
            favoriteCount = lable;
        }

        //设置header
        AccountHeader header = new AccountHeaderBuilder()
                .withActivity(act)
                .withHeaderBackground(R.drawable.header_day)
                .build();

        //设置badge style
        Integer badgeColor = ContextCompat.getColor(context,R.color.divider);
        BadgeStyle badgeStyle = new BadgeStyle(badgeColor,badgeColor).withTextColor(Color.GRAY);

        //设置drawer
        DrawerBuilder builder = new DrawerBuilder();
        builder.withActivity(act);
        builder.withToolbar(toolbar);
        builder.withAccountHeader(header);

        builder.addDrawerItems(
                new PrimaryDrawerItem().withName(context.getResources().getString(R.string.action_apps))
                        .withIcon(GoogleMaterial.Icon.gmd_phone_android)
                        .withBadge(appCount)
                        .withBadgeStyle(badgeStyle)
                        .withIdentifier(1),
                new PrimaryDrawerItem().withName(context.getResources().getString(R.string.action_system_apps))
                        .withIcon(GoogleMaterial.Icon.gmd_android)
                        .withBadge(systemCount)
                        .withBadgeStyle(badgeStyle)
                        .withIdentifier(2),
                new DividerDrawerItem(),
                new PrimaryDrawerItem().withName(context.getResources().getString(R.string.action_favorites))
                        .withIcon(GoogleMaterial.Icon.gmd_star)
                        .withBadge(favoriteCount)
                        .withBadgeStyle(badgeStyle)
                        .withIdentifier(3),
                new DividerDrawerItem(),
                new SecondaryDrawerItem().withName(context.getResources().getString(R.string.action_settings))
                        .withIcon(GoogleMaterial.Icon.gmd_settings)
                        .withSelectable(false)
                        .withIdentifier(4),
                new SecondaryDrawerItem().withName(context.getResources().getString(R.string.action_about))
                        .withIcon(GoogleMaterial.Icon.gmd_info)
                        .withSelectable(false)
                        .withIdentifier(5)
        );

        builder.withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
            @Override
            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                switch (drawerItem.getIdentifier()){
                    case 1:
                        recyclerView.setAdapter(appAdapter);
                        break;
                    case 2:
                        recyclerView.setAdapter(systemAdapter);
                        break;
                    case 3:
                        recyclerView.setAdapter(favoriteAdapter);
                        break;
                    case 4:
                        break;
                    case 5:
                        break;
                }
                return false;
            }
        });

        return builder.build();

    }
}
