package com.example.roomdatabaseapp

import android.content.Context
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
            finish()
        }


        binding.btnDefault.setOnClickListener {
            val pref = getSharedPreferences("pref", Context.MODE_PRIVATE)
            pref.edit().putString("language", "en").apply()
            finish()
        }
        binding.btnRu.setOnClickListener {
            val pref = getSharedPreferences("pref", Context.MODE_PRIVATE)
            pref.edit().putString("language", "ru").apply()
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

    private fun setAppLocale(languageFromPreference: String?, context: Context) {
//        if (languageFromPreference != null) {
//            val resources: Resources = context.resources
//            val dm: DisplayMetrics = resources.displayMetrics
//            val config: Configuration = resources.configuration
//            config.setLocale(Locale(languageFromPreference.lowercase(Locale.ROOT)))
//            val pref = getSharedPreferences("pref",Context.MODE_PRIVATE)
//            pref.edit().putString("language",languageFromPreference.toString().lowercase()).apply()
//            resources.updateConfiguration(config, dm)
//        }
    }

}