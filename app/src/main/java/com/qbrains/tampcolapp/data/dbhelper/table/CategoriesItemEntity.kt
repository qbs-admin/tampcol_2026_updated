package com.qbrains.tampcolapp.data.dbhelper.table
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = ["categoryId"], unique = true)])
class CategoriesItemEntity {
    @PrimaryKey(autoGenerate = true)
    var categoryId: Int = 0
    var categoryName: String=""
    var banner: String=""

    constructor(
        categoryId: Int,
        categoryName: String,
        banner: String,
    ) {
        this.categoryId = categoryId
        this.categoryName = categoryName
        this.banner = banner

    }
}