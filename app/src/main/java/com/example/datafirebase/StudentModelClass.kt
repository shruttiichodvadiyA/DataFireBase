package com.example.datafirebase

 class StudentModelClass {

    lateinit var key: String
    lateinit var name: String
    lateinit var adress: String
    lateinit var mobileno: String
    lateinit var email: String
    lateinit var images:String

    constructor(key: String, name: String, adress: String, mobileno: String, email: String,images:String) {
        this.key = key
        this.name = name
        this.adress = adress
        this.mobileno = mobileno
        this.email = email
        this.images=images
    }

    constructor() {

    }
}