package com.example.firebaseapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.recycler_row.view.*

class HaberRecyclerAdapter(val postList: ArrayList<Post>) : RecyclerView.Adapter<HaberRecyclerAdapter.PostHolder>(){

    class PostHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.recycler_row,parent,false)
        return PostHolder(view)
    }

    override fun onBindViewHolder(holder: PostHolder, position: Int) {
        holder.itemView.txtRecyclerEmail.text = postList[position].kullaniciEmail
        holder.itemView.txtRecyclerYorum.text = postList[position].kullaniciYorum
        Picasso.get().load(postList[position].gorselUrl).into((holder.itemView.imgRecyclerGorsel))

    }

    override fun getItemCount(): Int {
        return  postList.size
    }
}


