package client.ui;

import client.service.StudentInformation;
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
    private StudentInformation.oneStudentInformation student;



    public void setUser(User user) {
        this.user = user;
        this.student=StudentInformation.viewOneStudentInfo(user.getUsername());
        if (nameLabel != null) {
            nameLabel.setText("姓名: " + student.getName());
        }
        if (idlabel != null) {
            idlabel.setText("学号: "+student.getId());
        }
        if (genderlabel != null) {
            genderlabel.setText("性别: "+student.getGender() );
        }
        if (birthlabel != null) {
            birthlabel.setText("出生日期: "+student.getBirthday() );
        }
        if (originlabel != null) {
            originlabel.setText("籍贯: "+student.getOrigin() );
        }
        if (academylabel != null) {
            academylabel.setText("院系: "+student.getAcademy() );
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
