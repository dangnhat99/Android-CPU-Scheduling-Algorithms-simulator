package com.example.csa.Models;

public class ProcessModel {
    int firstTime;
    String processName;
    int lastTime;

    public ProcessModel(int firstTime, String processName, int lastTime) {
        this.firstTime = firstTime;
        this.processName = processName;
        this.lastTime = lastTime;
    }

    public int getFirstTime() {
        return firstTime;
    }

    public void setFirstTime(int firstTime) {
        this.firstTime = firstTime;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public int getLastTime() {
        return lastTime;
    }

    public void setLastTime(int lastTime) {
        this.lastTime = lastTime;
    }
}
