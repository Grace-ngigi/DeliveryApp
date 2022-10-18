package com.sais.deliveryapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.sais.deliveryapp.adapters.BusinessAdapter
import com.sais.deliveryapp.adapters.CategoryAdapter
import com.sais.deliveryapp.adapters.ItemAdapter
import com.sais.deliveryapp.adapters.SampleAdapter
import com.sais.deliveryapp.databinding.ActivityMainBinding
import com.sais.deliveryapp.frebase.FireStore
import com.sais.deliveryapp.logins.RegisterActivity
import com.sais.deliveryapp.models.*
import com.sais.deliveryapp.utils.Constants

class MainActivity : BaseActivity() {

	lateinit var binding: ActivityMainBinding
	private var catInfo = ArrayList<CategoryList>()
	private var popularInfo = ArrayList<SampleList>()
	private var itemsInfo = ArrayList<Item>()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivityMainBinding.inflate(layoutInflater)
		setContentView(binding.root)

		setCategories()
		setUpSampleItems()
		showProgressDialog()
		FireStore().fetchAllItems(this)
		binding.llWishList.setOnClickListener { startActivity(Intent(this, WishListActivity::class.java)) }
		binding.llOffers.setOnClickListener { startActivity(Intent(this, OffersActivity::class.java)) }
		binding.fabCart.setOnClickListener { startActivity(Intent(this, CartActivity::class.java)) }
		binding.llSupport.setOnClickListener { startActivity(Intent(this, SupportActivity::class.java)) }

		binding.llAccount.setOnClickListener {
			val currentUserId = FireStore().getCurrentUserId()
			if (currentUserId.isNotEmpty()){
				startActivity(Intent(this, ProfileActivity::class.java))
			} else {
				startActivity(Intent(this, RegisterActivity::class.java))
			}
			finish()
		}

	}

	fun allItems(items: ArrayList<Item>) {
		hideProgressDialog()
		if (items.size > 0){
		binding.rvItems.layoutManager = GridLayoutManager(this, 2)
		val itemAdapter = ItemAdapter(this, items)
		binding.rvItems.adapter = itemAdapter

			itemAdapter.setOnClickListener(object : ItemAdapter.ItemOnClickListener{
				override fun onClick(view: View, item: Item) {
					val intent = Intent(this@MainActivity, CartActivity::class.java)
					intent.putExtra(Constants.EXTRA_ITEM_INFO, item)
					startActivity(intent)				}
			})
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