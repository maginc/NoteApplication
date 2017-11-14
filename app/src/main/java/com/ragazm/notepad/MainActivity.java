package com.ragazm.notepad;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.ragazm.notepad.database.DBAdapter;
import com.ragazm.notepad.recycler.ItemClickListener;
import com.ragazm.notepad.recycler.MyAdapter;
import com.ragazm.notepad.recycler.RecyclerTouchListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    RecyclerView recyclerView;
    MyAdapter adapter;
    // Dialog deleteDialog =new Dialog();
    ArrayList<Note> notes = new ArrayList<>();
    TextView txtNoNote;
    private boolean showNoNote = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);


        Log.d("note.size()", String.valueOf(notes.size()));

        //Floating button action
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentFab = new Intent(MainActivity.this, EditActivity.class);
                startActivity(intentFab);
                Log.d("Fab pressed!!", "Pressed indeed!!");
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        adapter = new MyAdapter(this, notes);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, recyclerView, new ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                // Open EditActivity, pass data, create intent
                Intent intent = new Intent(MainActivity.this, EditActivity.class);
                //load data
                intent.putExtra("ID", position);
                startActivity(intent);
            }

            @Override
            public void onItemPress(View view, int position) {
                alertDialog(notes.get(position).getId());
                Log.d("POSITION:", String.valueOf(position));
                retrieve();


            }
        }));

        retrieve();

    }

    private void retrieve() {
        DBAdapter database = new DBAdapter(this);
        database.openDB();
        notes.clear();

        Cursor cursor = database.getAllNotes();

        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String title = cursor.getString(1);
            String text = cursor.getString(2);

            Note note = new Note(title, text, id);
            notes.add(note);


            Log.d("note.size()", String.valueOf(notes.size()));
        }

        //if (!(notes.size()<1)){
        recyclerView.setAdapter(adapter);
        //}
    }

    @Override
    protected void onResume() {
        super.onResume();
        retrieve();
    }

    public void alertDialog(final int id) {
        AlertDialog.Builder builder;
        // Context context;
        builder = new AlertDialog.Builder(MainActivity.this);

        builder.setTitle("Delete Note")
                .setMessage("Are you sure you want to delete this note?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DBAdapter database = new DBAdapter(MainActivity.this);
                        database.openDB();
                        database.delete(id);
                        database.close();
                        retrieve();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing if pressed NO
                    }
                })

                .show();

    }

    /**
     * Setting menu for future backup function
     *
     * @Override public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return super.onCreateOptionsMenu(menu);
    }
     @Override public boolean onOptionsItemSelected(MenuItem item) {
     // int id = item.getItemId();
     switch (item.getItemId()) {
     case R.id.make_backup:

     return true;

     case R.id.load_backup:
     //Save note


     }
     return super.onOptionsItemSelected(item);
     }
     **/


}
