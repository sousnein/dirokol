package com.insta.sousnen.activities


import android.content.Intent
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.insta.sousnen.R
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : BaseActivity(0) {
    private val TAG = "MainActivity"
    private lateinit var  nAuth:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setUpBottomNavigation()

        nAuth = FirebaseAuth.getInstance()
        //nAuth.signOut()
//        auth.signInWithEmailAndPassword("tubz.show@gmail.com","vova110302")
//            .addOnCompleteListener {
//                if (it.isSuccessful){
//                    Log.d(TAG,"sign In success")
//                }else{
//                    Log.d(TAG,"sign In failure",it.exception)
//                }

        home_text.setOnClickListener {
            nAuth.signOut()
        }
        /**если оставить так,
        то он будет выходить не проверяя состояние авторизации,
        то есть не перекинет на логин активити**/
        nAuth.addAuthStateListener {
            if (it.currentUser == null) {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if (nAuth.currentUser==null){
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}
