package com.bibo.newrecycleview;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class Recyclerworkadapter extends RecyclerView.Adapter<Recyclerworkadapter.ViewHolder> {
     Context context;
     ArrayList<listtodo>arrayList;
     DatabaseHelper databaseHelper;

    public  Recyclerworkadapter(Context context , ArrayList<listtodo> arrayList){
        this.context=context;
        this.arrayList=arrayList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v =LayoutInflater.from(parent.getContext()).inflate(R.layout.contactlayout,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textView.setText(arrayList.get(position).getStudentworks());
        holder.taskname.setText(arrayList.get(position).getTaskname());
        databaseHelper = new DatabaseHelper(context);



        // Get the current position and make it final
        final int currentPosition = position;

        // Get the toggle state from SharedPreferences
        boolean isChecked = getToggleStateFromPreferences(context, currentPosition);

        // Set the checked state of the checkbox
        holder.checkBox.setChecked(isChecked);

        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the current state of the checkbox
                boolean isChecked = holder.checkBox.isChecked();

                // Toggle the checked state of the checkbox
                holder.checkBox.setChecked(!isChecked);

                // Save the toggle state to SharedPreferences
                saveToggleStateToPreferences(context, currentPosition, !isChecked);

                // Show toast message based on toggled state
                if (!isChecked) {
                    // If the checkbox is now checked
                    Toast.makeText(context, "Checked", Toast.LENGTH_SHORT).show();
                } else {
                    // If the checkbox is now unchecked
                    Toast.makeText(context, "Unchecked", Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.clickrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.updating_element);
                EditText editText = dialog.findViewById(R.id.updating_edit_text);
//                EditText text = dialog.findViewById(R.id.addingedit_task_name);

                Button button = dialog.findViewById(R.id.updating_button);

                listtodo currentItem = arrayList.get(holder.getAbsoluteAdapterPosition());
                editText.setText(currentItem.getTaskname());
                String positions = (String.valueOf(1+holder.getAbsoluteAdapterPosition())); // Get position of current item

//                editText.setText(String.valueOf(position)); // Set position in editText


                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String updatedText = editText.getText().toString();
                        String updatedtexts2 = ("Task "+positions);
                            boolean isUpdated = databaseHelper.updateData(positions, updatedtexts2, updatedText);
                            currentItem.setStudentworks(updatedtexts2);
                            currentItem.setTaskname(updatedText);


                            if (isUpdated) {
                                Toast.makeText(context, "Record  is updated", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                                // Notify the adapter that the data has changed
                                notifyDataSetChanged();
                            }
                        }

                });


                dialog.show();
            }
        });



        holder.clickrow.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int position = holder.getAbsoluteAdapterPosition();
                listtodo currentItem = arrayList.get(position);

                // Retrieve the ID of the task to be deleted
                String taskId = currentItem.getId();

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Delete Task");
                builder.setMessage("Are you sure to delete?");
                builder.setIcon(R.drawable.baseline_delete_forever_24);
                builder.setCancelable(true);

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Delete the task from the database
                        Integer row = databaseHelper.deleteDataa(taskId);
                        if (row > 0) {
                            // Task deleted successfully
                            Toast.makeText(context, "Record is deleted", Toast.LENGTH_SHORT).show();
                            // Remove the task from the list and update the RecyclerView
                            arrayList.remove(position);
                            notifyItemRemoved(position);
                        } else {
                            // Task not found or deletion failed
                            Toast.makeText(context, "Record not found", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builder.show();
                return true;
            }
        });



    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }




    public static class ViewHolder extends  RecyclerView.ViewHolder{
        TextView textView,taskname;
        CheckBox checkBox;
        LinearLayout clickrow;
        public ViewHolder(View works){
            super(works);
            textView= works.findViewById(R.id.taskcounter);
            taskname=works.findViewById(R.id.taskname);
            checkBox=works.findViewById(R.id.checkbox);
            clickrow=works.findViewById(R.id.rowclick);



        }
    }
    // Method to update the data in the adapter
    public void updateData(ArrayList<listtodo> newData) {
        this.arrayList.clear(); // Clear the existing data
        this.arrayList.addAll(newData); // Add new data
        notifyDataSetChanged(); // Notify adapter about the data change
    }
    // Method to save toggle state to SharedPreferences
    private void saveToggleStateToPreferences(Context context, int position, boolean isChecked) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("ToggleState", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("ToggleState_" + position, isChecked);
        editor.apply();
    }

    // Method to get toggle state from SharedPreferences
    private boolean getToggleStateFromPreferences(Context context, int position) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("ToggleState", Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("ToggleState_" + position, false); // Default value is false if key is not found
    }



}


