package file_upload_demo.coderzheaven.com.fileuploaddemo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, Handler.Callback {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int SELECT_AUDIO = 2;
    private String selectedPath;
    private Handler handler;
    private TextView tvStatus;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.selectFile).setOnClickListener(this);
        findViewById(R.id.uploadFile).setOnClickListener(this);
        tvStatus = findViewById(R.id.tvStatus);
        handler = new Handler(this);
    }

    public void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image "), SELECT_AUDIO);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_AUDIO) {
                Uri selectedImageUri = data.getData();
                selectedImageUri = handleImageUri(selectedImageUri);
                selectedPath = getRealPathFromURI(selectedImageUri);
                tvStatus.setText("Selected Path :: " + selectedPath);
                Log.i(TAG, " Path :: " + selectedPath);
            }
        }
    }

    public static Uri handleImageUri(Uri uri) {
        if (uri.getPath().contains("content")) {
            Pattern pattern = Pattern.compile("(content://media/.*\\d)");
            Matcher matcher = pattern.matcher(uri.getPath());
            if (matcher.find())
                return Uri.parse(matcher.group(1));
            else
                throw new IllegalArgumentException("Cannot handle this URI");
        }
        return uri;
    }

    @SuppressLint("NewApi")
    public String getRealPathFromURI(Uri uri) {
        String filePath = "";
        String wholeID = DocumentsContract.getDocumentId(uri);

        // Split at colon, use second item in the array
        String id = wholeID.split(":")[1];

        String[] column = {MediaStore.Images.Media.DATA};

        // where id is equal to
        String sel = MediaStore.Images.Media._ID + "=?";

        Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                column, sel, new String[]{id}, null);

        int columnIndex = cursor.getColumnIndex(column[0]);

        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex);
        }
        cursor.close();
        return filePath;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.selectFile) {
            openGallery();
        }
        if (v.getId() == R.id.uploadFile) {
            if (null != selectedPath && !selectedPath.isEmpty()) {
                tvStatus.setText("Uploading..." + selectedPath);
                FileUploadUtility.doFileUpload(selectedPath, handler);
            }
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        Log.i("File Upload", "Response :: " + msg.obj);
        String success = 1 == msg.arg1 ? "File Upload Success" : "File Upload Error";
        Log.i(TAG, success);
        tvStatus.setText(success);
        return false;
    }
}