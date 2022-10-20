package com.sais.deliveryapp.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.sais.deliveryapp.R
import com.sais.deliveryapp.activities.IntroActivity
import com.sais.deliveryapp.activities.ItemsActivity
import com.sais.deliveryapp.activities.RegisterBusiness
import com.sais.deliveryapp.adapters.BusinessAdapter
import com.sais.deliveryapp.databinding.FragmentAccountBinding
import com.sais.deliveryapp.models.Business
import com.sais.deliveryapp.models.Retailer
import com.sais.deliveryapp.utils.Constants

class AccountFragment : Fragment(R.layout.fragment_account) {
	lateinit var binding: FragmentAccountBinding
	private lateinit var db: FirebaseFirestore
	private var retailer: Retailer? = null
	private var businessInfo: Business? = null

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		binding = FragmentAccountBinding.bind(view)

		binding.btCreateProfile.setOnClickListener {
			val intent = Intent(context, RegisterBusiness::class.java)
		startActivityForResult(intent, Constants.ADD_BS_REQUEST)
		}

		binding.btAddBs.setOnClickListener {
			val intent = Intent(context, RegisterBusiness::class.java)
			intent.putExtra(Constants.EXTRA_RETAILER_INFO, retailer)
			startActivityForResult(intent, Constants.ADD_BS_REQUEST)
		}

		binding.ivProfile.setOnClickListener {
			FirebaseAuth.getInstance().signOut()
			val intent = Intent(context, IntroActivity::class.java)
			startActivity(intent)
		}

		ViewModel().fetchBusiness(this)
		ViewModel().fetchBusinessProfile(this)
	}

	fun bsProfileDetails(bsProfile: ArrayList<Retailer>){
		if (bsProfile.size > 0){
			binding.clNoBusinessProfile.visibility = View.INVISIBLE
			binding.clBusinessProfile.visibility = View.VISIBLE
		bsProfile.forEach {
			retailer = it
			binding.tvOwnerName.text = it.name
			binding.tvContact.text = it.contact.toString()
			}
		} else {
			binding.clBusinessProfile.visibility = View.INVISIBLE
			binding.clNoBusinessProfile.visibility = View.VISIBLE
		}
	}

	fun businessDetails(business: ArrayList<Business>){
//		hideProgressDialog()
		if (business.size > 0){
			business.forEach {
				businessInfo = it
			}
//			binding.clNoBusinessProfile.visibility = View.INVISIBLE
//			binding.clBusinessProfile.visibility = View.VISIBLE

			binding.rvBusinessList.layoutManager =
				LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
			val itemAdapter = context?.let { BusinessAdapter(it, business) }
			binding.rvBusinessList.adapter = itemAdapter

			itemAdapter?.setOnClickListener(object : BusinessAdapter.BusinessOnClickListener{
				override fun onClick(view: View, item: Business) {
					when(view.id) {
						R.id.tvGoToItems -> {
							val intent = Intent(context, ItemsActivity::class.java)
							intent.putExtra(Constants.EXTRA_BUSINESS, item)
							startActivity(intent)
						}
						R.id.tvUpdate -> {
							val intent = Intent(context, RegisterBusiness::class.java)
							intent.putExtra(Constants.EXTRA_BUSINESS, item)
							startActivityForResult(intent, Constants.UPDATE_BS_REQUEST)
						}
					}
				}
			})
		} else {
//			binding.clBusinessProfile.visibility = View.INVISIBLE
//			binding.clNoBusinessProfile.visibility = View.VISIBLE
		}
	}

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		super.onActivityResult(requestCode, resultCode, data)
		if (resultCode == Activity.RESULT_OK &&
			requestCode == Constants.UPDATE_BS_REQUEST ||
			requestCode == Constants.ADD_BS_REQUEST){
			ViewModel().fetchBusiness(this)
		}	}

}

