package com.ragazm.notepad

import android.content.DialogInterface
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.TextView

import com.ragazm.notepad.database.DBAdapter
import com.ragazm.notepad.recycler.ItemClickListener
import com.ragazm.notepad.recycler.MyAdapter
import com.ragazm.notepad.recycler.RecyclerTouchListener

import java.util.ArrayList

class MainActivity : AppCompatActivity() {


    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MyAdapter
    // Dialog deleteDialog =new Dialog();
    internal var notes = ArrayList<Note>()
    internal var txtNoNote: TextView? = null
    private val showNoNote = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_main)


        Log.d("note.size()", notes.size.toString())

        //Floating button action
        val fab = findViewById(R.id.floatingActionButton) as FloatingActionButton
        fab.setOnClickListener {
            val intentFab = Intent(this@MainActivity, EditActivity::class.java)
            startActivity(intentFab)
            Log.d("Fab pressed!!", "Pressed indeed!!")
        }

        recyclerView = findViewById(R.id.recycler_view) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.itemAnimator = DefaultItemAnimator()

        adapter = MyAdapter(this, notes)

        recyclerView.addOnItemTouchListener(RecyclerTouchListener(this, recyclerView, object : ItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                // Open EditActivity, pass data, create intent
                val intent = Intent(this@MainActivity, EditActivity::class.java)
                //load data
                intent.putExtra("ID", position)
                startActivity(intent)
            }

            override fun onItemPress(view: View, position: Int) {
                alertDialog(notes[position].id)
                Log.d("POSITION:", position.toString())
                retrieve()


            }
        }))

        retrieve()

    }

    private fun retrieve() {
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


            Log.d("note.size()", notes.size.toString())
        }

        //if (!(notes.size()<1)){
        recyclerView.adapter = adapter
        //}
    }

    override fun onResume() {
        super.onResume()
        retrieve()
    }

    fun alertDialog(id: Int) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this@MainActivity)
        // Context context;

        builder.setTitle("Delete Note")
                .setMessage("Are you sure you want to delete this note?")
                .setPositiveButton(android.R.string.yes) { dialog, which ->
                    val database = DBAdapter(this@MainActivity)
                    database.openDB()
                    database.delete(id)
                    database.close()
                    retrieve()
                }
                .setNegativeButton(android.R.string.no) { dialog, which ->
                    // Do nothing if pressed NO
                }

                .show()

    }

    /**
     * Setting menu for future backup function
     *
     * @Override public boolean onCreateOptionsMenu(Menu menu) {
     * getMenuInflater().inflate(R.menu.menu_main, menu);
     * return super.onCreateOptionsMenu(menu);
     * }
     * @Override public boolean onOptionsItemSelected(MenuItem item) {
     * // int id = item.getItemId();
     * switch (item.getItemId()) {
     * case R.id.make_backup:
     *
     * return true;
     *
     * case R.id.load_backup:
     * //Save note
     *
     *
     * }
     * return super.onOptionsItemSelected(item);
     * }
     */


}
