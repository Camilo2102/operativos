package com.example.operativosui.models;

import com.example.operativosui.enums.Status;
import com.example.operativosui.utils.RandomUtil;

public class Process {
    private int pid;
    private static int pidCounter = 0;
    private String name;
    private double assignedResources;
    private long executionTime;
    private long remainingTime;
    private volatile Status status;

    public Process(double assignedResources, long executionTime) {
        this.pid = ++pidCounter;
        this.name = RandomUtil.generateRandomName();
        this.assignedResources = assignedResources;
        this.executionTime = executionTime;
        this.remainingTime = executionTime;
        this.setStatus(Status.CREATED);
    }

    public void setStatus(Status status) {
        System.out.println("Process with PID: " + pid +
                "\nChanged his status from: " + this.status + " to " + status +
                "\nResources assigned: " + this.assignedResources);
        this.status = status;
    }

    public void setAssignedResources(double assignedResources) {
        System.out.println("Process with PID: " + this.pid +
                "\nChanged his resources from: " + this.assignedResources + " to " + assignedResources);
        this.assignedResources = assignedResources;
    }

    public void stopProcess() {
        this.setStatus(Status.STOPPED);
    }

    public long getRemainingTime() {
        return remainingTime;
    }

    public void setRemainingTime(long elapsedTime) {
        this.remainingTime -= elapsedTime;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public static void setPidCounter(int pidCounter) {
        Process.pidCounter = pidCounter;
    }

    public double getAssignedResources() {
        return assignedResources;
    }

    public long getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(long executionTime) {
        this.executionTime = executionTime;
    }

    public Status getStatus() {
        return status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
