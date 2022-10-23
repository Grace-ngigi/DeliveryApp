package com.sais.deliveryapp.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.sais.deliveryapp.R
import com.sais.deliveryapp.databinding.ActivityBottomTestBinding
import com.sais.deliveryapp.fragments.*
import com.sais.deliveryapp.frebase.FireStore
import com.sais.deliveryapp.logins.RegisterActivity
import com.sais.deliveryapp.utils.Constants

class BottomTestActivity : AppCompatActivity() {

	lateinit var binding: ActivityBottomTestBinding
	override fun onCreate(savedInstanceState: Bundle?) {
		binding = ActivityBottomTestBinding.inflate(layoutInflater)
		super.onCreate(savedInstanceState)
		setContentView(binding.root)
		setResult(Activity.RESULT_OK)
		replaceFragment(HomeFragment())
		binding.bottomNav.background = null
		binding.bottomNav.setOnItemSelectedListener {

			val currentUserId = ViewModel().getCurrentUserId()

			when(it.itemId){
				R.id.home ->
//				}
					replaceFragment(HomeFragment())
				R.id.offers -> replaceFragment(OffersFragment())
				R.id.support -> replaceFragment(SupportFragment())
				R.id.account ->
					if (currentUserId.isNotEmpty()){
						replaceFragment(AccountFragment())
				} else {
					startActivity(Intent(this, RegisterActivity::class.java))
				}
				else ->{
				}
			}
			true
		}

		binding.fabCart.setOnClickListener {
			val intent = Intent(this, CartActivity::class.java)
			startActivity(intent)
		}
	}

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		super.onActivityResult(requestCode, resultCode, data)
		if (resultCode == Activity.RESULT_OK &&
			requestCode == Constants.UPDATE_BS_REQUEST ||
			requestCode == Constants.LOGIN_USER ||
			requestCode == Constants.ADD_BS_REQUEST){
			replaceFragment(AccountFragment())
		}
	}

	private fun replaceFragment(fragment : Fragment){
		val fragmentManager = supportFragmentManager
		val fragmentTransaction = fragmentManager.beginTransaction()
		fragmentTransaction.replace(R.id.frame_layout,fragment)
		fragmentTransaction.commit()
	}
}
