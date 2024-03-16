package com.bibo.newrecycleview;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    String tasknames = "", tasktodo = "";
    FloatingActionButton floatbutton;
    DatabaseHelper databaseHelper;
    RecyclerView recyclerView;
    Recyclerworkadapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        databaseHelper = new DatabaseHelper(this);

        RecyclerView recyclerView = findViewById(R.id.recycleview);
        floatbutton = findViewById(R.id.floatbutton);


        ArrayList<listtodo> arrayList = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        floatbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(MainActivity.this);
                dialog.setContentView(R.layout.updating_element);
                dialog.show();
                EditText Tasktodo, taskname;
//                taskname = dialog.findViewById(R.id.addingedit_task_name);
                Tasktodo = dialog.findViewById(R.id.updating_edit_text);
                Button button = dialog.findViewById(R.id.updating_button);


                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!Tasktodo.getText().toString().equals("")) {
                            tasknames ="Admin:Bibhanshu";
                            tasktodo = Tasktodo.getText().toString();
                            //using the boolean idadded things to check if there data is sended or not  if it is sended then true else  we all know .

                            boolean isAdded = databaseHelper.insertdata(tasknames, tasktodo);
                            if (isAdded) {
                                dialog.dismiss(); // Close the dialog after insertion
                                readData(); // Refresh the ListView after insertion
                                recyclerView.scrollToPosition(arrayList.size() - 1);
                                Toast.makeText(MainActivity.this, "data added successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(MainActivity.this, "data  failed to add ", Toast.LENGTH_SHORT).show();

                            }


                        } else {
                            Toast.makeText(MainActivity.this, "IT should not be blanked", Toast.LENGTH_SHORT).show();
                        }

                    }
                });


            }
        });


        adapter = new Recyclerworkadapter(MainActivity.this, arrayList);

        recyclerView.setAdapter(adapter);
        readData();


    }


    public void readData() {
        Cursor c = databaseHelper.getData();
        ArrayList<listtodo> taskList = new ArrayList<>();
        if (c.getCount() == 0) {
            showMesssage("Error", "No records found");
            return;
        }

        while (c.moveToNext()) {
            String id = c.getString(0);
            String taskToDo = c.getString(2); // Assuming column index 2 holds task details
            taskList.add(new listtodo("Task " + id, taskToDo, id)); // Pass the id as the third parameter
        }


        // Update the ArrayList used by the adapter
        adapter.updateData(taskList);
    }

//    public void updateData(){
//
//                boolean isupdated=databaseHelper.updateData(id.getText().toString(),name.getText().toString(),email.getText().toString());
//                if(isupdated){
//                    Toast.makeText(MainActivity.this, "Record  is updated", Toast.LENGTH_SHORT).show();
//                }
//
//
//
//    }







//    public  void deleteData(){
//        btn4.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Integer row = databaseHelper.deleteDataa(id.getText().toString());
//                if (row > 0) {
//                    Toast.makeText(MainActivity.this, "Record is deleted", Toast.LENGTH_SHORT).show();
//
//                }else{
//                    Toast.makeText(MainActivity.this, "Record not found", Toast.LENGTH_SHORT).show();
//                }
//
//
//            }
//        });
//
//
//    }
//
    public void showMesssage(String title,String Message){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        if(Message.length()==0){
            builder.setMessage("Nothing to show!");
        }else {
            builder.setMessage(Message);

        }

        builder.show();


    }
//    public void cleardata(){
//        btn5.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                id.setText("");
//                name.setText("");
//                email.setText("");
//            }
//        });
//
//    }

}