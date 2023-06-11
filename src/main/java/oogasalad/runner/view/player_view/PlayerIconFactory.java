package oogasalad.runner.view.player_view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import oogasalad.Main;
import oogasalad.runner.view.game_views.PlayerIcon;

public class PlayerIconFactory {

    private final String FILE_PATH = "oogasalad.runner.playerIcons.playerIcons";
    private final ResourceBundle iconProps = ResourceBundle.getBundle(FILE_PATH);
    private final int numberOfIcons;
    private List<Image> playerIconList;

    public PlayerIconFactory() {
      retrievePlayerIcons();
      this.numberOfIcons=playerIconList.size();
    }

    public PlayerIcon createPlayerIcon(String playerName, int playerIconIndex) {
      ImageView icon=createImageView(playerIconList.get(playerIconIndex));
      return new PlayerIcon(playerName, icon);
    }

    public PlayerIcon createPlayerIcon(int playerIconIndex) {
      ImageView icon=createImageView(playerIconList.get(playerIconIndex));
      return new PlayerIcon(icon);
    }

    public int getIconNumber(){
      return numberOfIcons;
    }

    private void retrievePlayerIcons(){
      String[] iconList=iconProps.getString("playerIconList").split(",");
      playerIconList= Arrays.stream(iconList)
          .map( key -> new Image(Objects.requireNonNull(Main.class.getResourceAsStream("/oogasalad/runner/playerIcons/%s.png".formatted(key)))))
          .collect(Collectors.toList());
    }

    private ImageView createImageView(Image image){
      double aspectRatio = image.getWidth() / image.getHeight();
      double maxHeight = 80;
      double newWidth = maxHeight * aspectRatio;
      ImageView imageView = new ImageView(image);
      imageView.setFitWidth(newWidth);
      imageView.setFitHeight(maxHeight);
      return imageView;
    }

  public List<PlayerIcon> getAllPlayerIcons() {
      List<PlayerIcon> playerIconList=new ArrayList<>();
      for(int i=0;i<numberOfIcons;i++){
        playerIconList.add(createPlayerIcon(i));
      }
    return playerIconList;
  }
}
