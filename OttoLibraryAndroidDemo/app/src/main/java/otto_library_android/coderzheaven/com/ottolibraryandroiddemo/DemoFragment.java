package otto_library_android.coderzheaven.com.ottolibraryandroiddemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.squareup.otto.Subscribe;

/**
 * Created by vipinvijayan on 30/12/17.
 */

public class DemoFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.demo_fragment, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        OttoBus.getBus().register(this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setClickListener(view);
    }

    public void setClickListener(final View view) {
        Button btnSubmit = (Button) view.findViewById(R.id.submit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessageToActivity();
            }
        });
    }

    private void sendMessageToActivity() {
        EditText etMessage = getView().findViewById(R.id.editText);
        OttoBus.getBus().post(etMessage.getText().toString());
    }

    @Subscribe
    public void getMessage(Events.FragmentActivityMessage message) {
        TextView messageView = getView().findViewById(R.id.message);
        messageView.setText(message.getMessage());
    }

    @Override
    public void onStop() {
        super.onStop();
        OttoBus.getBus().unregister(this);
    }
}
