package com.example.operativosui;

import com.example.operativosui.services.MainService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MainController {

    @FXML
    private VBox userContainer;

    @FXML
    private TextField userAmount;

    @FXML
    private TextField timeAmount;

    @FXML
    protected void startSimulation() {
        new MainService(userContainer, Double.parseDouble(userAmount.getText()), Double.parseDouble(timeAmount.getText()) ).start();
    }
}