 package com.example.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.zip.Inflater;

public class MainActivity extends AppCompatActivity {

    static ArrayList<String> notes = new ArrayList<>();

    static ArrayAdapter arrayAdapter;



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_note, menu);

        return super.onCreateOptionsMenu(menu);


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);

        if(item.getItemId() == R.id.addNote) {

            Intent intent = new Intent(getApplicationContext(), EditText.class);

            startActivity(intent);
        }

        return true;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = (ListView) findViewById(R.id.ListView);


        SharedPreferences sharedPrefs = getApplicationContext().getSharedPreferences("com.example.notes", Context.MODE_PRIVATE);

        HashSet<String> set = (HashSet<String>) sharedPrefs.getStringSet("notes", null);

        if (set == null) {

            notes.add("Add a note...");
        }
        else {
            notes = new ArrayList(set);
        }



        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, notes);

        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                Intent intent = new Intent(getApplicationContext(), EditText.class);

                intent.putExtra("noteId", position);

                startActivity(intent);


            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {


                new AlertDialog.Builder(MainActivity.this)
                        .setIcon(android.R.drawable.ic_delete)
                        .setTitle("Delete Note?")
                        .setMessage("Are you sure you want to delete the note?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                notes.remove(position);
                                arrayAdapter.notifyDataSetChanged();

                                SharedPreferences sharedPrefs = getApplicationContext().getSharedPreferences("com.example.notes", Context.MODE_PRIVATE);

                                HashSet<String> set = new HashSet(MainActivity.notes);

                                sharedPrefs.edit().putStringSet("notes", set).apply();

                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
                return true;
            }
        });



    }
}