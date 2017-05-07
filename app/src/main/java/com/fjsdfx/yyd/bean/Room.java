package com.fjsdfx.yyd.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/10/17.
 */

public class Room {
    private int temp;
    private int humi;
    private int light;
    private int curtion;
    private int smog;

    public int getSmog() {
        return smog;
    }

    public void setSmog(int smog) {
        this.smog = smog;
    }

    public Room() {
    }

    public int getCurtion() {
        return curtion;
    }

    public void setCurtion(int curtion) {
        this.curtion = curtion;
    }

    public int getHumi() {
        return humi;
    }

    public void setHumi(int humi) {
        this.humi = humi;
    }

    public int getLight() {
        return light;
    }

    public void setLight(int light) {
        this.light = light;
    }

    public int getTemp() {
        return temp;
    }

    public void setTemp(int temp) {
        this.temp = temp;
    }
}
