package com.sais.deliveryapp.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.sais.deliveryapp.activities.ItemsActivity
import com.sais.deliveryapp.databinding.BusinessListBinding
import com.sais.deliveryapp.databinding.ItemsListBinding
import com.sais.deliveryapp.models.Business
import com.sais.deliveryapp.models.Item
import com.sais.deliveryapp.utils.Constants

class BusinessAdapter(private val context: Context,
                      private val bizItems: ArrayList<Business>
	): RecyclerView.Adapter<BusinessAdapter.ViewHolder>() {

		class  ViewHolder(binding: BusinessListBinding) : RecyclerView.ViewHolder(binding.root){
			val pic = binding.ivBsLogo
			val name = binding.tvShop
			val type = binding.tvBsType
			val location = binding.tvLocation
			val tillNo = binding.tvTillNo
			val addItem = binding.tvGoToItems
		}
	private  var onClickListener: BusinessOnClickListener? = null

	interface BusinessOnClickListener {
		fun onClick(view: View, item: Business)
	}

	fun setOnClickListener(onClickListener: BusinessOnClickListener){
		this.onClickListener = onClickListener
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		return ViewHolder(BusinessListBinding.inflate(LayoutInflater.from(parent.context), parent, false))
	}

	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		val item = bizItems[position]
		holder.name.text = item.bsName
		holder.type.text = item.type
		holder.location.text = item.location
		holder.tillNo.text = item.till.toString()

		holder.addItem.setOnClickListener {
			onClickListener?.onClick(it, item)
		}
	}

	override fun getItemCount(): Int {
	return bizItems.size
	}
}