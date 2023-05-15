package com.example.datafirebase

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.datafirebase.databinding.ActivityDisplayDataBinding
import com.example.datafirebase.databinding.DeleteDialogBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DisplayDataActivity : AppCompatActivity() {
    lateinit var binding: ActivityDisplayDataBinding
    lateinit var firebaseDatabase: FirebaseDatabase
    var studentlist = ArrayList<StudentModelClass>()
    lateinit var adapter: StudentAdapter
    var id: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDisplayDataBinding.inflate(layoutInflater)
        setContentView(binding.root)
        data()
    }

    private fun data() {
        firebaseDatabase = FirebaseDatabase.getInstance()
        setAdapter()
        firebaseDatabase.reference.child("StudentTb").addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                studentlist.clear()
                for (i in snapshot.children) {
                    var data = i.getValue(StudentModelClass::class.java)
                    Log.e("TAG", "onDataChange: " + data?.name)
                    data?.let { it1 -> studentlist.add(it1) }

                }
//                setAdapter()
                adapter.updateData(studentlist)

            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    private fun setAdapter() {
        adapter = StudentAdapter( this, {
            var i = Intent(this, UpdateRecordActivity::class.java)
            id = it.key
            i.putExtra("key", id)
            i.putExtra("name", it.name)
            i.putExtra("adress", it.adress)
            i.putExtra("mobileNumber", it.mobileno)
            i.putExtra("email", it.email)
            startActivity(i)


        }, {
            id = it
            deleteDataFromDataBase()
        })
        var manager =
            LinearLayoutManager(this@DisplayDataActivity, LinearLayoutManager.VERTICAL, false)
        binding.rcvDataDisplay.layoutManager = manager
        binding.rcvDataDisplay.adapter = adapter
    }

    private fun deleteDataFromDataBase() {

        val dialog = Dialog(this)
        val dialogBinding: DeleteDialogBinding = DeleteDialogBinding.inflate(layoutInflater)
        dialog.setContentView(dialogBinding.root)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setLayout(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        dialogBinding.btndelete.setOnClickListener {
            firebaseDatabase.reference.child("StudentTb").child(id).removeValue()
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(this, "record delete ", Toast.LENGTH_SHORT).show()
                    }
                }.addOnFailureListener {
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            }
            Toast.makeText(this, "delete", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }

        dialogBinding.btncancel.setOnClickListener {
            Toast.makeText(this, "Cancel", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }
        dialog.show()

    }


}