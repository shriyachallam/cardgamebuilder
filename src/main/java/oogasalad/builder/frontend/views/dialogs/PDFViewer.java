package oogasalad.builder.frontend.views.dialogs;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import oogasalad.builder.frontend.views.StageBuilder;

public class PDFViewer {
    private Group group;
    private Scene scene;
    private Stage stage;
    private double WIDTH = 300, HEIGHT = 400;

    public PDFViewer(Stage primaryStage, String title, Parent pdfFile) {
        this.group = new Group();
        this.scene = new Scene(group, WIDTH, HEIGHT);
        this.stage = new Stage();

        stage.setTitle(title);
        stage.setScene(new Scene(pdfFile));
        stage.initOwner(primaryStage);

        stage.setX(primaryStage.getX() + 100);
        stage.setY(primaryStage.getY() + 100);



        stage.show();
    }
}
