package com.sais.deliveryapp.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.sais.deliveryapp.adapters.BusinessAdapter
import com.sais.deliveryapp.adapters.ItemAdapter
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

		showProgressDialog()
		FireStore().fetchBusiness(this)

		binding.ivProfile.setOnClickListener {
			FirebaseAuth.getInstance().signOut()
			val intent = Intent(this, MainActivity::class.java)
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
			startActivity(intent)
			finish()
		}
		binding.btCreateProfile.setOnClickListener {
			startActivity(
				Intent(
					this,
					RegisterBusiness::class.java
				)
			)
		}

		binding.tvUpdate.setOnClickListener { startActivity(Intent(this, RegisterBusiness::class.java)) }

		binding.btAddBs.setOnClickListener {
			startActivity(
				Intent(
					this,
					RegisterBusiness::class.java
				)
			)
		}
//		binding.btGoToItems.setOnClickListener {
//			val intent = Intent(this, ItemsActivity::class.java)
//			intent.putExtra(Constants.EXTRA_BUSINESS_INFO, businessInfo)
//			startActivity(intent)
//		}
	}

	fun businessDetails(business: ArrayList<Business>){
		hideProgressDialog()
		if (business.size > 0){
			binding.clNoBusinessProfile.visibility = View.INVISIBLE
			binding.clBusinessProfile.visibility = View.VISIBLE

			binding.rvBusinessList.layoutManager =
				LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
			val itemAdapter = BusinessAdapter(this, business)
			binding.rvBusinessList.adapter = itemAdapter

			itemAdapter.setOnClickListener(object : BusinessAdapter.BusinessOnClickListener{
				override fun onClick(view: View, item: Business) {
					val intent = Intent(this@ProfileActivity, ItemsActivity::class.java)
					intent.putExtra(Constants.EXTRA_BUSINESS_INFO, item)
					startActivity(intent)				}
			})
				business.forEach {
				businessInfo = it
				binding.tvOwnerName.text = it.bsOwner
				binding.tvContact.text = it.contact.toString()
			}
		} else {
			binding.clBusinessProfile.visibility = View.INVISIBLE
			binding.clNoBusinessProfile.visibility = View.VISIBLE
		}

	}
}