package com.insta.sousnen.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.insta.sousnen.R
import kotlinx.android.synthetic.main.activity_edit_profile.*
import kotlinx.android.synthetic.main.activity_edit_profile.email_input
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.fragment_register_email.*
import kotlinx.android.synthetic.main.fragment_register_namepass.*
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener

class LoginActivity : AppCompatActivity(), KeyboardVisibilityEventListener, View.OnClickListener {
    private val TAG = "HomeActivity"
    private lateinit var mAuth:FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        /**Поставили слушателей,
         * чтобы клавиатуро "сдвигала" все элементы так,
         * чтобы элементы влезли на экране**/
        KeyboardVisibilityEvent.setEventListener(this,this)
        coordinateBrnANdInputs(login_btn,login_email_input,login_password_input)
        login_btn.setOnClickListener(this)
        create_account_text.setOnClickListener(this)
        mAuth=FirebaseAuth.getInstance()
    }
    /**Скрываем sign in при нажатии на ввод**/
    override fun onClick(view: View) {
        when (view.id){
             R.id.login_btn->{
                val email = login_email_input.text.toString()
                val password = login_password_input.text.toString()
                if(validate(email,password)){
                    mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener{
                        if(it.isSuccessful){
                            startActivity(Intent(this,
                                HomeActivity::class.java))
                            finish()
                        }
                    }
                }else{
                    showToast("Please enter email and password")
                }
            }
            R.id.create_account_text->{
                startActivity(Intent(this,RegisterActivity::class.java))
            }
        }
    }

    /**Скрываем sign in при нажатии на ввод**/

    override fun onVisibilityChanged(isOpen: Boolean) {
        if (isOpen){
            create_account_text.visibility = View.GONE
        }else{
            create_account_text.visibility = View.VISIBLE
        }
    }
    /**Сделано для того,чтобы кнопка была кликабельной только в случае ввода сразу двух полей**/

    private fun validate(email:String,password:String): Boolean {
        return email.isNotEmpty() && password.isNotEmpty()
    }


}
