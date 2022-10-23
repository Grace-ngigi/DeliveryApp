package com.sais.deliveryapp.fragments

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.sais.deliveryapp.R
import com.sais.deliveryapp.activities.*
import com.sais.deliveryapp.adapters.CategoryAdapter
import com.sais.deliveryapp.adapters.ItemAdapter
import com.sais.deliveryapp.adapters.SampleAdapter
import com.sais.deliveryapp.databinding.FragmentAccountBinding
import com.sais.deliveryapp.databinding.FragmentHomeBinding
import com.sais.deliveryapp.frebase.FireStore
import com.sais.deliveryapp.logins.RegisterActivity
import com.sais.deliveryapp.models.*
import com.sais.deliveryapp.utils.Constants

class HomeFragment : Fragment(R.layout.fragment_home) {
		lateinit var binding: FragmentHomeBinding
		private var businessInfo : Business? = null
		private var totalPrice: Int = 0
		private lateinit var db: FirebaseFirestore
	private var catInfo = ArrayList<CategoryList>()
	private var popularInfo = ArrayList<SampleList>()

		override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
			super.onViewCreated(view, savedInstanceState)
			binding = FragmentHomeBinding.bind(view)
			db= FirebaseFirestore.getInstance()
			setCategories()
			setUpSampleItems()
//			showProgressDialog()
			ViewModel().fetchAllItems(this)
		}

	fun allItems(items: ArrayList<Item>) {
//		hideProgressDialog()
		if (items.size > 0){
			binding.rvItems.layoutManager = GridLayoutManager(context, 2)
			val itemAdapter = context?.let { ItemAdapter(it, items) }
			binding.rvItems.adapter = itemAdapter

			itemAdapter?.setOnClickListener(object : ItemAdapter.ItemOnClickListener{
				override fun onClick(view: View, item: Item) {

					val id = db.collection(Constants.CART_ITEM).document().id
					val cartItem = CartItem(
						id,
						bsId = item.bsId,
						title = item.title,
						bsName = item.bsName,
						price = item.price,
						quantity = item.quantity,
						description = item.description,
						category = item.category,
						image = item.image
					)
					ViewModel().addToCart(this@HomeFragment, cartItem)
				}
			})
		}
	}
	fun addToCartSuccess(){
		Toast.makeText(context, "Added to Cart", Toast.LENGTH_SHORT).show()
	}
	fun addToCartFailure(error: String){
		Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
	}
	private fun setCategories() {
		binding.rvCategoryList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
		val categoryAdapter = CategoryAdapter(requireContext(), catInfo)
		binding.rvCategoryList.adapter = categoryAdapter

		val catList = CategoryItems.itemsLists
		catList.forEach {
			val id = it.id
			val catItem = it.name
			catInfo.add(CategoryList(id, catItem))
		}
	}
	private fun setUpSampleItems() {
		binding.rvPopularItems.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
		val sampleAdapter = SampleAdapter(requireContext(), popularInfo)
		binding.rvPopularItems.adapter = sampleAdapter

		val catList = SampleItems.sampleLists
		catList.forEach {
			val catItem = it.item
			val id = it.id
			val img = it.pic
			val price = it.price
			popularInfo.add(SampleList(id, catItem, img, price))
		}
	}
}
