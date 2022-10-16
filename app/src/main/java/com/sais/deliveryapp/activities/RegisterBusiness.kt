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
	private var bsTill: Int? = 0
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
		val owner = FireStore().getCurrentUserId()
		bsName = binding.teBsName.text.toString()
		bsType = binding.tetBsType.text.toString()
		location = binding.tetBsLocation.text.toString()
		bsTill= binding.tetBsTill.text.toString().toInt()

		if (validateInput(bsName!!, bsType!!,location!!, bsTill!!)){
			showProgressDialog()
			val business = Business(owner, bsName!!, bsType!!, location!!, bsTill!!, id)
			FireStore().saveBsProfile(this, business)
		}
	}

	private fun validateInput(name: String, type: String, location: String, till:Int): Boolean {
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
			TextUtils.isEmpty(till.toString()) -> {
				showErrorSnackBar("Please Your Mpesa Till Number")
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

