package com.example.prm4.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.prm4.data.model.KierowcaEntity

@Dao
interface KierowcaDao {
    @Query("SELECT * FROM kierowcy;")
    fun getAll(): List<KierowcaEntity>

    @Query("SELECT * FROM kierowcy ORDER BY imie ASC;")
    fun getAllSortedByName(): List<KierowcaEntity>

    @Insert
    fun addKierowca(newDish: KierowcaEntity)

    @Delete
    fun removeKierowca(kierowca: KierowcaEntity)

    @Query("DELETE FROM kierowcy WHERE id = :id")
    fun removeById(id: Int)

    @Update
    fun updateKierowca(newDish: KierowcaEntity)

    @Query("SELECT * FROM kierowcy WHERE id = :id")
    fun getById(id: Long): KierowcaEntity?
}