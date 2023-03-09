package com.example.roomdatabaseapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.roomdatabaseapp.data.StudentsDatabase
import com.example.roomdatabaseapp.data.dao.StudentsDao
import com.example.roomdatabaseapp.data.models.Student
import com.example.roomdatabaseapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var dao: StudentsDao
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dao = StudentsDatabase.getInstance(this).getStudentsDao()


        binding.btnAdd.setOnClickListener {
            val name = binding.etName.text.toString()
            val surname = binding.etSurname.text.toString()

            if (name.isNotEmpty() && surname.isNotEmpty()) {
                lifecycleScope.launchWhenResumed {
                    dao.addStudent(Student(0, name, surname))
                }
            }
        }




        binding.btnGet.setOnClickListener {
            lifecycleScope.launchWhenResumed {
                dao.getListOfStudents().forEach {
                    Log.d("TTTT", "\n${it.id}, name: ${it.name}, surname: ${it.surname}")
                }
            }
        }

        binding.btnSearch.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }
    }
}