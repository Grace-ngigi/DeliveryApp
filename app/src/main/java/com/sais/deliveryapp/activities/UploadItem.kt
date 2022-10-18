package com.sais.deliveryapp.activities

import android.R
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.webkit.PermissionRequest
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.sais.deliveryapp.databinding.ActivityUploadItemBinding
import com.sais.deliveryapp.frebase.FireStore
import com.sais.deliveryapp.models.Business
import com.sais.deliveryapp.models.Item
import com.sais.deliveryapp.models.Items
import com.sais.deliveryapp.utils.Constants
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
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

		if (intent.hasExtra(Constants.EXTRA_BUSINESS_INFO)) {
			business = intent.getParcelableExtra<Business>(Constants.EXTRA_BUSINESS_INFO)!!
		}

		if (item != null){
			Log.e("quantity", item!!.toString())
			bsId = item!!.bsId
			bsName = item!!.bsName
			title = item!!.title
			price = item!!.price.toString()
			quantity = item!!.quantity
			description = item!!.description
			category = item!!.category
		}

		categoryText()
		binding.btSubmit.setOnClickListener { uploadItems() }
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
				category!!
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
			else -> {
				true
			}
		}
	}

	fun saveItemSuccess() {
		hideProgressDialog()
		showToast("Item Uploaded Successfully")
		startActivity(Intent(this, ItemsActivity::class.java))
		finish()
	}

	private fun categoryText() {
		val catItems = Constants.category
		val catAdapter = ArrayAdapter(
			this,
			R.layout.simple_spinner_dropdown_item,
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
}