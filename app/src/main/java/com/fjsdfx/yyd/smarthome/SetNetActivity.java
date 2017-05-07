package com.fjsdfx.yyd.smarthome;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SetNetActivity extends AppCompatActivity {
    private Button connect;
    private Button cancel;
    private EditText serverIP;
    private EditText serverPort;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_net);
        getView();
        //设置监听器
        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((!serverIP.getText().toString().equals("")) &&(!serverPort.getText().toString().equals(""))) {
                    Intent intent = getIntent();
                    intent.putExtra("serverIP", serverIP.getText().toString());
                    intent.putExtra("serverPort", serverPort.getText().toString());
                    SetNetActivity.this.setResult(1, intent);
                    SetNetActivity.this.finish();
                }else{
                    Toast.makeText(SetNetActivity.this, "IP或端口不能为空", Toast.LENGTH_SHORT).show();
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetNetActivity.this.finish();
            }
        });
    }

    private void getView()
    {
        connect = (Button)findViewById(R.id.connect);
        cancel = (Button)findViewById(R.id.cancel);
        serverIP = (EditText)findViewById(R.id.serverIP);
        serverPort = (EditText)findViewById(R.id.serverPort);
    }
}
