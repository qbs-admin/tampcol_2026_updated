package com.qbrains.tampcolapp.homemvc.model

data class Top(
    val discount_variant: List<DiscountVariantX>,
    val image_variant: List<ImageVariantX>,
    val price_variant: List<PriceVariantX>,
    val sales: String,
    val stock_variant: List<StockVariantX>,
    val title: String,
    val wishlist: String
)