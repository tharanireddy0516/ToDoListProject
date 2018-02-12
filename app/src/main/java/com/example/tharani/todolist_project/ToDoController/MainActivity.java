package com.example.tharani.todolist_project.ToDoController;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.tharani.todolist_project.Handler.MyDatabaseManager;
import com.example.tharani.todolist_project.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;


public class MainActivity extends AppCompatActivity {
    List<DataActivity> dataList;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView =  findViewById(R.id.listView);
        updateUI();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_item, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_task:
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);

                LayoutInflater inflater = getLayoutInflater();
                final View inflaterView = inflater.inflate(R.layout.dialog_layout, null);
                dialog.setView(inflaterView);


                final EditText title =  inflaterView.findViewById(R.id.edtxt1);
                String s = title.getText().toString();
                if(s.length()  != 0){
                    title.setError("title can not be empty");
                } else {

                }
                final EditText description =  inflaterView.findViewById(R.id.edtxt2);
                final DatePicker date =  inflaterView.findViewById(R.id.date);

                dialog.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("MainActivity: ", "Add Title Of task: " + title.getText().toString());
                        Log.d("MainActivity: ", "Description Of Task: " + description.getText().toString());
                        Log.d("MainActivity: ", "Date: " + date.getYear() + "-" + date.getMonth() + 1 + "-" + date.getDayOfMonth());

                        final MyDatabaseManager myDatabaseManager = new MyDatabaseManager(getApplicationContext());
                        myDatabaseManager.insert(title.getText().toString(), description.getText().toString(), date.getYear() + "-" + (date.getMonth() + 1) + "-" + date.getDayOfMonth(), "1");

                        myDatabaseManager.close();

                        updateUI();

                    }
                });
                dialog.setNegativeButton("Cancel", null);
                dialog.create();
                dialog.show();
                return true;
            case R.id.complete:
                Toast.makeText(this, "Completed Tasks", Toast.LENGTH_LONG).show();

                Intent intent=new Intent(MainActivity.this,SecondActivity.class);
                startActivity(intent);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void updateUI() {
        MyDatabaseManager db = new MyDatabaseManager(getApplicationContext());

        Log.d("Called: ", "update");
        Cursor cursor = db.fetch();
        ArrayList<Integer> ids = new ArrayList<>();
        ArrayList<String> title = new ArrayList<>();
        ArrayList<String> description = new ArrayList<>();
        ArrayList<String> date = new ArrayList<>();
        ArrayList<Integer> status = new ArrayList<>();


        while (cursor.moveToNext()) {
            //Log.d("title: ", cursor.getString(cursor.getColumnIndex(MyDatabaseManager.MyDatabaseHelper.COLUMN_TITLE)));
            ids.add(cursor.getInt(cursor.getColumnIndex(MyDatabaseManager.MyDatabaseHelper.COLUMN_ID)));
            title.add(cursor.getString(cursor.getColumnIndex(MyDatabaseManager.MyDatabaseHelper.COLUMN_TITLE)));
            description.add(cursor.getString(cursor.getColumnIndex(MyDatabaseManager.MyDatabaseHelper.COLUMN_DESCRIPTION)));
            date.add(cursor.getString(cursor.getColumnIndex(MyDatabaseManager.MyDatabaseHelper.COLUMN_DATE)));
            status.add(cursor.getInt(cursor.getColumnIndex(MyDatabaseManager.MyDatabaseHelper.COLUMN_STATUS)));
        }
        ListViewAdapter listViewAdapter = new ListViewAdapter(this,ids,title, description, date,status);
        listView.setAdapter(listViewAdapter);
        cursor.close();
        db.close();
    }
    public void deleteTask(View view) {
        MyDatabaseManager db = new MyDatabaseManager(getApplicationContext());
        Cursor cursor = db.fetch();
        String title = cursor.getString(cursor.getColumnIndex(MyDatabaseManager.MyDatabaseHelper.COLUMN_TITLE));
        String description = cursor.getString(cursor.getColumnIndex(MyDatabaseManager.MyDatabaseHelper.COLUMN_DESCRIPTION));
        String date = cursor.getString(cursor.getColumnIndex(MyDatabaseManager.MyDatabaseHelper.COLUMN_DATE));
        String status = cursor.getString(cursor.getColumnIndex(MyDatabaseManager.MyDatabaseHelper.COLUMN_STATUS));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (Objects.equals(cursor.getString(cursor.getColumnIndex(MyDatabaseManager.MyDatabaseHelper.COLUMN_STATUS)), "1")) {
                view.findViewById(R.id.status).setBackgroundResource(R.drawable.complete);
                db.delete(cursor.getInt(cursor.getColumnIndex(MyDatabaseManager.MyDatabaseHelper.COLUMN_ID)));
                Log.d("MainActivity", "deleteTask: " + status);
            } else {
                db.update(cursor.getInt(cursor.getColumnIndex(MyDatabaseManager.MyDatabaseHelper.COLUMN_ID)), title, description, date, "1" );
                view.findViewById(R.id.status).setBackgroundResource(R.drawable.incomplete);
            }
        }
        updateUI();
        cursor.close();
        db.close();
    }
}
