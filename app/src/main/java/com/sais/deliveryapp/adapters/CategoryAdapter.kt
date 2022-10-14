package com.sais.deliveryapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sais.deliveryapp.databinding.CategoryListBinding
import com.sais.deliveryapp.models.CategoryList

class CategoryAdapter(private val context: Context,
                      private val catItems: ArrayList<CategoryList>
	): RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

		class  ViewHolder(binding:CategoryListBinding) : RecyclerView.ViewHolder(binding.root){
			val catItem = binding.tvCatItem
		}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		return ViewHolder(CategoryListBinding.inflate(LayoutInflater.from(parent.context), parent, false))
	}

	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		val category = catItems[position]
		holder.catItem.text = category.name
	}

	override fun getItemCount(): Int {
	return catItems.size
	}
}