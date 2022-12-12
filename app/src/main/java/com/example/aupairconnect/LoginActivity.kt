package com.example.aupairconnect

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity


class LoginActivity : AppCompatActivity(){

    private lateinit var txtEmail: EditText
    private lateinit var txtPassword: EditText
    private lateinit var loginButton: Button
    private lateinit var registerButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        txtEmail = findViewById(R.id.etEmail)
        txtPassword = findViewById(R.id.etPassword)

        loginButton = findViewById(R.id.btnLogin)
        registerButton = findViewById(R.id.btnRegister)

        loginButton.setOnClickListener{
            startActivity(Intent(this, ACMainActivity::class.java))
        }


        registerButton.setOnClickListener{
//            val options = AuthSignUpOptions.builder()
//                .userAttribute(AuthUserAttributeKey.email(), txtEmail.text.toString())
//                .build()
//
//            Amplify.Auth.signUp(txtEmail.text.toString(), txtPassword.text.toString())

//            val intent = Intent(this, SecondActivity::RegisterActivity.kt)
//            intent.putExtra("key", value)
//            startActivity(intent)

//            val intent =
            startActivity(Intent(this, RegisterActivity::class.java))

        }
    }
}