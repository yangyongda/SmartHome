package com.fjsdfx.yyd.util;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created by Administrator on 2016/10/17.
 */

public class ReadThread implements Runnable {
    private Handler handler;
    public  static  Handler revHandler;
    private InetSocketAddress address;
    private BufferedInputStream  bis;
    private OutputStream os;
    byte[] receData;
    Socket socket;

    public ReadThread(Handler handler, InetSocketAddress address){
        this.handler = handler;
        this.address = address;
        receData = new byte[50];
    }
    @Override
    public void run() {

        socket = new Socket();
        try {
            socket.connect(address,5000);
            handler.sendEmptyMessage(0x01);
            bis = new BufferedInputStream(socket.getInputStream());
            os = socket.getOutputStream();
            new Thread(){
                @Override
                public void run() {
                    int len;
                    try {
                        while((len = bis.read(receData,0,50)) != -1) {
                            Message msg = new Message();
                            msg.what = 0x123;
                            msg.obj = receData;
                            handler.sendMessage(msg);
                            receData = new byte[50];
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        } catch (IOException e) {
            handler.sendEmptyMessage(0x02);
            e.printStackTrace();
        }

        Looper.prepare();
        // 创建revHandler对象
        revHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                // 接收到UI线程的中用户输入的数据
                if (msg.what == 0x345) {
                    // 将用户在文本框输入的内容写入网络
                    try {
                            os.write((byte[]) msg.obj);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

        };
        // 启动Looper
        Looper.loop();
    }

}
