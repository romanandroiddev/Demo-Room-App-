package com.example.roomdatabaseapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.example.roomdatabaseapp.data.StudentsDatabase
import com.example.roomdatabaseapp.data.dao.StudentsDao
import com.example.roomdatabaseapp.databinding.ActivitySearchBinding

class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding
    private lateinit var dao: StudentsDao
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)


        dao = StudentsDatabase.getInstance(this).getStudentsDao()




        binding.btnSearch.setOnClickListener {
            val text = binding.etSearch.text.toString()
            if (text.isNotEmpty()) {
                lifecycleScope.launchWhenResumed {
                    dao.searchStudentByName(text).forEach {
                        Log.d("TTTT", "id: ${it.id}, name: ${it.name}, surname: ${it.surname}\n")
                    }
                }
            }
        }
    }
}