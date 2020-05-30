package com.insta.sousnen.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.insta.sousnen.R
import com.insta.sousnen.models.User
import com.insta.sousnen.views.PasswordDialog
import kotlinx.android.synthetic.main.activity_edit_profile.*
import kotlinx.android.synthetic.main.activity_edit_profile.name_input

class EditProfileActivity : AppCompatActivity() ,PasswordDialog.Listener{
    private val TAG = "EditProfileActivity"
    private lateinit var mUser:User
    private lateinit var mPendingUser:User
    private lateinit var mAuth:FirebaseAuth
    private lateinit var mDatabase:DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        close_image.setOnClickListener{finish()}
        save_image.setOnClickListener{updateProfile()}

        mAuth = FirebaseAuth.getInstance()
        mDatabase = FirebaseDatabase.getInstance().reference
        mDatabase.child("users").child(mAuth.currentUser!!.uid).addListenerForSingleValueEvent(ValueEventListenerAdapter {
                mUser = it.getValue(User::class.java)!!
                name_input.setText(mUser.name, TextView.BufferType.EDITABLE)
                username_input.setText(mUser.username, TextView.BufferType.EDITABLE)
                website_input.setText(mUser.website, TextView.BufferType.EDITABLE)
                bio_input.setText(mUser.bio, TextView.BufferType.EDITABLE)
                email_input.setText(mUser.email, TextView.BufferType.EDITABLE)
                phone_input.setText(mUser.phone.toString(), TextView.BufferType.EDITABLE)
        })
    }

    private fun updateProfile() {
            mPendingUser = User(
            name = name_input.text.toString(),
            username = username_input.text.toString(),
            website = website_input.text.toString(),
            bio = bio_input.text.toString(),
            email = email_input.text.toString(),
            phone = phone_input.text.toString().toLong()
        )
        val error= validate(mPendingUser)
        if(error==null){
            if(mPendingUser.email==mUser.email){
                updateUser(mPendingUser)
            }else{
                //show dialog
                PasswordDialog().show(supportFragmentManager,"password_dialog" )
            }
        }else{
                showToast(error)
        }
    }

    override fun onPasswordConfirm(password: String) {
        val credential = EmailAuthProvider.getCredential(mUser.email,password)
        mAuth.currentUser!!.reauthenticate(credential).addOnCompleteListener{
            if (it.isSuccessful){
                mAuth.currentUser!!.updateEmail(mPendingUser.email).addOnCompleteListener {
                    if (it.isSuccessful){
                        updateUser(mPendingUser)
                    }else{
                        showToast(it.exception!!.message!!)
                    }
                }
            }else{
                showToast(it.exception!!.message!!)
            }
        }
    }

    private fun updateUser(user:User){
        val updatesMap = mutableMapOf<String,Any>()
        if(user.name!=mUser.name)updatesMap["name"] = user.name
        if(user.username!=mUser.username)updatesMap["username"] = user.username
        if(user.bio!=mUser.bio)updatesMap["bio"] = user.bio
        if(user.website!=mUser.website)updatesMap["website"] = user.website
        if(user.email!=mUser.email)updatesMap["email"] = user.email
        if(user.phone!=mUser.phone)updatesMap["phone"] = user.phone

        mDatabase.child("users").child(mAuth.currentUser!!.uid).updateChildren(updatesMap)
            .addOnCompleteListener{
                if(it.isSuccessful){
                    showToast("Changes saved")
                    finish()
                }else{
                    showToast(it.exception!!.message!!)
                }
            }
    }

    private fun validate(user:User):String?=
        when{
            user.name.isEmpty()->"Please enter name"
            user.username.isEmpty()->"Please enter name"
            user.email.isEmpty()->"Please enter name"
            else->null
        }


}


