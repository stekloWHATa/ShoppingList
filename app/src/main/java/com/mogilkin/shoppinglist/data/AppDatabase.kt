package com.mogilkin.shoppinglist.data

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ShopItemDbModel::class], version = 1, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {

    abstract fun shopListDao() : ShopListDao

    companion object{
        private var INSTANCE: AppDatabase? = null
        private val LOCK = Any()
        private const val DB_NAME = "shop_item.db"

        fun getInstance(application: Application): AppDatabase{
            INSTANCE?.let {
                return it
            }
            synchronized(LOCK){// чтобы из двух потоков разных не могли обратиться одновременно
                // и тем самым создать 2 экземпляра бд
                INSTANCE?.let {// эта проверка добавлена для того, чтобы опять же из разных потоков
                    // не могли насоздавать экземпляров бд
                    return it
                }
                val db = Room.databaseBuilder(
                    application,
                    AppDatabase::class.java,
                    DB_NAME
                ).build()
                INSTANCE = db
                return db
            }
        }
    }
}