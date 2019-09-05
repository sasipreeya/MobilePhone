package com.scb.mobilephone.view

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ouattararomuald.slider.ImageSlider
import com.ouattararomuald.slider.SliderAdapter
import com.ouattararomuald.slider.loaders.picasso.PicassoImageLoaderFactory
import com.scb.mobilephone.R
import com.scb.mobilephone.presenters.interfaces.DetailInterface
import com.scb.mobilephone.presenters.DetailPresenter
import kotlinx.android.synthetic.main.activity_detail.*


class DetailActivity : AppCompatActivity(), DetailInterface.DetailView {

    lateinit var detailPresenter: DetailInterface.DetailPresenter
    private lateinit var imageSlider: ImageSlider

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        detailPresenter = DetailPresenter(this)
        detailPresenter.feedDatailData(intent.getIntExtra("id", 0))

        phoneName.text = intent.getStringExtra("name")
        phoneBrand.text = intent.getStringExtra("brand")
        phoneDetail.text = intent.getStringExtra("detail")
        phonePrice.text = "Price : " + intent.getDoubleExtra("price", 0.0).toString()
        phoneRating.text = "Rating : " + intent.getDoubleExtra("rating", 0.0).toString()
    }

    override fun showImageDetail(urlList: ArrayList<String>) {
        imageSlider = findViewById(R.id.phoneImage)
        imageSlider.adapter = SliderAdapter(
            applicationContext,
            PicassoImageLoaderFactory(),
            imageUrls = urlList
        )
    }
}
