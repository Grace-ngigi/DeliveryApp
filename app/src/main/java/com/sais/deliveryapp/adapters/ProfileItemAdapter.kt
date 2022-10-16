package com.sais.deliveryapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sais.deliveryapp.databinding.ProfileItemsBinding
import com.sais.deliveryapp.models.Item

class ProfileItemAdapter(private val context: Context,
                         private val catItems: ArrayList<Item>
	): RecyclerView.Adapter<ProfileItemAdapter.ViewHolder>() {

		class  ViewHolder(binding: ProfileItemsBinding) : RecyclerView.ViewHolder(binding.root){
			val title = binding.tvItemTitle
			val desc = binding.tvDescription
			val price = binding.tvDisplayPrice
			val quantity = binding.tvItemQuantity
			val addItem = binding.btAddItem
		}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		return ViewHolder(ProfileItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false))
	}

	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		val item = catItems[position]
		holder.title.text = item.title
		holder.desc.text = item.description
//		holder.pic.setImageURI(Uri.parse(item.pic))
		holder.price.text = item.price.toString()
		holder.quantity.text = item.quantity.toString()
	}

	override fun getItemCount(): Int {
	return catItems.size
	}
}