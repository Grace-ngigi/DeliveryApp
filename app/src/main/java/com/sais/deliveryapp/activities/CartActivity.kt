package com.sais.deliveryapp.activities

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.sais.deliveryapp.R
import com.sais.deliveryapp.adapters.CartItemAdapter
import com.sais.deliveryapp.databinding.ActivityCartBinding
import com.sais.deliveryapp.frebase.FireStore
import com.sais.deliveryapp.models.CartItem
import com.sais.deliveryapp.models.Item
import com.sais.deliveryapp.utils.Constants

class CartActivity : BaseActivity() {
	lateinit var binding: ActivityCartBinding
	private var item: CartItem? = null
	var numberOrder: Int = 1
	private var totalValue: Int = 0

	override fun onCreate(savedInstanceState: Bundle?) {
		binding = ActivityCartBinding.inflate(layoutInflater)
		super.onCreate(savedInstanceState)
		setContentView(binding.root)

		FireStore().fetchCartItems(this)
	}

	fun fetchCartItems(items: ArrayList<CartItem>){
		if (items.size > 0){
			items.forEach {
				item = it
			}
			binding.rvCartItems.layoutManager = LinearLayoutManager(this,
			LinearLayoutManager.VERTICAL, false)
			val adapter = CartItemAdapter(this, items)
			binding.rvCartItems.adapter = adapter

			binding.tvDisplayTotal.text = totalValue.toString()
//
//			adapter.setOnClickListener(object : CartItemAdapter.ItemOnClickListener{
//				override fun onClick(view: View, item: CartItem) {
//					when(view.id){
//						R.id.tvAdd -> {
//
//						}
//					}
//				}
//
//			})
		}

	}
}