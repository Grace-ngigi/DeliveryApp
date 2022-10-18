package com.sais.deliveryapp.activities

import android.os.Bundle
import android.text.TextUtils
import com.google.firebase.firestore.FirebaseFirestore
import com.sais.deliveryapp.databinding.ActivityRegisterBusinessBinding
import com.sais.deliveryapp.frebase.FireStore
import com.sais.deliveryapp.models.Business
import com.sais.deliveryapp.utils.Constants

class RegisterBusiness : BaseActivity() {
	lateinit var binding: ActivityRegisterBusinessBinding
	private var bsName: String? = null
	private var bsType: String? = null
	private var location: String? = null
	private var bsTill: String? = null
	private var  bsOwner: String? = null
	private var contact: String? = null
	private var businessList = ArrayList<Business>()
	lateinit var db: FirebaseFirestore

	override fun onCreate(savedInstanceState: Bundle?) {
		binding = ActivityRegisterBusinessBinding.inflate(layoutInflater)
		super.onCreate(savedInstanceState)
		setContentView(binding.root)

		db= FirebaseFirestore.getInstance()

		binding.btSubmit.setOnClickListener { registerBsProfile() }
	}

	private fun registerBsProfile() {
		val id = db .collection(Constants.BUSINESS).document().id
		val retailerId = FireStore().getCurrentUserId()
		bsName = binding.teBsName.text.toString()
		bsType = binding.tetBsType.text.toString()
		location = binding.tetBsLocation.text.toString()
		bsTill= binding.tetBsTill.text.toString()
		bsOwner = binding.tetBsOwner.text.toString()
		contact = binding.tetBsContact.text.toString()


		if (validateInput(bsName!!, bsType!!,location!!, bsTill!!, bsOwner!!, contact!!)){
			showProgressDialog()
			val business = Business(retailerId, bsName!!, bsType!!, location!!, bsTill!!.toInt(), bsOwner!!, contact!!.toInt(), id)
			FireStore().saveBsProfile(this, business)
		}
	}

	private fun validateInput(name: String, type: String, 
	                          location: String, till:String, 
	                          bsOwner: String, contact: String): Boolean {
		return when {
			TextUtils.isEmpty(name) -> {
				showErrorSnackBar("Please Enter your Business Name")
				false
			}
			TextUtils.isEmpty(type) -> {
				showErrorSnackBar("Please Enter your Business type")
				false
			}
			TextUtils.isEmpty(location) -> {
				showErrorSnackBar("Please your Business Location")
				false
			}
			TextUtils.isEmpty(till) -> {
				showErrorSnackBar("Please Your Mpesa Till Number")
				false
			}
			TextUtils.isEmpty(bsOwner) -> {
				showErrorSnackBar("Please Your Name")
				false
			}
			TextUtils.isEmpty(contact) -> {
				showErrorSnackBar("Please Your Phone Number")
				false
			}
			else -> {
				true
			}
		}
	}

	fun saveBusinessSuccess(){
		hideProgressDialog()
		showToast("Business Profile Created")
		finish()
	}
}

