package com.sais.deliveryapp.activities

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.sais.deliveryapp.R
import com.sais.deliveryapp.databinding.ActivityRegisterBusinessBinding
import com.sais.deliveryapp.frebase.FireStore
import com.sais.deliveryapp.models.Business
import com.sais.deliveryapp.models.Retailer
import com.sais.deliveryapp.utils.Constants
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

class RegisterBusiness : BaseActivity() {
	lateinit var binding: ActivityRegisterBusinessBinding
	private var bsName: String? = null
	private var bsType: String? = null
	private var location: String? = null
	private var bsTill: String? = null
	private var bsOwner: String? = null
	private var contact: String? = null
	private var mSelectedImageUri: Uri? = null
	private var business: Business? = null
	private var retailer: Retailer? = null
	private var mBsLogoUri: String = ""
	private lateinit var db: FirebaseFirestore

	override fun onCreate(savedInstanceState: Bundle?) {
		binding = ActivityRegisterBusinessBinding.inflate(layoutInflater)
		super.onCreate(savedInstanceState)
		setContentView(binding.root)

		db = FirebaseFirestore.getInstance()

		binding.btUpdate.setOnClickListener { if (checkInternetConnectivity()){ updateBusinessHashMap() }}

		binding.ivUploadLogo.setOnClickListener { chooseBsLogo() }

		if (intent.hasExtra(Constants.EXTRA_BUSINESS)){
			business = intent.getParcelableExtra<Business>(Constants.EXTRA_BUSINESS)!! as Business
			}

		if (intent.hasExtra(Constants.EXTRA_RETAILER_INFO)){
			retailer = intent.getParcelableExtra<Retailer>(Constants.EXTRA_RETAILER_INFO)!! as Retailer
		}

		if (business != null){
			binding.teBsName.setText(business!!.bsName)
			binding.tetBsType.setText(business!!.type)
			binding.tetBsTill.setText(business!!.till.toString())
			binding.tetBsLocation.setText(business!!.location)
			mBsLogoUri = business!!.bsLogo
			Glide.with(this)
				.load(business!!.bsLogo)
				.centerCrop()
				.into(binding.ivDisplayLogo)
			binding.btSubmit.visibility = View.GONE
			binding.btUpdate.visibility = View.VISIBLE
			binding.tilBsContact.visibility = View.GONE
			binding.tilBsOwner.visibility = View.GONE
		}

		if(retailer != null){
			binding.tilBsContact.visibility = View.GONE
			binding.tilBsOwner.visibility = View.GONE
		}

		binding.btSubmit.setOnClickListener {
			if (checkInternetConnectivity()){
				if(retailer != null){
					addBusiness()
					binding.tilBsContact.visibility = View.GONE
					binding.tilBsOwner.visibility = View.GONE
				}else {
					registerBsProfile()
				}
			}
		}
	}

	private fun registerBsProfile() {
		val retailerId = FireStore().getCurrentUserId()
		val id = db.collection(Constants.BUSINESS).document().id
		bsName = binding.teBsName.text.toString()
		bsType = binding.tetBsType.text.toString()
		location = binding.tetBsLocation.text.toString()
		bsTill = binding.tetBsTill.text.toString()
		contact= binding.tetBsContact.text.toString()
		bsOwner = binding.tetBsOwner.text.toString()

		if (validateInput(bsName!!, bsType!!, location!!, bsTill!!, contact!!, bsOwner!!)) {
			showProgressDialog()
			val bsInfo = Business(retailerId, bsName!!,bsType!!,location!!, bsTill!!.toInt(),mBsLogoUri,id)
			val retailer = Retailer(
				retailerId,
				contact!!.toInt(),
				bsOwner!!
			)
			FireStore().saveBusiness(this,bsInfo)
			FireStore().saveRetailer(this, retailer)
		}
	}

	private fun addBusiness() {
		val id = db.collection(Constants.BUSINESS).document().id
		bsName = binding.teBsName.text.toString()
		bsType = binding.tetBsType.text.toString()
		location = binding.tetBsLocation.text.toString()
		bsTill = binding.tetBsTill.text.toString()

		when {
			bsName!!.isEmpty() -> {
				showErrorSnackBar("PLease Input Business Name")
			}
			bsType!!.isEmpty() -> {
				showErrorSnackBar("PLease Input Business Type")
			}
			location!!.isEmpty() -> {
				showErrorSnackBar("PLease Input Business Location")
			}
			bsTill!!.isEmpty() -> {
				showErrorSnackBar("PLease Input Business Mpesa Till Number")
			}
			mBsLogoUri.isEmpty() -> {
				showErrorSnackBar("Please Upload Business Logo")
			}
			else -> {
				showProgressDialog()
				val business = Business(
					retailer!!.id,
					bsName!!,
					bsType!!,
					location!!,
					bsTill!!.toInt(),
					mBsLogoUri,
					id
				)
				FireStore().addBusiness(this, business)
			}
		}
	}

	private fun validateInput(
		name: String, type: String,
		location: String, till: String,
		bsOwner: String, contact: String
	): Boolean {
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
			mBsLogoUri.isEmpty() -> {
				showErrorSnackBar("Please Upload Business Logo")
				false
			}
			else -> {
				true
			}
		}
	}

	fun addBusinessSuccess() {
		hideProgressDialog()
		showToast("Business Added")
		setResult(Activity.RESULT_OK)
		finish()
	}

	private fun updateBusinessHashMap(){
		val businessHashMap = HashMap<String, Any>()
		var anyChangesMade = false
		if (binding.teBsName.text.toString() != business!!.bsName){
			businessHashMap["bsName"] = binding.teBsName.text.toString()
			anyChangesMade = true
		}
		if (binding.tetBsType.text.toString() != business!!.type){
			businessHashMap["type"] = binding.tetBsType.text.toString()
			anyChangesMade = true
		}
		if (binding.tetBsLocation.text.toString() != business!!.location){
			businessHashMap["location"] = binding.tetBsLocation.text.toString()
			anyChangesMade = true
		}
		if (binding.tetBsTill.text.toString() != business!!.till.toString()){
			businessHashMap["till"] = binding.tetBsTill.text.toString().toInt()
			anyChangesMade = true
		}
		if (mBsLogoUri != business!!.bsLogo){
			businessHashMap["bsLogo"] = mBsLogoUri
			anyChangesMade = true
		}
		if (anyChangesMade) {
			showProgressDialog()
			FireStore().updateBusiness(this, businessHashMap, business!!)
		}
		else{
			Toast.makeText(this, "No changes Made", Toast.LENGTH_SHORT).show()
			finish()
		}
	}

	fun updateBsSuccess(){
		hideProgressDialog()
		setResult(Activity.RESULT_OK)
		finish()
	}

	private fun uploadBsLogo() {
		if (mSelectedImageUri != null) {
			val ref: StorageReference = FirebaseStorage.getInstance()
				.reference.child(
					"BS_LOGO" + System.currentTimeMillis()
							+ "." + Constants.getFileExtension(this, mSelectedImageUri)
				)
			ref.putFile(mSelectedImageUri!!).addOnSuccessListener { snapshot ->
				Log.i("Firebase", snapshot.metadata!!.reference!!.downloadUrl.toString())
				snapshot.metadata!!.reference!!.downloadUrl.addOnSuccessListener { uri ->
					Toast.makeText(this, "Update Success", Toast.LENGTH_SHORT).show()
					Log.i("Downloadable Image", uri.toString())
					mBsLogoUri = uri.toString()
//					hideProgressDialog()
				}
			}.addOnFailureListener { e ->
				Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
//				hideProgressDialog()
			}
		}
	}

	private fun chooseBsLogo() {
		Dexter.withActivity(this)
			.withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE)
			.withListener(object : MultiplePermissionsListener {
				override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
					if (report!!.areAllPermissionsGranted()){
						val galleryIntent = Intent(Intent.ACTION_PICK,
							MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
						startActivityForResult(galleryIntent, Constants.LOGO_GALLERY)
					}
				}

				override fun onPermissionRationaleShouldBeShown(
					permissions: MutableList<PermissionRequest>?,
					token: PermissionToken?
				) {
					token?.continuePermissionRequest()
					showRationalDialogForPermissions()
				}
			}).onSameThread().check()
	}

	fun showRationalDialogForPermissions() {
		AlertDialog.Builder(this).setMessage("" + "Delivery App requires to access your gallery")
			.setPositiveButton("Go to Settings") { _, _ ->
				try {
					val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
					val uri = Uri.fromParts("package", packageName, null)
					intent.data = uri
					startActivity(intent)
				} catch (e: ActivityNotFoundException) {
					e.printStackTrace()
				}
			}.setNegativeButton("Cancel") { dialog, _ ->
				dialog.dismiss()
			}.show()
	}

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		super.onActivityResult(requestCode, resultCode, data)
		if (resultCode == Activity.RESULT_OK && requestCode == Constants.LOGO_GALLERY
			&& data!!.data !=null) {
				if (checkInternetConnectivity()) {
					mSelectedImageUri = data.data
					uploadBsLogo()
					try {
						Glide.with(this@RegisterBusiness)
							.load(mSelectedImageUri)
							.centerCrop()
							.placeholder(R.drawable.ic_view_list)
							.into(binding.ivDisplayLogo)
					} catch (e: IOException){
						e.printStackTrace()
					}
				}
			}
		}
}

