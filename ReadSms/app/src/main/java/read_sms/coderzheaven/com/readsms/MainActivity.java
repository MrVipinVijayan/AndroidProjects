package read_sms.coderzheaven.com.readsms;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private final static int REQUEST_READ_SMS_PERMISSION = 2000;
    private static final String INBOX_URI = "content://sms/inbox";

    private boolean isPermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return checkSelfPermission(Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            if (REQUEST_READ_SMS_PERMISSION == requestCode) {
                Log.i("SMS", "REQUEST_READ_SMS_PERMISSION Permission Granted");
            }

        } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
            if (REQUEST_READ_SMS_PERMISSION == requestCode) {
                // TODO REQUEST_READ_SMS_PERMISSION Permission is not Granted.
                // TODO Request Not Granted.

                // This code is for get permission from setting.
                final Intent i = new Intent();
                i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                i.addCategory(Intent.CATEGORY_DEFAULT);
                i.setData(Uri.parse("package:" + getPackageName()));
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                startActivity(i);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!isPermissionGranted()) {
                requestPermissions(new String[]{Manifest.permission.READ_SMS}, REQUEST_READ_SMS_PERMISSION);
                return;
            } else {
                List<String> messages = readSMS();
                Log.i(TAG, "Number of Messages :: " + messages.size());
            }
        }
    }

    @NonNull
    private List<String> readSMS() {
        Uri mSmsQueryUri = Uri.parse(INBOX_URI);
        List<String> messages = new ArrayList<String>();

        Cursor cursor = null;
        try {
            cursor = getContentResolver().query(mSmsQueryUri, null, null, null, null);
            if (cursor == null) {
                Log.i(TAG, "cursor is null. uri: " + mSmsQueryUri);
            }
            for (boolean hasData = cursor.moveToFirst(); hasData; hasData = cursor.moveToNext()) {
                messages.add(cursor.getString(cursor.getColumnIndexOrThrow("body")));
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        } finally {
            cursor.close();
        }
        return messages;
    }
}
