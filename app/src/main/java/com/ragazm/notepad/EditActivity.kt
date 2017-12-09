package com.ragazm.notepad

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.database.Cursor
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

import com.ragazm.notepad.database.DBAdapter

import java.util.ArrayList

class EditActivity : AppCompatActivity() {
    private var editTitle: EditText? = null
    private var editText: EditText? = null
    private lateinit var tempTitle: String
    private lateinit var tempText: String
    private var notes = ArrayList<Note>()
    private var editFlag: Boolean = false
    private var extraId: Int = 0


    // create an action bar button
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    // Make Save button on action bar
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }

            R.id.mybutton -> {
                //Save note
                save(editTitle!!.text.toString(), editText!!.text.toString())
                return true
            }

            R.id.deleteButton -> if (extraId >= 0) {
                alertDialog(notes[extraId].id)
            } else {
                Toast.makeText(applicationContext, "Nothing to delete", Toast.LENGTH_SHORT).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        setContentView(R.layout.activity_edit)

        editFlag = false


        editText = findViewById(R.id.editText) as EditText
        editTitle = findViewById(R.id.editTitle) as EditText

        //Focus cursor on edit text field instead of default title field
        editText!!.requestFocus()
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)


        val database = DBAdapter(this)
        database.openDB()
        notes.clear()
        val cursor = database.allNotes

        while (cursor.moveToNext()) {
            val id = cursor.getInt(0)
            val title = cursor.getString(1)
            val text = cursor.getString(2)

            val note = Note(title, text, id)
            notes.add(note)
        }

        database.close()

        val intent = intent
        extraId = intent.getIntExtra("ID", -1)

        Log.d("ExtraId = ", extraId.toString())

        if (extraId >= 0) {

            val extraTitle = notes[extraId].title
            val extraText = notes[extraId].text

            editText!!.setText(extraText)
            editTitle!!.setText(extraTitle)
            editFlag = true
        }

        cache()
    }

    //    Save/edit function
    private fun save(title: String, text: String) {
        val database = DBAdapter(this)
        if (editFlag && ifEdited()) {
            edit(notes[extraId].id, editTitle!!.text.toString(), editText!!.text.toString())
            cache()
        } else if (ifEdited()) {
            if (editTitle != null || editText != null) {
                database.openDB()
                val result = database.add(title, text)
                cache()
                if (result > 0) {
                    Toast.makeText(applicationContext, "Note saved successfully", Toast.LENGTH_SHORT).show()
                }
                database.close()
            }
        } else {
            if (editTitle!!.text.toString() == "" && editText!!.text.toString() == "") {
                Toast.makeText(applicationContext, "Note is empty!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(applicationContext, "Note already saved!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    //Edit function
    private fun edit(id: Int, title: String, text: String) {
        val database = DBAdapter(this)
        database.openDB()
        database.update(id, title, text)
        database.close()
        Toast.makeText(applicationContext, "Note edited successfully", Toast.LENGTH_SHORT).show()
    }

    //Alert dialog on back button pressed
    override fun onBackPressed() {
        if (ifEdited()) {
            val builder: AlertDialog.Builder
            builder = AlertDialog.Builder(this@EditActivity)
            val textView = TextView(this)
            textView.text = resources.getString(R.string.save_chages)
            textView.gravity = Gravity.CENTER_HORIZONTAL
            textView.textSize = 22f
            textView.setTextColor(Color.BLACK)
            textView.setTypeface(null, Typeface.BOLD)

            builder.setTitle(null)
                    .setView(textView)
                    .setPositiveButton(R.string.save) { dialog, which ->
                        save(editTitle!!.text.toString(), editText!!.text.toString())
                        finish()
                    }
                    .setNeutralButton(R.string.cancel) { dialog, which -> }
                    .setNegativeButton(R.string.discard) { dialog, which -> finish() }
                    .setIcon(null)
                    .show()
        } else {
            finish()
        }

    }

    private fun ifEdited(): Boolean {
        return !(tempTitle == editTitle!!.text.toString() && tempText == editText!!.text.toString())
    }

    private fun cache() {
        tempTitle = editTitle!!.text.toString()
        tempText = editText!!.text.toString()
    }

    fun alertDialog(id: Int) {
        val builder: AlertDialog.Builder
        // Context context;
        builder = AlertDialog.Builder(this@EditActivity)

        builder.setTitle("Delete Note")
                .setMessage("Are you sure you want to delete this note?")
                .setPositiveButton(android.R.string.yes) { dialog, which ->
                    val database = DBAdapter(this@EditActivity)
                    database.openDB()
                    database.delete(id)
                    database.close()
                    finish()
                }
                .setNegativeButton(android.R.string.no) { dialog, which ->
                    // Do nothing if pressed NO
                }

                .show()

    }
}
