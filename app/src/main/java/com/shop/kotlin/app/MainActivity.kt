package com.shop.kotlin.app

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var listItems = ArrayList<Item>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        listItems.add(Item(5, "Test", "Test.", 2, 10))

        loadQueryAll()

       //
         lvItems.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, position, id ->
            Toast.makeText(this, "Click on " + listItems[position].title, Toast.LENGTH_SHORT).show()
        }

        floatingActionButton3.setOnClickListener { view ->
            var intent = Intent(this, ItemActivity::class.java)
            startActivity(intent)
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

        return super.onCreateOptionsMenu(menu)
    }

    //Test - add buttonHead
    /*
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item != null) {
            when (item.itemId) {
                R.id.addNote -> {
                    var intent = Intent(this, ItemActivity::class.java)
                    startActivity(intent)
                }

            }
        }

        return super.onOptionsItemSelected(item)
    }
    */


    override fun onResume() {
        super.onResume()
        loadQueryAll()
    }

    fun loadQueryAll() {

        var dbManager = ItemDbManager(this)
        val cursor = dbManager.queryAll()

        listItems.clear()
        if (cursor.moveToFirst()) {

            do {
                val id = cursor.getInt(cursor.getColumnIndex("Id"))
                val title = cursor.getString(cursor.getColumnIndex("Title"))
                val content = cursor.getString(cursor.getColumnIndex("Content"))
                val cost = cursor.getInt(cursor.getColumnIndex("Cost"))
                val qvc = cursor.getInt(cursor.getColumnIndex("QVC"))

                listItems.add(Item(id, title, content, cost, qvc))

            } while (cursor.moveToNext())
        }

        var notesAdapter = NotesAdapter(this, listItems)
        lvItems.adapter = notesAdapter
    }

    inner class NotesAdapter : BaseAdapter {

        private var notesList = ArrayList<Item>()
        private var context: Context? = null

        constructor(context: Context, notesList: ArrayList<Item>) : super() {
            this.notesList = notesList
            this.context = context
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {

            val view: View?
            val vh: ViewHolder

            if (convertView == null) {
                view = layoutInflater.inflate(R.layout.item, parent, false)
                vh = ViewHolder(view)
                view.tag = vh
                Log.i("JSA", "set Tag for ViewHolder, position: " + position)
            } else {
                view = convertView
                vh = view.tag as ViewHolder
            }

            var mNote = notesList[position]

            vh.tvTitle.text = mNote.title
            vh.tvContent.text = mNote.content
            vh.tvCost.text = mNote.cost.toString()
            vh.tvQVC.text = mNote.qvc.toString()

            vh.ivEdit.setOnClickListener {
                updateNote(mNote)
            }

            vh.ivDelete.setOnClickListener {
                var dbManager = ItemDbManager(this.context!!)
                val selectionArgs = arrayOf(mNote.id.toString())
                dbManager.delete("Id=?", selectionArgs)
                loadQueryAll()
            }

            return view
        }

        override fun getItem(position: Int): Any {
            return notesList[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return notesList.size
        }
    }

    private fun updateNote(item: Item) {
        var intent = Intent(this, ItemActivity::class.java)
        intent.putExtra("MainActId", item.id)
        intent.putExtra("MainActTitle", item.title)
        intent.putExtra("MainActContent", item.content)
        intent.putExtra("MainActCost", item.content)
        intent.putExtra("MainActQVC", item.content)
        startActivity(intent)
    }

    private class ViewHolder(view: View?) {
        val tvTitle: TextView
        val tvContent: TextView
        val ivEdit: ImageView
        val ivDelete: ImageView
        val tvCost: TextView
        val tvQVC: TextView

        init {
            this.tvTitle = view?.findViewById(R.id.tvTitle) as TextView
            this.tvContent = view?.findViewById(R.id.tvContent) as TextView
            this.ivEdit = view?.findViewById(R.id.ivEdit) as ImageView
            this.ivDelete = view?.findViewById(R.id.ivDelete) as ImageView
            this.tvCost = view?.findViewById(R.id.tvCost) as TextView
            this.tvQVC = view?.findViewById(R.id.tvQVC) as TextView
        }
    }

}