
package com.example.heroku;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserRecord;
import spark.Spark;
import java.io.FileInputStream;
import java.io.IOException;
import static spark.Spark.get;
import static spark.Spark.post;
@Controller
@SpringBootApplication
public class HerokuApplication {
  public static void main(String[] args) throws Exception {
    ProcessBuilder process = new ProcessBuilder();
    Integer port;
    if (process.environment().get("PORT") != null) {
      port = Integer.parseInt(process.environment().get("PORT"));
    } else {
      port = 4567;
    }
    Spark.port(port);

    FileInputStream serviceAccount = new FileInputStream("medical-58ea2-firebase-adminsdk-ycupx-6c52293e4b.json");

    FirebaseOptions options = new FirebaseOptions.Builder()
            .setCredentials(GoogleCredentials.fromStream(serviceAccount))
            .setDatabaseUrl("https://medical-58ea2-default-rtdb.firebaseio.com")
            .build();

    FirebaseApp.initializeApp(options);

    get("/", (req, res) -> "Hello, World my name is mukti gupta");
    System.out.println("Hello mukti");
    post("/com.mukti.account", (req, res) -> {
      String email = req.queryParams("email");
      String Password = req.queryParams("Password");

      UserRecord.CreateRequest request = new UserRecord.CreateRequest().setEmail(email)
              .setEmailVerified(false)
              .setPassword(Password)
              .setEmail(email)
              .setDisabled(false);

      UserRecord userRecord = FirebaseAuth.getInstance().createUser(request);
      System.out.println("Successfully created new user: "+userRecord.getUid());
      return null;
    });

    post("/com.mukti.deleteAccount", (req, res) -> {
      String email = req.queryParams("email");
      UserRecord userRecord = FirebaseAuth.getInstance().getUserByEmail(email);
      FirebaseAuth.getInstance().deleteUser(userRecord.getUid());
      System.out.println("Successfully deleted user.");
      return null;
    });
  }
}
