package com.example.operativosui.models;

import com.example.operativosui.enums.Status;
import com.example.operativosui.utils.RandomUtil;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User {
    private String name;
    private long executionTime;
    private long totalTime;
    private List<Process> processHistory;
    private List<Process> processCreatedList;
    private List<Process> processReadyList;
    private List<Process> processExecutedList;
    private List<Process> processEndedList;
    private List<Process> processStoppedList;
    private TitledPane[] titledPane;

    private long actualTime;

    public User() {
        this.titledPane = new TitledPane[5];
        this.processHistory = new ArrayList<>();
        this.processCreatedList = new ArrayList<>();
        this.processReadyList = new ArrayList<>();
        this.processExecutedList = new ArrayList<>();
        this.processEndedList = new ArrayList<>();
        this.processStoppedList = new ArrayList<>();
    }

    public void addToCreatedList(Process createdProcess) {
        this.processCreatedList.add(createdProcess);
        addProcessToPane(0, processCreatedList, Status.CREATED);
    }

    public void setFromCreatedToReady(){
        this.processReadyList = this.processCreatedList;
        this.processCreatedList = new ArrayList<>();
        addProcessToPane(1, this.processReadyList, Status.READY);
        addProcessToPane(0, this.processCreatedList, Status.CREATED);
    }

    public void setFromReadyToExecuted(){
        List<Process> tempStoppedList = new ArrayList<>();
        for (Process process : this.processStoppedList) {
            boolean resume = RandomUtil.randomNumber(5) == 1;
            if(resume){
                this.processReadyList.add(process);
            } else {
                tempStoppedList.add(process);
            }
        }

        this.processStoppedList = tempStoppedList;
        this.processExecutedList.addAll(this.processReadyList);
        this.processReadyList = new ArrayList<>();
        addProcessToPane(4, this.processStoppedList, Status.STOPPED);
        addProcessToPane(2, this.processExecutedList, Status.EXECUTED);
        addProcessToPane(1, this.processReadyList, Status.READY);
    }

    public void setFromExecutedToReady(){
        this.processExecutedList.addAll(processReadyList);
        Map<String, List<Process>> validProcess = validateExecutedProcess(processExecutedList);
        this.processReadyList = new ArrayList<>(validProcess.get("pending"));
        this.processEndedList.addAll(validProcess.get("complete"));
        this.processStoppedList.addAll(validProcess.get("stopped"));
        this.processExecutedList = new ArrayList<>();
        addProcessToPane(4, this.processStoppedList, Status.STOPPED);
        addProcessToPane(3, this.processEndedList, Status.ENDED);
        addProcessToPane(2, this.processExecutedList, Status.EXECUTED);
        addProcessToPane(1, this.processReadyList, Status.READY);
    }

    private Map<String, List<Process>> validateExecutedProcess(List<Process> processes){
        Map<String, List<Process>> filteredProcess = new HashMap<>();
        List<Process> pendingProcess = new ArrayList<>();
        List<Process> completedProcess = new ArrayList<>();
        List<Process> stoppedProcess = new ArrayList<>();
        for (Process process : processes) {
            process.setRemainingTime(actualTime);
            if(process.getExecutionTime() < totalTime) {
                completedProcess.add(process);
            }else {
                boolean stop = RandomUtil.randomNumber(20) == 1;
                if(stop){
                    stoppedProcess.add(process);
                } else {
                    pendingProcess.add(process);
                }
            }
        }
        filteredProcess.put("stopped", stoppedProcess);
        filteredProcess.put("pending", pendingProcess);
        filteredProcess.put("complete", completedProcess);
        return filteredProcess;
    }


    private void addProcessToPane(int index, List<Process> processList, Status status){
        VBox content = new VBox();
        for (Process process : processList) {
            process.setStatus(status);
            Label label = new Label(process.getPid() + " : " + process.getName() + " | " + (process.getRemainingTime() / 1000) + " seg ");
            content.getChildren().add(label);
        }
        getTitledPane()[index].setText(getTitledPane()[index].getText().split("/")[0] + "/Procesos: " + processList.size());
        ScrollPane pane = (ScrollPane) getTitledPane()[index].getContent();
        pane.setContent(content);
        getTitledPane()[index].setContent(pane);
    }


    public List<Process> getProcessReadyList() {
        return processReadyList;
    }

    public TitledPane[] getTitledPane() {
        return titledPane;
    }

    public void setTitledPane(int index, TitledPane pane) {
        this.titledPane[index] = pane;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTotalTime(long totalTime) {
        this.totalTime += totalTime;
        this.actualTime = totalTime;
    }

    public List<Process> getProcessHistory() {
        return processHistory;
    }

    public List<Process> getProcessCreatedList() {
        return processCreatedList;
    }

    public List<Process> getProcessExecutedList() {
        return processExecutedList;
    }

    public List<Process> getProcessEndedList() {
        return processEndedList;
    }

    public long getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(long executionTime) {
        this.executionTime = executionTime;
    }
}
