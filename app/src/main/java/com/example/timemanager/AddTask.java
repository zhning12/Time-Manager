package com.example.timemanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class AddTask extends AppCompatActivity {
    String[] pnArray;
    int pn;
    SqliteDbManager todoDb;
    EditText taskName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_task_dialog);

        todoDb = SqliteDbManager.getInstance();
        todoDb.setSqliteDbOpen(this);

        pnArray = getResources().getStringArray(R.array.pomodoronum);
        Spinner spinnerPn = (Spinner) findViewById(R.id.pomodoro_num);
        ArrayAdapter<String> adapterPn = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, pnArray);
        spinnerPn.setAdapter(adapterPn);
        spinnerPn.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                pn = arg0.getSelectedItemPosition() + 1;
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        taskName = (EditText) findViewById(R.id.et_new_task_name);

        Button addBtn = (Button) findViewById(R.id.addBtn);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                todoDb.insertTb(taskName.getText().toString(),Integer.parseInt(pnArray[pn-1]));
            }
        });
    }
}
