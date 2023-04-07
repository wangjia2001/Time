package com.example.time;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.TimePickerDialog;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class LockFragment_Activity extends AppCompatActivity
{
    private static final String TAG = "LockFragment_Activity";

    private ListView lv_main;
    private List<AppInfo> data;
    private AppAdapter adapter;
    private TextView tv_nv;
    private NavView navView;

    private int mHour;
    private int mMinute;

    private RadioGroup mRg;

    String unlockpackageName;

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

    //   private static final String TAG = "MainActivity";

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock);
        mRg = findViewById(R.id.rg_lock);

        //初始化成员变
        Log.d(TAG, "onCreate: "+isNoOption());
        Log.d(TAG, "onCreate: "+isNoSwitch());

        if(isNoOption()==true && isNoSwitch() == false){
            Intent intent = new Intent(
                    Settings.ACTION_USAGE_ACCESS_SETTINGS);
            startActivity(intent);
            Toast.makeText(LockFragment_Activity.this,"请设置允许访问使用记录",Toast.LENGTH_LONG).show();
        }



        lv_main = (ListView) findViewById(R.id.lv_main);
        tv_nv = (TextView)findViewById(R.id.tv_nv );
        navView = (NavView)findViewById(R.id.nv);
        tv_nv.setVisibility(View.GONE);
        navView.setTextView(tv_nv);
        SpinnerCreate();

        data = getAllAppInfos();
        adapter = new AppAdapter();
        //显示列表
        lv_main.setAdapter(adapter);


        mRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(RadioGroup group,int checkedId){
                switch (checkedId){
                    case R.id.lock_count:
                        Intent b_count = new Intent(
                                LockFragment_Activity.this, MainActivity.class);
                        b_count.putExtra("has",1);
                        startActivity(b_count);
                        break;

                    case R.id.lock_chart:
                        Intent b_chart = new Intent(
                                LockFragment_Activity.this, MainActivity.class);
                        b_chart.putExtra("has",2);
                        startActivity(b_chart);
                        break;

                    case R.id.lock_me:
                        Log.d("TAG", "onCheckedChanged:4 ");
                        Intent b_me = new Intent(
                                LockFragment_Activity.this, MainActivity.class);
                        b_me.putExtra("has",4);
                        startActivity(b_me);

                        break;
                    case R.id.lock_lock:
                        break;
                }
            }

        });

//^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^第一次启动
        PackageInfo info = null;
        try {
            info = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        int currentVersion = info.versionCode;
        SharedPreferences prefs = getSharedPreferences("data",MODE_PRIVATE);
        int lastVersion = prefs.getInt("VERSION_KEY", 0);

        if (currentVersion > lastVersion) {
            Toast.makeText(LockFragment_Activity.this,"第一次打开软件，进行初始化",Toast.LENGTH_LONG).show();


            SharedPreferences.Editor editormy =getSharedPreferences("data",MODE_PRIVATE).edit();
            Log.i("version", "currentVersion"+currentVersion);
            editormy.putInt("VERSION_KEY", currentVersion);
            editormy.apply();

            try
            {
                plintPkgAndCls(getResolveInfos()); //把所有软件存储起来
            } catch (ParseException e) {
                e.printStackTrace();
            }


////6666666666666666666666666666666666666测试，锁三个应用，微信，qq，喜马拉雅，可以删除
//            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
//            Date date=new Date();
//            Calendar calendar = Calendar.getInstance();
//            calendar.setTime(date);
//            calendar.add(Calendar.DAY_OF_MONTH, 1);
//            date = calendar.getTime();
//            changetime("com.taobao.idlefish",date);
//            changetime("com.iflytek.mscv5plusdemo",date);
//            changetime("com.powersi.zhrs",date);
//            changetime("com.ximalaya.ting.android",date);
//            //       Toast.makeText(MainActivity.this,simpleDateFormat.format(date),Toast.LENGTH_LONG).show();
////        changetime("com.example.bcreceive",date);
////        Log.i("11111", simpleDateFormat.format(date));
////6666666666666666666666666666666666666

        } else
        {
//                Toast.makeText(LockFragment_Activity.this,"不是第一次启动",Toast.LENGTH_LONG).show();
//不是第一次启动
        }

//^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

        //wj!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//给ListView设置item的点击监听
        lv_main.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            /**
             * parent : ListView
             * view : 当前行的item视图对象
             * position : 当前行的下标
             */
            @SuppressLint("WrongConstant")

            //时间选择器
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //提示当前行的应用名称
                final String PackageName = data.get(position).getPackageName();
                //提示
                Toast.makeText(LockFragment_Activity.this, PackageName, Toast.LENGTH_SHORT).show();

                Log.i(TAG, "获取到的包名"+PackageName);

                //点击触发时间选择器
                new TimePickerDialog(LockFragment_Activity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                        //hour，minute为设置的时间
                        mHour= hourOfDay;
                        mMinute = minute;

                        Log.i(TAG, "获取到的时间: "+mHour+":"+mMinute);
                        Log.i(TAG, "第二次的包名: "+PackageName);

                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
                        Date date=new Date();
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(date);
                        calendar.set(Calendar.HOUR_OF_DAY,mHour);
                        calendar.set(Calendar.MINUTE,mMinute);
                        date = calendar.getTime();
                        Log.i(TAG, "修改后设置的时间为："+simpleDateFormat.format(date));

                        changetime(PackageName,date);

                    }
                },0,0,true).show();


            }
        });
//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!


        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);

//解锁---------------------------
        Intent i=getIntent();
        unlockpackageName=i.getStringExtra("UNLOCK_APP");
        if(unlockpackageName!=null)
        {
            Log.i("jiesuo", "Main收到了解锁的包名"+unlockpackageName);
            Date date2=new Date();
            Calendar calendar2 = Calendar.getInstance();
            calendar2.setTime(date2);
            calendar2.add(Calendar.DAY_OF_MONTH, -1);
            date2 = calendar2.getTime();

            changetime(unlockpackageName,date2);
//            Intent intent=new Intent(MainActivity.this,);

//跳转+++++++++++++++++++++++
            Log.i("jiesuo", "Main准备跳转");
            doStartApplicationWithPackageName(unlockpackageName);
//+++++++++++++++++++++++

        }
//---------------------------

        timer(this);
    }



    private void plintPkgAndCls(List<ResolveInfo> resolveInfos) throws ParseException {
        PackageManager packageManager = getPackageManager();

        List<String> a = new ArrayList<>();

        SharedPreferences.Editor editor =getSharedPreferences("data",MODE_PRIVATE).edit();


        //?????????????????????????????????????????????????????????????????
        Log.i("pkg", "####################start######################");
        for (int i = 0; i < resolveInfos.size(); i++) {
            String pkg = resolveInfos.get(i).activityInfo.packageName;
            String cls = resolveInfos.get(i).activityInfo.name;
            String title = null;
            try {
                ApplicationInfo applicationInfo = packageManager.getPackageInfo(pkg, i).applicationInfo;
                title = applicationInfo.loadLabel(packageManager).toString();
            } catch (Exception e) {
            }
            Date date=new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DAY_OF_MONTH, 0);
            date = calendar.getTime();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
            editor.putString(pkg,simpleDateFormat.format(date));
            editor.apply();
            Log.i("pkg", title + "：" + pkg + "/" + cls);//打印
        }
        Log.i("pkg", "#####################end#######################");

        //?????????????????????????????????????????????????????????????????
    }

    private List<ResolveInfo> getResolveInfos()
    {
        List<ResolveInfo> appList = null;

        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        PackageManager pm = getPackageManager();
        appList = pm.queryIntentActivities(intent, 0);
        Collections.sort(appList, new ResolveInfo.DisplayNameComparator(pm));
        return appList;
    }



    public void timer(final Context context){
        //clock为程序锁标志位，true为启用程序锁功能，false为禁用程序锁功能，
        //当用户已经输入密码，进入被锁程序后，程序锁标志位为false；其他情况为true

        Timer timer=new Timer();
        TimerTask timerTask=new TimerTask()
        {
            public String packageName ;
            public String re()
            {
                return packageName;
            }

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void run() {
                @SuppressLint("WrongConstant") UsageStatsManager usageStatsManager = (UsageStatsManager) context.getApplicationContext()
                        .getSystemService("usagestats");

                long ts = System.currentTimeMillis();
                List<UsageStats> queryUsageStats = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_BEST,0, ts);

                UsageStats recentStats = null;
                for (UsageStats usageStats : queryUsageStats) {
                    if (recentStats == null || recentStats.getLastTimeUsed() < usageStats.getLastTimeUsed()) {
                        recentStats = usageStats;
                    }
                }
                packageName = recentStats != null ? recentStats.getPackageName() : null;


                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
                Date date2 = new Date(System.currentTimeMillis());//当前时间

                Log.i("pkg", "软件名称："+packageName);
                Log.i("jiesuo", "当前顶端的包名为："+packageName);
                SharedPreferences p =getSharedPreferences("data",MODE_PRIVATE);
                String date=p.getString(packageName,"");//软件时间
                Log.i("pkg", packageName+"软件的时间："+date);

                try
                {
                    Date date1 =simpleDateFormat.parse(date);
                    Date date3 =simpleDateFormat.parse(simpleDateFormat.format(date2));
//                    Log.i("pkg", "软件时间: "+date1);//软件时间
//                    Log.i("pkg", "手机时间: "+date3);//手机时间

                    if (date3.getTime()<date1.getTime()){//手机时间小于软件时间，锁机
                        Log.i("pkg", "run: nonono");

                        Intent i = new Intent(
                                LockFragment_Activity.this, lockactivity.class);
                        i.putExtra("packet",packageName);
                        startActivity(i);

//                        startActivity(new Intent(MainActivity.this,lockactivity.class));

                    }else if (date2.getTime()>date3.getTime()){
                        Log.i("pkg", "run: yesyesyes");
                    }
                } catch (ParseException e) {}
            }
        };
        timer.schedule(timerTask,0,1*1000);//1s查看一次
    }

    public boolean changetime(String name,Date time)//修改时间
    {
        SharedPreferences.Editor e =getSharedPreferences("data",MODE_PRIVATE).edit();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");

        e.putString(name,simpleDateFormat.format(time));
        e.apply();
        return true;

    }


    //跳转到其他app
    private void doStartApplicationWithPackageName(String packagename) {

        // 通过包名获取此APP详细信息，包括Activities、services、versioncode、name等等
        PackageInfo packageinfo = null;

        try {
            packageinfo = getPackageManager().getPackageInfo(packagename, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        if (packageinfo == null) {
            return;
        }

        // 创建一个类别为CATEGORY_LAUNCHER的该包名的Intent
        Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
        resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        resolveIntent.setPackage(packageinfo.packageName);

        // 通过getPackageManager()的queryIntentActivities方法遍历
        List<ResolveInfo> resolveinfoList = getPackageManager()
                .queryIntentActivities(resolveIntent, 0);

        ResolveInfo resolveinfo = resolveinfoList.iterator().next();
        if (resolveinfo != null) {
            // packagename = 参数packname
            String packageName = resolveinfo.activityInfo.packageName;
            // 这个就是我们要找的该APP的LAUNCHER的Activity[组织形式：packagename.mainActivityname]
            String className = resolveinfo.activityInfo.name;
            // LAUNCHER Intent
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);

            // 设置ComponentName参数1:packagename参数2:MainActivity路径
            ComponentName cn = new ComponentName(packageName, className);

            intent.setComponent(cn);
            startActivity(intent);
        }
    }


    class  AppAdapter extends BaseAdapter {

        private List<AppInfo> list;
        private AppInfo appInfo;

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return 0;
        }

        //返回带数据当前行的Item视图对象
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //1. 如果convertView是null, 加载item的布局文件
            if(convertView==null) {
                Log.e("TAG", "getView() load layout");
                convertView = View.inflate(LockFragment_Activity.this, R.layout.item_main, null);
            }


            //2. 得到当前行数据对象
            AppInfo appInfo = data.get(position);

            //3. 得到当前行需要更新的子View对象
            ImageView imageView = (ImageView) convertView.findViewById(R.id.iv_item_icon);
            TextView textView = (TextView) convertView.findViewById(R.id.tv_item_name);

            //4. 给视图设置数据
            imageView.setImageDrawable(appInfo.getIcon());
            textView.setText(appInfo.getAppName());

            //返回convertView
            return convertView;
        }
    }

    /*
     * 得到手机中所有应用信息的列表
     * AppInfo
     *  Drawable icon  图片对象
     *  String appName
     *  String packageName
     */

    protected List<AppInfo> getAllAppInfos() {

        List<AppInfo> list = new ArrayList<AppInfo>();
        // 得到应用的packgeManager
        PackageManager packageManager = getPackageManager();
        // 创建一个主界面的intent
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        // 得到包含应用信息的列表
        List<ResolveInfo> ResolveInfos = packageManager.queryIntentActivities(
                intent, 0);
        // 遍历
        for (ResolveInfo ri : ResolveInfos) {
            // 得到包名
            String packageName = ri.activityInfo.packageName;
            // 得到图标
            Drawable icon = ri.loadIcon(packageManager);
            // 得到应用名称
            String appName = ri.loadLabel(packageManager).toString();
            // 封装应用信息对象
            AppInfo appInfo = new AppInfo(icon, appName, packageName);
            // 添加到list
            list.add(appInfo);
        }
        return list;
    }

    public boolean onItemLongClick(AdapterView<?> parent, View view,
                                   int position, long id) {
        //删除当前行
        //删除当前行的数据
        data.remove(position);
        //更新列表
        //lv_main.setAdapter(adapter);//显示列表, 不会使用缓存的item的视图对象
        adapter.notifyDataSetChanged();//通知更新列表, 使用所有缓存的item的视图对象
        return true;

    }

    protected void SpinnerCreate(){
        Spinner mSpinner = null;
        mSpinner = (Spinner)findViewById(R.id.spin_app);

        String[] arr = {"所有应用","防沉迷中"};

        //创建ArrayAdapter对象
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,arr);
        //为Spinner设置Adapter
        mSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
            public void onItemSelected(AdapterView<?>arg0,View arg1,int arg2,long arg3){

            }
            public void onNothingSelected(AdapterView<?>arg0){

            }
        });
    }

}