package data_backup_demo.coderzheaven.com.databackupdemo;

import android.app.backup.BackupAgentHelper;
import android.app.backup.BackupDataInput;
import android.app.backup.BackupDataOutput;
import android.app.backup.SharedPreferencesBackupHelper;
import android.content.SharedPreferences;
import android.os.ParcelFileDescriptor;
import android.util.Log;

import java.io.IOException;

/**
 * Created by vipinvijayan on 28/12/17.
 */

public class DemoBackUpAgent extends BackupAgentHelper {

    // The name of the SharedPreferences file
    static final String PREFS = "myprefs";

    // A key to uniquely identify the set of backup data
    static final String PREFS_BACKUP_KEY = "myprefs";

    @Override
    public void onCreate() {
        SharedPreferencesBackupHelper helper = new SharedPreferencesBackupHelper(this, PREFS);
        addHelper(PREFS_BACKUP_KEY, helper);

        SharedPreferences pref = getApplicationContext().getSharedPreferences(PREFS, 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("key_name1", true);
        editor.commit();
    }

    @Override
    public void onBackup(ParcelFileDescriptor oldState, BackupDataOutput data, ParcelFileDescriptor newState) throws IOException {
        super.onBackup(oldState, data, newState);
        Log.i("backup", "OnBackUp");
    }

    @Override
    public void onRestore(BackupDataInput data, int appVersionCode, ParcelFileDescriptor newState) throws IOException {
        super.onRestore(data, appVersionCode, newState);
        Log.i("restore", "onRestore");
    }

    @Override
    public void onRestoreFinished() {
        super.onRestoreFinished();
        Log.i("restore", "onRestoreFinished");
    }
}
