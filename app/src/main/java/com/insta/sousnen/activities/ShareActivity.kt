package com.insta.sousnen.activities

import android.os.Bundle
import com.insta.sousnen.R

class ShareActivity : BaseActivity(2) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setUpBottomNavigation()
    }
}
