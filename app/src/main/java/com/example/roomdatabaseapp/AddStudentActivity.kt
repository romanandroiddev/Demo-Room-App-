package com.example.roomdatabaseapp

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.roomdatabaseapp.data.StudentsDatabase
import com.example.roomdatabaseapp.data.dao.StudentsDao
import com.example.roomdatabaseapp.data.models.Student
import com.example.roomdatabaseapp.databinding.ActivitySearchBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

class AddStudentActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding
    private lateinit var dao: StudentsDao
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val isEdit = intent.getBooleanExtra("isEdit", false)
        val studentId = intent.getIntExtra("id", 0)
        val studentName = intent.getStringExtra("name")
        val studentSurname = intent.getStringExtra("surname")

        dao = StudentsDatabase.getInstance(this).getStudentsDao()


        if (isEdit) {
            binding.btnAddEdit.text = "Edit"
            binding.etName.setText(studentName)
            binding.etSurname.setText(studentSurname)
        } else {
            binding.btnAddEdit.text = "Add"
        }


        binding.icBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }


        binding.btnDefault.setOnClickListener {
            val pref = getSharedPreferences("pref", Context.MODE_PRIVATE)
            pref.edit().putString("language", "en").apply()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        binding.btnRu.setOnClickListener {
            val pref = getSharedPreferences("pref", Context.MODE_PRIVATE)
            pref.edit().putString("language", "ru").apply()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        binding.btnAddEdit.setOnClickListener {
            if (isEdit) {
                lifecycleScope.launchWhenResumed {
                    val name = binding.etName.text.toString()
                    val surname = binding.etSurname.text.toString()
                    if (name.isNotEmpty() && surname.isNotEmpty()) {
                        lifecycleScope.launchWhenResumed {
                            dao.updateStudent(Student(studentId, name, surname))
                        }
                    } else {
                        Toast.makeText(
                            this@AddStudentActivity,
                            "Mag'liwmatlardi toliq kiritin'",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    Toast.makeText(
                        this@AddStudentActivity, "Successfully edited", Toast.LENGTH_SHORT
                    ).show()
                    finish()
                }
            } else {
                val name = binding.etName.text.toString()
                val surname = binding.etSurname.text.toString()
                if (name.isNotEmpty() && surname.isNotEmpty()) {
                    lifecycleScope.launchWhenResumed {
                        dao.addStudent(Student(0, name, surname))
                    }
                } else {
                    Toast.makeText(this, "Mag'liwmatlardi toliq kiritin'", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()

    }

}