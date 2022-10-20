package com.sais.deliveryapp.logins

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.sais.deliveryapp.activities.BaseActivity
import com.sais.deliveryapp.databinding.ActivityRegisterBinding
import com.sais.deliveryapp.frebase.FireStore
import com.sais.deliveryapp.models.Retailer
import com.sais.deliveryapp.utils.Constants

class RegisterActivity : BaseActivity() {

	lateinit var binding: ActivityRegisterBinding
	private val auth = FirebaseAuth.getInstance()
	private var retailer: Retailer? = null
	lateinit var email: String
	lateinit var password: String
	lateinit var confirmPassword: String
	lateinit var firebaseUser: FirebaseUser

	override fun onCreate(savedInstanceState: Bundle?) {
		binding = ActivityRegisterBinding.inflate(layoutInflater)
		super.onCreate(savedInstanceState)
		setContentView(binding.root)


		binding.tvLogin.setOnClickListener {
			startActivity(
				Intent(
					this,
					LoginActivity::class.java
				)
			)
		}
		binding.btRegister.setOnClickListener {
			if (checkInternetConnectivity()){
				registerRetailer()
			}
		}
	}


	private fun registerRetailer() {
		email = binding.tetEmail.text.toString().trim { it <= ' ' }
		password  = binding.tetPassword.text.toString()
		confirmPassword  = binding.tetConfirmPassword.text.toString()
		if (validateForm(email, password, confirmPassword)) {

			if (password != confirmPassword) {
				showErrorSnackBar("Passwords Do not Match!")
			} else {
			showProgressDialog()
				auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
					if (task.isSuccessful) {
						FireStore().sendEmailVerification(this)
//						firebaseUser = task.result!!.user!!
					} else {
					hideProgressDialog()
						task.exception!!.message?.let { showErrorSnackBar(it) }
					}

				}
			}
		}

	}

	private fun validateForm(email: String, password: String, confirmPassword: String): Boolean {
		return when {
			TextUtils.isEmpty(email) -> {
				showErrorSnackBar("Please Enter your Email Address")
				false
			}
			TextUtils.isEmpty(password) -> {
				showErrorSnackBar("Please Enter your Password")
				false
			}
			TextUtils.isEmpty(confirmPassword) -> {
				showErrorSnackBar("Please Confirm your Password")
				false
			}
			else -> {
				true
			}
		}
	}

	fun sendEmailVerificationSuccess() {
		hideProgressDialog()
//		saveRetailerToDb()
		auth.signOut()
		val intent = Intent(this, LoginActivity::class.java)
		intent.putExtra(Constants.EXTRA_RETAILER_INFO, retailer)
		startActivity(intent)
		finish()
	}

//	private fun saveRetailerToDb() {
//		val registeredEmail = auth.currentUser?.email!!
//		retailer= Retailer(auth.currentUser!!.uid, registeredEmail)
//		FireStore().saveRetailerInfo(this, retailer!!)
//	}

	fun saveRetailerSuccess(){
		hideProgressDialog()
		showToast("Registered Successfully")
		startActivity(Intent(this, LoginActivity::class.java))
	}

	fun saveRetailerFailure(){
		hideProgressDialog()
		showToast("Error Saving Retailer, Please ")
	}
}