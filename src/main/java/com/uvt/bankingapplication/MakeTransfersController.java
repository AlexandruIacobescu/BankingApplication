package com.uvt.bankingapplication;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
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
        String queryString = "SELECT IBAN FROM accounts WHERE client_id = (SELECT client_id FROM clients WHERE name = ?);";
        PreparedStatement stmt = conn.prepareStatement(queryString);
        stmt.setString(1, HelloApplication.clientName);
        ResultSet set = stmt.executeQuery();
        while(set.next()){
            accounts.add(set.getString(1));
        }
        conn.close();
        return accounts;
    }

    private void showUnexpectedSystemErrorDialog(Exception e){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Transfer operation failed");
        alert.setContentText("An unexpected system error occurred, please contact administrator.");

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        String exceptionText = sw.toString();

        Label label = new Label("The exception stacktrace was:");

        TextArea textArea = new TextArea(exceptionText);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(label, 0, 0);
        expContent.add(textArea, 0, 1);
        alert.getDialogPane().setExpandableContent(expContent);

        alert.showAndWait();
    }

    private double getConvertedAmount(String convertFrom, String conversionIban, double amount) throws SQLException {
        Connection conn = DriverManager.getConnection(url, uname, password);
        PreparedStatement st = conn.prepareStatement("SELECT currency FROM accounts WHERE IBAN = ?");
        st.setString(1, conversionIban);
        ResultSet set = st.executeQuery();
        set.next();
        String convertTo = set.getString(1);
        if(convertFrom.equals(convertTo))
            return amount;
        else{
            PreparedStatement stmt = conn.prepareStatement("SELECT value FROM conversions WHERE `from`= ? AND `to`= ?");
            stmt.setString(1, convertFrom);
            stmt.setString(2, convertTo);
            ResultSet res = stmt.executeQuery();
            res.next();
            amount *= res.getDouble(1);
            return amount;
        }
    }

    private ArrayList<String> personalAccounts = (ArrayList<String>) getAccountsOfCurrentClient();
    @FXML
    public Label clientLabel, controlLabel;
    @FXML
    public TextField currencyTextField, amountTextField, transferToTextField, balanceTextField;
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

    public void setPersonalAccountsToComboBoxIndexChanged(){

    }

    public void setPersonalAccountsFromComboBoxIndexChanged() throws SQLException {
        Connection conn = DriverManager.getConnection(url, uname, password);
        PreparedStatement st = conn.prepareStatement("SELECT currency,amount FROM accounts WHERE IBAN = ?");
        st.setString(1, personalAccountsFromComboBox.getValue());
        ResultSet set = st.executeQuery();
        set.next();
        currencyTextField.setText(set.getString(1));
        balanceTextField.setText(set.getString(2));
        conn.close();
    }

    private void showTransferFailDialog(String message){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Transfer Error");
        alert.setHeaderText("The transfer operation failed");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean isPersonalAccount(String iban) throws SQLException {
        Connection conn = DriverManager.getConnection(url, uname, password);
        PreparedStatement stmt = conn.prepareStatement("SELECT iban FROM accounts WHERE client_id = (SELECT client_id FROM clients WHERE name = ?)");
        stmt.setString(1, HelloApplication.clientName);;
        ResultSet set = stmt.executeQuery();
        while(set.next()){
            if(set.getString(1).equals(iban))
                return true;
        }
        return false;
    }

    private boolean ibanExists(String iban) throws SQLException {
        Connection conn = DriverManager.getConnection(url, uname, password);
        PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(account_number) FROM accounts WHERE iban = ?");
        stmt.setString(1, iban);;
        ResultSet set = stmt.executeQuery();
        set.next();
        return !set.getString(1).equals("0");
    }

    private void transferFromTo(String fromIban, String toIban, double amount) throws SQLException {
        Connection conn = DriverManager.getConnection(url, uname, password);
        PreparedStatement stmt1 = conn.prepareStatement("UPDATE accounts SET amount = amount - " + amount + " WHERE IBAN = ?");
        stmt1.setString(1, fromIban);
        stmt1.executeUpdate();
        PreparedStatement stmt2 = conn.prepareStatement("UPDATE accounts SET amount = amount + ? WHERE IBAN = ?");
        stmt2.setDouble(1, getConvertedAmount(currencyTextField.getText(), toIban, amount));
        stmt2.setString(2, toIban);
        stmt2.executeUpdate();
        conn.close();
    }

    private boolean isDoubleAmount(String amount){
        try{
            Double.parseDouble(amount);
        }catch(NumberFormatException e){
            return false;
        }
        return true;
    }

    private boolean hasSufficientFunds(String FromIban, double amount) throws SQLException {
        Connection conn = DriverManager.getConnection(url, uname, password);
        PreparedStatement stmt = conn.prepareStatement("SELECT amount FROM accounts WHERE IBAN = ?");
        stmt.setString(1, FromIban);
        ResultSet set = stmt.executeQuery();
        set.next();
        if(Double.parseDouble(set.getString(1)) < amount)
            return false;
        conn.close();
        return true;
    }

    private boolean isNonNegativeAmount(double amount){
        return amount >= 0;
    }

    private void showTransferSuccessDialog(String message){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Transfer Result");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    public void transferButtonClick() throws SQLException {
        try {
            switch (operationComboBox.getValue()) {
                case "Transfer to a specific account" -> {
                    if (personalAccountsFromComboBox.getValue() == null)
                        showTransferFailDialog("Please select an account to transfer from.");
                    else if (!ibanExists(transferToTextField.getText()))
                        showTransferFailDialog("Inserted IBAN is not valid.");
                    else if (isPersonalAccount(transferToTextField.getText()))
                        showTransferFailDialog("Please Insert a non-personal account to transfer to.");
                    else if (!isDoubleAmount(amountTextField.getText()))
                        showTransferFailDialog("Please enter a valid transfer amount.");
                    else if (!isNonNegativeAmount(Double.parseDouble(amountTextField.getText())))
                        showTransferFailDialog("Please enter a non-negative amount.");
                    else if (!hasSufficientFunds(personalAccountsFromComboBox.getValue(), Double.parseDouble(amountTextField.getText())))
                        showTransferFailDialog("Insufficient funds to execute transfer. Transfer failed.");
                    else {
                        try {
                            transferFromTo(personalAccountsFromComboBox.getValue(), transferToTextField.getText(), Double.parseDouble(amountTextField.getText()));
                            showTransferSuccessDialog("Transfer executed successfully.");
                        } catch (Exception e) {
                            showTransferFailDialog("Unexpected transfer failure. Please contact your bank agent.");
                            e.printStackTrace();
                        }
                    }
                }
                case "Transfer between personal accounts" -> {
                    if (personalAccountsFromComboBox.getValue() == null || personalAccountsToComboBox.getValue() == null)
                        showTransferFailDialog("Please select two accounts to transfer between.");
                    else if (personalAccountsFromComboBox.getValue().equals(personalAccountsToComboBox.getValue()))
                        showTransferFailDialog("Please select two different accounts to transfer between.");
                    else if (!isDoubleAmount(amountTextField.getText()))
                        showTransferFailDialog("Please enter a valid transfer amount.");
                    else if (!isNonNegativeAmount(Double.parseDouble(amountTextField.getText())))
                        showTransferFailDialog("Please enter a non-negative amount.");
                    else if (!hasSufficientFunds(personalAccountsFromComboBox.getValue(), Double.parseDouble(amountTextField.getText())))
                        showTransferFailDialog("Insufficient funds to execute transfer. Transfer failed.");
                    else {
                        try {
                            transferFromTo(personalAccountsFromComboBox.getValue(), personalAccountsToComboBox.getValue(), Double.parseDouble(amountTextField.getText()));
                            showTransferSuccessDialog("Transfer executed successfully.");
                        } catch (Exception e) {
                            showTransferFailDialog("Unexpected transfer failure. Please contact your bank agent.");
                            e.printStackTrace();
                        }
                    }
                }
                default -> showTransferFailDialog("Please select a transfer operation.");
            }
        }catch (Exception e){
            showUnexpectedSystemErrorDialog(e);
            e.printStackTrace();
        }
        setPersonalAccountsFromComboBoxIndexChanged();
    }

    public void operationsComboBoxIndexChanged(){
        if(operationComboBox.getValue().equals("Transfer between personal accounts")){
            transferToTextField.setVisible(false);
            personalAccountsToComboBox.setVisible(true);
            personalAccountsFromComboBox.setVisible(true);
        }else{
            transferToTextField.setVisible(true);
            personalAccountsToComboBox.setVisible(false);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        clientLabel.setText("Hello, " + HelloApplication.clientName);
        operationComboBox.getItems().addAll("Transfer to a specific account", "Transfer between personal accounts");
        try {
            personalAccountsToComboBox.getItems().addAll(getAccountsOfCurrentClient());
            personalAccountsFromComboBox.getItems().addAll(getAccountsOfCurrentClient());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
