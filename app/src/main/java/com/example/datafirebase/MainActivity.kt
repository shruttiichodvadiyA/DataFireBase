package com.example.datafirebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.datafirebase.databinding.ActivityMainBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var firebaseDatabase: FirebaseDatabase
    var studentlist = ArrayList<StudentModelClass>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initview()
    }

    private fun initview() {
        firebaseDatabase = FirebaseDatabase.getInstance()
        binding.btnRegister.setOnClickListener {
            var name = binding.edtname.text.toString()
            var adress = binding.edtadress.text.toString()
            var mobileno = binding.edtMobileNumber.text.toString()
            var email = binding.edtemail.text.toString()

            val key = firebaseDatabase.reference.child("StudentTb").push().key ?: ""
            val data = StudentModelClass(key, name, adress, mobileno, email)
            firebaseDatabase.reference.child("StudentTb").child(key).setValue(data)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        binding.edtname.text.clear()
                        binding.edtadress.text.clear()
                        binding.edtemail.text.clear()
                        binding.edtMobileNumber.text?.clear()
                        binding.edtpassword.text.clear()
                        Toast.makeText(this, "Record added is successfully", Toast.LENGTH_SHORT)
                            .show()
                    }
                }.addOnFailureListener {
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            }


        }
        binding.btnDisplay.setOnClickListener {

            var i = Intent(this, DisplayDataActivity::class.java)
            startActivity(i)
        }




    }


}