package com.qbrains.tampcolapp.data.network.api.reponse

import com.google.gson.annotations.SerializedName

data class CategoriesResponse(

	@field:SerializedName(value = "categories")
	val categories: List<CategoriesItem>? = null,
)

data class CategoriesItem(

	@field:SerializedName(value = "category_id")
	val categoryId: String? = null,

	@field:SerializedName(value = "category_name")
	val categoryName: String? = null,


	@field:SerializedName(value = "banner")
	val banner: String? = null,

)
