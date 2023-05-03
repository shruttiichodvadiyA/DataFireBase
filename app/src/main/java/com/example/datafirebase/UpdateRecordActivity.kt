package com.example.datafirebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.datafirebase.databinding.ActivityUpdateRecordBinding
import com.google.firebase.database.FirebaseDatabase

class UpdateRecordActivity : AppCompatActivity() {
    lateinit var binding: ActivityUpdateRecordBinding
    lateinit var firebaseDatabase: FirebaseDatabase

    //    var studentlist = ArrayList<StudentModelClass>()
//    lateinit var adapter: StudentAdapter
    var key: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateRecordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initview()
    }

    private fun initview() {
        key = intent.getStringExtra("key").toString()
        binding.edtname.setText(intent.getStringExtra("name"))
        binding.edtadress.setText(intent.getStringExtra("adress"))
        binding.edtMobileNumber.setText(intent.getStringExtra("mobileNumber"))
        binding.edtemail.setText(intent.getStringExtra("email"))

        firebaseDatabase = FirebaseDatabase.getInstance()
        binding.btnupdate.setOnClickListener {

            var data = StudentModelClass(
                key,
                binding.edtname.text.toString(),
                binding.edtadress.text.toString(),
                binding.edtMobileNumber.text.toString(),
                binding.edtemail.text.toString()

            )
            firebaseDatabase.reference.child("StudentTb").child(key).setValue(data)
                .addOnCompleteListener {

                    if (it.isSuccessful) {
                        Toast.makeText(this, "record updated successfully", Toast.LENGTH_SHORT)
                            .show()
                    }
                }.addOnFailureListener {
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }
            var i = Intent(this, DisplayDataActivity::class.java)
            startActivity(i)
        }
    }
}

