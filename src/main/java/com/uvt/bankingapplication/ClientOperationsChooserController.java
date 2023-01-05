package com.uvt.bankingapplication;

import com.uvt.bankingapplication.HelloApplication;
import com.uvt.bankingapplication.classes.Message;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import javax.xml.namespace.QName;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class ClientOperationsChooserController implements Initializable {
    @FXML
    public Label helloLabel, errorLabel;
    @FXML
    public ComboBox<String> opChooserComboBox;
    @FXML
    public Button logoutButton, selectButton;

    public void logoutAndExitButtonClick(){
        Platform.exit();
    }

    public void selectButtonClick(ActionEvent e) throws IOException {
        if(opChooserComboBox.getValue() == null) {
            Message msg = new Message(errorLabel, 4000);
            msg.start();
        }
        else
            switch(opChooserComboBox.getValue()){
                case "Display Client Details" ->{
                    Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("client-details-view.fxml")));
                    Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
                    Scene scene = new Scene(root);
                    stage.setTitle("Client Details");
                    stage.setScene(scene);
                    stage.centerOnScreen();
                    stage.show();
                }
                case "Make transfers" ->{
                    Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("make-transfers-view.fxml")));
                    Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
                    Scene scene = new Scene(root);
                    stage.setTitle("Transfer Operations");
                    stage.setScene(scene);
                    stage.centerOnScreen();
                    stage.show();
                }
                case "Display account details" ->{

                }
                case "Retrieve cash" ->{

                }
            }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        helloLabel.setText("Hello, " + HelloApplication.clientName);
        opChooserComboBox.getItems().addAll("Display Client Details", "Make transfers", "Display account details", "Retrieve cash");
    }
}
