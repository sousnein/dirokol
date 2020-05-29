package com.insta.sousnen.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.insta.sousnen.*
import kotlinx.android.synthetic.main.bottom_navigation_view.*
import java.lang.Exception

abstract class BaseActivity(val navNumber:Int):AppCompatActivity() {
    fun setUpBottomNavigation() {
        bottom_navigation_view.setTextVisibility(false)
        bottom_navigation_view.enableItemShiftingMode(false)
        bottom_navigation_view.enableShiftingMode(false)
        bottom_navigation_view.enableAnimation(false)
        bottom_navigation_view.setIconSize(28f, 28f)
        for (i in 0 until bottom_navigation_view.menu.size()) {
            bottom_navigation_view.setIconTintList(i, null)
        }
        bottom_navigation_view.setOnNavigationItemReselectedListener {
            val nextActivity =
                when (it.itemId) {
                    R.id.nav_item_home -> HomeActivity::class.java
                    R.id.nav_item_search -> SearchActivity::class.java
                    R.id.nav_item_share -> ShareActivity::class.java
                    R.id.nav_item_likes -> LikesActivity::class.java
                    R.id.nav_item_profile -> ProfileActivity::class.java
                    else -> {
                        throw Exception("unknown nav")
                        null
                    }
                }

            if (nextActivity != null) {

                val intent = Intent(this, nextActivity)
                intent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
                startActivity(intent)
                overridePendingTransition(0,0)
                true
            } else {
                false
            }
        }
        bottom_navigation_view.menu.getItem(navNumber).isChecked=true
    }
}