package com.sais.deliveryapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.sais.deliveryapp.adapters.ItemAdapter
import com.sais.deliveryapp.adapters.ProfileItemAdapter
import com.sais.deliveryapp.adapters.SampleAdapter
import com.sais.deliveryapp.databinding.ActivityProfileBinding
import com.sais.deliveryapp.logins.RegisterActivity
import com.sais.deliveryapp.models.ItemList
import com.sais.deliveryapp.models.Items
import com.sais.deliveryapp.models.SampleItems
import com.sais.deliveryapp.models.SampleList

class ProfileActivity : AppCompatActivity() {
	lateinit var binding: ActivityProfileBinding
	private var addedInfo = ArrayList<ItemList>()

	override fun onCreate(savedInstanceState: Bundle?) {
		binding = ActivityProfileBinding.inflate(layoutInflater)
		super.onCreate(savedInstanceState)
		setContentView(binding.root)
		setUploadedItems()
		binding.ivProfile.setOnClickListener { startActivity(Intent(this, RegisterActivity::class.java)) }
	}

	private fun setUploadedItems() {
		binding.rvUploadedItems.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
		val sampleAdapter = ProfileItemAdapter(this, addedInfo)
		binding.rvUploadedItems.adapter = sampleAdapter

		val catList = Items.itemLists
		catList.forEach {
			val id = it.id
			val title = it.title
			val shop = it.shop
			val price = it.price
			val quantity = it.quantity
			val desc = it.description
			addedInfo.add(ItemList(id, title, shop, price,quantity, desc))
		}
	}
}