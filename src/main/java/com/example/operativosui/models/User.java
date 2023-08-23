package com.example.operativosui.models;

import com.example.operativosui.enums.Status;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
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
    private ScrollPane[] scrollPane;

    public User() {
        this.scrollPane = new ScrollPane[4];
        this.processHistory = new ArrayList<>();
        this.processCreatedList = new ArrayList<>();
        this.processReadyList = new ArrayList<>();
        this.processExecutedList = new ArrayList<>();
        this.processEndedList = new ArrayList<>();
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
        this.processExecutedList.addAll(this.processReadyList);
        this.processReadyList = new ArrayList<>();
        addProcessToPane(2, this.processExecutedList, Status.EXECUTED);
        addProcessToPane(1, this.processReadyList, Status.READY);
    }

    public void setFromExecutedToReady(){
        this.processExecutedList.addAll(processReadyList);
        Map<String, List<Process>> validProcess = validateExecutedProcess(processExecutedList);
        this.processReadyList = new ArrayList<>(validProcess.get("pending"));
        this.processEndedList.addAll(validProcess.get("complete"));
        this.processExecutedList = new ArrayList<>();
        addProcessToPane(3, this.processEndedList, Status.ENDED);
        addProcessToPane(2, this.processExecutedList, Status.EXECUTED);
        addProcessToPane(1, this.processReadyList, Status.READY);
    }

    private Map<String, List<Process>> validateExecutedProcess(List<Process> processes){
        Map<String, List<Process>> filteredProcess = new HashMap<>();
        List<Process> pendingProcess = new ArrayList<>();
        List<Process> completedProcess = new ArrayList<>();
        for (Process process : processes) {
            if(process.getExecutionTime() < totalTime) {
                completedProcess.add(process);
            }else {
                pendingProcess.add(process);
            }
        }
        filteredProcess.put("pending", pendingProcess);
        filteredProcess.put("complete", completedProcess);
        return filteredProcess;
    }


    private void addProcessToPane(int index, List<Process> processList, Status status){
        VBox content = new VBox();
        for (Process process : processList) {
            process.setStatus(status);
            Label label = new Label("Proceso: " + process.getPid());
            content.getChildren().add(label);
        }
        getScrollPane()[index].setContent(content);
    }


    public List<Process> getProcessReadyList() {
        return processReadyList;
    }

    public ScrollPane[] getScrollPane() {
        return scrollPane;
    }

    public void setScrollPane(int index, ScrollPane pane) {
        this.scrollPane[index] = pane;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTotalTime(long totalTime) {
        this.totalTime += totalTime;
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
