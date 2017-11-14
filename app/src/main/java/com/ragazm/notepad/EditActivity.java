package com.ragazm.notepad;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ragazm.notepad.database.DBAdapter;

import java.util.ArrayList;

public class EditActivity extends AppCompatActivity {
    EditText editTitle;
    EditText editText;
    String tempTitle;
    String tempText;
    ArrayList<Note> notes = new ArrayList<>();
    boolean editFlag;
    int extraId;


    // create an action bar button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // Make Save button on action bar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            case R.id.mybutton:
                //Save note
                save(editTitle.getText().toString(), editText.getText().toString());
                return true;

            case R.id.deleteButton:
                if (extraId >= 0) {
                    alertDialog(notes.get(extraId).getId());
                } else {
                    Toast.makeText(getApplicationContext(), "Nothing to delete", Toast.LENGTH_SHORT).show();
                }

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_edit);

        editFlag = false;


        editText = (EditText) findViewById(R.id.editText);
        editTitle = (EditText) findViewById(R.id.editTitle);

        //Focus cursor on edit text field instead of default title field
        editText.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);


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
        }

        database.close();

        Intent intent = getIntent();
        extraId = intent.getIntExtra("ID", -1);

        Log.d("ExtraId = ", String.valueOf(extraId));

        if (extraId >= 0) {

            String extraTitle = notes.get(extraId).getTitle();
            String extraText = notes.get(extraId).getText();

            editText.setText(extraText);
            editTitle.setText(extraTitle);
            editFlag = true;
        }

        cache();
    }

    //    Save/edit function
    private void save(String title, String text) {
        DBAdapter database = new DBAdapter(this);
        if (editFlag && ifEdited()) {
            edit(notes.get(extraId).getId(), editTitle.getText().toString(), editText.getText().toString());
            cache();
        } else if (ifEdited()) {
            if (editTitle != null || editText != null) {
                database.openDB();
                long result = database.add(title, text);
                cache();
                if (result > 0) {
                    Toast.makeText(getApplicationContext(), "Note saved successfully", Toast.LENGTH_SHORT).show();
                }
                database.close();
            }
        } else {
            if (editTitle.getText().toString().equals("") && editText.getText().toString().equals("")) {
                Toast.makeText(getApplicationContext(), "Note is empty!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Note already saved!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //Edit function
    private void edit(int id, String title, String text) {
        DBAdapter database = new DBAdapter(this);
        database.openDB();
        database.update(id, title, text);
        database.close();
        Toast.makeText(getApplicationContext(), "Note edited successfully", Toast.LENGTH_SHORT).show();
    }

    //Alert dialog on back button pressed
    @Override
    public void onBackPressed() {
        if (ifEdited()) {
            AlertDialog.Builder builder;
            builder = new AlertDialog.Builder(EditActivity.this);
            TextView textView = new TextView(this);
            textView.setText(getResources().getString(R.string.save_chages));
            textView.setGravity(Gravity.CENTER_HORIZONTAL);
            textView.setTextSize(22);
            textView.setTextColor(Color.BLACK);
            textView.setTypeface(null, Typeface.BOLD);

            builder.setTitle(null)
                    .setView(textView)
                    .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            save(editTitle.getText().toString(), editText.getText().toString());
                            finish();
                        }
                    })
                    .setNeutralButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setNegativeButton(R.string.discard, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .setIcon(null)
                    .show();
        } else {
            finish();
        }

    }

    private boolean ifEdited() {
        if (tempTitle.equals(editTitle.getText().toString()) &&
                tempText.equals(editText.getText().toString())) {

            return false;

        } else {
            return true;
        }
    }

    private void cache() {
        tempTitle = editTitle.getText().toString();
        tempText = editText.getText().toString();
    }

    public void alertDialog(final int id) {
        AlertDialog.Builder builder;
        // Context context;
        builder = new AlertDialog.Builder(EditActivity.this);

        builder.setTitle("Delete Note")
                .setMessage("Are you sure you want to delete this note?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DBAdapter database = new DBAdapter(EditActivity.this);
                        database.openDB();
                        database.delete(id);
                        database.close();
                        finish();

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
}
