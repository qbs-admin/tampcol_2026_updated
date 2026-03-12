package com.qbrains.tampcolapp.homemvc.model

data class Featured(
    val discount_variant: List<DiscountVariant>,
    val image_variant: List<ImageVariant>,
    val price_variant: List<PriceVariant>,
    val product_id: String,
    val stock_variant: List<StockVariant>,
    val title: String,
    val wishlist: String
)