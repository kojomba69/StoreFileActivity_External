package com.example.edu.storefileactivity_external;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    final int REQUEST_CODE = 10;//리퀘스트 코드를 10으로 정해서 넘겨준다.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        File file = null;

        Button buttonFileReadFromInner = findViewById(R.id.buttonFileReadFromPublicOutter);
        buttonFileReadFromInner.setOnClickListener(this);
        Button buttonFileWriteFromInner = findViewById(R.id.buttonFileWriteFromPublicOutter);
        buttonFileWriteFromInner.setOnClickListener(this);
        EditText editTextInput=findViewById(R.id.editTextInput);

        int permission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
        }
    }
    //권한
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    Log.i("", "Permission has been granted by user");//i보단 e를 권장함 erra
                }
        }
    }
    @Override
    public void onClick(View v) {
        File file = null;

        EditText editTextInput = findViewById(R.id.editTextInput);
        FileInputStream fileInputStream = null;
        FileOutputStream fileOutputStream = null;
        switch (v.getId()) {
            case R.id.buttonFileReadFromPublicOutter:
                file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "sun_publicouter");
                try {
                    fileInputStream = new FileInputStream(file);
                    byte[] buffer = new byte[fileInputStream.available()];
                    fileInputStream.read(buffer);
                    editTextInput.setText(new String(buffer));
                    fileInputStream.close();
                    break;
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            case R.id.buttonFileWriteFromPublicOutter:
                File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
                if(!dir.exists()) dir.mkdirs();

                file = new File(dir, "sun_publicouter");
                try {
                    fileOutputStream = new FileOutputStream(file);
                    fileOutputStream.write(editTextInput.getText().toString().getBytes());
                    editTextInput.setText("");
                    fileOutputStream.close();
                    break;
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }
}