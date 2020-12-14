package com.example.playerxui;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;

import androidx.annotation.Nullable;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by dang on 2020-12-14.
 * Time will tell.
 *
 * @description
 */
public class MusicService extends Service {
    private MediaPlayer player;
    private Timer timer;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MusicControl();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        player = new MediaPlayer();//创建音乐播放器对象
    }

    /**
     * 计时器对象，获取歌曲时长，即当前播放时长
     * 会创建一个线程。需要在主线程处理
     */
    public void addTimer(){
        if (timer == null){
            timer = new Timer();//创建计时器对象
            TimerTask task = new  TimerTask(){
                @Override
                public void run() {
                    if (player == null) return;
                    int musicLength = player.getDuration();//歌曲总时长
                    int currentPosition = player.getCurrentPosition();//播放进度
                    Message message = MusicActivity.handler.obtainMessage();//创建消息对象
                    Bundle bundle = new Bundle();
                    bundle.putInt("musicLength",musicLength);
                    bundle.putInt("currentPosition",currentPosition);
                    message.setData(bundle);
                    //发送消息
                    MusicActivity.handler.sendMessage(message);
                }
            };
            //开始计时任务后的5毫秒，第一次执行task任务，以后每500毫秒执行一次
            timer.schedule(task,5,500);
        }
    }

    //处理有关播放音乐的逻辑
    class MusicControl extends Binder{
        public void play(int i){
            Uri uri = Uri.parse("android.resource://"+getPackageName()+"/raw/"+"music"+i);
            try{
                player.reset();//重置播放器
                //加载音乐文件
                player = MediaPlayer.create(getApplicationContext(),uri);
                player.start();
                addTimer();//计时器开始
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        public void pausePlay(){
            player.pause();
        }
        public void continuePlay(){
            player.start();
        }
        //拖动进度条时跳到相应的位置
        public void seekTo(int progress){
            player.seekTo(progress);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (player == null) return;
        if (player.isPlaying()) player.stop();
        player.release();//释放占用的空间
        player = null;
    }
}














