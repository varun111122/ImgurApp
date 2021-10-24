package com.example.imgurapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide

class SliderAdapter(
    val context: Context,
    val viewPager: ViewPager2,
    val imgList: MutableList<String>?
) :
    RecyclerView.Adapter<SliderAdapter.SliderViewHolder>() {
    inner class SliderViewHolder(var v: View) : RecyclerView.ViewHolder(v) {
        val img = v.findViewById<ImageView>(R.id.img)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderViewHolder {
        var view = LayoutInflater.from(parent.context)
            .inflate(R.layout.slide_item_container, parent, false)
        return SliderViewHolder(view)
    }

    override fun onBindViewHolder(holder: SliderViewHolder, position: Int) {
        var listImg = imgList!![position]
        Glide.with(context).load(listImg).into(holder.img)
        if (position == imgList.size -2){
            viewPager.post(run)
        }
    }
    val  run= Runnable {
        imgList!!.addAll(imgList)
        notifyDataSetChanged()
    }

    override fun getItemCount() = imgList!!.size

}