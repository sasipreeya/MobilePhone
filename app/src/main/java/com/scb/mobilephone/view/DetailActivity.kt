package com.scb.mobilephone.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.DisplayMetrics
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.scb.mobilephone.R
import com.scb.mobilephone.presenters.DetailPresenter
import com.scb.mobilephone.presenters.interfaces.DetailInterface
import com.scb.mobilephone.view.adapters.DetailPagerAdapter
import kotlinx.android.synthetic.main.activity_detail.*


class DetailActivity : AppCompatActivity(), DetailInterface.DetailView {

    private lateinit var detailPresenter: DetailInterface.DetailPresenter
    private lateinit var viewPager: ViewPager
    private lateinit var detailPagerAdapter: DetailPagerAdapter
    private var width:Int = 0
    private var height:Int = 0

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        viewPager = findViewById(R.id.imageViewPager)

        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        width = displayMetrics.widthPixels
        height = (displayMetrics.heightPixels * 35) / 100

        detailPresenter = DetailPresenter(this)
        detailPresenter.feedDetailData(intent.getIntExtra("id", 0))

        phoneName.text = intent.getStringExtra("name")
        phoneBrand.text = intent.getStringExtra("brand")
        phoneDetail.text = intent.getStringExtra("detail")
        phonePrice.text = "Price : " + intent.getDoubleExtra("price", 0.0).toString()
        phoneRating.text = "Rating : " + intent.getDoubleExtra("rating", 0.0).toString()
    }

    override fun showImageDetail(urlList: ArrayList<String>) {
        detailPagerAdapter =
            DetailPagerAdapter(this, urlList, width, height)
        val params = LinearLayout.LayoutParams(width, height)
        viewPager.layoutParams = params
        viewPager.adapter = detailPagerAdapter
    }
}
