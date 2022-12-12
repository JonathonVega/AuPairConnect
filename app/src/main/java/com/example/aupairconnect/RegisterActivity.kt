package com.example.aupairconnect

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.amplifyframework.auth.AuthUserAttributeKey
import com.amplifyframework.auth.options.AuthSignUpOptions
import com.amplifyframework.core.Amplify

class RegisterActivity : AppCompatActivity(){

    private lateinit var txtEmail: EditText
    private lateinit var txtPassword: EditText
    private lateinit var txtConfirmPassword: EditText
    private lateinit var loginButton: Button
    private lateinit var registerButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        txtEmail = findViewById(R.id.etEmail)
        txtPassword = findViewById(R.id.etPassword)
        txtConfirmPassword = findViewById(R.id.etConfirmPassword)

        registerButton = findViewById(R.id.btnRegisterUser)
        loginButton = findViewById(R.id.btnBackToLogin)

        registerButton.setOnClickListener{
//            if((txtPassword.text.toString().isNotEmpty())
//                && (txtConfirmPassword.text.toString().isNotEmpty())
//                && txtPassword.text.toString() == txtConfirmPassword.text.toString()){
//
//                val options = AuthSignUpOptions.builder()
//                    .userAttribute(AuthUserAttributeKey.email(), txtEmail.text.toString())
//                    .build()
//                Amplify.Auth.signUp(txtEmail.text.toString(), txtPassword.text.toString(), options,
//                    { Log.i("SMP", "Sign Up Error: $it") },
//                    { Log.e("SMP", "Sign Up Error:", it) }
//                )
////                startActivity(Intent(this, RegisterActivity::class.java))
//            }
            startActivity(Intent(this, ACMainActivity::class.java))


        }

        loginButton.setOnClickListener{
            finish()
        }

    }

}