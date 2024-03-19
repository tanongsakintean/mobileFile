package com.example.demofile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity implements
        View.OnClickListener {
    EditText nameText, scoreText;
    Button btnSave, btnLoad;
    Files files;
    TextView showData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nameText = findViewById(R.id.nameText);
        scoreText = findViewById(R.id.scoreText);
        btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(this);
        btnLoad = findViewById(R.id.btnLoad);
        btnLoad.setOnClickListener(this);
        showData = findViewById(R.id.showData);

        if (!isExternalStroageAvailable() || isExternalStroageReadOnly())
            btnSave.setEnabled(false);
        else {
            try {
                files = new Files(getApplicationContext());
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            try {
                String text = "";
                if (files.selectAll().length != 0) {
                    for (String[] items:files.selectAll()) {
                        for (String item:items) {
                            text+=item+" ";
                        }
                        text+="\n";
                    }
                }
                showData.setText(text);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.product) {
            Intent product = new Intent(this,Products.class) ;
            startActivity(product);
        } else if (item.getItemId() == R.id.exit) {
            finishAffinity();
        } else if (item.getItemId() == R.id.home) {
            
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        String id , name, score;
        if (v.getId() == R.id.btnSave) {
            name = nameText.getText().toString();
            score = scoreText.getText().toString();
            if (checkData(name, score)) {
                showAlertDialog2(name,score);

            }
            else {
                Toast.makeText(this, "ข้อมูลไม่ครบ", Toast.LENGTH_SHORT).show();
            }
        } else if (v.getId() == R.id.btnLoad) {
            try {
                String text = "";
                for (String[] items:files.selectAll()) {
                    for (String item:items) {
                        text+=item+" ";
                    }
                    text+="\n";
                }
                showData.setText(text);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public boolean checkData( String name, String score) {
        if ( !name.isEmpty() && !score.isEmpty())
            return(true);
        return(false);
    }
    public boolean isExternalStroageAvailable() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(extStorageState)) return true;
        return false;
    }
    public boolean isExternalStroageReadOnly() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState)) {
            return true;
        }
        return false;
    }

    public void showAlertDialog2(String name , String score) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setTitle("Exit");
        builder.setMessage("Are you sure you want to leave ?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                files.insert(new String[]{name, score});
                nameText.setText("");
                scoreText.setText("");
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setNeutralButton("Ignore", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create();
        builder.show();
    }
}