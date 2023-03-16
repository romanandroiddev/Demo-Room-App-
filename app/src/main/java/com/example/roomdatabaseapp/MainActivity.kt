package com.example.roomdatabaseapp

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.widget.Toast
import androidx.core.view.accessibility.AccessibilityEventCompat.setAction
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.roomdatabaseapp.adapters.StudentsAdapter
import com.example.roomdatabaseapp.data.StudentsDatabase
import com.example.roomdatabaseapp.data.dao.StudentsDao
import com.example.roomdatabaseapp.data.models.Student
import com.example.roomdatabaseapp.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var dao: StudentsDao

    private val adapter = StudentsAdapter()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val pref = getSharedPreferences("pref",Context.MODE_PRIVATE)
        setAppLocale(pref.getString("language","en"),this@MainActivity)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dao = StudentsDatabase.getInstance(this).getStudentsDao()


        binding.rvStudents.adapter = adapter


        val itemTouchHelperCallback = object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ) = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val student: Student = adapter.models[position]

                lifecycleScope.launchWhenResumed {
                    dao.deleteStudent(student)
                }
                adapter.models.removeAt(position)
                adapter.notifyItemRemoved(position)
//                    noteViewModel.delete(note)
//                    Toast.makeText(this@MainActivity, "O'shirdi", Toast.LENGTH_SHORT).show()
                Snackbar.make(
                    viewHolder.itemView, // The ID of your coordinator_layout
                    "O'shdi", Snackbar.LENGTH_LONG
                ).apply {
                    setAction("UNDO") {

                        lifecycleScope.launchWhenResumed {
                            dao.addStudent(student)
                        }

                        adapter.models.add(position, student)
                        adapter.notifyItemInserted(position)

                        // If you're not using LiveData you might need to tell the adapter
                        // that an item was inserted: notifyItemInserted(position);
                        binding.rvStudents.scrollToPosition(position)
                    }
                    setActionTextColor(Color.YELLOW)
                }.show()
            }
        }

        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(binding.rvStudents)
        }

        binding.btnAdd.setOnClickListener {
            val intent = Intent(this, AddStudentActivity::class.java)
            intent.putExtra("isEdit", false)
            startActivity(intent)
            finish()
        }



        adapter.setOnStudentClickListener {
            val intent = Intent(this, AddStudentActivity::class.java)
            intent.putExtra("isEdit", true)
            intent.putExtra("id", it.id)
            intent.putExtra("name", it.name)
            intent.putExtra("surname", it.surname)
            startActivity(intent)
            finish()
        }



        binding.etSearch.addTextChangedListener {
            if (it.toString().isNotEmpty()) {
                lifecycleScope.launchWhenResumed {
                    adapter.models = dao.searchStudentByName(it.toString()).toMutableList()
                }
            } else {
                lifecycleScope.launchWhenResumed {
                    adapter.models = dao.getListOfStudents().toMutableList()
                }
            }
        }


    }


    override fun onResume() {
        super.onResume()

        lifecycleScope.launchWhenResumed {
            adapter.models = dao.getListOfStudents().toMutableList()
        }

    }


    private fun setAppLocale(languageFromPreference: String?, context: Context) {
        if (languageFromPreference != null) {
            val resources: Resources = context.resources
            val dm: DisplayMetrics = resources.displayMetrics
            val config: Configuration = resources.configuration
            config.setLocale(Locale(languageFromPreference.lowercase(Locale.ROOT)))
            val pref = getSharedPreferences("pref", Context.MODE_PRIVATE)
            pref.edit().putString("language",languageFromPreference.toString().lowercase()).apply()
            resources.updateConfiguration(config, dm)
        }
    }
}