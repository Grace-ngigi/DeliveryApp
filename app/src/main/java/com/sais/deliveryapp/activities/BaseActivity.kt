package com.sais.deliveryapp.activities

import android.app.Dialog
import android.content.Context
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.sais.deliveryapp.R
import com.sais.deliveryapp.databinding.DialogProgessBarBinding

open class BaseActivity : AppCompatActivity() {
	private lateinit var binding:DialogProgessBarBinding

	private var doubleBackToExitPressedOnce = false
	private  lateinit var mProgressDialog: Dialog

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_base)

	}

	fun checkInternetConnectivity(): Boolean{
		var connected = false
		val connectionManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
		val networkInfo = connectionManager.activeNetworkInfo
		if (networkInfo != null && networkInfo.isConnected) {
			connected = true
		} else {
			connected = false
			showErrorSnackBar("Network Unavailable")
		}
		return connected
	}

	fun showProgressDialog(){
//		progressBinding = DialogProgressBinding.inflate(layoutInflater)
		mProgressDialog = Dialog(this)
		mProgressDialog.setCanceledOnTouchOutside(false)
		mProgressDialog.setContentView(R.layout.dialog_progess_bar)
		mProgressDialog.show()
	}

	fun hideProgressDialog(){
		mProgressDialog.dismiss()
	}

//	fun getCurrentUserId(): String{
////		return FirebaseAuth.getInstance().currentUser!!.uid
//	}
	fun doubleBackToExit(){
		if (doubleBackToExitPressedOnce){
			super.onBackPressed()
			return
		}
		this.doubleBackToExitPressedOnce = true
		Toast.makeText(this, resources.getString(R.string.please_click_back_again_to_exit),
			Toast.LENGTH_SHORT).show()

		Handler().postDelayed({doubleBackToExitPressedOnce = false}, 2000)
	}
	fun showErrorSnackBar(message: String){
		val snackBar = Snackbar.make(findViewById(android.R.id.content),
			message, Snackbar.LENGTH_LONG)
		val snackBarView = snackBar.view
		snackBarView.setBackgroundColor(ContextCompat.getColor(this, R.color.snackBar_error_color))
		snackBar.show()
	}
	fun showToast(message: String){
		Toast.makeText(applicationContext,
			message, Toast.LENGTH_LONG).show()
	}
}