package com.scb.mobilephone.presenters

import android.annotation.SuppressLint
import android.util.Log
import com.scb.mobilephone.models.PhotoBean
import com.scb.mobilephone.models.network.ApiInterface
import com.scb.mobilephone.presenters.interfaces.DetailInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailPresenter(_view : DetailInterface.DetailView): DetailInterface.DetailPresenter {

    private var view: DetailInterface.DetailView = _view
    private var mDetailArray: ArrayList<PhotoBean> = ArrayList()
    private lateinit var photoDetailURL: String

    companion object {
        @SuppressLint("StaticFieldLeak")
        var imageUrls = arrayListOf(
            "http://i.imgur.com/CqmBjo5.jpg",
            "http://i.imgur.com/zkaAooq.jpg",
            "http://i.imgur.com/0gqnEaY.jpg"
        )
    }

    override fun feedDetailData(id: Int) {
        photoDetailURL = "api/mobiles/${id}/images/"

        val call = ApiInterface.getClient().getPhotos(photoDetailURL)

        call.enqueue(object : Callback<List<PhotoBean>> {
            override fun onFailure(call: Call<List<PhotoBean>>, t: Throwable) {
                Log.d("error", t.message.toString())
            }

            override fun onResponse(call: Call<List<PhotoBean>>, response: Response<List<PhotoBean>>) {
                Log.d("response", response.body().toString())
                if (response.isSuccessful) {
                    mDetailArray.clear()
                    mDetailArray.addAll((response.body()!!))
                    imageUrls.clear()
                    for (i in 0 until mDetailArray.size) {
                        if (mDetailArray[i].url.contains("http", true)) {
                            imageUrls.add(mDetailArray[i].url)
                        } else {
                            imageUrls.add("https://" + mDetailArray[i].url)
                        }
                    }
                }
                view.showImageDetail(imageUrls)
            }

        })
    }
}