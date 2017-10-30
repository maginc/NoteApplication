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

import java.util.ArrayList;

//TODO !!! sort out double saving
//TODO sort out alert dialog design
//TODO prevent asking Save if note already saved and not changed
//TODO implement some variable which check state of text( if its saved, if edited, if empty etc.)





public class EditActivity extends AppCompatActivity {
    EditText editTitle;
    EditText editText;

    String tempTitle;
    String tempText;

    ArrayList<Note> notes = new ArrayList<>();
    boolean edit = false;
    int extraId;
    boolean sameNote = false;

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
            if (editTitle.getText().toString().trim().length() !=0 ||
                    editText.getText().toString().trim().length() !=0){
                //Save note
                save(editTitle.getText().toString(), editText.getText().toString());
            }else{
                Toast.makeText(getApplicationContext(), "Note is empty!", Toast.LENGTH_SHORT).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_edit);
        sameNote = true;



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
            edit = true;

        }

        tempTitle = editTitle.getText().toString();
        tempText = editText.getText().toString();




    }

    //    Save button
    private void save(String title, String text){
        DBAdapter database = new DBAdapter(this);
        if(ifEdited()){
            edit(notes.get(extraId).getId(), editTitle.getText().toString(), editText.getText().toString());
            edit =false;
        }else
        if(!sameNote) {
            if(editTitle!=null || editText!=null){
            database.openDB();
            long result = database.add(title, text);
            if (result > 0) {
                Toast.makeText(getApplicationContext(), "Note saved successfully", Toast.LENGTH_SHORT).show();
            }

            database.close();
        }
        }else{Toast.makeText(getApplicationContext(), "Note is empty!", Toast.LENGTH_SHORT).show();}
    }

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
                            if (ifEdited()){

                                save(editTitle.getText().toString(),editText.getText().toString());
                                edit = false;
                                finish();
                            }else {

                                edit(notes.get(extraId).getId(), editTitle.getText().toString(), editText.getText().toString());

                                edit = false;
                                finish();
                            }



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
            edit = false;
            return false;

        }else {
            edit = true;

            tempTitle = editTitle.getText().toString();
            tempText = editText.getText().toString();

            return true;
        }
    }
}
