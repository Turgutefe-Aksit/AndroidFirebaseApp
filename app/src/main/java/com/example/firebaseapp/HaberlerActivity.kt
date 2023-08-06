package com.example.firebaseapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_haberler.*

class HaberlerActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database : FirebaseFirestore
    private lateinit var recyclerAdapter: HaberRecyclerAdapter
    var postListesi = ArrayList<Post>()


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_haberler)

        auth = FirebaseAuth.getInstance()
        database = FirebaseFirestore.getInstance()

        verileriAl()

        var layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        recyclerAdapter= HaberRecyclerAdapter(postListesi)
        recyclerView.adapter = recyclerAdapter

    }


    fun verileriAl(){

        database.collection("Post").orderBy("tarih",Query.Direction.DESCENDING).addSnapshotListener { value, error ->

            if (error != null) {
                Toast.makeText(this,error.localizedMessage, Toast.LENGTH_LONG).show()
            }else{

                if (value != null){
                    if (value.isEmpty == false){

                        val documents = value.documents
                        postListesi.clear()
                        for (document in documents){
                            val kullaniciEmail = document.get("kullaniciEmail").toString()
                            val kullaniciYorumu = document.get("kullaniciYorum").toString()
                            val gorselUrl = document.get("gorselUrl").toString()

                            val indirilenPost = Post(kullaniciEmail,kullaniciYorumu,gorselUrl)
                            postListesi.add(indirilenPost)
                        }
                        recyclerAdapter.notifyDataSetChanged()

                    }
                }

            }
        }
    }



    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val menuInflater =menuInflater
        menuInflater.inflate(R.menu.options_menu,menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.foto_paylas){
            val intent = Intent(this,FotoPaylasActivity::class.java)
            startActivity(intent)
        }
        else if (item.itemId == R.id.Cikis){
            auth.signOut()
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        }


        return super.onOptionsItemSelected(item)
    }
}