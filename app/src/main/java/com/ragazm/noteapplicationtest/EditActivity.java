package com.ragazm.noteapplicationtest;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.ragazm.noteapplicationtest.database.DBAdapter;

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
        int id = item.getItemId();

        if (id == R.id.mybutton) {

                //Save note
            save(editTitle.getText().toString(),editText.getText().toString());

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_edit);

        editFlag = false;


        editText = (EditText)findViewById(R.id.editText);
        editTitle = (EditText)findViewById(R.id.editTitle);


        DBAdapter database = new DBAdapter(this);
        database.openDB();
        notes.clear();
        Cursor cursor = database.getAllNotes();

        while(cursor.moveToNext()){
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

        if (extraId >= 0){

            String extraTitle = notes.get(extraId).getTitle();
            String extraText =  notes.get(extraId).getText();

            editText.setText(extraText);
            editTitle.setText(extraTitle);
            editFlag = true;
        }

        cache();
    }

    //    Save/edit function
    private void save(String title, String text){
        DBAdapter database = new DBAdapter(this);
        if(editFlag && ifEdited()){
            edit(notes.get(extraId).getId(), editTitle.getText().toString(), editText.getText().toString());
            cache();
        }else
        if(ifEdited()) {
            if(editTitle!=null || editText!=null){
            database.openDB();
            long result = database.add(title, text);
            cache();
            if (result > 0) {
                Toast.makeText(getApplicationContext(), "Note saved successfully", Toast.LENGTH_SHORT).show();
            }
                database.close();

        }
        }else{Toast.makeText(getApplicationContext(), "Note is empty!", Toast.LENGTH_SHORT).show();}
    }

    //Edit function
    private void edit(int id, String title, String text){
        DBAdapter database = new DBAdapter(this);
        database.openDB();
        database.update(id, title, text);
        database.close();
        Toast.makeText(getApplicationContext(), "Note edited successfully", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        if (ifEdited()){

            AlertDialog.Builder builder;

            builder = new AlertDialog.Builder(EditActivity.this, android.R.style.Theme_Material_Dialog_Alert);
            builder.setTitle(null)
                    .setMessage("Save changes?")
                    .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            save(editTitle.getText().toString(),editText.getText().toString());
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
        } else{
            finish();
        }

    }

    private boolean ifEdited(){
        if (tempTitle.equals(editTitle.getText().toString()) &&
                tempText.equals(editText.getText().toString())){

            return false;

        }else {
            return true;
        }
    }

    private void cache(){
        tempTitle = editTitle.getText().toString();
        tempText = editText.getText().toString();
    }
}
