package com.insta.sousnen.activities

import android.os.Bundle
import com.insta.sousnen.R

class LikesActivity : BaseActivity(3) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        setUpBottomNavigation()
    }
}
