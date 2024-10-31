package com.example.content_provider;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.Browser;
import android.provider.CallLog;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnshowallcontact;
    Button btnaccesscalllog;
    Button btnaccessmediastore;
    Button btnaccessbookmarks;

    private static final int REQUEST_PERMISSION_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnshowallcontact = findViewById(R.id.btnshowallcontact);
        btnshowallcontact.setOnClickListener(this);

        btnaccesscalllog = findViewById(R.id.btnaccesscalllog);
        btnaccesscalllog.setOnClickListener(this);

        btnaccessmediastore = findViewById(R.id.btnmediastore);
        btnaccessmediastore.setOnClickListener(this);

        btnaccessbookmarks = findViewById(R.id.btnaccessbookmarks);
        btnaccessbookmarks.setOnClickListener(this);

        checkAndRequestPermissions();
    }

    private void checkAndRequestPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.READ_CONTACTS,
                    Manifest.permission.READ_CALL_LOG,
                    Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION_CODE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        if (v == btnshowallcontact) {
            intent = new Intent(this, ShowAllContactActivity.class);
            startActivity(intent);
        } else if (v == btnaccesscalllog) {
            accessTheCallLog();
        } else if (v == btnaccessmediastore) {
            accessMediaStore();
        } else if (v == btnaccessbookmarks) {
            accessBookmarks();
        }
    }

    public void accessTheCallLog() {
        Cursor cursor = getContentResolver().query(CallLog.Calls.CONTENT_URI,
                new String[]{CallLog.Calls.DATE, CallLog.Calls.NUMBER, CallLog.Calls.DURATION},
                CallLog.Calls.DURATION + "<?", new String[]{"30"}, CallLog.Calls.DATE + " ASC");

        StringBuilder callLogBuilder = new StringBuilder();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                for (int i = 0; i < cursor.getColumnCount(); i++) {
                    callLogBuilder.append(cursor.getString(i)).append(" - ");
                }
                callLogBuilder.append("\n");
            }
            cursor.close();
        }
        Toast.makeText(this, callLogBuilder.toString(), Toast.LENGTH_LONG).show();
    }

    public void accessMediaStore() {
        Cursor cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.MediaColumns.DISPLAY_NAME, MediaStore.MediaColumns.DATE_ADDED, MediaStore.MediaColumns.MIME_TYPE},
                null, null, null);

        StringBuilder mediaStoreBuilder = new StringBuilder();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                for (int i = 0; i < cursor.getColumnCount(); i++) {
                    mediaStoreBuilder.append(cursor.getString(i)).append(" - ");
                }
                mediaStoreBuilder.append("\n");
            }
            cursor.close();
        }
        Toast.makeText(this, mediaStoreBuilder.toString(), Toast.LENGTH_LONG).show();
    }

    public void accessBookmarks() {
        Cursor cursor = getContentResolver().query(Browser.BOOKMARKS_URI,
                new String[]{Browser.BookmarkColumns.TITLE, Browser.BookmarkColumns.URL}, null, null, null);

        StringBuilder bookmarksBuilder = new StringBuilder();
        if (cursor != null) {
            int titleIndex = cursor.getColumnIndex(Browser.BookmarkColumns.TITLE);
            int urlIndex = cursor.getColumnIndex(Browser.BookmarkColumns.URL);

            while (cursor.moveToNext()) {
                bookmarksBuilder.append(cursor.getString(titleIndex)).append(" - ").append(cursor.getString(urlIndex)).append("\n");
            }
            cursor.close();
        }
        Toast.makeText(this, bookmarksBuilder.toString(), Toast.LENGTH_LONG).show();
    }
}
