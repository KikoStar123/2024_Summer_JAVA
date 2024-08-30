package client.ui;

import client.service.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class Studentuicontroller {
    @FXML
    private Label academylabel;

    @FXML
    private Button back;

    @FXML
    private Label birthlabel;

    @FXML
    private Label genderlabel;

    @FXML
    private Label idlabel;

    @FXML
    private Label nameLabel;

    @FXML
    private Label originlabel;

    private User user;

    public void setUser(User user) {
        this.user = user;
        if (nameLabel != null) {
            nameLabel.setText("姓名: " + user.getTruename());
        }
        //调用函数返回学生id
        if (idlabel != null) {
            idlabel.setText("学号: " );
        }
        if (genderlabel != null) {
            genderlabel.setText("性别: "+user.getgender() );
        }


    }

    @FXML
    void back(ActionEvent event) {
        Stage stage = (Stage) nameLabel.getScene().getWindow();
        stage.close();
        MainUI mainUI = new MainUI(user);
        mainUI.display();
    }

}
