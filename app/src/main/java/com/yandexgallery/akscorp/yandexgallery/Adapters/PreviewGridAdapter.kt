package com.yandexgallery.akscorp.yandexgallery.Adapters

import android.app.Activity
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.yandexgallery.akscorp.yandexgallery.Essentials.Image
import com.yandexgallery.akscorp.yandexgallery.R
import kotlinx.android.synthetic.main.preview_rv_item.view.*
import android.content.Intent
import android.os.Bundle
import java.io.Serializable
import android.support.v4.app.ActivityOptionsCompat
import com.yandexgallery.akscorp.yandexgallery.Activities.DetailsActivity
import com.yandexgallery.akscorp.yandexgallery.Utilities.GlideApp

/**
 * Created by AksCorp on 03.04.2018.
 * akscorp2014@gmail.com
 * web site aksenov-vladimir.herokuapp.com
 */
class PreviewGridAdapter(private val context: Context, private val images: List<Image>) :
    RecyclerView.Adapter<PreviewGridAdapter.previewHolder>()
{
    /**
     * Image load animation duration [onBindViewHolder]
     */
    val TRANSITION_DURATION = 400
    
    override fun getItemCount(): Int
    {
        return images.size
    }
    
    override fun onCreateViewHolder(parent: ViewGroup,
        viewType: Int): PreviewGridAdapter.previewHolder
    {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val photoView = inflater.inflate(R.layout.preview_rv_item, parent, false)
        return previewHolder(photoView)
    }
    
    /**
     * Load image by [GlideApp] library from [PUBLIC_FOLDER] urls
     */
    override fun onBindViewHolder(holder: PreviewGridAdapter.previewHolder, position: Int)
    {
        val imageView = holder.preview
        
        GlideApp.with(context)
            .load(images[position].previewUrl)
            .centerCrop()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .placeholder(R.color.placeholder)
            .error(R.drawable.placeholder_image_error)
            .transition(DrawableTransitionOptions.withCrossFade(TRANSITION_DURATION))
            .into(imageView)
        
        imageView.setOnClickListener { v: View? -> imageOnClick(v!!, images[position]) }
    }
    
    /**
     * Go to Detail activity with selected image
     */
    fun imageOnClick(v: View, image: Image)
    {
        val intent = Intent(context, DetailsActivity::class.java)
        val bundle = Bundle()
        bundle.putSerializable("image", image as Serializable)
        intent.putExtras(bundle)
        
        val options =
            ActivityOptionsCompat.makeSceneTransitionAnimation(context as Activity, v as View,
                "preview_iv")
        
        context.startActivity(intent, options.toBundle())
    }
    
    inner class previewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        val preview: ImageView = itemView.preview_iv as ImageView
    }
}