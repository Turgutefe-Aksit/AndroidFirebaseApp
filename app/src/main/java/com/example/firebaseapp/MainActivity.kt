package com.example.firebaseapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    private  lateinit var  auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()

        val guncelKullanici = auth.currentUser
        if (guncelKullanici != null){
            val intent = Intent(this,HaberlerActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    fun girisYap(view: View){

        auth.signInWithEmailAndPassword(textMail.text.toString(),textPassword.text.toString()).addOnCompleteListener { task->
            if(task.isSuccessful){

                val guncelKullanici = auth.currentUser?.email.toString()
                Toast.makeText(this,"Giriş Başarılı ${guncelKullanici}",Toast.LENGTH_LONG).show()

                val intent = Intent(this,HaberlerActivity::class.java)
                startActivity(intent)
                finish()
            }
        }.addOnFailureListener { exception ->
            Toast.makeText(this,exception.localizedMessage,Toast.LENGTH_LONG).show()
        }

    }

    fun kayitOl(view: View){

        val email = textMail.text.toString()
        val passWord = textPassword.text.toString()

        auth.createUserWithEmailAndPassword(email,passWord).addOnCompleteListener { task ->
            if(task.isSuccessful){
                val intent = Intent(this, HaberlerActivity::class.java)
                startActivity(intent)
                finish()
            }
        }.addOnFailureListener { exception ->
            Toast.makeText(applicationContext,exception.localizedMessage,Toast.LENGTH_LONG).show()
        }


    }
}