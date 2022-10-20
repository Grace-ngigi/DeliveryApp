package com.sais.deliveryapp.activities

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.sais.deliveryapp.R
import com.sais.deliveryapp.adapters.ProfileItemAdapter
import com.sais.deliveryapp.databinding.ActivityItemsBinding
import com.sais.deliveryapp.frebase.FireStore
import com.sais.deliveryapp.models.Business
import com.sais.deliveryapp.models.Item
import com.sais.deliveryapp.utils.Constants

class ItemsActivity : BaseActivity() {

	lateinit var binding: ActivityItemsBinding
	private var businessInfo : Business? = null
	private lateinit var db: FirebaseFirestore

	override fun onCreate(savedInstanceState: Bundle?) {
		binding = ActivityItemsBinding.inflate(layoutInflater)
		super.onCreate(savedInstanceState)
		setContentView(binding.root)

		db = FirebaseFirestore.getInstance()

		if (intent.hasExtra(Constants.EXTRA_BUSINESS)) {
			businessInfo = intent.getParcelableExtra<Business>(Constants.EXTRA_BUSINESS)!! as Business
		}
		if (businessInfo != null){
			showProgressDialog()
			FireStore().fetchItems(this, businessInfo!!)
		}
		binding.fabItems.setOnClickListener {
				val intent = Intent(this, UploadItem::class.java)
				intent.putExtra(Constants.EXTRA_BUSINESS, businessInfo)
				startActivityForResult(intent, Constants.ADD_ITEM_REQUEST)
		}
	}

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		super.onActivityResult(requestCode, resultCode, data)
		if (resultCode == Activity.RESULT_OK &&
			requestCode == Constants.ADD_ITEM_REQUEST ||
				requestCode == Constants.UPDATE_ITEM_REQUEST){
			FireStore().fetchItems(this,businessInfo!!)
		}
	}

		 fun itemsDetails(items: ArrayList<Item>) {
		 hideProgressDialog()
		 if (items.size > 0){
			 binding.rvUploadedItems.visibility= View.VISIBLE
			 binding.tvNoItems.visibility = View.INVISIBLE

			 val itemsAdapter = ProfileItemAdapter(this, items)
			 binding.rvUploadedItems.adapter = itemsAdapter
			 binding.rvUploadedItems.layoutManager =
			LinearLayoutManager(this, LinearLayoutManager.VERTICAL,
				false)

			 itemsAdapter.setOnclickListener(object: ProfileItemAdapter.ProfileItemClickLister{
				 override fun onClick(view: View, item: Item) {
					 when (view.id) {
						 R.id.btEdit -> {
								 val intent = Intent(this@ItemsActivity, UploadItem::class.java)
								 intent.putExtra(Constants.EXTRA_ITEM_INFO, item)
								 startActivityForResult(intent, Constants.UPDATE_ITEM_REQUEST)
						 }
						 R.id.btDelete ->{
							 val builder = AlertDialog.Builder(this@ItemsActivity)
							 builder.setTitle("Deleting ${item.title}")
							 builder.setMessage(R.string.delete_confirmation)
							 builder.setPositiveButton(R.string.delete) { _, _ ->
								 showProgressDialog()
								 FireStore().deleteItem(this@ItemsActivity, item)
							 }

							 builder.setNegativeButton(R.string.no) { _, _ ->
								 showToast("No")
							 }
							 builder.show()
						 }
					 }
				 }
			 })

		 } else{
		binding.rvUploadedItems.visibility= View.INVISIBLE
			 binding.tvNoItems.visibility = View.VISIBLE
		 }
	 }

	fun deleteSuccess(){
		hideProgressDialog()
		showToast("Deleted")
		FireStore().fetchItems(this, businessInfo!!)
	}
}