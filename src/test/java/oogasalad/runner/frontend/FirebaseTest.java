package oogasalad.runner.frontend;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.io.File;
import oogasalad.builder.backend.record.GameRecord;
import org.junit.jupiter.api.Test;

public class FirebaseTest{
  @Test
  void testFirebaseGamesFiles() {
    File folder = new File("data/json/games/firebase/");
    File[] listOfFiles = folder.listFiles();

    for (int i = 0; i < listOfFiles.length; i++) {
      if (listOfFiles[i].isFile()) {
        assertNotEquals( "data/json/games/firebase/WYXIxwwwk5GwCw5AfomL.json", listOfFiles[i].getName());
      }
  }

  }
}
