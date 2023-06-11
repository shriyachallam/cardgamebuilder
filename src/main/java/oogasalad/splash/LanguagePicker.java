package oogasalad.splash;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import oogasalad.builder.frontend.GUIFactory;

import java.util.ResourceBundle;

public class LanguagePicker extends Scene {
    private final String Language_CSS = "BuilderUI/CSSFiles/language.css";
    private final ResourceBundle properties = ResourceBundle.getBundle("oogasalad/builder/languages/LanguagePicker");
    private static final int WIDTH = 200;
    private static final int HEIGHT = 100;
    private GUIFactory f;

    /**
     * Creates a new LanguagePicker scene
     * @param bp BorderPane to add the scene to
     * @param callback Callback to call when a language is chosen
     */
    public LanguagePicker(BorderPane bp, Callback<String, Void> callback) {
        super(bp, WIDTH, HEIGHT);
        this.getStylesheets().add(Language_CSS);
        f = new GUIFactory("LanguagePicker");

        Label langTitle = f.createLabel(properties.getString("Title"), "LanguagePrompt", null);

        ComboBox<String> langChooser = new ComboBox<>();
        for(String s : properties.getString("Options").split(" ")){
            langChooser.getItems().add(s);
        }
        langChooser.getSelectionModel().select(0); //Default english

        Label error = f.createLabel(properties.getString("Error"), "LanguageError", null);
        error.getStyleClass().add("error");

        Button submit = new Button(properties.getString("ChooseBtn"));
        submit.setOnAction(e -> {
            if(langChooser.getValue() == null){
                bp.setBottom(error);
                bp.setAlignment(error, Pos.CENTER);
            } else {
                bp.setBottom(null);
                callback.call(langChooser.getValue());
            }
        });

        HBox chooseBox = new HBox(langChooser, submit);
        chooseBox.getStyleClass().add("box");

        VBox languageBox = new VBox(langTitle, chooseBox);
        languageBox.getStyleClass().add("box");

        bp.setCenter(languageBox);
    }
}
