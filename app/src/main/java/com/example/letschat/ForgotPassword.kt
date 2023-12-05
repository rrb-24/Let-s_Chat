package com.example.letschat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth


class ForgotPassword : AppCompatActivity() {

    private lateinit var email:EditText
    private lateinit var send:Button
    private lateinit var firebaseAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        firebaseAuth= FirebaseAuth.getInstance()
        email=findViewById(R.id.email)
        send=findViewById(R.id.btn_send)


        send.setOnClickListener {
            val email=email.text.toString()
            firebaseAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Password Reset Link Sent.(Check Spam)", Toast.LENGTH_SHORT).show()
                    }

                }
        }
    }
}