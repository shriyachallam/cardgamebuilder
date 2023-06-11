package oogasalad.runner.view.game_views;

import java.util.stream.Collectors;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import oogasalad.i18n.translators.Translator;
import oogasalad.runner.model.board.ImmutableBoard;
import oogasalad.runner.model.card.ImmutableCard;
import oogasalad.runner.model.card.group.ImmutableCardGroup;
import oogasalad.runner.model.player.ImmutablePlayer;
import oogasalad.runner.model.player.Player;
import oogasalad.runner.view.assistance.ExternalAssistant;
import oogasalad.runner.view.assistance.chat_gpt.ChatGPTExternalAssistant;

public class AssistPane {
  private final String filePathToCurrentGameJson = "src\\main\\java\\oogasalad\\runner\\view\\assistance\\chat_gpt\\ExternalAssistantJsonPlaceHolderPath.json";
  private final ExternalAssistant externalAssistant = new ChatGPTExternalAssistant(filePathToCurrentGameJson);
  private final VBox assistPane= new VBox();
  private final TextArea textArea = new TextArea();
  private final TextField textField = new TextField();

  ImmutableBoard immutableBoard;
  public AssistPane(Translator translator, ImmutableBoard immutableBoard) {
    assistPane.setId("assistPane");
    textArea.setEditable(false);
    textArea.setWrapText(true);
    Button button = new Button(translator.translateToUserSpace("Ask"));
    button.setOnAction(e -> askForHelpHandler());
    assistPane.getChildren().addAll(textArea, textField, button);
    VBox.setVgrow(assistPane, javafx.scene.layout.Priority.ALWAYS);
    this.immutableBoard = immutableBoard;
  }

  /**
   * Passes in the current game state to the external assistant and displays the response
   */
  private void askForHelpHandler() {
    if (!textField.getText().isEmpty()) {


      String message = "My hand contains ";
     for(ImmutablePlayer player : immutableBoard.players()) {
       if (player.isActive()) {
        message += formatCardGroup(player.hand());
       }
     }

     message += "\n Board contains: \n";
     for(ImmutableCardGroup cardGroup : immutableBoard.groups()) {
       message += cardGroup.tag() + " pile: " + formatCardGroup(cardGroup) + "\n";
     }

      String response = externalAssistant.provideAdvice(message + " " + textField.getText());
      showResponse(textField.getText(), response);
    }
  }


  public String formatCardGroup(ImmutableCardGroup group) {
    String groupString = group.stream()
        .map(ImmutableCard::toString)
        .collect(Collectors.joining(", "));
    return groupString;
  }


  private void showResponse(String prompt, String response) {
    textArea.setText(String.format("You asked: %s\nMy response: %s", prompt, response));
  }

  public Node getNode() {
    return assistPane;
  }


}
