package oogasalad.file_manager.mock_game;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Enumeration;
import java.util.ResourceBundle;
import oogasalad.i18n.AppType;
import oogasalad.i18n.translators.DefaultTranslator;
import oogasalad.i18n.translators.Translator;

public class MockGameConverter {
  private static final String RESOURCE_FILEPATH = "oogasalad.file_manager.languages.es-mx_messages";
  private static final String RESOURCE_FILEPATH_OG = "oogasalad.file_manager.languages.en-us_messages";
  private static final ResourceBundle messages = ResourceBundle.getBundle(RESOURCE_FILEPATH);
  private static final ResourceBundle messages_og = ResourceBundle.getBundle(RESOURCE_FILEPATH_OG);
  private static final Translator translator = new DefaultTranslator(AppType.FILE_MANAGER, "Spanish");
  public static void main(String[] args){

    BufferedReader reader;

    try {
      reader = new BufferedReader(new FileReader("/Users/alecliu/CSProjects/CS308/oogasalad_team01/src/test/java/oogasalad/file_manager/mock_game/mock.txt"));
      String line = reader.readLine();

      while (line != null) {
        String processedLine = processLine(line);
        System.out.println(processedLine);
        line = reader.readLine();
      }

      reader.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static String processLine(String line) {
    String done = line;
    Enumeration<String> keys = messages_og.getKeys();
    while (keys.hasMoreElements()) {
      String key = keys.nextElement();
      String words = messages_og.getString(key);
      if(line.contains(words)){
        String newPhrase = translator.translateToUserSpace(key);
        done = done.replaceAll(words, newPhrase);
      }
    }
    if(done.length() == 0){
      return line;
    }
    return done;
  }

}
