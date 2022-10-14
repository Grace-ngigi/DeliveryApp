package com.sais.deliveryapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sais.deliveryapp.databinding.SampleListBinding
import com.sais.deliveryapp.models.SampleList

class SampleAdapter(private val context: Context,
                    private val catItems: ArrayList<SampleList>
	): RecyclerView.Adapter<SampleAdapter.ViewHolder>() {

		class  ViewHolder(binding: SampleListBinding) : RecyclerView.ViewHolder(binding.root){
			val pic = binding.ivCatImage
			val items = binding.tvItemName
//			val price = binding.tvItemPrice
			val layout = binding.clCatList
		}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		return ViewHolder(SampleListBinding.inflate(LayoutInflater.from(parent.context), parent, false))
	}

	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		val category = catItems[position]
		holder.items.text = category.item
//		holder.pic.setImageURI(Uri.parse(category.pic))
//		holder.price.text = category.price.toString()
	}

	override fun getItemCount(): Int {
	return catItems.size
	}
}