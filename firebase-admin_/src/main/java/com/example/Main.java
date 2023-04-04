package com.example;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.ExportedUserRecord;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.ListUsersPage;
import com.google.firebase.auth.UserRecord;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws IOException, FirebaseAuthException {
        // see https://firebase.google.com/docs/auth/admin/manage-users
        InputStream is = Main.class.getClassLoader().getResourceAsStream("mathematics-9814d-firebase-adminsdk-kcg3j-400ae1b03f.json");
        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(is))
//                .setDatabaseUrl("https://<DATABASE_NAME>.firebaseio.com/")
                .build();
        FirebaseApp.initializeApp(options);

        ListUsersPage page = FirebaseAuth.getInstance().listUsers(null);
        for (ExportedUserRecord value : page.getValues()) {
            System.out.println(value.getEmail());
            Map<String, Object> customClaims = value.getCustomClaims();
            customClaims.forEach((s, o) -> System.out.println(s + "=" + o));
        }

        // see https://firebase.google.com/docs/auth/admin/custom-claims
        UserRecord adminRecord = FirebaseAuth.getInstance().getUserByEmail("leah@gmail.com");
        Map<String, Object> claims = new HashMap<>();
        claims.put("admin", true);

        FirebaseAuth.getInstance().setCustomUserClaims(adminRecord.getUid(), claims);
    }
}
