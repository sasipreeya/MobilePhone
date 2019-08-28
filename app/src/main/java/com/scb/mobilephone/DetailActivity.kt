package com.scb.mobilephone

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.ouattararomuald.slider.ImageSlider
import com.ouattararomuald.slider.SliderAdapter
import com.ouattararomuald.slider.loaders.picasso.PicassoImageLoaderFactory
import com.scb.mobilephone.models.PhotoBean
import com.scb.mobilephone.network.ApiInterface
import kotlinx.android.synthetic.main.activity_detail.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DetailActivity : AppCompatActivity() {

    private var mDataArray: ArrayList<PhotoBean> = ArrayList<PhotoBean>()
    lateinit var photoDetailURL: String

    private lateinit var imageSlider: ImageSlider
    private var imageUrls = arrayListOf(
        "http://i.imgur.com/CqmBjo5.jpg",
        "http://i.imgur.com/zkaAooq.jpg",
        "http://i.imgur.com/0gqnEaY.jpg"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        feedData()

        phoneName.text = intent.getStringExtra("name")
        phoneBrand.text = intent.getStringExtra("brand")
        phoneDetail.text = intent.getStringExtra("detail")
        phonePrice.text = "Price : " + intent.getDoubleExtra("price", 0.0).toString()
        phoneRating.text = "Rating : " + intent.getDoubleExtra("rating", 0.0).toString()
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
                    imageUrls.clear()
                    for (i in 0 until mDataArray.size) {
                        if (mDataArray[i].url.contains("http", true)) {
                            imageUrls.add(mDataArray[i].url)
                        } else {
                            imageUrls.add("https://" + mDataArray[i].url)
                        }
                    }
                    Log.d("scb_network", imageUrls.toString())

                    imageSlider = findViewById(R.id.phoneImage)
                    imageSlider.adapter = SliderAdapter(
                        applicationContext,
                        PicassoImageLoaderFactory(),
                        imageUrls = imageUrls
                    )
                }
            }

        })

    }

}
