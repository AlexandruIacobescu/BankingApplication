package com.uvt.bankingapplication;

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
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.*;

public class MakeTransfersController implements Initializable {
    private final String url = "jdbc:mysql://localhost:3306/bank";
    private final String uname = "root";
    private final String password = "Zeppelin2001";

    public MakeTransfersController() throws SQLException {
    }

    private Collection<String> getAccountsOfCurrentClient() throws SQLException {
        Collection<String> accounts = new ArrayList<>();
        Connection conn = DriverManager.getConnection(url, uname, password);
        String queryString = "SELECT account_number FROM accounts WHERE client_id = (SELECT client_id FROM clients WHERE name = ?);";
        PreparedStatement stmt = conn.prepareStatement(queryString);
        stmt.setString(1, HelloApplication.clientName);
        ResultSet set = stmt.executeQuery();
        while(set.next()){
            accounts.add(set.getString(1));
        }
        conn.close();
        return accounts;
    }
    private ArrayList<String> personalAccounts = (ArrayList<String>) getAccountsOfCurrentClient();
    @FXML
    public Label clientLabel, controlLabel;
    @FXML
    public TextField transferFromTextField, amountTextField, transferToTextField;
    @FXML
    public Button TransferButton, backButton, logoutButton;
    @FXML
    public ComboBox<String> operationComboBox, personalAccountsToComboBox, personalAccountsFromComboBox;

    public void logoutButtonClick(){
        Platform.exit();
    }

    public void backButtonClick(ActionEvent e) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("client-operations-chooser-view.fxml")));
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Client Operations Center");
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    private void setPersonalAccountsToComboBoxes(){
        if(personalAccountsToComboBox.getValue() == null && personalAccountsFromComboBox.getValue() == null){
            personalAccountsToComboBox.getItems().addAll(personalAccounts);
            personalAccountsFromComboBox.getItems().addAll(personalAccounts);
        }else if(personalAccountsToComboBox.getValue() == null && personalAccountsFromComboBox.getValue() != null){
            ArrayList<String> exclusiveList = new ArrayList<>();
            for(var account : personalAccounts){
                if(!personalAccountsFromComboBox.getValue().equals(account))
                    exclusiveList.add(account);
            }
            personalAccountsToComboBox.getItems().removeAll();
            personalAccountsToComboBox.getItems().addAll(exclusiveList);
        }else if(personalAccountsToComboBox.getValue() != null && personalAccountsFromComboBox.getValue() == null){
            ArrayList<String> exclusiveList = new ArrayList<>();
            for(var account : personalAccounts){
                if(!personalAccountsToComboBox.getValue().equals(account))
                    exclusiveList.add(account);
            }
            personalAccountsFromComboBox.getItems().removeAll();
            personalAccountsFromComboBox.getItems().addAll(exclusiveList);
        }
    }

    public void setPersonalAccountsToComboBoxIndexChanged(){
        setPersonalAccountsToComboBoxes();
    }

    public void setPersonalAccountsFromComboBoxIndexChanged(){
        setPersonalAccountsToComboBoxes();
    }

    public void personalAccountsComboBoxIndexChanged(){
        if(operationComboBox.getValue().equals("Transfer between personal accounts")){
            transferToTextField.setVisible(false);
            transferFromTextField.setVisible(false);
            personalAccountsToComboBox.setVisible(true);
            personalAccountsFromComboBox.setVisible(true);
            setPersonalAccountsToComboBoxes();
        }else{
            transferToTextField.setVisible(true);
            transferFromTextField.setVisible(true);
            personalAccountsToComboBox.setVisible(false);
            personalAccountsFromComboBox.setVisible(false);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        clientLabel.setText("Hello, " + HelloApplication.clientName);
        operationComboBox.getItems().addAll("Transfer to a specific account", "Transfer between personal accounts");
    }
}
