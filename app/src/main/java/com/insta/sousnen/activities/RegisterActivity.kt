package com.insta.sousnen.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.insta.sousnen.R
import com.insta.sousnen.models.User
import kotlinx.android.synthetic.main.fragment_register_email.*
import kotlinx.android.synthetic.main.fragment_register_namepass.*

class RegisterActivity : AppCompatActivity(),EmailFragment.Listener,NamePassFragment.Listener {
    private val TAG = "RegisterActivity"
    private var mEmail : String? = null
    private lateinit var mAuth:FirebaseAuth
    private lateinit var mDatabase:DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        mAuth = FirebaseAuth.getInstance()
        mDatabase = FirebaseDatabase.getInstance().reference
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().add(R.id.frame_layout, EmailFragment())
                .commit()
        }
    }

    override fun onNext(email: String) {
        if (email.isNotEmpty()){
            mEmail =email
            mAuth.fetchSignInMethodsForEmail(email).addOnCompleteListener {
                if(it.isSuccessful){
                    if(it.result?.signInMethods?.isEmpty()==true){
                        supportFragmentManager.beginTransaction().replace(R.id.frame_layout, NamePassFragment())
                            .addToBackStack(null)
                            .commit()

                    }else{
                        showToast("this email already exists")
                    }
                }else{
                    showToast(it.exception!!.message!!)
                }
            }
        }else{
            showToast("Please enter your email")
        }
    }

    override fun onRegister(fullname: String, password: String) {
        if(fullname.isNotEmpty()&& password.isNotEmpty()){
            val email = mEmail
            if (email!=null){

                mAuth.createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener{
                        if (it.isSuccessful){
                            val user = makeUser(fullname, email)
                            val reference = mDatabase.child("users").child(it.result!!.user!!.uid)
                            reference.setValue(user).addOnCompleteListener{
                                if(it.isSuccessful){
                                   startHomeActivity()
                                }else{
                                    unknownRegisterError(it)
                                }
                            }
                        }else{
                            unknownRegisterError(it)
                        }
                    }
            }else{
                Log.e(TAG,"email is null")
                showToast("Pleases enter name and password")
                supportFragmentManager.popBackStack()
            }
        }else{
            showToast("Pleases enter name and password")
        }
    }

    private fun unknownRegisterError(it: Task<*>) {
        Log.e(TAG, "Failure create user profile", it.exception)
        showToast("We have a problem,try again")
    }

    private fun startHomeActivity() {
        startActivity(Intent(this,HomeActivity::class.java))
        finish()
    }
    private fun makeUser(fullname:String,email:String):User{
        val username = makeUsername(fullname)
        return User(name=fullname,username=username,email = email)
    }

    private fun makeUsername(fullname: String)=
        fullname.toLowerCase().replace(" ",".")
}

class EmailFragment:Fragment(){
    private lateinit var mListener:Listener

    interface Listener{
        fun onNext(email: String)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_register_email,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        coordinateBrnANdInputs(next_btn,email_input)
        next_btn.setOnClickListener {
            val email = email_input.text.toString()
            mListener.onNext(email)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mListener = context as Listener
    }
}

class NamePassFragment:Fragment(){
    private lateinit var mListener: Listener
    interface Listener{
        fun onRegister(fullname: String,password: String)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_register_namepass,container,false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mListener = context as Listener
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        coordinateBrnANdInputs(registration_btn,name_input,password_input)
        registration_btn.setOnClickListener {
            val fullName = name_input.text.toString()
            val password = password_input.text.toString()
            mListener.onRegister(fullName,password)

        }
    }
}