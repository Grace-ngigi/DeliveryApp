package com.sais.deliveryapp.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.sais.deliveryapp.R
import com.sais.deliveryapp.activities.ItemsActivity
import com.sais.deliveryapp.activities.RegisterBusiness
import com.sais.deliveryapp.adapters.BusinessAdapter
import com.sais.deliveryapp.databinding.FragmentAccountBinding
import com.sais.deliveryapp.models.Business
import com.sais.deliveryapp.utils.Constants

class AccountFragment : Fragment(R.layout.fragment_account) {
	lateinit var binding: FragmentAccountBinding
	private var businessInfo : Business? = null
	private lateinit var db: FirebaseFirestore

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		val accountBinding = FragmentAccountBinding.bind(view)
		binding = accountBinding
		binding.btAddBs.setOnClickListener {
			startActivity(Intent(context, RegisterBusiness::class.java))
		}

		ViewModel().fetchBusiness(this)
	}
	fun businessDetails(business: ArrayList<Business>){
//		hideProgressDialog()
		if (business.size > 0){
			binding.clNoBusinessProfile.visibility = View.INVISIBLE
			binding.clBusinessProfile.visibility = View.VISIBLE

			binding.rvBusinessList.layoutManager =
				LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
			val itemAdapter = context?.let { BusinessAdapter(it, business) }
			binding.rvBusinessList.adapter = itemAdapter

			itemAdapter?.setOnClickListener(object : BusinessAdapter.BusinessOnClickListener{
				override fun onClick(view: View, item: Business) {
					val intent = Intent(context, ItemsActivity::class.java)
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

