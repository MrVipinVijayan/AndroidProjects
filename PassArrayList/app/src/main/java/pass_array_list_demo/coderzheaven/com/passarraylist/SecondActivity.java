package pass_array_list_demo.coderzheaven.com.passarraylist;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.ArrayList;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_activity);
        Bundle b = getIntent().getExtras();

        if (null != b) {
            ArrayList<String> arr = b.getStringArrayList("array_list");
            Log.i("List", "Passed Array List :: " + arr);
        }

    }
}
