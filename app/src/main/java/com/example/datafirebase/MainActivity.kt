package com.example.datafirebase

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.datafirebase.databinding.ActivityMainBinding
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var firebaseDatabase: FirebaseDatabase
    lateinit var storageReference: StorageReference
    var PICK_IMAGE_REQUEST = 100
    lateinit var filePath: Uri
   private lateinit var images:String
    var studentlist = ArrayList<StudentModelClass>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initview()
    }

    private fun initview() {
        firebaseDatabase = FirebaseDatabase.getInstance()
        storageReference = FirebaseStorage.getInstance().reference
        binding.btnRegister.setOnClickListener {
            var name = binding.edtname.text.toString()
            var adress = binding.edtadress.text.toString()
            var mobileno = binding.edtMobileNumber.text.toString()
            var email = binding.edtemail.text.toString()

            if (name.isEmpty()){
                Toast.makeText(this, "please enter your name", Toast.LENGTH_SHORT).show()
            }
            else if (adress.isEmpty()){
                Toast.makeText(this, "please enter your adress", Toast.LENGTH_SHORT).show()
            }
            else if (mobileno.isEmpty()){
                Toast.makeText(this, "please enter your mobileno", Toast.LENGTH_SHORT).show()
            }
            else if (email.isEmpty()){
                Toast.makeText(this, "please enter your email", Toast.LENGTH_SHORT).show()
            }
            else{
                val key = firebaseDatabase.reference.child("StudentTb").push().key ?: ""
                val data = StudentModelClass(key, name, adress, mobileno, email,images)
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
            
        }
        binding.btnDisplay.setOnClickListener {
            var i = Intent(this, DisplayDataActivity::class.java)
            startActivity(i)
        }



        binding.btnselectimg.setOnClickListener {
            selectImage()
        }
        binding.btnuploadimg.setOnClickListener {
            uploadImage()
        }

    }

    private fun uploadImage() {
        if (filePath != null) {

            // Code for showing progressDialog while uploading
            val progressDialog = ProgressDialog(this)
            progressDialog.setTitle("Uploading...")
            progressDialog.show()

            // Defining the child of storageReference
            val ref: StorageReference = storageReference.child(
                "images/" + UUID.randomUUID().toString())

            // adding listeners on upload
            // or failure of image
            ref.putFile(filePath)

                .addOnCompleteListener{
                    ref.downloadUrl.addOnSuccessListener {
                        images=it.toString()
                        Log.e("TAG", "uploadImage: "+it )
                    }
                }
                .addOnSuccessListener { // Image uploaded successfully
                    // Dismiss dialog
                    progressDialog.dismiss()
                    Toast.makeText(this@MainActivity, "Image Uploaded!!", Toast.LENGTH_SHORT).show()

                }
                .addOnFailureListener { e -> // Error, Image not uploaded
                    progressDialog.dismiss()
                    Toast.makeText(this@MainActivity, "Failed " + e.message, Toast.LENGTH_SHORT)
                        .show()
                }
                .addOnProgressListener { taskSnapshot ->

                    // Progress Listener for loading
                    // percentage on the dialog box
                    val progress = (100.0
                            * taskSnapshot.bytesTransferred
                            / taskSnapshot.totalByteCount)
                    progressDialog.setMessage(
                        "Uploaded "
                                + progress.toInt() + "%"
                    )
                }
        }
    }

    private fun selectImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(
            Intent.createChooser(intent, "Select Image from here..."),
            PICK_IMAGE_REQUEST
        )

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            // Get the Uri of data
            filePath = data.getData()!!
            try {

                // Setting image on image view using Bitmap
                val bitmap = MediaStore.Images.Media
                    .getBitmap(
                        contentResolver,
                        filePath
                    )
                binding.imgview.setImageBitmap(bitmap)
            } catch (e: IOException) {
                // Log the exception
                e.printStackTrace()
            }
        }
    }


}