package com.example.operativosui.models;


import java.util.ArrayList;
import java.util.List;

import com.example.operativosui.enums.Status;
import com.example.operativosui.utils.RandomUtil;


public class FairShareThread extends Thread{
    private List<Process> processExecutionList;
    private int totalResources;
    private double resourcesPerProcess;
    private int lengthProcess;

    public FairShareThread() {
        this.totalResources = 100;
        this.resourcesPerProcess = 100;
        this.lengthProcess = 0;
        this.processExecutionList = new ArrayList<>();
    }

    @Override
    public void run() {
        super.run();
        synchronized (this){
            while (true){
                validateProcessStatus();
                generateNewProcess();
                this.waitTimer();
            }
        }
    }

    private void validateProcessStatus(){
        if(lengthProcess < processExecutionList.size()){
            this.getResourcesPerProcess();
            this.reassignProcessResources();
            lengthProcess++;
        }

    }

    private void generateNewProcess(){
        boolean generate = RandomUtil.randomNumber(4) == 1;
        if(generate){
            this.addProcess();
        }
    }

    private void waitTimer(){
        try {
            super.wait(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void addProcess(){
        this.getResourcesPerProcess();

        Process createdProcess = createProcess();
        createdProcess.setStatus(Status.READY);

        this.processExecutionList.add(createdProcess);
    }

    private void reassignProcessResources(){
        for (Process process : this.processExecutionList) {
            if(process.getStatus() == Status.EXECUTED) {
                process.setAssignedResources(this.resourcesPerProcess);
            }
        }
    }
    private Process createProcess(){
        return new Process(this.resourcesPerProcess, RandomUtil.randomNumber(100000));
    }

    private void getResourcesPerProcess(){
        int totalActiveProcess = this.getTotalActiveProcess();
        this.resourcesPerProcess = (double) totalResources / (totalActiveProcess);
    }

    private int getTotalActiveProcess(){
        int total = 1;
        for (Process process : this.processExecutionList) {
            if(process.getStatus() == Status.EXECUTED){
                total++;
            }
        }
        return total;
    }


    public void setProcessExecutionList(List<Process> processExecutionList) {
        this.processExecutionList = processExecutionList;
    }

    public void setTotalResources(int totalResources) {
        this.totalResources = totalResources;
    }

    public void setResourcesPerProcess(double resourcesPerProcess) {
        this.resourcesPerProcess = resourcesPerProcess;
    }

    public void setLengthProcess(int lengthProcess) {
        this.lengthProcess = lengthProcess;
    }
}
