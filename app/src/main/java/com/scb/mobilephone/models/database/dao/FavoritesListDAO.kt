package com.scb.mobilephone.models.database.dao

import androidx.room.*
import com.scb.mobilephone.models.database.entities.FavoritesEntity


@Dao
interface FavoritesListDAO {

    @Query("select * from favorite")
    fun queryFavoritesList(): List<FavoritesEntity>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addFavorite(favorites: FavoritesEntity)

    // update when sorted
    @Transaction
    fun updateData(favorites: List<FavoritesEntity>) {
        deleteAllFavorite()
        insertAll(favorites)
    }

    @Insert
    fun insertAll(favorites: List<FavoritesEntity>)

    @Query("DELETE FROM favorite")
    fun deleteAllFavorite()

    // delete favorite item
    @Query("DELETE FROM favorite WHERE id = :id")
    fun deleteFavorite(id: Int)
}