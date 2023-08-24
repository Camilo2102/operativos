package com.example.operativosui.services;

import com.example.operativosui.enums.Status;
import com.example.operativosui.models.Process;
import com.example.operativosui.models.User;
import com.example.operativosui.utils.RandomUtil;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainService extends Service<Void> {

    private VBox userBox;
    private User[] users;
    private final List<Process> processCreatedList;
    private List<Process> processReadyList;
    private List<Process> processExecutedList;

    private List<Process> processStopedList;
    private int totalTime;
    private double resourcesPerProcess;
    private ExecutorService userScheduler;
    private final ScheduledExecutorService processGeneratorScheduler;

    public MainService(VBox userBox, double userAmount, double totalTime) {
        this.userBox = userBox;
        this.totalTime = (int) (totalTime * 1000);
        this.processCreatedList = new ArrayList<>();
        this.processReadyList = new ArrayList<>();
        this.processExecutedList = new ArrayList<>();
        this.processStopedList = new ArrayList<>();
        this.processGeneratorScheduler = Executors.newScheduledThreadPool(0);
        this.userScheduler = Executors.newSingleThreadExecutor();
        generateUsersWindows((int) userAmount);
    }


    private void generateUsersWindows(int userAmount) {
        Status[] statuses = Status.values();
        this.users = new User[userAmount];
        this.userBox.getChildren().clear();

        for (int i = 0; i < userAmount; i++) {
            users[i] = new User();
            HBox processBox = new HBox();
            TitledPane titledPane = new TitledPane();
            HBox.setHgrow(titledPane, Priority.ALWAYS);
            VBox.setVgrow(titledPane, Priority.ALWAYS);
            titledPane.setPrefWidth(1920);
            titledPane.setPrefHeight(1080);

            for (int j = 0; j < statuses.length; j++) {
                ScrollPane scrollPane = new ScrollPane();
                TitledPane subTittlePane = new TitledPane("" + statuses[j], scrollPane);
                subTittlePane.setPrefWidth(1920);
                subTittlePane.setPrefHeight(1080);
                users[i].setTitledPane(j, subTittlePane);
                processBox.getChildren().add(subTittlePane);
            }

            titledPane.setContent(processBox);
            double timeInitial = (double) (totalTime / userAmount) / 1000;
            titledPane.setText("Usuario: " + (i + 1) + " / Tiempo: " + timeInitial + " seg ");
            this.userBox.getChildren().add(titledPane);
        }
    }

    private void processInitialize() {
        for (int i = 0; i < users.length; i++) {
            double actualTime = getActualTime(i);

            int finalI = i;

            Runnable userProcessRun = () -> {
                try {
                    long start = System.currentTimeMillis();
                    Platform.runLater(() -> {
                        setExecutedForReadyProcess(finalI);
                    });
                    Thread.sleep((long) actualTime / 2);
                    Platform.runLater(() -> {
                        setReadyForProcessCreated(finalI);
                    });
                    Thread.sleep((long) (actualTime / 2 ));
                    long end = System.currentTimeMillis();
                    users[finalI].setTotalTime(end - start);

                    Platform.runLater(() -> {
                        setReadyForExecutedProcess(finalI);
                    });


                } catch (Exception e) {
                    e.printStackTrace();
                }
            };

            userScheduler.execute(userProcessRun);
        }

    }

    private double getActualTime(int i) {
        TitledPane userPane = (TitledPane) this.userBox.getChildren().get(i);

        int totalProcess = getTotalProcess();

        double actualTime;

        if(totalProcess > 0){
            actualTime = (((double) totalTime / totalProcess) * (users[i].getProcessReadyList().size() + users[i].getProcessExecutedList().size()))/1000;
        } else {
            actualTime = (double) (totalTime / users.length) / 1000;
        }

        userPane.setText("Usuario: " + (i + 1) + " / Tiempo: " + actualTime + " seg ");
        return actualTime * 1000;
    }


    private int getTotalProcess(){
        int count = 0;
        for (User user : users) {
            count += user.getProcessReadyList().size();
            count += user.getProcessExecutedList().size();
        }
        return count;
    }

    private void startProcessGenerator() {
        Runnable generateProcessTask = () -> {
            Platform.runLater(this::generateNewProcess);
        };

        processGeneratorScheduler.scheduleAtFixedRate(generateProcessTask, 0, (totalTime / 1000), TimeUnit.SECONDS);
    }

    @Override
    protected Task<Void> createTask() {
        return new Task<>() {
            @Override
            protected Void call() throws Exception {
                startProcessGenerator();
                return null;
            }
        };
    }
    private void setReadyForProcessCreated(int userIndex) {
        users[userIndex].setFromCreatedToReady();
    }

    private void setExecutedForReadyProcess(int userIndex){
        if(!users[userIndex].getProcessReadyList().isEmpty()){
            users[userIndex].setFromReadyToExecuted();
        }
    }

    private void setReadyForExecutedProcess(int userIndex){
        if(!users[userIndex].getProcessExecutedList().isEmpty()){
            users[userIndex].setFromExecutedToReady();
        }
    }

    private void generateNewProcess() {
        boolean generate = RandomUtil.randomNumber(4) == 1;
        if (true) {
            int index = RandomUtil.randomNumber(users.length) - 1;
            this.addProcess(index);
        } else {
            processInitialize();
        }
    }


    public void addProcess(int index) {
        this.getResourcesPerProcess();

        Process createdProcess = createProcess();

        users[index].addToCreatedList(createdProcess);
        this.processCreatedList.add(createdProcess);

        processInitialize();
    }


    private Process createProcess() {
        return new Process(this.resourcesPerProcess, RandomUtil.randomNumber(totalTime * 10, totalTime * 2) );
    }

    private void getResourcesPerProcess() {
        int totalActiveProcess = this.getTotalActiveProcess();
        this.resourcesPerProcess = (double) totalTime / (totalActiveProcess);
    }

    private int getTotalActiveProcess() {
        int total = 1;
        for (Process process : this.processExecutedList) {
            if (process.getStatus() == Status.EXECUTED) {
                total++;
            }
        }
        return total;
    }


}
