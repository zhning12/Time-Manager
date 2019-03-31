package com.example.timemanager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.EditText;

import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    String[] p_num;
    SqliteDbManager todoDb;
    private List<Map<String, Object>> dataList;
    private SimpleAdapter simpleAdapter;
    ArrayList<ToDoList> allTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        allTask = new ArrayList<>();

        todoDb = SqliteDbManager.getInstance();
        todoDb.setSqliteDbOpen(this);
        allTask = todoDb.getAll();

        ListView listView = (ListView) this.findViewById(R.id.listView);

        dataList = new ArrayList<Map<String, Object>>();
        simpleAdapter = new SimpleAdapter(this, getData(), R.layout.item, new String[]{"taskName", "totalClock","finished"}, new int[]{R.id.checkBox, R.id.totalClock,R.id.finished});

        // 3. 视图加载适配器
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listView.setAdapter(simpleAdapter);

//        listView.setOnItemClickListener(new ItemClickListener());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddTask.class);
                Bundle bundle = new Bundle();
//                bundle.putString("height", height);
//                bundle.putString("weight", weight);
                intent.putExtras(bundle);
                startActivity(intent);
//                showCustomizeDialog();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private List<Map<String, Object>> getData() {
        // 创建数据
        for (ToDoList task : allTask) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("taskName", task.taskName);
            map.put("totalClock", task.clockNum);
            map.put("finished", task.finished);

            dataList.add(map);
        }
        return dataList;
    }

    //dialog
    private void showCustomizeDialog() {
        /* @setView 装入自定义View ==> R.layout.dialog_customize
         * 由于dialog_customize.xml只放置了一个EditView，因此和图8一样
         * dialog_customize.xml可自定义更复杂的View
         */

        AlertDialog.Builder customizeDialog =
                new AlertDialog.Builder(MainActivity.this);

        final View dialogView = LayoutInflater.from(MainActivity.this)
                .inflate(R.layout.add_task_dialog, null);

        customizeDialog.setTitle("Add a new task");
        customizeDialog.setView(dialogView);


        p_num = getResources().getStringArray(R.array.pomodoronum);

        customizeDialog.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 获取EditView中的输入内容
                        EditText edit_text = (EditText) dialogView.findViewById(R.id.et_new_task_name);
                        Toast.makeText(MainActivity.this,
                                edit_text.getText().toString(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
        customizeDialog.setNegativeButton("CANCEL",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //...To-do
                    }
                });

        customizeDialog.show();
    }


//    Spinner pSpinner = (Spinner) findViewById(R.id.pomodoro_num);
//    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, p_num);
//    pSpinner.setAdapter(adapter);
//    pSpinner.setOnItemSelectedListener(
//            new AdapterView.OnItemSelectedListener() {
//                @Override
//                public void onItemSelected(AdapterView<?> arg0,
//                                           View arg1, int arg2, long arg3) {
//                    int index = arg0.getSelectedItemPosition();
//                    Toast.makeText(getBaseContext(),
//                            "You have selected item : " + p_num[index], Toast.LENGTH_LONG).show();
//                }
//                @Override
//                public void onNothingSelected(AdapterView<?> arg0) {
//                }
//            });


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
