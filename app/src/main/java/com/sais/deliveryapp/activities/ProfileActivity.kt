package com.sais.deliveryapp.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.sais.deliveryapp.adapters.ProfileItemAdapter
import com.sais.deliveryapp.databinding.ActivityProfileBinding
import com.sais.deliveryapp.frebase.FireStore
import com.sais.deliveryapp.models.Business
import com.sais.deliveryapp.models.Item
import com.sais.deliveryapp.models.Items
import com.sais.deliveryapp.utils.Constants

class ProfileActivity : BaseActivity() {
	lateinit var binding: ActivityProfileBinding
	private var businessInfo : Business? = null
	private lateinit var db: FirebaseFirestore

	override fun onCreate(savedInstanceState: Bundle?) {
		binding = ActivityProfileBinding.inflate(layoutInflater)
		super.onCreate(savedInstanceState)
		setContentView(binding.root)
		db = FirebaseFirestore.getInstance()

		FireStore().fetchBusiness(this)

		binding.ivProfile.setOnClickListener { startActivity(Intent(this, MainActivity::class.java)) }
		binding.btCreateProfile.setOnClickListener { startActivity(Intent(this, RegisterBusiness::class.java)) }
		binding.fabUploadItem.setOnClickListener {
			val intent = Intent(this, UploadItem::class.java)
			intent.putExtra(Constants.EXTRA_BUSINESS_INFO, businessInfo)
			startActivity(intent)
		}
	}

	fun businessDetails(business: ArrayList<Business>){
		if (business.size > 0){
			binding.clNoBusinessProfile.visibility = View.GONE
			binding.clBusinessProfile.visibility = View.VISIBLE
			business.forEach {
				businessInfo = it
				binding.tvShop.text = it.name
				binding.tvBsType.text = it.type
				binding.tvLocation.text = it.location
				binding.tvTillNo.text = it.till.toString()
			}
			FireStore().fetchItems(this, businessInfo!!)
		} else {
			binding.clBusinessProfile.visibility = View.GONE
			binding.clNoBusinessProfile.visibility = View.VISIBLE
		}

	}

	 fun itemsDetails(items: ArrayList<Item>) {
		 if (items.size > 0){
			 binding.rvUploadedItems.visibility= View.VISIBLE
			 binding.tvNoItems.visibility = View.GONE

		binding.rvUploadedItems.layoutManager =
			LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,
				false)

		val itemsAdapter = ProfileItemAdapter(this, items)
		binding.rvUploadedItems.adapter = itemsAdapter

//		items.forEach {
//			val id = it.id
//			val title = it.title
//			val shop = it.bsName
//			val price = it.price
//			val quantity = it.quantity
//			val desc = it.description
//		}
		 } else{
		binding.rvUploadedItems.visibility= View.GONE
			 binding.tvNoItems.visibility = View.VISIBLE
		 }
	 }
}