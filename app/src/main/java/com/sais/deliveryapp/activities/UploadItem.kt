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
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
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
import com.sais.deliveryapp.databinding.ActivityUploadItemBinding
import com.sais.deliveryapp.frebase.FireStore
import com.sais.deliveryapp.models.Business
import com.sais.deliveryapp.models.Item
import com.sais.deliveryapp.utils.Constants
import java.io.IOException
import java.util.*

class UploadItem : BaseActivity() {

	lateinit var binding: ActivityUploadItemBinding
	private lateinit var db: FirebaseFirestore
	private var title: String? = null
	private var bsName: String? = null
	private var bsId: String? = null
	private var category: String? = null
	private var quantity: String? = null
	private var description: String? = null
	private var price: String? = null
	private var mSelectedImageUri: Uri? = null
	private var mItemImage: String = ""
	private var item: Item? = null
	private var business: Business? = null

	override fun onCreate(savedInstanceState: Bundle?) {
		binding = ActivityUploadItemBinding.inflate(layoutInflater)
		super.onCreate(savedInstanceState)
		setContentView(binding.root)
		db = FirebaseFirestore.getInstance()

		if (intent.hasExtra(Constants.EXTRA_ITEM_INFO)) {
			item = intent.getParcelableExtra<Item>(Constants.EXTRA_ITEM_INFO)!!
		}

		if (intent.hasExtra(Constants.EXTRA_BUSINESS)) {
			business = intent.getParcelableExtra<Business>(Constants.EXTRA_BUSINESS)!!
		}

		if (item != null){
			binding.tetTitle.setText(item!!.title)
			binding.tetQuantity.setText(item!!.quantity)
			binding.tetBsDesc.setText(item!!.description)
			binding.tetBsPrice.setText(item!!.price.toString())
			loadCategory()
			binding.btItemUpdate.visibility = View.VISIBLE
			binding.btItemSubmit.visibility = View.GONE
		} else{
			categoryText()
		}
		binding.btItemSubmit.setOnClickListener { if (checkInternetConnectivity()){uploadItems() }}

		binding.btItemUpdate.setOnClickListener { if (checkInternetConnectivity()){updateItemHashMap() }}

		binding.tvUploadPhoto.setOnClickListener { chooseItemImage() }
	}

	private fun uploadItems() {
		val id = db.collection(Constants.ITEMS).document().id
		bsId = business!!.id
		bsName = business!!.bsName
		title = binding.tetTitle.text.toString()
		quantity = binding.tetQuantity.text.toString()
		description = binding.tetBsDesc.text.toString()
		price = binding.tetBsPrice.text.toString()

		if (validateInput(title!!, quantity!!, description!!, price!!)) {
			showProgressDialog()
			val item = Item(
				id,
				bsId!!,
				title!!,
				bsName!!,
				price!!.toInt(),
				quantity!!,
				description!!,
				category!!,
				mItemImage
			)
			FireStore().saveItem(this, item)
		}
	}

	private fun validateInput(
		title: String,
		quantity: String,
		desc: String,
		price: String
	): Boolean {
		return when {
			TextUtils.isEmpty(title) -> {
				showErrorSnackBar("Please Enter Item's Title")
				false
			}
			TextUtils.isEmpty(quantity) -> {
				showErrorSnackBar("Please Enter Item's Quantity")
				false
			}
			desc.isEmpty() -> {
				showErrorSnackBar("Please Enter Item's Description")
				false
			}
			price.isEmpty() -> {
				showErrorSnackBar("Please Enter Item's Price")
				false
			}
			mItemImage.isEmpty() ->{
				showErrorSnackBar("Please Upload Item's Photo")
				false
			}
			else -> {
				true
			}
		}
	}

	fun saveItemSuccess() {
		hideProgressDialog()
		showToast("Item Uploaded Successfully")
		setResult(Activity.RESULT_OK)
		finish()
	}

	private fun updateItemHashMap(){
		val itemHashMap = HashMap<String, Any>()
		var anyChangesMade = false
		if (binding.tetTitle.text.toString() != item!!.title){
			itemHashMap["title"] = binding.tetTitle.text.toString()
			anyChangesMade = true
		}
		if (binding.tetBsDesc.text.toString() != item!!.description){
			itemHashMap["description"] = binding.tetBsDesc.text.toString()
			anyChangesMade = true
		}
		if (binding.tetQuantity.text.toString() != item!!.quantity){
			itemHashMap["quantity"] = binding.tetQuantity.text.toString()
			anyChangesMade = true
		}
		if (binding.tetBsPrice.text.toString() != item!!.price.toString()){
			itemHashMap["price"] = binding.tetBsPrice.text.toString().toInt()
			anyChangesMade = true
		}
		if (mItemImage != item!!.image){
			itemHashMap["image"] = mItemImage
			anyChangesMade = true
		}
		if (category != item!!.category){
			itemHashMap["category"] = category!!
			anyChangesMade = true
		}
		if (anyChangesMade) {
			showProgressDialog()
			FireStore().updateItem(this, itemHashMap, item!!)
		}
		else{
			Toast.makeText(this, "No changes Made", Toast.LENGTH_SHORT).show()
			finish()
		}
	}

	fun updateItemSuccess(){
		hideProgressDialog()
		setResult(Activity.RESULT_OK)
		finish()
	}

	private fun categoryText() {
		val catItems = Constants.category
		val catAdapter = ArrayAdapter(
			this,
			androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
			catItems
		)
		binding.spCategory.adapter = catAdapter
		binding.spCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
			override fun onItemSelected(
				parent: AdapterView<*>?,
				view: View?,
				position: Int,
				id: Long
			) {
				category = catItems[position]
			}
			override fun onNothingSelected(p0: AdapterView<*>?) {
				TODO("Not yet implemented")
			}
		}
	}

	private fun loadCategory(){
		val currentCategory = item!!.category
		val itemCategory = Constants.category
		val categoryAdapter = ArrayAdapter(
			this,
			android.R.layout.simple_spinner_dropdown_item,
			itemCategory
		)
		binding.spCategory.adapter = categoryAdapter
		binding.spCategory.setSelection(categoryAdapter.getPosition(currentCategory))
		binding.spCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
			override fun onItemSelected(
				parent: AdapterView<*>?,
				view: View?,
				position: Int,
				id: Long
			) {
				category = itemCategory[position]
			}

			override fun onNothingSelected(p0: AdapterView<*>?) {
				TODO("Not yet implemented")
			}
		}
	}

	private fun uploadItemImage() {
		if (mSelectedImageUri != null) {
			val ref: StorageReference = FirebaseStorage.getInstance()
				.reference.child(
					"ITEM_LOGO" + System.currentTimeMillis()
							+ "." + Constants.getFileExtension(this, mSelectedImageUri)
				)
			ref.putFile(mSelectedImageUri!!).addOnSuccessListener { snapshot ->
				snapshot.metadata!!.reference!!.downloadUrl.addOnSuccessListener { uri ->
					Toast.makeText(this, "Update Success", Toast.LENGTH_SHORT).show()
					mItemImage = uri.toString()
//					hideProgressDialog()
				}
			}.addOnFailureListener { e ->
				Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
//				hideProgressDialog()
			}
		}
	}

	private fun chooseItemImage() {
		Dexter.withActivity(this)
			.withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE)
			.withListener(object : MultiplePermissionsListener {
				override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
					if (report!!.areAllPermissionsGranted()){
						val galleryIntent = Intent(Intent.ACTION_PICK,
							MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
						startActivityForResult(galleryIntent, Constants.ITEM_GALLERY)
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
		if (resultCode == Activity.RESULT_OK && requestCode == Constants.ITEM_GALLERY
			&& data!!.data !=null) {
			if (checkInternetConnectivity()) {
				mSelectedImageUri = data.data
				uploadItemImage()
				try {
					Glide.with(this@UploadItem)
						.load(mSelectedImageUri)
						.centerCrop()
						.placeholder(com.sais.deliveryapp.R.drawable.ic_view_list)
						.into(binding.ivDisplayPhoto)
				} catch (e: IOException){
					e.printStackTrace()
				}
			}
		}
	}
}