package com.example.letschat

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignupActivity : AppCompatActivity() {

    private lateinit var Name: EditText
    private lateinit var Email: EditText
    private lateinit var Password: EditText
    private lateinit var ConPassword: EditText
    private lateinit var BtnSignup: Button
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var mDbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        firebaseAuth = FirebaseAuth.getInstance()

        Email = findViewById(R.id.email)
        Password = findViewById(R.id.password)
        ConPassword = findViewById(R.id.conpassword)
        Name = findViewById(R.id.name)
        BtnSignup = findViewById(R.id.btn_signup)

        BtnSignup.setOnClickListener {
            val email = Email.text.toString()
            val password = Password.text.toString()
            val conPassword = ConPassword.text.toString()
            val name = Name.text.toString()

            signup(name, email, password, conPassword)
        }
    }

    private fun signup(name: String, email: String, password: String, conPassword: String) {
        if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && conPassword.isNotEmpty()) {
            if (password == conPassword) {
                if (isPasswordValid(password)) {
                    firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val user = firebaseAuth.currentUser
                                user!!.sendEmailVerification()
                                    .addOnCompleteListener { verificationTask ->
                                        if (verificationTask.isSuccessful) {
                                            addUserToDatabase(name, email, firebaseAuth.currentUser?.uid!!)
                                            Toast.makeText(
                                                this,
                                                "Verification Mail Sent. (Check Spam)",
                                                Toast.LENGTH_LONG
                                            ).show()
                                            Log.d(ContentValues.TAG, "Email sent.")
                                            val intent = Intent(this, LoginActivity::class.java)
                                            startActivity(intent)
                                        } else {
                                            Toast.makeText(
                                                this,
                                                verificationTask.exception.toString(),
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                            } else {
                                Toast.makeText(
                                    this,
                                    task.exception.toString(),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                } else {
                    Toast.makeText(
                        this,
                        "Password should contain at least one capital letter, one number, one special character, and be a minimum of 8 characters long.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(this, "Password doesn't match!", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Empty Fields Not Allowed", Toast.LENGTH_SHORT).show()
        }
    }

    private fun isPasswordValid(password: String): Boolean {
        val passwordPattern =
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$".toRegex()
        return password.matches(passwordPattern)
    }

    private fun addUserToDatabase(name: String, email: String, uid: String) {
        mDbRef = FirebaseDatabase.getInstance().getReference()
        mDbRef.child("user").child(uid).setValue(User(name, email, uid))
    }
}
