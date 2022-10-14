package com.sais.deliveryapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sais.deliveryapp.databinding.ActivityCartBinding
import com.sais.deliveryapp.databinding.ActivitySupportBinding

class SupportActivity : AppCompatActivity() {
	lateinit var binding: ActivitySupportBinding
	override fun onCreate(savedInstanceState: Bundle?) {
		binding = ActivitySupportBinding.inflate(layoutInflater)
		super.onCreate(savedInstanceState)
		setContentView(binding.root)
	}
}