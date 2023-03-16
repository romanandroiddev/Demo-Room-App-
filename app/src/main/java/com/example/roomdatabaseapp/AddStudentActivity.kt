package com.example.roomdatabaseapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.roomdatabaseapp.data.StudentsDatabase
import com.example.roomdatabaseapp.data.dao.StudentsDao
import com.example.roomdatabaseapp.data.models.Student
import com.example.roomdatabaseapp.databinding.ActivitySearchBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

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
}