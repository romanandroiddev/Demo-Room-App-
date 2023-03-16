package com.example.roomdatabaseapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.roomdatabaseapp.data.models.Student
import com.example.roomdatabaseapp.databinding.ItemStudentBinding

class StudentsAdapter : RecyclerView.Adapter<StudentsAdapter.StudentViewHolder>() {

    private var onClickStudentListener: ((Student) -> Unit)? = null


    fun setOnStudentClickListener(block: (Student) -> Unit) {
        onClickStudentListener = block
    }

    inner class StudentViewHolder(private val binding: ItemStudentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            val d = models[adapterPosition]


            binding.tvName.text = d.name
            binding.tvSurname.text = d.surname


            binding.root.setOnClickListener {
                onClickStudentListener?.invoke(d)
            }
        }
    }

    var models = mutableListOf<Student>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        return StudentViewHolder(
            ItemStudentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount() = models.size

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        holder.bind()
    }
}
