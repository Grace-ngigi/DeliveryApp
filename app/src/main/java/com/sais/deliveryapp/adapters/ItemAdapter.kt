package com.sais.deliveryapp.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sais.deliveryapp.activities.UploadItem
import com.sais.deliveryapp.databinding.ItemsListBinding
import com.sais.deliveryapp.models.Business
import com.sais.deliveryapp.models.Item

class ItemAdapter(private val context: Context,
                  private val catItems: ArrayList<Item>
	): RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

		class  ViewHolder(binding: ItemsListBinding) : RecyclerView.ViewHolder(binding.root){
			val pic = binding.ivItem
			val title = binding.tvItemTitle
			val shop = binding.tvShop
			val price = binding.tvDisplayPrice
			val quantity = binding.tvItemQuantity
			val addToCart = binding.btAddItem
		}

	private  var onClickListener: ItemOnClickListener? = null
	interface ItemOnClickListener {
		fun onClick(view: View, item: Item)
	}

	fun setOnClickListener(onClickListener: ItemOnClickListener){
		this.onClickListener = onClickListener
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		return ViewHolder(ItemsListBinding.inflate(LayoutInflater.from(parent.context), parent, false))
	}

	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		val item = catItems[position]
		holder.title.text = item.title
		holder.shop.text = item.bsName
		holder.price.text = item.price.toString()
		holder.quantity.text = item.quantity
		Glide.with(context)
			.load(item.image)
			.centerCrop()
			.into(holder.pic)

		holder.addToCart.setOnClickListener {
			onClickListener?.onClick(it, item)
		}
	}

	override fun getItemCount(): Int {
	return catItems.size
	}
}