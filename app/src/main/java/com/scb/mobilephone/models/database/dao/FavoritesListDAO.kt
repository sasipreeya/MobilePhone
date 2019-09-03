package com.scb.mobilephone.models.database.dao

import androidx.room.*
import com.scb.mobilephone.models.database.entities.FavoritesListEntity

@Dao
interface FavoritesListDAO {

    // favorites_list
    @Query("select favoritesList from favorites_list")
    fun queryFavoritesList(): FavoritesListEntity?

    @Insert
    fun addFavoritesList(favoritesList: FavoritesListEntity)

    @Update
    fun updateFavoritesList(favoritesList: FavoritesListEntity)

    @Delete
    fun deleteFavoritesList(favoritesList: FavoritesListEntity)
}