package com.insta.sousnen.activities

import android.content.Intent
import android.os.Bundle
import com.insta.sousnen.R
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : BaseActivity(4) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        setUpBottomNavigation()
        edit_profile_button.setOnClickListener {
            val intent = Intent(this, EditProfileActivity::class.java)
            startActivity(intent)
        }
    }
}
