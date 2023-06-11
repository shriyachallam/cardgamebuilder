package oogasalad.builder.frontend;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class GUIFactory {
    private String name;
    public GUIFactory(String id){
        name = "GUIFactory_" + id;
    }
    public String getName(){
        return name;
    }

    /**
     * Creates a button with the given text, id, and position type
     *
     * @param text
     * @param id
     * @param position
     */
    public Button createButton(String text, String id, Pos position){
        Button b = new Button(text);
        b.setId(id);
        b.setAlignment(position);
        return b;
    }

    /**
     * Creates a label with the given text, id, and position type
     *
     * @param text
     * @param id
     * @param position
     * @return
     */
    public Label createLabel(String text, String id, Pos position){
        Label l = new Label(text);
        l.setId(id);
        if(position!=null){l.setAlignment(position);}
        return l;
    }

    /**
     * Formats a VBox with the given spacing and position type
     *
     * @param v
     * @param spacing
     * @param position
     * @return
     */
    public VBox formatVBox(VBox v, int spacing, Pos position){
        if(position==null){position = Pos.CENTER;}
        v.setAlignment(position);
        if(spacing>0){
            v.setSpacing(spacing);
            v.setPadding(new Insets(spacing));
        }
        return v;
    }

    /**
     * Formats an HBox with the given spacing and position type
     *
     * @param h
     * @param spacing
     * @param position
     * @return
     */
    public HBox formatHBox(HBox h, int spacing, Pos position){
        if(position!=null){h.setAlignment(position);}
        if(spacing>0){h.setSpacing(spacing);}
        return h;
    }

    /**
     * Creates an ImageView with the given image, height, and width
     *
     * @param i
     * @param h
     * @param w
     * @return
     */
    public ImageView createIV(Image i, int h, int w, boolean glow){
        ImageView iv = new ImageView(i);
        iv.setFitHeight(h);
        iv.setFitWidth(w);
        if(glow){addGlowOnHover(iv);}
        return iv;
    }

    /**
     * Adds a glow effect to the given node on hover
     * @param node
     */
    public void addGlowOnHover(ImageView node) {
        final Glow glow = new Glow(1.0);
        node.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                node.setEffect(glow);
            }
        });
        node.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                node.setEffect(null);
            }
        });
    }

}
