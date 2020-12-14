package com.example.playerxui;

import android.animation.ObjectAnimator;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.playerxui.bean.LocaAndWeather;
import com.xuexiang.xui.XUI;

import static java.lang.Integer.parseInt;

public class MusicActivity extends AppCompatActivity implements View.OnClickListener {
    private static SeekBar seekBar;
    private static TextView progress,total,music_name;
    private static ImageView imageView;
    //旋转动画
    private ObjectAnimator animator;

    MyServiceConn conn;//绑定服务的类

    private MusicService.MusicControl musicControl;

    private boolean isUnbind = false;//记录服务是否被解绑
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        XUI.initTheme(this);
        super.onCreate(savedInstanceState);
        //消除导航框
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_music);

        init();
       /* String name = getIntent().getStringExtra("name");
        String position = getIntent().getStringExtra("position");
        LocaAndWeather locaAndWeather = (LocaAndWeather)getIntent().getSerializableExtra("locaAndWeather");
        Log.d("MusicActivity", "onCreate: "+name+":::"+position+"::"+locaAndWeather.getAddrStr());*/
    }

    /**
     * 加载布局方法
     */
    private void init(){
        progress = findViewById(R.id.tv_progress);
        total = findViewById(R.id.tv_total);
        seekBar = findViewById(R.id.seek_bar);
        music_name = findViewById(R.id.song_name);
        imageView =findViewById(R.id.iv_music);

        //四个播放按钮
        findViewById(R.id.btn_play).setOnClickListener(this);
        findViewById(R.id.btn_pause).setOnClickListener(this);
        findViewById(R.id.btn_continue_play).setOnClickListener(this);
        findViewById(R.id.btn_exit).setOnClickListener(this);

        //拿出从上以活动传递过来的数据并设置
        music_name.setText(getIntent().getStringExtra("name"));
        imageView.setImageResource(MainActivity.icons[Integer.parseInt(getIntent().getStringExtra("position"))]);
        //设置旋转动画
        animator = ObjectAnimator.ofFloat(imageView,"rotation",0f,360.0f);
        animator.setDuration(10000);//动画旋转一周的时间为10秒
        animator.setInterpolator(new LinearInterpolator());//匀速
        animator.setRepeatCount(-1);//-1表示设置动画无限循环

        //绑定服务
        Intent intent = new Intent(this,MusicService.class);
        conn = new MyServiceConn();
        bindService(intent,conn,BIND_AUTO_CREATE);
        //设置滑动条事件
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //进度条改变时，会调用此方法
                if (progress==seekBar.getMax()){//当滑动条到末端时，结束动画
                    animator.pause();//停止播放动画
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //滑动条开始滑动时调用
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //滑动条停止滑动时调用
                //根据拖动的进度改变音乐播放进度
                int progress=seekBar.getProgress();//获取seekBar的进度
                musicControl.seekTo(progress);//改变播放进度
            }
        });

    }

    /**
     * 绑定服务的类
     */
    class MyServiceConn implements ServiceConnection{
        //连接服务
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            musicControl = (MusicService.MusicControl) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }

    /**
     * 获取从子线程发送的播放数据，动态设置
     */
    public static Handler handler=new Handler(){//创建消息处理器对象
        //在主线程中处理从子线程发送过来的消息
        @Override
        public void handleMessage(Message msg){
            Bundle bundle=msg.getData();//获取从子线程发送过来的音乐播放进度
            int musicLength=bundle.getInt("musicLength");
            int currentPosition=bundle.getInt("currentPosition");
            seekBar.setMax(musicLength);
            seekBar.setProgress(currentPosition);
            //歌曲总时长
            int minute=musicLength/1000/60;
            int second=musicLength/1000%60;
            String strMinute=null;
            String strSecond=null;
            if(minute<10){//如果歌曲的时间中的分钟小于10
                strMinute="0"+minute;//在分钟的前面加一个0
            }else{
                strMinute=minute+"";
            }
            if (second<10){//如果歌曲中的秒钟小于10
                strSecond="0"+second;//在秒钟前面加一个0
            }else{
                strSecond=second+"";
            }
            total.setText(strMinute+":"+strSecond);
            //歌曲当前播放时长
            minute=currentPosition/1000/60;
            second=currentPosition/1000%60;
            if(minute<10){//如果歌曲的时间中的分钟小于10
                strMinute="0"+minute;//在分钟的前面加一个0
            }else{
                strMinute=minute+" ";
            }
            if (second<10){//如果歌曲中的秒钟小于10
                strSecond="0"+second;//在秒钟前面加一个0
            }else{
                strSecond=second+" ";
            }
            progress.setText(strMinute+":"+strSecond);
        }
    };

    /**
     * 设置按钮的点击事件方法
     * @param v
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_play:
                musicControl.play(parseInt(getIntent().getStringExtra("position")));
                animator.start();
                break;
            case R.id.btn_pause:
                musicControl.pausePlay();
                animator.pause();
                break;
            case R.id.btn_continue_play://继续播放按钮点击事件
                musicControl.continuePlay();
                animator.start();
                break;
            case R.id.btn_exit://退出按钮点击事件
                if (!isUnbind){
                    //暂停播放同时解绑服务
                    musicControl.pausePlay();
                    unbindService(conn);
                }
                isUnbind=true;
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!isUnbind){
            musicControl.pausePlay();
            unbindService(conn);
        }
    }
}