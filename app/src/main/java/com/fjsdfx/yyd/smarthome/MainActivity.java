package com.fjsdfx.yyd.smarthome;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.fjsdfx.yyd.bean.Room;
import com.fjsdfx.yyd.util.ReadThread;
import com.fjsdfx.yyd.view.CircleMenuLayout;
import java.net.InetSocketAddress;


public class MainActivity extends AppCompatActivity {

    private CircleMenuLayout mCircleMenuLayout;
    private Handler handler;
    public static Room kitchen,bedroom,livingroom;
    private int Lamp1Status, Lamp2Status,Lamp3Status;
    private  int Curtain2Status,Curtain3Status;

    private String[] mItemTexts = new String[] { "厨房", "客厅", "卧室",
            "设置"};
    private int[] mItemImgs = new int[] { R.drawable.home_mbank_1_normal,
            R.drawable.home_mbank_2_normal, R.drawable.home_mbank_3_normal,
            R.drawable.home_mbank_4_normal};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        kitchen = new Room();
        bedroom = new Room();
        livingroom = new Room();
        mCircleMenuLayout = (CircleMenuLayout) findViewById(R.id.id_menulayout);
        mCircleMenuLayout.setMenuItemIconsAndTexts(mItemImgs, mItemTexts);  //设置每个item的图标和文字



        mCircleMenuLayout.setOnMenuItemClickListener(new CircleMenuLayout.OnMenuItemClickListener()
        {

            @Override
            public void itemClick(View view, int pos)
            {
               switch (pos)
               {
                   case 0:
                       Intent intent0 = new Intent(MainActivity.this, KitchenActivity.class);
                       startActivity(intent0);
                       break;
                   case 1:
                       Intent intent1 = new Intent(MainActivity.this, LivingroomActivity.class);
                       startActivity(intent1);
                       break;
                   case 2:
                       Intent intent2 = new Intent(MainActivity.this, BedroomActivity.class);
                       startActivity(intent2);
                       break;
                   case 3:
                       Intent intent3 = new Intent(MainActivity.this, SetNetActivity.class);
                       startActivityForResult(intent3, 1);
                       break;
               }
            }

            @Override
            public void itemCenterClick(View view)
            {
                Toast.makeText(MainActivity.this,
                        "you can do something just like ccb  ",
                        Toast.LENGTH_SHORT).show();

            }
        });
        //处理服务器发来的数据
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what == 0x123){
                    byte[] data = (byte[]) msg.obj;
                    //Toast.makeText(MainActivity.this, "接收到："+data[0]+","+data[1],Toast.LENGTH_SHORT).show();
                    if(XOR(data, data.length-1) == data[data.length-1]){
                        if(0x01 == data[0]) { //厨房
                            kitchen.setTemp(data[2]);
                            kitchen.setHumi(data[1]);
                            kitchen.setSmog(data[3] * 256 + data[4]);
                            if(data[5] != Lamp1Status) {
                                kitchen.setLight(data[5]);
                                Lamp1Status = data[5];
                            }
                        }else if(0x02 == data[0]) {  //卧室
                            bedroom.setTemp(data[2]);
                            bedroom.setHumi(data[1]);
                            if(data[3] != Lamp2Status) {
                                bedroom.setLight(data[3]);
                                Lamp2Status = data[3];
                            }
                            if (data[4] != Curtain2Status) {
                                bedroom.setCurtion(data[4]);
                                Curtain2Status = data[4];
                            }
                        }else if(0x03 == data[0]){  //客厅
                            livingroom.setTemp(data[2]);
                            livingroom.setHumi(data[1]);
                            if(data[3] != Lamp3Status) {
                                livingroom.setLight(data[3]);
                                Lamp3Status = data[3];
                            }
                            if(data[4] != Curtain3Status) {
                                livingroom.setCurtion(data[4]);
                                Curtain3Status = data[4];
                            }
                        }
                    }
                }else if(msg.what == 0x01){
                    Toast.makeText(MainActivity.this,"连接成功", Toast.LENGTH_SHORT).show();
                }else if(msg.what == 0x02){
                    Toast.makeText(MainActivity.this,"网络异常，无法连接到服务器", Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 1 && resultCode == 1){
            Bundle serverData = data.getExtras();
            String serverIP = serverData.getString("serverIP");
            String serverPort = serverData.getString("serverPort");
            InetSocketAddress address = new InetSocketAddress(serverIP,Integer.parseInt(serverPort));
            new Thread( new ReadThread(handler,address)).start();
        }
    }

    public static byte XOR(byte[] data, int len)
    {
        byte result = data[0];
        for(int i = 1; i<len; i++)
        {
            result = (byte) (result^data[i]);
        }
        return result;
    }
}
