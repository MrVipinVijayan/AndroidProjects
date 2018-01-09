package pass_array_list_demo.coderzheaven.com.passarraylist;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passArrayList();
            }
        });
    }

    private void passArrayList() {
        final ArrayList<String> arr = new ArrayList<>();
        arr.add("Hello");
        arr.add("CoderzHeaven");

        Intent intent = new Intent(MainActivity.this, SecondActivity.class);
        intent.putExtra("array_list", arr);
        startActivity(intent);
    }

}
