package otto_library_android.coderzheaven.com.ottolibraryandroiddemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.squareup.otto.Subscribe;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addFragment();
    }

    @Override
    protected void onStart() {
        super.onStart();
        OttoBus.getBus().register(this);
    }

    private void addFragment() {
        getSupportFragmentManager().beginTransaction().add(R.id.fragmentContainer, new DemoFragment()).commit();
    }

    public void sendMessageToFragment(View view) {
        EditText etMessage = findViewById(R.id.activityData);
        OttoBus.getBus().post(String.valueOf(etMessage.getText()));
    }

    @Subscribe
    public void getMessage(Events.ActivityFragmentMessage message) {
        TextView messageView = findViewById(R.id.message);
        messageView.setText(message.getMessage());
    }

    @Override
    protected void onStop() {
        super.onStop();
        OttoBus.getBus().unregister(this);
    }
}
