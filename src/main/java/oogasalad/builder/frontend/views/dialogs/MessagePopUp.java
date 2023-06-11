package oogasalad.builder.frontend.views.dialogs;

import javafx.animation.PauseTransition;
import javafx.scene.control.Alert;
import javafx.util.Duration;

public class MessagePopUp {
    private String message;

    public MessagePopUp(String message){
        this.message = message;
        showMessage(message, "Error!", new Duration(3000));
    }

    public MessagePopUp(String message, Duration duration) {
        this.message = message;
        showMessage(message, "Error!", duration);
    }

    public MessagePopUp(String message, String title) {
        this.message = message;

        showMessage(message, title, new Duration(3000));
    }

    public void showMessage(String message, String title, Duration duration) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();

        // Set the duration of the pause before the pop-up window disappears
        PauseTransition delay = new PauseTransition(duration);
        delay.setOnFinished(event -> alert.close());
        delay.play();
    }

    @Override
    public String toString(){
        return message;
    }
}
