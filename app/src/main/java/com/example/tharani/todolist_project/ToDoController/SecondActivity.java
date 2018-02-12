package com.example.tharani.todolist_project.ToDoController;

import android.content.DialogInterface;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tharani.todolist_project.Handler.MyDatabaseManager;
import com.example.tharani.todolist_project.R;

import java.util.ArrayList;

public class SecondActivity extends AppCompatActivity {
    ListViewAdapter listViewAdapter;
    ListView listView;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        toolbar =  findViewById(R.id.toolbar);
        toolbar.setTitle("Completed Tasks");
        listView =  findViewById(R.id.list_todo);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, final View view, int position, long id) {


                final AlertDialog.Builder dialog = new AlertDialog.Builder(SecondActivity.this);

                dialog.setMessage("Are you sure? You want to delete this task?");
                dialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        MyDatabaseManager db = new MyDatabaseManager(getApplicationContext());

                        String ids = ((TextView) view.findViewById(R.id.taskId)).getText().toString();
                        db.delete(Long.parseLong(ids));
                        Toast.makeText(SecondActivity.this, "Task is deleted.", Toast.LENGTH_LONG).
                                show();
                        updateUI();
                        db.close();
                    }
                });
                dialog.setNegativeButton("Cancel", null);
                dialog.create();
                dialog.show();
                return true;
            }

        });
        updateUI();
    }

    private void updateUI() {
        MyDatabaseManager db = new MyDatabaseManager(getApplicationContext());
        Cursor cursor = db.fetch();
        final ArrayList<Integer> ids= new ArrayList<>();
        final ArrayList<String> title = new ArrayList<>();
        ArrayList<String> description = new ArrayList<>();
        ArrayList<String> date = new ArrayList<>();
        ArrayList<Integer> status = new ArrayList<>();

        while (cursor.moveToNext()) {
            ids.add(cursor.getInt(cursor.getColumnIndex(MyDatabaseManager.MyDatabaseHelper.COLUMN_ID)));
            title.add(cursor.getString(cursor.getColumnIndex(MyDatabaseManager.MyDatabaseHelper.COLUMN_TITLE)));
            description.add(cursor.getString(cursor.getColumnIndex(MyDatabaseManager.MyDatabaseHelper.COLUMN_DESCRIPTION)));
            date.add(cursor.getString(cursor.getColumnIndex(MyDatabaseManager.MyDatabaseHelper.COLUMN_DATE)));
            status.add(cursor.getInt(cursor.getColumnIndex(MyDatabaseManager.MyDatabaseHelper.COLUMN_STATUS)));
        }
        listViewAdapter = new ListViewAdapter(this,ids, title, description,date,status);
        listView.setAdapter(listViewAdapter);
        cursor.close();
        db.close();


    }
}
