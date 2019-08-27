package com.scb.mobilephone

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.scb.mobilephone.models.PhoneBean
import com.scb.mobilephone.models.PhotoBean
import com.scb.mobilephone.network.ApiInterface
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.fragment_list.view.*
import kotlinx.android.synthetic.main.phone_list.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DetailActivity : AppCompatActivity() {

    private var mDataArray: ArrayList<PhotoBean> = ArrayList<PhotoBean>()
    lateinit var photoDetailURL: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        phoneName.setText(intent.getStringExtra("name"))
        phoneBrand.setText(intent.getStringExtra("brand"))
        phoneDetail.setText(intent.getStringExtra("detail"))

        Glide.with(this).load(intent.getStringExtra("image")).apply(RequestOptions.centerCropTransform())
            .into(phoneImage)

        feedData()
    }


    fun feedData() {
        photoDetailURL = "api/mobiles/${intent.getIntExtra("id", 0)}/images/"

        val _call = ApiInterface.getClient().getPhotos(photoDetailURL)
        // check url api
        Log.d("scb_network", _call.request().url().toString())

        _call.enqueue(object : Callback<List<PhotoBean>> {
            override fun onFailure(call: Call<List<PhotoBean>>, t: Throwable) {
                Log.d("scb_network", t.message.toString())
            }

            override fun onResponse(call: Call<List<PhotoBean>>, response: Response<List<PhotoBean>>) {
                Log.d("scb_network", response.body().toString())
                if (response.isSuccessful) {
                    mDataArray.clear()
                    mDataArray.addAll((response.body()!!))

                }
            }

        })

    }

}
