package com.qbrains.tampcolapp.data.dbhelper

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.qbrains.tampcolapp.data.dbhelper.table.*


@Database(entities = [
    (MyCartEntity::class), (DeliveryAddressEntity::class), (LoginEntity::class), (ProductPageEntity::class),
    (ProductEntity::class), (ProductCategoryEntity::class), (ProductPriceEntity::class), (SubCategoriesItemEntity::class), (CategoriesItemEntity::class),
],
    version = 6,exportSchema = false)
@TypeConverters(Converters::class)
abstract class LaaFreshDB : RoomDatabase( ) {
    abstract fun laaFreshDAO(): LaaFreshDAO

    companion object {
        private var INSTANCE: LaaFreshDB? = null

//        val MIGRATION_4_5: Migration = object : Migration(2, 3) {
//            override fun migrate(database: SupportSQLiteDatabase) {
//                database.execSQL(
//                    "ALTER TABLE users "
//                            + " ADD COLUMN last_update INTEGER"
//                )
//            }
//        }

        fun getInstance(context: Context?): LaaFreshDB? {
            if (INSTANCE == null) {
                synchronized(LaaFreshDB::class) {
                    INSTANCE = Room.databaseBuilder(context!!.applicationContext,
                            LaaFreshDB::class.java, "Harina.db")
//                           .addMigrations(MIGRATION_4_5)
                            .allowMainThreadQueries()
                           .fallbackToDestructiveMigration()
                            .build()
                }
            }
            return INSTANCE
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }




}