package com.sais.deliveryapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.sais.deliveryapp.R
import com.sais.deliveryapp.databinding.ActivityBottomTestBinding
import com.sais.deliveryapp.fragments.*
import com.sais.deliveryapp.frebase.FireStore
import com.sais.deliveryapp.logins.RegisterActivity

class BottomTestActivity : AppCompatActivity() {

	lateinit var binding: ActivityBottomTestBinding
	override fun onCreate(savedInstanceState: Bundle?) {
		binding = ActivityBottomTestBinding.inflate(layoutInflater)
		super.onCreate(savedInstanceState)
		setContentView(binding.root)
		replaceFragment(HomeFragment())
		binding.bottomNav.background = null
		binding.bottomNav.setOnItemSelectedListener {

			when(it.itemId){
				R.id.home ->
//					val currentUserId = ViewModel().getCurrentUserId()
//				if (currentUserId.isNotEmpty()){
//					startActivity(Intent(this, ProfileActivity::class.java))
//				} else {
//					startActivity(Intent(this, RegisterActivity::class.java))
//				}
					replaceFragment(HomeFragment())
				R.id.offers -> replaceFragment(OffersFragment())
				R.id.support -> replaceFragment(SupportFragment())
				R.id.account -> replaceFragment(AccountFragment())
				else ->{
				}
			}
			true
		}
	}

	private fun replaceFragment(fragment : Fragment){

		val fragmentManager = supportFragmentManager
		val fragmentTransaction = fragmentManager.beginTransaction()
		fragmentTransaction.replace(R.id.frame_layout,fragment)
		fragmentTransaction.commit()


	}
	}
