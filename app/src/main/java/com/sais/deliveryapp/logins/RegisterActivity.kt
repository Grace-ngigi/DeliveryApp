package com.sais.deliveryapp.logins

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sais.deliveryapp.activities.ProfileActivity
import com.sais.deliveryapp.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
	lateinit var binding: ActivityRegisterBinding
	override fun onCreate(savedInstanceState: Bundle?) {
		binding = ActivityRegisterBinding.inflate(layoutInflater)
		super.onCreate(savedInstanceState)
		setContentView(binding.root)

		binding.tvLogin.setOnClickListener { startActivity(Intent(this, LoginActivity:: class.java)) }
		binding.btRegister.setOnClickListener { startActivity(Intent(this, ProfileActivity::class.java)) }
	}
}