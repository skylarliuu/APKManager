package com.skylar.apkmanager.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.skylar.apkmanager.R;
import com.skylar.apkmanager.utils.UtilApp;

/**
 * Created by Skylar on 2017/9/27.
 */

public class AppActivity extends AppCompatActivity {

    private AppInfo mAppInfo;
    private Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app);

        mContext = this;

        getData();
        initUI();

    }

    private void getData() {
        mAppInfo = new AppInfo();
        Intent intent = getIntent();
        mAppInfo.setName(intent.getStringExtra("name"));
        mAppInfo.setIcon(new BitmapDrawable((Bitmap) intent.getParcelableExtra("icon")));
        mAppInfo.setVersion(intent.getStringExtra("version"));
        mAppInfo.setPackageName(intent.getStringExtra("packageName"));
        mAppInfo.setPath(intent.getStringExtra("path"));
        mAppInfo.setSize(intent.getStringExtra("size"));
        mAppInfo.setSystem(intent.getBooleanExtra("isSystem",false));

    }

    private void initUI() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        ImageView icon = (ImageView) findViewById(R.id.icon);
        TextView name = (TextView) findViewById(R.id.name);
        TextView version = (TextView) findViewById(R.id.version);
        TextView packageName = (TextView) findViewById(R.id.packageName);
        FloatingActionButton fab_share = (FloatingActionButton) findViewById(R.id.share);

        CardView packageView = (CardView) findViewById(R.id.packageCardView);
        ImageView img_google = (ImageView) findViewById(R.id.img_google);
        CardView runAppView = (CardView) findViewById(R.id.runAppCardView);
        CardView uninstallView = (CardView) findViewById(R.id.uninstallCardView);
        CardView removeCacheView = (CardView) findViewById(R.id.removeCacheCardView);
        CardView clearData = (CardView) findViewById(R.id.clearDataCardView);

        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle("");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            //沉浸式
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            getWindow().setStatusBarColor();
//            mToolbar.setBackgroundColor();
//            if(){
//                getWindow().setNavigationBarColor();
//            }
        }

        icon.setImageDrawable(mAppInfo.getIcon());
        name.setText(mAppInfo.getName());
        version.setText(mAppInfo.getVersion());
        packageName.setText(mAppInfo.getPackageName());

        if(mAppInfo.isSystem()){
            img_google.setVisibility(View.INVISIBLE);
            runAppView.setVisibility(View.GONE);
        }else{
            packageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    UtilApp.goToMarket(mContext,mAppInfo.getPackageName());
                }
            });

            packageView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    ClipData data;
                    ClipboardManager manager = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                    data = ClipData.newPlainText("text",mAppInfo.getPackageName());
                    manager.setPrimaryClip(data);
                    return false;
                }
            });

            runAppView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

            uninstallView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }

        fab_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.app_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
            case R.id.favorite:

                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
