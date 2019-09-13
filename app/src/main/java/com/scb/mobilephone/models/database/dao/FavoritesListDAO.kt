package com.scb.mobilephone.models.database.dao

import androidx.room.*
import com.scb.mobilephone.models.database.entities.FavoritesEntity


@Dao
interface FavoritesListDAO {

    @Query("select * from favorite")
    fun queryFavoritesList(): List<FavoritesEntity>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addFavorite(favorites: FavoritesEntity)

    @Update
    fun updateFavoritesList(phoneslist: List<FavoritesEntity>)

    @Query("DELETE FROM favorite WHERE id = :id")
    fun deleteFavorite(id: Int)
}