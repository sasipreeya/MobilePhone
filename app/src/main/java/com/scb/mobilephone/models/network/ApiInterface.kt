package com.scb.mobilephone.models.network

import com.scb.mobilephone.models.PhotoBean
import com.scb.mobilephone.models.database.entities.PhonesListEntity
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Url


interface ApiInterface {

    @GET("api/mobiles/")
    fun getPhones(): Call<List<PhonesListEntity>>

    @GET
    fun getPhotos(@Url url: String): Call<List<PhotoBean>>

    companion object Factory {
        private const val BASE_URL = "https://scb-test-mobile.herokuapp.com/"

        private var retrofit: Retrofit? = null

        fun getClient(): ApiInterface {
            if (retrofit == null) {
                retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return retrofit!!.create(ApiInterface::class.java)
        }
    }
}