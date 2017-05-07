package com.fjsdfx.yyd.smarthome;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.fjsdfx.yyd.bean.Room;
import com.fjsdfx.yyd.util.ReadThread;

import java.util.Timer;
import java.util.TimerTask;

public class KitchenActivity extends AppCompatActivity {
    private TextView temp;
    private TextView humi;
    private TextView smog;
    private Switch lamp;
    private Toolbar toolbar;
    private Handler handler;
    private int LampStatus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kitchen);
        getView();
        toolbar.setNavigationIcon(R.drawable.activity_back_bg);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KitchenActivity.this.finish();
            }
        });
        lamp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    //Toast.makeText(KitchenActivity.this,"选中",Toast.LENGTH_SHORT).show();
                    byte[] array = new byte[10];
                    byte[] temp = new byte[4];
                    array[0] = (byte) 0xFE;
                    array[1] = (byte) 0x01;
                    array[2] = (byte) 0x01;
                    array[3] = (byte) 0x01;
                    temp[0] = array[2];
                    temp[1] = array[3];
                    array[4] = MainActivity.XOR(temp,2);
                    temp[0] = array[1];
                    temp[1] = array[2];
                    temp[2] = array[3];
                    temp[3] = array[4];
                    array[5] = MainActivity.XOR(temp,4);
                    Message msg = new Message();
                    msg.what = 0x345;
                    msg.obj = array;
                    if(ReadThread.revHandler != null)
                        ReadThread.revHandler.sendMessage(msg);
                }else {
                    byte[] array = new byte[10];
                    byte[] temp = new byte[4];
                    array[0] = (byte) 0xFE;
                    array[1] = (byte) 0x01;
                    array[2] = (byte) 0x01;
                    array[3] = (byte) 0x00;
                    temp[0] = array[2];
                    temp[1] = array[3];
                    array[4] = MainActivity.XOR(temp,2);
                    temp[0] = array[1];
                    temp[1] = array[2];
                    temp[2] = array[3];
                    temp[3] = array[4];
                    array[5] = MainActivity.XOR(temp,4);
                    Message msg = new Message();
                    msg.what = 0x345;
                    msg.obj = array;
                    if(ReadThread.revHandler != null)
                        ReadThread.revHandler.sendMessage(msg);
                }
            }
        });
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what == 0x135){
                    temp.setText(MainActivity.kitchen.getTemp()+"");
                    humi.setText(MainActivity.kitchen.getHumi()+"");
                    smog.setText(MainActivity.kitchen.getSmog()+"");
                    if(MainActivity.kitchen.getLight() != LampStatus)
                    {
                        if(MainActivity.kitchen.getLight() == 1){
                            lamp.setChecked(true);
                            LampStatus = 1;
                        }else {
                            lamp.setChecked(false);
                            LampStatus = 0;
                        }
                    }
                }
            }
        };
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(0x135);
            }
        },0,2000);
    }

    private void getView(){
        toolbar = (Toolbar)findViewById(R.id.toolBar);
        temp = (TextView)findViewById(R.id.temp);
        humi = (TextView)findViewById(R.id.humi);
        smog = (TextView)findViewById(R.id.smog);
        lamp = (Switch)findViewById(R.id.LampSwitch);
    }
}
