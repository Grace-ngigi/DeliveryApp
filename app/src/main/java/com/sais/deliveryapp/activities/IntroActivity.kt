package com.sais.deliveryapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sais.deliveryapp.databinding.ActivityIntroBinding
import com.sais.deliveryapp.logins.RegisterActivity

class IntroActivity : AppCompatActivity() {
	lateinit var binding : ActivityIntroBinding

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivityIntroBinding.inflate(layoutInflater)
		setContentView(binding.root)
binding.btnStart.setOnClickListener { startActivity(Intent(this, BottomTestActivity::class.java)) }

	}
}