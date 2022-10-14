package com.sais.deliveryapp.logins

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sais.deliveryapp.activities.ProfileActivity
import com.sais.deliveryapp.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
	lateinit var binding: ActivityLoginBinding
	override fun onCreate(savedInstanceState: Bundle?) {
		binding = ActivityLoginBinding.inflate(layoutInflater)
		super.onCreate(savedInstanceState)
		setContentView(binding.root)

		binding.tvLogin.setOnClickListener { startActivity(Intent(this, RegisterActivity::class.java)) }
		binding.btLogin.setOnClickListener { startActivity(Intent(this, ProfileActivity::class.java)) }

	}
}