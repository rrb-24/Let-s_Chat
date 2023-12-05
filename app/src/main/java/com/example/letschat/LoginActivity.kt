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

class LoginActivity : AppCompatActivity() {


    private lateinit var Email: EditText
    private lateinit var Passowrd: EditText
    private lateinit var BtnLogin: Button
    private lateinit var BtnToSignup: TextView
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var forgotPassword:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        firebaseAuth = FirebaseAuth.getInstance()

        Email = findViewById(R.id.email)
        Passowrd = findViewById(R.id.password)
        BtnLogin = findViewById(R.id.btn_login)
        BtnToSignup = findViewById(R.id.txt_signup)
        forgotPassword=findViewById(R.id.txt_forgotPassword)

        BtnToSignup.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }

        BtnLogin.setOnClickListener {
            val Email = Email.text.toString()
            val Password = Passowrd.text.toString()
            login(Email, Password)
        }
        forgotPassword.setOnClickListener {
            val intent = Intent(this, ForgotPassword::class.java)
            startActivity(intent)
        }


    }

    private fun login(Email: String, Password: String) {
        if (Email.isNotEmpty() && Password.isNotEmpty()) {

            firebaseAuth.signInWithEmailAndPassword(Email, Password).addOnCompleteListener {
                if (it.isSuccessful) {
                    val user = firebaseAuth.currentUser
                    if (user!!.isEmailVerified) {
                        val intent = Intent(this, MainActivity::class.java)
                        finish()
                        startActivity(intent)
                    } else {
                        user!!.sendEmailVerification()
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Toast.makeText(
                                        this,
                                        "Verification Mail Sent.(Check Spam)",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    Log.d(ContentValues.TAG, "Email sent.")
                                }
                            }
                    }
                } else {
                    Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                }

            }
        } else {
            Toast.makeText(this, "Empty Fields Not Allowed", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onStart() {
        super.onStart()
        if (firebaseAuth.currentUser != null) {
            if (firebaseAuth.currentUser!!.isEmailVerified) {
                val intent = Intent(this, MainActivity::class.java)
                finish()
                startActivity(intent)
            }
        }
    }
}