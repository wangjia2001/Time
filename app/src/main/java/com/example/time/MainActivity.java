package com.example.time;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements FragmentCallBack {

    private FrameLayout mFrameLayout;
    private RadioGroup mRg;
    private RadioButton mRbCount;
    private RadioButton mRbChart;
    private RadioButton mRbLock;
    private RadioButton mRbMe;
    private List<Fragment> mFragments = new ArrayList<>();
    private com.example.time.countFragment countFragment;
    private com.example.time.chartFragment chartFragment;
    private meFragment meFragment;
    private FragmentManager mSupportFragmentManager;
    private FragmentTransaction mTransaction;
    private UsageStatsManager mUsmManager,mUsmManager1;
    private ArrayList<String> mDateList;
    private ArrayList<String> mDateList1;
    private RelativeLayout relativeLayout;
    private int or = 0;
    private UseTimeDataManager mUseTimeDataManager,mUseTimeDataManager1;
    private ArrayList<UsageEvents.Event> mEventListChecked;
    private ArrayList<UsageEvents.Event> mEventListChecked1;
    private ArrayList<PackageInfo> mPackageInfoList = new ArrayList<>();
    private ArrayList<PackageInfo> mPackageInfoList1 = new ArrayList<>();
    List<AppInfo> list1 = new ArrayList<AppInfo>();                      //所有文件信息
    ArrayList<Week> lists=new ArrayList<>();
    ArrayList<Week> lists1=new ArrayList<>();                      //七天的时间存储
    ArrayList<Week> lists2=new ArrayList<>();                      //七天的时间已排序并且合并过的数据
    ArrayList g=new ArrayList<>();                                 //七天中每天的时间
    ProgressDialog progressDialog;
    public ArrayList<PackageInfo> getmPackageInfoList() {
        return mPackageInfoList;
    }


    private boolean isNoOption() {//判断当前设备中是否有"有权查看使用情况的应用程序"这个选项：
        PackageManager packageManager = getApplicationContext()
                .getPackageManager();
        Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private boolean isNoSwitch() {//判断这个选项的打开状态：
        long ts = System.currentTimeMillis();
        @SuppressLint("WrongConstant") UsageStatsManager usageStatsManager = (UsageStatsManager) getApplicationContext()
                .getSystemService("usagestats");
        List<UsageStats> queryUsageStats = usageStatsManager.queryUsageStats(
                UsageStatsManager.INTERVAL_BEST, 0, ts);
        if (queryUsageStats == null || queryUsageStats.isEmpty()) {
            return false;
        }
        return true;
    }

    public void Once(){
        relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);
        relativeLayout.setVisibility(View.VISIBLE);
        // 旋转
        RotateAnimation rotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(1000);
        rotateAnimation.setFillAfter(true);
        //缩放
        ScaleAnimation scaleAnimation = new ScaleAnimation(0,1,0,1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(1000);
        scaleAnimation.setFillAfter(true);
        //渐变
        AlphaAnimation alphaAnimation = new AlphaAnimation(0,1);
        alphaAnimation.setDuration(8000);
        alphaAnimation.setFillAfter(true);
//<span style="white-space:pre">	</span>//把每个效果到放进来
        AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(rotateAnimation);
        animationSet.addAnimation(scaleAnimation);
        animationSet.addAnimation(alphaAnimation);

        relativeLayout.startAnimation(animationSet);

        //设置AnimationListener
        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {  //Animation开始调用的方法
//                if(t==0)
//                {
//                    relativeLayout.setVisibility(View.GONE);
//                }
            }

            @Override
            public void onAnimationEnd(Animation animation) {  // <span style="font-family: Arial, Helvetica, sans-serif;">Animation结束调用的方法</span>
//<span style="white-space:pre">			</span>
                relativeLayout.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {// Animation重复调用的方法
            }
        });

    }

//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences prefs = getSharedPreferences("data",MODE_PRIVATE);
        int lastVersion = prefs.getInt("MYTiMe", 0);


        if(lastVersion==0){
            Once();
            SharedPreferences.Editor editormy =getSharedPreferences("data",MODE_PRIVATE).edit();
            editormy.putInt("MYTiMe", 1);
            editormy.apply();

            lastVersion = prefs.getInt("MYTiMe", 0);
        }
        //   private static final String TAG = "MainActivity";

            if(isNoOption()==true && isNoSwitch() == false){
                Intent intent = new Intent(
                        Settings.ACTION_USAGE_ACCESS_SETTINGS);
                startActivity(intent);
                Toast.makeText(MainActivity.this,"请设置允许访问使用记录",Toast.LENGTH_LONG).show();
            }

        mFrameLayout = findViewById(R.id.framelayout);
        mRg = findViewById(R.id.rg_main);
        mRbCount = findViewById(R.id.rb_count);
        mRbChart = findViewById(R.id.rb_chart);
        mRbLock = findViewById(R.id.rb_lock);
        mRbMe = findViewById(R.id.rb_me);
        Constant.point = new Point();
        getWindowManager().getDefaultDisplay().getSize(Constant.point);//获取屏幕分辨率
        showProgressDialog("提示","加载中,请稍等...");//弹出Loading框
        initView();
    }
    private void showProgressDialog(String title, String content) {
        if (progressDialog == null) {
            progressDialog = ProgressDialog.show(this, title,
                    content, true, false);
        } else if (progressDialog.isShowing()) {
            progressDialog.setTitle(title);
            progressDialog.setMessage(content);
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                test();
                getAllAppNamesPackages();       //获取所有包名
                initData();
                initData2();
                hideProgressDialog();
            }
        }, 2000);
        progressDialog.show();
    }
    private void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            //这里请求一次网络 并把刚刚在弹框时 取到的定位信息 作为参数上传上去
            progressDialog.dismiss();
        }
    }

    private void initView(){
        mSupportFragmentManager = getSupportFragmentManager();
        mTransaction = mSupportFragmentManager.beginTransaction();
        //设置默认选中首页
        mRg.check(R.id.rb_me);
        meFragment = new meFragment();
        mFragments.add(meFragment);
        hideOtherFragment(meFragment,true);
        mRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(RadioGroup group,int checkedId){
                switch (checkedId){
                    case R.id.rb_count:
                        if(countFragment==null){
                            countFragment = new countFragment(lists);
                            mFragments.add(countFragment);
                            hideOtherFragment(countFragment,true);
                        }
                        else {
                            hideOtherFragment(countFragment,false);
                        }
                        break;
                    case R.id.rb_chart:
                        if(chartFragment == null){
                            chartFragment = new chartFragment(g);
                            mFragments.add(chartFragment);
                            hideOtherFragment(chartFragment,true);
                        }
                        else {
                            hideOtherFragment(chartFragment,false);
                        }
                        break;
                    case R.id.rb_lock:
//                        if(lockFragment == null){
//                            lockFragment = new lockFragment();
//                            mFragments.add(lockFragment);
//                            hideOtherFragment(lockFragment,true);
//                        }
//                        else {
//                            hideOtherFragment(lockFragment,false);
//                        }
                        Intent intent = new Intent(MainActivity.this, LockFragment_Activity.class);
                        startActivity(intent);
                        break;
                    case R.id.rb_me:
                        hideOtherFragment(meFragment,false);
                        break;

                }
            }

        });
    }

    private void hideOtherFragment(Fragment showFragment, boolean add) {
        mTransaction = mSupportFragmentManager.beginTransaction();
        if(add){
            mTransaction.add(R.id.framelayout,showFragment);
        }
        for (Fragment fragment:mFragments){
            if(showFragment.equals(fragment)){
                mTransaction.show(fragment);
            }
            else{
                mTransaction.hide(fragment);
            }
        }
        mTransaction.commit();
    }

    private void initData(){                                //一天的数据
        int dayNum=0;
        mDateList = DateTransUtils.getSearchDays();         //获取当天时间
        mUseTimeDataManager = UseTimeDataManager.getInstance(getApplicationContext());//获得系统文件
        mUseTimeDataManager.refreshData(dayNum);            //更新时间
        mUseTimeDataManager.getmPackageInfoListOrderByTime();   //按应用使用长短排序
        mPackageInfoList=mUseTimeDataManager.getPkgInfoListFromEventList();
        for(int i=0;i<mPackageInfoList.size();i++){             //匹配计算得到软件的包名和时间
            for(int j=0;j<list1.size();j++){                     //所有包的名称
                if(list1.get(j).getPackageName().equals(mPackageInfoList.get(i).getmPackageName())){
                    Week week=new Week(mPackageInfoList.get(i).getmUsedTime(),list1.get(j).getAppName(),list1.get(j).getIcon());
                    lists.add(week);
                }
            }
        }
//        for(int i=0;i<lists.size();i++){
//            Log.i("zoushenngjun","包名为："+lists.get(i).getApp()+"时间为："+lists.get(i).getTime());
//        }
    }

    private void initData2(){                               //七天的数据
        int n=0;
        mDateList1 = DateTransUtils.getSearchDays();         //获取当天时间
        mUseTimeDataManager1 = UseTimeDataManager.getInstance(getApplicationContext());//获得系统文件
        for(n=0;n<=6;n++){
            int flag=mUseTimeDataManager1.refreshData(n);            //更新时间
            mUseTimeDataManager1.getmPackageInfoListOrderByTime();                   //按应用使用长短排序
            mPackageInfoList1=mUseTimeDataManager1.getPkgInfoListFromEventList();
            long sum=0;
            for(int i=0;i<mPackageInfoList1.size();i++){                             //一天的时间
                sum=sum+mPackageInfoList1.get(i).getmUsedTime();                    //计算一天花费的时间
                for(int j=0;j<list1.size();j++){
                    if(list1.get(j).getPackageName().equals(mPackageInfoList1.get(i).getmPackageName())){
                        Week week=new Week(mPackageInfoList1.get(i).getmUsedTime(),list1.get(j).getAppName(),list1.get(j).getIcon());
                        lists1.add(week);
                    }
                }
            }
            g.add(sum);
        }
        for(int i=0;i<lists1.size();i++){
            if(lists1.get(i).getApp()==null){//如果lists1app的名称为null就直接跳过
                continue;
            }
            for(int j=i+1;j<lists1.size();j++) {
                if(lists1.get(i).getApp().equals(lists1.get(j).getApp())){
                    lists1.get(i).setTime(lists1.get(i).getTime()+lists1.get(j).getTime());             //如果后面有相同的app就把数据相加
                    lists1.get(j).setApp(null);
                }
            }
        }
        int m=0;
        for(int i=0;i<lists1.size();i++){
            if(lists1.get(i).getApp()!=null){
                Week week=new Week(lists1.get(i).getTime(),lists1.get(i).getApp(),lists1.get(i).getIcon());
                lists2.add(week);
            }
        }
        for(int i=0;i<lists2.size();i++){
            Log.i("七天的数据为","包名为："+lists2.get(i).getApp());
        }
//        appTitleFragment appTitleFragment=new appTitleFragment(f,g);
    }

    public void test(){                 //将文件复制到其他地方
        int n=UseTimeDataManager.getInstance(getApplicationContext()).getmDayNum();
        long endTime = 0,startTime = 0;
        long time = System.currentTimeMillis() - n * DateTransUtils.DAY_IN_MILLIS;
        startTime = ReadRecordFileUtils.getRecordStartTime(WriteRecordFileUtils.BASE_FILE_PATH,time);
        endTime = ReadRecordFileUtils.getRecordEndTime(WriteRecordFileUtils.BASE_FILE_PATH,time) ;
        Toast.makeText(this,"已将系统数据写入本地文件",Toast.LENGTH_SHORT).show();
//        Log.i("BaseActivity"," BaseActivity--copyEventsToFile()    startTime = " + startTime + "  endTime = " + endTime);
        EventCopyToFileUtils.write(this,startTime-1000,endTime);
    }


        public void getAllAppNamesPackages(){
            PackageManager pm=getPackageManager();
            List<android.content.pm.PackageInfo> list=pm.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
            for (android.content.pm.PackageInfo packageInfo : list) {
                //获取到设备上已经安装的应用的名字,即在AndriodMainfest中的app_name。
                String appName=packageInfo.applicationInfo.loadLabel(getPackageManager()).toString();
                //获取到应用所在包的名字,即在AndriodMainfest中的package的值。
                String packageName=packageInfo.packageName;
                Drawable icon=packageInfo.applicationInfo.loadIcon(getPackageManager());
                AppInfo info=new AppInfo(icon,appName,packageName);
                list1.add(info);
            }
        }


    @Override
    public ArrayList<Week> callbackFun1() {
        return lists;
    }

    @Override
    public ArrayList<Week> callbackFun2() {
        return lists1;
    }
}