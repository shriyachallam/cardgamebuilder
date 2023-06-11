package oogasalad.service;
import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.ExportedUserRecord;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.ListUsersPage;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.auth.UserRecord.CreateRequest;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.internal.FirebaseService;
import com.google.gson.Gson;
import java.io.File;
import java.io.FileWriter;
import java.io.FileInputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import javax.print.Doc;
import oogasalad.builder.backend.record.GameRecord;
import oogasalad.builder.frontend.views.dialogs.MessagePopUp;
import oogasalad.runner.model.player.ImmutablePlayer;
import oogasalad.splash.dialogs.ProfileHashMap;

public class Firebase {
  FirebaseApp firebaseApp;
  FirebaseAuth firebaseAuth;
  Firestore firestoreDb;
  String currentUser;
  public void initialize() {
    try {
      FileInputStream refreshToken = new FileInputStream("src/main/resources/serviceAccountKey.json");

      FirebaseOptions options = FirebaseOptions.builder()
          .setCredentials(GoogleCredentials.fromStream(refreshToken))
          .setDatabaseUrl("https://houseofcards-e4a2b.firebaseio.com/")
          .build();

      firebaseApp = FirebaseApp.initializeApp(options);
      firebaseAuth = FirebaseAuth.getInstance(firebaseApp);
      firestoreDb = FirestoreClient.getFirestore(firebaseApp);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public boolean login (String username, String password) {
    Map<String, String> credentials = Map.of(
        "password", password
    );
    DocumentReference docRef = firestoreDb.collection("users").document(username);
    ApiFuture<DocumentSnapshot> future = docRef.get();
    try {
      DocumentSnapshot document = future.get();
      if (document.exists()) {
        if (!docRef.get().get().getData().get("password").equals(password)) {
          MessagePopUp incorrectPassword = new MessagePopUp("Incorrect Password");
        } else {
          currentUser = username;
          return true;
        }
      } else {
        MessagePopUp noExistingAccount = new MessagePopUp("Account doesn't exists");
      }
    } catch (Exception e) {
      MessagePopUp error = new MessagePopUp(e.getMessage());
    }
    return false;
  }

  public boolean signup (String username, String password, String confirmPassword, String role) {
    Map<String, String> credentials = Map.of(
        "password", password,
        "role", role
    );
    Map<String, Object> stats = Map.of(
        "gamesWon", 0,
        "gamesLost", 0,
        "gamesMade", 0
    );
    DocumentReference docRef = firestoreDb.collection("users").document(username);
    ApiFuture<DocumentSnapshot> future = docRef.get();
    try {
      DocumentSnapshot document = future.get();
      if (document.exists()) {
        MessagePopUp existingAccount = new MessagePopUp("Account already exists");
      } else {
        if (!password.equals(confirmPassword)) {
          MessagePopUp passwordMatch = new MessagePopUp("Passwords don't match");
        } else if (password.length() < 6) {
          MessagePopUp passwordLength = new MessagePopUp("Password must be at least length 6");
        } else {
          docRef.set(credentials);
          docRef.update(stats);

          currentUser = username;
          return true;
        }
      }
    } catch (Exception e) {
      MessagePopUp error = new MessagePopUp(e.getMessage());
    }
    return false;
  }

  public boolean hasBuilderAccess() {
    DocumentReference docRef = firestoreDb.collection("users").document(currentUser);
    ApiFuture<DocumentSnapshot> future = docRef.get();

    try {
      DocumentSnapshot document = future.get();
      if (document.exists()) {
        if (((String) docRef.get().get().getData().get("role")).equals("Member")) {
          return true;
        }
        else {
          MessagePopUp error = new MessagePopUp("You do no have access to build games");
          return false;
        }
      } else {
        MessagePopUp error = new MessagePopUp("There is no user signed in currently");
      }
    } catch (Exception e) {
      MessagePopUp error = new MessagePopUp(e.getMessage());
    }
    return false;
  }

  public void uploadGame(String gameName, String json) {
    Map<String, String> game = Map.of(
        "gameName", gameName,
        "json", json,
        "username", currentUser
    );
    DocumentReference docRef = firestoreDb.collection("users").document(currentUser);
    ApiFuture<DocumentSnapshot> future = docRef.get();

    CollectionReference gameRef = firestoreDb.collection("games");
    try {
      DocumentSnapshot document = future.get();
      if (document.exists()) {
        gameRef.add(game);
        Long newScore = (Long) docRef.get().get().getData().get("gamesMade") + 1;
        docRef.update("gamesMade", newScore);
      } else {
        MessagePopUp error = new MessagePopUp("There is no user signed in currently");
      }
    } catch (Exception e) {
      MessagePopUp error = new MessagePopUp(e.getMessage());
    }
  }

  public void getGames() {
    Gson g = new Gson();
    CollectionReference gameRef = firestoreDb.collection("games");
    try {
      gameRef.listDocuments().forEach((DocumentReference docRef) -> {
        try {
          String json = (String) docRef.get().get().getData().get("json");
          String uid = docRef.getId();
          String gameName = (String) docRef.get().get().getData().get("gameName");
          File f = new File("data/json/games/firebase/" + uid + ".json");
          FileWriter myWriter = new FileWriter("data/json/games/firebase/" + gameName + ".json");
          myWriter.write(json);
          myWriter.close();
        } catch (Exception e) {
          MessagePopUp error = new MessagePopUp(e.getMessage());
        }
      });
    } catch (Exception e) {
      MessagePopUp error = new MessagePopUp(e.getMessage());
    }
  }

  public void updatePlayerStats(List<? extends ImmutablePlayer> players) {
    players.forEach(player -> {
      DocumentReference docRef = firestoreDb.collection("users").document(player.name());
      ApiFuture<DocumentSnapshot> future = docRef.get();
      try {
        DocumentSnapshot document = future.get();
        if (document.exists()) {
          if (player.isWinner()) {
            Long newScore = (Long) docRef.get().get().getData().get("gamesWon") + 1;
            docRef.update("gamesWon", newScore);
          }
          else {
            Long newScore = (Long) docRef.get().get().getData().get("gamesLost") + 1;
            docRef.update("gamesLost", newScore);
          }
        }
      } catch (Exception e) {
        MessagePopUp error = new MessagePopUp(e.getMessage());
      }
    });
  }

  public ProfileHashMap getUserStats() {
    DocumentReference docRef = firestoreDb.collection("users").document(currentUser);
    ApiFuture<DocumentSnapshot> future = docRef.get();
    ProfileHashMap profileStats = new ProfileHashMap();
    try {
      DocumentSnapshot document = future.get();
      if (document.exists()) {
        profileStats.add("Username", currentUser);
        profileStats.add("Password", (String) docRef.get().get().getData().get("password"));
        profileStats.add("Role", (String) docRef.get().get().getData().get("role"));
        profileStats.add("Games Won", ((Long) docRef.get().get().getData().get("gamesWon")).toString());
        profileStats.add("Games Lost", ((Long) docRef.get().get().getData().get("gamesLost")).toString());
        profileStats.add("Games Made", ((Long) docRef.get().get().getData().get("gamesMade")).toString());
      }
    } catch (Exception e) {
      MessagePopUp error = new MessagePopUp(e.getMessage());
    }
    return profileStats;
  }
}
