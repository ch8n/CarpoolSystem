package com.example.carpoolsystem.screens

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.carpoolsystem.R
import com.example.carpoolsystem.utility.RegistrationUtils
import com.google.firebase.auth.FirebaseAuth

class SignInScreen : AppCompatActivity() {
    private lateinit var signUpButton: Button
    private lateinit var otpLoginButton: Button
    private lateinit var forgetPasswordButton: Button
    private lateinit var loginButton: Button
    private lateinit var emailIdEditText: EditText
    private lateinit var passwordEditText: EditText

    private lateinit var progressDialog: ProgressDialog

    private var firebaseAuth: FirebaseAuth? = null

    private val PASSWORD_ERROR =
        "Password should contain at least one upper case letter, lower case letter, number, and special characters(@#\$%^&+=!)"
    private val EMAIL_ID_ERROR = "Enter your GLA Email address"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in_screen)
        signUpButton = findViewById(R.id.buttonSignUp)
        otpLoginButton = findViewById(R.id.buttonOtpLogin)
        forgetPasswordButton = findViewById(R.id.buttonForgetPassword)
        loginButton = findViewById(R.id.buttonLogin)
        emailIdEditText = findViewById(R.id.editTextEnterEmail)
        passwordEditText = findViewById(R.id.editTextEnterPassword)

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Loading")
        progressDialog.setMessage("Please wait...")

        firebaseAuth = FirebaseAuth.getInstance()

        emailIdEditText.addTextChangedListener(
            object : TextWatcher {
                override fun beforeTextChanged(
                    p0: CharSequence?,
                    p1: Int, p2: Int, p3: Int
                ) {
                }

                override fun onTextChanged(
                    p0: CharSequence?,
                    p1: Int, p2: Int, p3: Int
                ) {
                    p0?.apply {
                        if (RegistrationUtils.isValidEmail(p0.toString())) {

                            emailIdEditText.error = null
                        } else {
                            emailIdEditText.error = EMAIL_ID_ERROR
                        }
                    }
                }

                override fun afterTextChanged(p0: Editable?) {}
            })
        passwordEditText.addTextChangedListener(
            object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int, count: Int, after: Int
                ) {

                }


                override fun onTextChanged(
                    s: CharSequence?,
                    start: Int, before: Int, count: Int
                ) {
                    s?.apply {
                        if (RegistrationUtils.isValidPassword(s.toString())) {
                            passwordEditText.error = null

                        } else {
                            passwordEditText.error = PASSWORD_ERROR
                        }
                    }
                }

                override fun afterTextChanged(s: Editable?) {

                }
            })

        signUpButton.setOnClickListener {
            val intent = Intent(this, SignupScreen1::class.java)
            startActivity(intent)
        }

        otpLoginButton.setOnClickListener {
            val intent = Intent(this, SignupScreen2PhoneNumber::class.java)
            startActivity(intent)
        }
        forgetPasswordButton.setOnClickListener {
            val intent = Intent(this, ChangePassword::class.java)
            startActivity(intent)
        }

        loginButton.setOnClickListener {
            progressDialog.show()
            val email = emailIdEditText.text.toString()
            val password = passwordEditText.text.toString()
            if (email.isEmpty() || password.isEmpty()) {
                progressDialog.hide()
                Toast.makeText(this, "Please enter email Id and password", Toast.LENGTH_SHORT)
                    .show()
            } else {
                firebaseAuth?.signInWithEmailAndPassword(email, password)
                    ?.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            if (firebaseAuth?.currentUser?.isEmailVerified == true) {
                                progressDialog.hide()
                                startActivity(Intent(this, Dashboard::class.java))
                            } else {
                                progressDialog.hide()
                                Toast.makeText(
                                    this,
                                    "Please verify your Email Id",
                                    Toast.LENGTH_SHORT
                                ).show()

                            }
                        } else {
                            progressDialog.hide()
                            Toast.makeText(this, "Some error occurred", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
    }

}
