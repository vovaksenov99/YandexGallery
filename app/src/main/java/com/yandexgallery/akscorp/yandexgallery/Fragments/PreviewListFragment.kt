package com.yandexgallery.akscorp.yandexgallery.Fragments

import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.JsonParser
import com.yandexgallery.akscorp.yandexgallery.Adapters.PreviewGridAdapter
import com.yandexgallery.akscorp.yandexgallery.Essentials.Image
import com.yandexgallery.akscorp.yandexgallery.PUBLIC_FOLDER
import com.yandexgallery.akscorp.yandexgallery.R
import com.yandexgallery.akscorp.yandexgallery.SPAN_PREVIEW_RV_COUNT
import com.yandexgallery.akscorp.yandexgallery.Utilities.MessageSnackbar
import com.yandexgallery.akscorp.yandexgallery.Utilities.Utilities
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.preview_images_grid_fragment.view.*
import okhttp3.Call
import okhttp3.Callback
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.io.Serializable

/**
 * Created by AksCorp on 29.04.2018.
 * akscorp2014@gmail.com
 * web site aksenov-vladimir.herokuapp.com
 */

/**
 * Yandex API constant https://tech.yandex.ru/disk/api/reference/public-docpage
 */
private const val PUBLIC_RESOURCE_META_URL = "https://cloud-api.yandex.net/v1/disk/public/resources"
private const val IMAGES_LIMIT = 500
private const val SORT_BY = "name"
private const val PREVIEW_SIZE = "M"

/**
 * Time to reconnect
 */
private const val RECONNECT_TIMEOUT_MILLIS = 2000L

class PreviewListFragment : Fragment()
{
    lateinit var snackbar: MessageSnackbar
    var images: MutableList<Image> = mutableListOf<Image>()
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        val view = inflater.inflate(R.layout.preview_images_grid_fragment, container, false)
        
        snackbar = MessageSnackbar(activity!!, activity!!.main_activity_coordinator_layout)
        
        return view
    }
    
    override fun onActivityCreated(savedInstanceState: Bundle?)
    {
        super.onActivityCreated(savedInstanceState)
        
        if (savedInstanceState != null)
        {
            images = savedInstanceState.get("images") as MutableList<Image>
            activity!!.progressBar2.visibility = View.INVISIBLE
            initPreviewGrid()
        } else
        {
            getPublicFolderContent()
        }
    }
    
    private fun initPreviewGrid()
    {
        val layoutManager = GridLayoutManager(context, SPAN_PREVIEW_RV_COUNT)
        view!!.main_preview_rv_grid.setHasFixedSize(true)
        view!!.main_preview_rv_grid.layoutManager = layoutManager
        view!!.main_preview_rv_grid.isNestedScrollingEnabled = true
        val adapter = PreviewGridAdapter(context!!, images)
        view!!.main_preview_rv_grid.adapter = adapter
    }
    
    /**
     * Get all image list from public folder [PUBLIC_FOLDER]
     */
    private fun getPublicFolderContent()
    {
        val builder = OkHttpClient.Builder()
        val client = builder.build()
        val urlBuilder = HttpUrl.parse(PUBLIC_RESOURCE_META_URL)!!.newBuilder()
        urlBuilder.addQueryParameter("public_key", PUBLIC_FOLDER)
        urlBuilder.addQueryParameter("limit", IMAGES_LIMIT.toString())
        urlBuilder.addQueryParameter("sort", SORT_BY)
        urlBuilder.addQueryParameter("preview_size", PREVIEW_SIZE)
        urlBuilder.addQueryParameter("preview_crop", "true")
        
        val request = Request.Builder()
            .url(urlBuilder.build())
            .build()
        client.newCall(request).enqueue(publicFolderContentCallback())
    }
    
    override fun onSaveInstanceState(outState: Bundle)
    {
        super.onSaveInstanceState(outState)
        outState.putSerializable("images", images as Serializable)
    }
    
    /**
     * Get all data from [PUBLIC_FOLDER] folder and run [initPreviewGrid]
     */
    private fun publicFolderContentCallback(): Callback
    {
        return object : Callback
        {
            override fun onFailure(call: Call?, e: IOException?)
            {
                
                errorNotify()
                
                activity!!.runOnUiThread {
                    val handler = Handler()
                    handler.postDelayed({
                        getPublicFolderContent()
                    }, RECONNECT_TIMEOUT_MILLIS)
                }
            }
            
            override fun onResponse(call: Call?, response: Response?)
            {
                if (!response!!.isSuccessful)
                {
                    onFailure(call, IOException(response.code().toString() + response.message()))
                    return
                }
                
                val resp = response.body()?.string()
                
                images = getImagesList(resp!!)
                
                activity!!.runOnUiThread {
                    snackbar.dismiss()
                    activity!!.progressBar2.visibility = View.INVISIBLE
                    
                    initPreviewGrid()
                }
            }
        }
    }
    
    /**
     * Parse Yandex.Disk API response
     *
     * @param json - string by Yandex.Disk API with folder information and items
     * @return all image items from folder [PUBLIC_FOLDER]
     */
    fun getImagesList(json: String): MutableList<Image>
    {
        try
        {
            val writer = JsonParser()
            
            val jsonFolder = writer.parse(json)
            val items = jsonFolder.asJsonObject["_embedded"].asJsonObject["items"].asJsonArray
            val images = mutableListOf<Image>()
            
            for (item in items)
            {
                val item = item.asJsonObject
                if (item["media_type"].asString == "image")
                    when (item["mime_type"].asString)
                    {
                        "image/gif" -> images.add(Image(previewUrl = item["file"].asString,
                            url = item["file"].asString,
                            name = item["name"].asString,
                            size = item["size"].asLong,
                            created = item["created"].asString,
                            type = item["mime_type"].asString))
                        else -> images.add(Image(previewUrl = item["preview"].asString,
                            url = item["file"].asString,
                            name = item["name"].asString,
                            size = item["size"].asLong,
                            created = item["created"].asString,
                            type = item["mime_type"].asString))
                    }
            }
            return images
        } catch (e: Exception)
        {
            errorNotify()
            return mutableListOf()
        }
    }
    
    /**
     * Show snackbar with notify with error message
     */
    private fun errorNotify()
    {
        if (!Utilities.isInternetConnected(activity!!) && !snackbar.isShow())
            snackbar.show(activity!!.getString(R.string.offline_mod))
        else
            if (!snackbar.isShow())
                snackbar.show(activity!!.getString(R.string.unexpected_error))
    }
}