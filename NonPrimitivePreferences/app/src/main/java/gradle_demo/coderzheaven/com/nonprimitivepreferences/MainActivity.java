package gradle_demo.coderzheaven.com.nonprimitivepreferences;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String MY_PREFS_NAME = "Pref";
    public static final String OBJECT_KEY = "user";
    private Gson gson;
    private Button btnStore, btnRetrieve;
    private EditText edFirstName, edLastName;
    private TextView tvDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnStore = findViewById(R.id.btnStore);
        btnRetrieve = findViewById(R.id.btnRetrieve);
        tvDisplay = findViewById(R.id.tvDisplay);
        edFirstName = findViewById(R.id.firstName);
        edLastName = findViewById(R.id.lastName);

        btnStore.setOnClickListener(this);
        btnRetrieve.setOnClickListener(this);

        gson = new Gson();
    }

    @NonNull
    private void storeObject(User user) {
        // Convert User Object to Json String
        String json = gson.toJson(user);
        // Store the Json String in Shared Preferences
        store(OBJECT_KEY, json);
        Toast.makeText(this, "User Stored in preferences", Toast.LENGTH_SHORT).show();
    }

    private User getObject() {
        // Get the Stored object as String
        String userStr = get(OBJECT_KEY);
        // Convert back to object using GSON
        User user = gson.fromJson(userStr, User.class);
        return user;
    }

    @NonNull
    private User createUser(String firstName, String secondName) {
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(secondName);
        return user;
    }

    void store(String key, String value) {
        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        editor.putString(key, value);
        editor.apply();
    }

    String get(String key) {
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        return prefs.getString(key, null);
    }

    @Override
    public void onClick(View view) {
        if (view == btnStore) {
            String fName = edFirstName.getText().toString().trim();
            String lName = edLastName.getText().toString().trim();
            if (fName.isEmpty() || lName.isEmpty()) {
                Toast.makeText(this, "Fields empty", Toast.LENGTH_SHORT).show();
                return;
            }
            User user = createUser(fName, lName);
            storeObject(user);
            return;
        }

        if (view == btnRetrieve) {
            User user = getObject();
            if (null != user)
                tvDisplay.setText("Firstname :: " + user.firstName + "\nLastname :: " + user.getLastName());
        }
    }
}
