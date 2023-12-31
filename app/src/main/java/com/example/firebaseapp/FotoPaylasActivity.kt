package com.example.firebaseapp

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.media.Image
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_foto_paylas.*
import java.util.UUID

class FotoPaylasActivity : AppCompatActivity() {

    var secilenGorsel : Uri? = null
    var secilenBitmap : Bitmap? = null
    private  lateinit var storage: FirebaseStorage
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_foto_paylas)

        storage = FirebaseStorage.getInstance()
        auth = FirebaseAuth.getInstance()
        database = FirebaseFirestore.getInstance()


    }

    fun paylas(view: View){

        //depo işlemleri
        val uuid = UUID.randomUUID()
        val gorselIsmi = "${uuid}.jpg"


        val reference = storage.reference
        val gorselReference = reference.child("images").child(gorselIsmi)

        if (secilenGorsel != null){

            gorselReference.putFile(secilenGorsel!!).addOnSuccessListener {

                val yukleneneGorselReference = FirebaseStorage.getInstance().reference.child("images").child(gorselIsmi)
                yukleneneGorselReference.downloadUrl.addOnSuccessListener { uri ->

                    val downloadUri = uri.toString()

                    val kullaniciMail = auth.currentUser!!.email.toString()
                    val kullaniciYorum = txtYorum.text.toString()
                    val tarih = Timestamp.now()
                    //veri tabanı

                    val postHashMap = hashMapOf<String,Any>()
                    postHashMap.put("gorselUrl",downloadUri)
                    postHashMap.put("kullaniciEmail",kullaniciMail)
                    postHashMap.put("kullaniciYorum",kullaniciYorum)
                    postHashMap.put("tarih",tarih)

                    database.collection("Post").add(postHashMap).addOnCompleteListener {
                        if (it.isSuccessful){
                            finish()
                        }
                    }.addOnFailureListener {
                        Toast.makeText(applicationContext,it.localizedMessage,Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

    }



    fun gorselSec(view: View){

        if(ContextCompat.checkSelfPermission(this,android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            //izni almadık
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),1)
        }
        else{
            //izin varsa
            val galari = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galari,2)

        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

        if (requestCode == 1){
            //izin verilince
            if(grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                val galari = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(galari,2)

            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == 2 && resultCode == Activity.RESULT_OK && data != null){
            secilenGorsel = data.data




            if (secilenGorsel != null){
                if(Build.VERSION.SDK_INT >= 28){
                    val source = ImageDecoder.createSource(this.contentResolver,secilenGorsel!!)
                    secilenBitmap = ImageDecoder.decodeBitmap(source)
                    imgGorselSec.setImageBitmap(secilenBitmap)
                }
                else{
                    secilenBitmap = MediaStore.Images.Media.getBitmap(this.contentResolver,secilenGorsel)
                    imgGorselSec.setImageBitmap(secilenBitmap)
                }


            }



        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}