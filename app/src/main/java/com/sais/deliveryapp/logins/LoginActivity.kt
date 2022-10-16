package com.sais.deliveryapp.logins

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.sais.deliveryapp.activities.BaseActivity
import com.sais.deliveryapp.activities.ProfileActivity
import com.sais.deliveryapp.databinding.ActivityLoginBinding
import com.sais.deliveryapp.frebase.FireStore
import com.sais.deliveryapp.models.Retailer

class LoginActivity : BaseActivity() {
	lateinit var binding: ActivityLoginBinding
	lateinit var email: String
	lateinit var password: String
	private lateinit var auth: FirebaseAuth
	private var retailer: Retailer? = null

	override fun onCreate(savedInstanceState: Bundle?) {
		binding = ActivityLoginBinding.inflate(layoutInflater)
		super.onCreate(savedInstanceState)
		setContentView(binding.root)

		auth = FirebaseAuth.getInstance()
		binding.tvLogin.setOnClickListener { startActivity(Intent(this, RegisterActivity::class.java)) }
		binding.btLogin.setOnClickListener {
			if (checkInternetConnectivity()){
				loginRetailer()
			}
		}

	}

	private fun loginRetailer(){
		 email = binding.tetEmail.text.toString().trim{it <= ' '}
		 password = binding.tetPassword.text.toString()

		//TODO:Save Users Credentials in Firebase

		if (validateForm(email,password)){
			showProgressDialog()
			auth.signInWithEmailAndPassword(email,password)
				.addOnCompleteListener { task ->
					hideProgressDialog()
					if (task.isSuccessful) {
						FireStore().checkEmailVerified(this)
					} else {
						showErrorSnackBar(task.exception.toString())
					}
				}
				.addOnFailureListener {
					hideProgressDialog()
					showErrorSnackBar(it.message.toString())
				}
		}

	}

	private fun validateForm(email:String,password: String): Boolean{
		return when {
			TextUtils.isEmpty(email) ->{
				showErrorSnackBar("Please Enter your Email Address")
				false
			}
			TextUtils.isEmpty(password) ->{
				showErrorSnackBar("Please Enter your Password")
				false
			}
			else -> { true }
		}
	}

	fun emailVerifiedSuccess(){
		hideProgressDialog()
		startActivity(Intent(this, ProfileActivity::class.java))
		finish()
		Toast.makeText(this,"Signed In Successfully", Toast.LENGTH_SHORT).show()
	}

	fun emailVerifiedFailure(){
		hideProgressDialog()
		FirebaseAuth.getInstance().signOut();
		startActivity(Intent(this, LoginActivity::class.java))
		Toast.makeText(this,"Email not Verified. Please Verify from the Email Sent.", Toast.LENGTH_LONG).show()
	}
}