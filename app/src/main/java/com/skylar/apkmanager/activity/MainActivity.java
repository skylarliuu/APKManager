package com.skylar.apkmanager.activity;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.mikepenz.materialdrawer.Drawer;
import com.pnikosis.materialishprogress.ProgressWheel;
import com.skylar.apkmanager.R;
import com.skylar.apkmanager.adapter.AppAdapter;
import com.skylar.apkmanager.utils.UtilApp;
import com.skylar.apkmanager.utils.UtilsUI;
import com.yalantis.phoenix.PullToRefreshView;

import java.util.ArrayList;
import java.util.List;

import xyz.danoz.recyclerviewfastscroller.vertical.VerticalRecyclerViewFastScroller;

public class MainActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private ProgressWheel mProgressWheel;
    private LinearLayout mNoResult;
    private PullToRefreshView mPullToRefresh;
    private RecyclerView mRecyclerView;
    private VerticalRecyclerViewFastScroller mFastScroller;
    private Drawer mDrawer;

    private ArrayList<AppInfo> mAppList,mSystemAppList,mFavoriteAppList;
    private AppAdapter mAppAdapter,mSystemAdapter,mFavoriteAdapter;

    private Context context;
    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;
        activity = this;

        initUI();//初始化控件
        checkAppPermision();//检测读写SD卡权限
//        setAppDir();//创建App文件夹

        //加载APP列表
        new getInstalledApp().execute();
    }

    private void initUI() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mProgressWheel = (ProgressWheel) findViewById(R.id.progressWheel);
        mNoResult = (LinearLayout) findViewById(R.id.noResult);
        mPullToRefresh = (PullToRefreshView) findViewById(R.id.pullToRefresh);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mFastScroller = (VerticalRecyclerViewFastScroller) findViewById(R.id.fastScroller);

        setSupportActionBar(mToolbar);
        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle(R.string.app_name);
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

        mPullToRefresh.setEnabled(false);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(llm);

        mProgressWheel.setBarColor(R.color.primary);
        mProgressWheel.setVisibility(View.VISIBLE);

        mPullToRefresh.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mAppAdapter.clear();
//                mSystemAdapter.clear();
//                mFavoriteAdapter.clear();
                mRecyclerView.setAdapter(null);
                mProgressWheel.setVisibility(View.VISIBLE);
                new getInstalledApp().execute();

//                mPullToRefresh.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        mPullToRefresh.setRefreshing(false);
//                    }
//                },2000);
            }
        });

        mDrawer = UtilsUI.setDrawer(activity,context,mToolbar,mAppAdapter,mSystemAdapter,mFavoriteAdapter,mRecyclerView);

    }

    private void checkAppPermision() {
        UtilApp.checkPermission(activity);
    }
    

    private void setAppDir() {
        
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case UtilApp.Permission_RequestCode:
                if(grantResults[0] != PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(context,"no permission",Toast.LENGTH_SHORT).show();
                }
        }

    }

    class getInstalledApp extends AsyncTask<Void,Integer,Void>{

        private Integer totalApp,actualApp;

        public getInstalledApp(){
            actualApp = 0;
            totalApp = 0;

            mAppList = new ArrayList<>();
            mSystemAppList = new ArrayList<>();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            PackageManager manager = getPackageManager();
            List<PackageInfo> installedPackages =
                    manager.getInstalledPackages(PackageManager.GET_META_DATA);
            totalApp = installedPackages.size();
            Log.i("test","totalApp"+totalApp);
            //对获取到的包列表进行排序（名称/大小/安装时间/更新时间）

            //对获取包列表的进行区分（系统应用/安装的应用）
            for(int i=0;i<installedPackages.size();i++){
                PackageInfo info = installedPackages.get(i);
                if(!manager.getApplicationLabel(info.applicationInfo).equals("") && !info.applicationInfo.packageName.equals("")){
                    //应用名称和包名不为空
                    if((info.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0){
                        //安装的应用
                        try{
                            AppInfo app = new AppInfo(manager.getApplicationLabel(info.applicationInfo).toString(),manager.getApplicationIcon(info.applicationInfo),info.versionName,info.packageName,info.applicationInfo.sourceDir,info.applicationInfo.dataDir,false);
                            mAppList.add(app);
                        }catch (OutOfMemoryError error){
                            AppInfo app = new AppInfo(manager.getApplicationLabel(info.applicationInfo).toString(),getResources().getDrawable(R.drawable.ic_android),info.versionName,info.packageName,info.applicationInfo.sourceDir,info.applicationInfo.dataDir,false);
                            mAppList.add(app);
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }else{
                        //系统应用
                        try{
                            AppInfo app = new AppInfo(manager.getApplicationLabel(info.applicationInfo).toString(),manager.getApplicationIcon(info.applicationInfo),info.versionName,info.packageName,info.applicationInfo.sourceDir,info.applicationInfo.dataDir,true);
                            mSystemAppList.add(app);
                        }catch (OutOfMemoryError error){
                            AppInfo app = new AppInfo(manager.getApplicationLabel(info.applicationInfo).toString(),getResources().getDrawable(R.drawable.ic_android),info.versionName,info.packageName,info.applicationInfo.sourceDir,info.applicationInfo.dataDir,true);
                            mSystemAppList.add(app);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }
                Log.i("test","actualApp"+actualApp);
                actualApp++;
                publishProgress(actualApp*100/totalApp);

            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            Log.i("test","progress"+values[0]);
            mProgressWheel.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            mProgressWheel.setVisibility(View.GONE);
            mFastScroller.setVisibility(View.VISIBLE);
            mPullToRefresh.setEnabled(true);
            mPullToRefresh.setRefreshing(false);

            mAppAdapter = new AppAdapter(mAppList,context);
            mSystemAdapter = new AppAdapter(mSystemAppList, context);
            mRecyclerView.setAdapter(mAppAdapter);
            mFastScroller.setRecyclerView(mRecyclerView);
            mRecyclerView.setOnScrollListener(mFastScroller.getOnScrollListener());

            mDrawer.closeDrawer();
            mDrawer = UtilsUI.setDrawer(activity,context,mToolbar,mAppAdapter,mSystemAdapter,mFavoriteAdapter,mRecyclerView);

        }
    }

}
