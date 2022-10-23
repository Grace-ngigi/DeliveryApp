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
import com.sais.deliveryapp.databinding.CartItemsListBinding
import com.sais.deliveryapp.databinding.ItemsListBinding
import com.sais.deliveryapp.models.Business
import com.sais.deliveryapp.models.CartItem
import com.sais.deliveryapp.models.Item

class CartItemAdapter(private val context: Context,
                      private val catItems: ArrayList<CartItem>
	): RecyclerView.Adapter<CartItemAdapter.ViewHolder>() {

		class  ViewHolder(binding: CartItemsListBinding) : RecyclerView.ViewHolder(binding.root){
			val pic = binding.ivCartItemImage
			val title = binding.tvCartTitle
			val price = binding.tvCartPrice
			val desc = binding.tvCartDescription
			val totalPrice = binding.tvTotalPrice
			val quantity = binding.tvCarQuantity
			val value = binding.tvNumber
			val addValue = binding.tvAdd
			val minusValue = binding.tvMinus
		}

	private  var onClickListener: ItemOnClickListener? = null
	var numberOrder: Int = 1
	private var totalValue: Int = 0
	interface ItemOnClickListener {
		fun onClick(view: View, item: CartItem)
	}

	fun setOnClickListener(onClickListener: ItemOnClickListener){
		this.onClickListener = onClickListener
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		return ViewHolder(CartItemsListBinding.inflate(LayoutInflater.from(parent.context), parent, false))
	}

	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		val item = catItems[position]
		holder.title.text = item.title
		holder.price.text = item.price.toString()
		holder.quantity.text = item.quantity
		holder.desc.text = item.description
		holder.value.text = numberOrder.toString()
		Glide.with(context)
			.load(item.image)
			.centerCrop()
			.into(holder.pic)

		holder.addValue.setOnClickListener {
//			onClickListener!!.onClick(it, item)
			numberOrder += 1
			holder.value.text = numberOrder.toString()
			 totalValue= item.price * numberOrder
			holder.totalPrice.text = totalValue.toString()
		}

		holder.minusValue.setOnClickListener {
//			onClickListener!!.onClick(it, item)
			if (numberOrder > 1){
				numberOrder -= 1
				totalValue = item.price * numberOrder
			}
			holder.value.text = numberOrder.toString()
			holder.totalPrice.text = totalValue.toString()
		}
	}

	override fun getItemCount(): Int {
	return catItems.size
	}
}