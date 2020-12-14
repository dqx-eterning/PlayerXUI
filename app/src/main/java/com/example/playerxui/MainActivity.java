package com.example.playerxui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.example.playerxui.bean.City;
import com.example.playerxui.bean.LocaAndWeather;
import com.example.playerxui.bean.Now;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.qweather.sdk.bean.base.Code;
import com.qweather.sdk.bean.base.Lang;
import com.qweather.sdk.bean.base.Unit;
import com.qweather.sdk.bean.weather.WeatherNowBean;
import com.qweather.sdk.view.QWeather;
import com.xuexiang.xui.XUI;

import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.LitePal;
import org.litepal.tablemanager.Connector;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    //滑动布局
    private DrawerLayout mDrawerLayout;

    private LocationClient mLocationClient;
    private TextView positionText;

    //位置信息，和天气信息
    private LocaAndWeather locaAndWeather = new LocaAndWeather();
    private Now weather;


    public String[] name={"邓紫棋——光年之外","蔡健雅——红色高跟鞋","Taylor Swift——Love Story",
            "王诗安——Home","V.A.——Title 허밍","亦轩&圈妹——秋天的风它不曾见过桃花","音阙诗听&赵方婧——暗光",
            "亦轩&圈妹——秋天的风它不曾见过桃花","音阙诗听&赵方婧——暗光","亦轩&圈妹——秋天的风它不曾见过桃花",
            "音阙诗听&赵方婧——暗光"};
    public static int[] icons={R.drawable.music0,R.drawable.music1,
            R.drawable.music2,R.drawable.music3,R.drawable.music4,
            R.drawable.music5,R.drawable.music6,R.drawable.music5,
            R.drawable.music6,R.drawable.music5,R.drawable.music6};

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        XUI.initTheme(this);
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        //声明LocationClient类
        mLocationClient = new LocationClient(getApplicationContext());
        //注册监听函数
        mLocationClient.registerLocationListener(new MyLocationListener());


        /*Connector.getDatabase();
        City city = new City();
        city.setCityName("西安市");
        city.setCityId("101110101");
        city.save();*/

        //加载基础布局
        initView();


        //动态获取权限
        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (!permissionList.isEmpty()){
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(MainActivity.this,permissions,1);
        }else {
            initLocation();
        }


        //页面上展示列表
        RecyclerView recyclerView = findViewById(R.id.lv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        myAdapter adapter = new myAdapter();
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);


    }



    /**
     * 列表适配器
     */
    public class myAdapter extends RecyclerView.Adapter<myAdapter.ViewHolder>{

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_music, parent, false);
            final ViewHolder holder = new ViewHolder(view);
            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getAdapterPosition();
                    Intent intent=new Intent(MainActivity.this,MusicActivity.class);//创建Intent对象，
                    //将数据存入Intent对象
                    intent.putExtra("name",name[position]);
                    intent.putExtra("position",String.valueOf(position));
                    intent.putExtra("locaAndWeather",locaAndWeather);
                    startActivity(intent);
                }
            });
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.mName.setText(name[position]);
            holder.mImage.setImageResource(icons[position]);
        }

        @Override
        public int getItemCount() {
            return name.length;
        }
        class ViewHolder extends RecyclerView.ViewHolder{
            View mView;
            ImageView mImage;
            TextView mName;

            public ViewHolder(View view){
                super(view);
                mView = view;
                mImage = view.findViewById(R.id.iv);
                mName = view.findViewById(R.id.item_name);
            }
        }
    }
    /**
     * 请求权限回调函数
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if (grantResults.length > 0){
                    for (int result : grantResults) {
                        if (result != PackageManager.PERMISSION_GRANTED){
                            Toast.makeText(this, "拒绝权限无法使用定位服务", Toast.LENGTH_SHORT).show();
                        }
                        finish();
                        return;
                    }
                    initLocation();
                }else {
                    Toast.makeText(this, "发生未知错误", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
        }
    }

    /**
     * 加载布局
     */
    private void initView(){
        //toolbar替换actionbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            //设置点击图标切换到侧滑布局栏
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.icon_menu);
        }

        //侧滑布局
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.nav_call);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                mDrawerLayout.closeDrawers();
                switch (item.getItemId()){
                    case R.id.nav_location:
                        Toast.makeText(MainActivity.this, "点击了location", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        mDrawerLayout.closeDrawers();
                        break;
                }
                return true;
            }
        });
        //右下角悬浮按钮的点击事件
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v,"点击查看位置及天气信息>.<",Snackbar.LENGTH_SHORT)
                        .setAction("查看", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(MainActivity.this,locaAndWeather.getAddrStr()+"  今天天气："+
                                        locaAndWeather.getWeather()+"  温度："+locaAndWeather.getTemp()+"℃", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .show();
            }
        });
    }

    /**
     * 创建导航栏菜单
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar,menu);
        return true;
    }

    /**
     * 导航栏点击事件
     * @param item
     * @return
     */
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.setting:
                Toast.makeText(this, "当前版本为1", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }

    /**
     * 加载位置信息
     */
    private void initLocation(){


        LocationClientOption option = new LocationClientOption();
        option.setScanSpan(2000);
        option.setIsNeedAddress(true);
        mLocationClient.setLocOption(option);
        mLocationClient.start();
    }

    /**
     * 位置监听函数，返回位置信息
     */
    public class MyLocationListener implements BDLocationListener{
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            //Log.d("xxxxxxx", "onReceiveLocation: "+bdLocation.getLongitude());
            locaAndWeather.setAddrStr(bdLocation.getAddrStr());
            locaAndWeather.setCity(bdLocation.getCity());
            //Log.d("这是城市名称", city);
            //根据返回的位置信息，在数据库中查询天气id
            List<City> cities = LitePal.select("cityId").where("cityName = ?", locaAndWeather.getCity()).find(City.class);
            for (City city : cities) {
                //Log.d("数据库查询的结果", "onCreate: "+city2.getCityId()+"s::");
                locaAndWeather.setCityId(city.getCityId());
            }
            //此时城市id已近有了，直接调用获取天气方法
            Log.d("城市id", "onReceiveLocation: "+locaAndWeather.getCityId());
            getWeather();
        }
    }

    /**
     * 获取天气的方法
     */
    public void getWeather() {
        QWeather.getWeatherNow(MainActivity.this, "CN"+locaAndWeather.getCityId(), Lang.ZH_HANS, Unit.METRIC, new QWeather.OnResultWeatherNowListener() {
            public static final String TAG = "he_feng_now";
            @Override
            public void onError(Throwable e) {
                Log.i(TAG, "getWeather onError: " + e);
            }

            @Override
            public void onSuccess(WeatherNowBean weatherBean) {
                Log.i(TAG, "getWeather onSuccess: " + new Gson().toJson(weatherBean));
                //先判断返回的status是否正确，当status正确时获取数据，若status不正确，可查看status对应的Code值找到原因
                Log.d(TAG, "onSuccess: "+weatherBean.getCode()+":::"+Code.OK.getCode());
                if (Code.OK.getCode().equalsIgnoreCase(weatherBean.getCode())) {
                    Log.d(TAG, "onSuccess: 我在这里");
                    //把json对象转为String字符串
                   String jsonNow = new Gson().toJson(weatherBean.getNow());
                    //转module对象，因为其中有时间所以添加setDateFormat("yyyy-MM-dd HH:mm")方法
                    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm").create();
                    weather = gson.fromJson(jsonNow, Now.class);
                    Log.d(TAG, "onSuccess: "+weather.getTemp()+weather.getText());
                    locaAndWeather.setTemp(weather.getTemp());
                    locaAndWeather.setWeather(weather.getText());
                } else {
                    //在此查看返回数据失败的原因
                    String status = weatherBean.getCode();
                    Code code = Code.toEnum(status);
                    Log.i(TAG, "failed code: " + code);
                }
            }
        });
    }

    /**
     * 活动销毁方法，处理某些事件
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationClient.stop();
    }
}