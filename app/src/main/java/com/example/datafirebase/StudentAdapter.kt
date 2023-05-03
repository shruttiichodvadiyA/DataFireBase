package com.example.datafirebase

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class StudentAdapter(var studentlist: ArrayList<StudentModelClass>,var onEditClick:((StudentModelClass)->Unit),var onDeleteClick:((String)->Unit)) : RecyclerView.Adapter<StudentAdapter.myAdapter>() {
    class myAdapter(v: View) : RecyclerView.ViewHolder(v) {
        var txtname: TextView = v.findViewById(R.id.txtname)
        var txtAdress: TextView = v.findViewById(R.id.txtAdress)
        var txtMobileno: TextView = v.findViewById(R.id.txtMobileno)
        var txtEmail: TextView = v.findViewById(R.id.txtEmail)
        var imgedit: ImageView = v.findViewById(R.id.imgedit)
        var imgdelete: ImageView = v.findViewById(R.id.imgdelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myAdapter {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.studentdata_item_file, parent, false)
        val adapter = myAdapter(v)
        return adapter
    }

    override fun onBindViewHolder(holder: myAdapter, position: Int) {
        holder.apply {
            holder.txtname.setText(studentlist[position].name)
            holder.txtAdress.setText(studentlist[position].adress)
            holder.txtMobileno.setText(studentlist[position].mobileno)
            holder.txtEmail.setText(studentlist[position].email)


            holder.imgedit.setOnClickListener {
                onEditClick.invoke(studentlist[position])
            }
            holder.imgdelete.setOnClickListener {
               onDeleteClick.invoke(studentlist[position].key)
            }
        }

    }

    override fun getItemCount(): Int {
        return studentlist.size

    }

    fun updateData(studentlist: ArrayList<StudentModelClass>) {
        this.studentlist=studentlist
        notifyDataSetChanged()

    }
}