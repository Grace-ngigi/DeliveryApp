package com.sais.deliveryapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.sais.deliveryapp.adapters.CategoryAdapter
import com.sais.deliveryapp.adapters.ItemAdapter
import com.sais.deliveryapp.adapters.SampleAdapter
import com.sais.deliveryapp.databinding.ActivityMainBinding
import com.sais.deliveryapp.models.*

class MainActivity : AppCompatActivity() {

	lateinit var binding: ActivityMainBinding
	private var catInfo = ArrayList<CategoryList>()
	private var popularInfo = ArrayList<SampleList>()
	private var itemsInfo = ArrayList<ItemList>()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivityMainBinding.inflate(layoutInflater)
		setContentView(binding.root)

		setCategories()
		setUpSampleItems()
		setUpItems()
		binding.llWishList.setOnClickListener { startActivity(Intent(this, WishListActivity::class.java)) }
		binding.llOffers.setOnClickListener { startActivity(Intent(this, OffersActivity::class.java)) }
		binding.fabCart.setOnClickListener { startActivity(Intent(this, CartActivity::class.java)) }
		binding.llSupport.setOnClickListener { startActivity(Intent(this, SupportActivity::class.java)) }
		binding.llAccount.setOnClickListener { startActivity(Intent(this, ProfileActivity::class.java)) }

	}

	private fun setUpItems() {
		binding.rvItems.layoutManager = GridLayoutManager(this, 2)
		val itemAdapter = ItemAdapter(this, itemsInfo)
		binding.rvItems.adapter = itemAdapter

		val itemList = Items.itemLists
		itemList.forEach {
			val id = it.id
			val title = it.title
			val shop = it.shop
			val price = it.price
			val quantity = it.quantity
			val desc = it.description
			itemsInfo.add(ItemList(id, title, shop, price, quantity, desc))
		}
	}

	private fun setCategories() {
		binding.rvCategoryList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
		val categoryAdapter = CategoryAdapter(this, catInfo)
		binding.rvCategoryList.adapter = categoryAdapter

		val catList = CategoryItems.itemsLists
		catList.forEach {
			val id = it.id
			val catItem = it.name
			catInfo.add(CategoryList(id, catItem))
		}
	}
	private fun setUpSampleItems() {
		binding.rvPopularItems.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
		val sampleAdapter = SampleAdapter(this, popularInfo)
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