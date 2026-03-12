package com.qbrains.tampcolapp.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.qbrains.tampcolapp.R
import com.qbrains.tampcolapp.data.dbhelper.LaaFreshDAO
import com.qbrains.tampcolapp.data.dbhelper.LaaFreshDB
import com.qbrains.tampcolapp.data.dbhelper.table.ProductPageEntity
import com.qbrains.tampcolapp.data.network.api.reponse.ProductItem
import com.qbrains.tampcolapp.data.network.api.reponse.varient
import com.qbrains.tampcolapp.data.network.api.request.AddCartRequest
import com.qbrains.tampcolapp.data.preference.PreferenceManager
import com.qbrains.tampcolapp.data.viewmodel.VMFavorite
import com.qbrains.tampcolapp.data.viewmodel.VMProduct
import com.qbrains.tampcolapp.databinding.FragmentProductDetailsBinding
import com.qbrains.tampcolapp.ui.DashBoardActivity
import com.qbrains.tampcolapp.ui.extension.isConnected
import com.qbrains.tampcolapp.ui.extension.toSpanned
import com.qbrains.tampcolapp.ui.extension.toast
//import kotlinx.android.synthetic.main.fragment_product_details.*

class ProductDetailsFragment : Fragment() {

    private var _binding:FragmentProductDetailsBinding?=null
    private val binding get() = _binding!!
    var db: LaaFreshDAO? = null
    var getProductsList = ArrayList<ProductPageEntity>()
    var productId = ""
    var varientId = ""
    var productName = ""
    var position = 0
    lateinit var product: ProductItem
    val vmProduct: VMProduct by lazy {
        this.let {
            ViewModelProvider(it, VMProduct.Factory(requireContext()))
                .get(VMProduct::class.java)
        }
    }

    val vmFavorite: VMFavorite by lazy {
        this.let {
            ViewModelProvider(it, VMFavorite.Factory(requireContext()))
                .get(VMFavorite::class.java)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductDetailsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        db = LaaFreshDB.getInstance(context = requireContext())?.laaFreshDAO()
        val bundle = this.arguments
        initAppBar()
        if (bundle != null) {
            productId = bundle.getString("productId")!!
            varientId = bundle.getString("varientId")!!
//            product= bundle.get("product") as blockItem
        }
        showLoading(true)
        if (bundle!!.getBoolean("blockbuster")) {
            getProductOnline()
        } else {
            getProductOffline(productId)
        }

        /////


        binding.productItemAddcart.setOnClickListener {
            shareProduct();
        }
        binding.addBtn.setOnClickListener() {
            if (::product.isInitialized)
                addToCart(product, position)

//            Toast.makeText(context, "Product Added Successfully", Toast.LENGTH_SHORT).show()

//            findNavController().navigate(
//                R.id.action_productDetailsFragment_to_cartFragment,
//                bundleOf(
//                    "productId" to productId
//                )
//            )
        }
        binding.productDetailsWishIcon!!.tag = "ic_favorite_border_black_18dp"
//        addOrRemoveWishList()
        binding.productItemWishList.setOnClickListener {
            if (PreferenceManager(requireContext()).getIsLoggedIn())
                addOrRemoveWishList()
            else
                findNavController().navigate(
                    R.id.action_productDetailsFragment_to_loginFragment,
//                bundleOf(
//                    "productId" to productId,
//                    "blockbuster" to true,
//                    "varientId" to item.price_variant!![0].id
//                )
                )

        }

    }

    private fun getProductOffline(productId: String) {
        getProductsList.clear()
        getProductsList = db?.getProductItemsDetails(productId) as ArrayList<ProductPageEntity>
        if (getProductsList.size == 0) {
            activity?.toast(getString(R.string.no_internet))
            if (isConnected(requireActivity())) {
                getProductOnline()
            }
        }

        if(getProductsList.size > 0) {
            if (getProductsList[0].deal == "no") {
                setSpinnerAdopter(getProductsList[0].pricevariant)
            } else {
                binding.tsPrice.text =
                    activity?.getString(R.string.Rs) + getProductsList[0].pricevariant!![0].price.toString()
                binding.spinner.visibility = View.GONE
                val discountPrice =
                    getProductsList[0].pricevariant[0].discount?.toDouble()!! +
                            getProductsList[0].pricevariant[0].price?.toDouble()!!
                binding.tsDiscount.text = discountPrice.toString()

            }
//        setSpinnerAdopter(getProductsList[0].pricevariant)


            Glide.with(this)
                .load(getProductsList[0].pricevariant[0].image)
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .into(binding.ivProductDetailsImage)
            binding.tvDescriptionTitle.text = getProductsList[0].title
            binding.tvProductDescription.text = getProductsList[0].description.toSpanned()
        }
    }

    private fun getProductOnline() {
        vmProduct.getIndividualProduct(productId, onSuccess = {
            product = it.products?.get(0)!!
            binding.pbIndividualProduct.visibility = View.GONE
            showLoading(false)
            Glide.with(this)
                .load(it.products!![0].price_variant!![0].image.toString())
                .into(binding.ivProductDetailsImage)
            binding.tvDescriptionTitle.text = it.products[0].title
            binding.tvProductDescription.text = it.products[0].description?.toSpanned()
            if (it.products[0].deal == "no") {
                setSpinnerAdopter(it.products[0].price_variant)
            } else {
                binding.tsPrice.text =
                    activity?.getString(R.string.Rs) + it.products[0].price_variant!![0].price.toString()
                binding.spinner.visibility = View.GONE
                val discountPrice =
                    it.products[0].price_variant!![0].discount?.toDouble()!! +
                            it.products[0].price_variant!![0].price?.toDouble()!!
                binding.tsDiscount.text = discountPrice.toString()

            }


        }, onError = {
            binding.pbIndividualProduct.visibility = View.GONE
        })
    }


    private fun setSpinnerAdopter(priceVariant: List<varient>?) {

        var spinnerList = ArrayList<String>()
        for (spinnerItem in priceVariant!!) {
            spinnerList.add(spinnerItem.product_weight.toString())
        }

        val adapter = ArrayAdapter<String>(
            requireContext(),
            R.layout.spinner_item, spinnerList
        )
        binding.spinner.adapter = adapter
        binding.spinner?.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {}

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    positio: Int,
                    id: Long
                ) {
                    binding.tsPrice.text =
                        activity?.getString(R.string.Rs) + priceVariant!![positio].price.toString()
                    if (priceVariant[positio].discount_percent != "") {
                        val discountPrice =
                            priceVariant!![positio].discount?.toDouble()!! +
                                    priceVariant!![positio].price?.toDouble()!!
                        binding.tsDiscount.text = discountPrice.toString()
                        position = positio
                        varientId = priceVariant[positio].id.toString()
                    } else {
                        binding.sellerDiscountLayout.visibility = View.GONE
                    }
                    Glide.with(binding.ivProductDetailsImage)
                        .load(priceVariant[positio].image)
                        .diskCacheStrategy(DiskCacheStrategy.DATA)
                        .into(binding.ivProductDetailsImage)
                    position = positio
                }


            }
    }


    private fun initAppBar() {
        binding.tbProductsDetails?.apply {
            (activity as AppCompatActivity).setSupportActionBar(binding.tbProductsDetails)
            (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
            setNavigationOnClickListener { findNavController().navigateUp() }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (binding.tbProductsDetails != null) {
            if (isLoading)
                binding.tbProductsDetails.visibility = View.VISIBLE
            else
                binding.tbProductsDetails.visibility = View.GONE
        }
    }

    private fun addOrRemoveWishList() {
        try {

            if (binding.productDetailsWishIcon.tag == "ic_favorite_border_black_18dp") {
                binding.productDetailsWishIcon.setImageResource(R.drawable.ic_favorite_red_24dp)
                binding.productDetailsWishIcon.tag = "ic_favorite_black_24dp"
                addtoWishlist()
//                wishListViewModel!!.callAddWishListService(userId, productCode!!)
            } else if (binding.productDetailsWishIcon.tag == "ic_favorite_black_24dp") {
                binding.productDetailsWishIcon.setImageResource(R.drawable.ic_favorite_border_black_18dp)
                binding.productDetailsWishIcon.tag = "ic_favorite_border_black_18dp"
//                wishListViewModel!!.callDeleteWishListService(userId, productCode!!)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun addtoWishlist() {
//        val request=AddWishListRequest()
//        request.productId=productId

//        request.userId=req
        Log.e("tagaddtoWishlist:>>> ", varientId.toString())
        vmFavorite.addtoWishlist(varientId, onSuccess = {
//            pbIndividualProduct.visibility = View.GONE
            Log.e("addtoWishlist:>>> ", it.success.toString())
            Log.e("addtoWishlist2:>>> ", it.message.toString())


        }, onError = {
//            pbIndividualProduct.visibility = View.GONE
            Log.e("addtoWishlist:>>> ", "error")

        })

    }

    private fun shareProduct() {
        val i = Intent(Intent.ACTION_SEND)
        i.type = "text/plain"
        i.putExtra(Intent.EXTRA_SUBJECT, "Sharing URL")
        i.putExtra(Intent.EXTRA_TEXT, "https://tampcol.com/")
        if (productName != null) {
            productName = productName.replace("&", "and").replace(" ", "_")
        }
        i.putExtra(
            Intent.EXTRA_TEXT,
            "https://tampcol.com/public/product/detail/${
                binding.tvDescriptionTitle.text.toString().replace(" ", "")
            }/$productId"
        )
        startActivity(Intent.createChooser(i, "Share URL"))
    }


    fun addToCart(product: ProductItem, position: Int) {
/*
        try {
            Log.e("tagaddToCart: ", "123456")
//            getIndividualProductsList.clear()
//            getIndividualProductsList = db?.getProductItemsDetails(product.product_id.toString()) as java.util.ArrayList<ProductPageEntity>

            var cartEntity =
                db?.getQtyAndQtyValue(product.price_variant?.get(position)?.id.toString())
            var cartCount = db?.getCartCount(product.price_variant?.get(position)?.id.toString())

            //     TODO(.pricevariant[0] - > .pricevariant[spinner position])
            if (cartCount == 0) {
                var myCard = MyCartEntity(
                    productCode = product.product_id.toString(),
                    productName = product.title.toString(),
                    orderQty = 1,
                    orderValue = product.price_variant?.get(position)?.price!!.toDouble(),
                    productWight = product.price_variant[position].product_weight!!.toString(),
                    productURI = product.price_variant[position].image!!,
                    priceId = product.price_variant[position].id.toString(),
                    user_id = "",
                    discountAmount = ParseDouble(product.price_variant[position].discount),
                    subtotal = ParseDouble(product.price_variant[position].price),
                    tax = 0.0
                )
//                db?.insertMyCard(myCard)
                val addCartRequest = AddCartRequest()
                addCartRequest.productPriceId = product.price_variant[0].id.toString()
                if (PreferenceManager(requireContext()).getIsLoggedIn())
                    addCartRequest.userId = PreferenceManager(requireContext()).getCustomerId()
                else
                    addCartRequest.deviceToken =
                        PreferenceManager(requireContext()).getDeviceToken()
                vmProduct.addCart(addCartRequest,
                    {
                        requireActivity().toast(it.message ?: "")
                        (requireActivity() as DashBoardActivity).setCartCount(it.cartCount ?: 0)
                    },
                    {
                        requireActivity().toast(it.toString())
                    })
            } else {
                Log.e("cartdata>>>", (cartEntity?.orderQty!! + 1).toString())
//                Log.e("cartdata>>>",(getIndividualProductsList[0].salePrice.toDouble() * (cartEntity?.orderQty!!+1)).toString())
                var myCard = MyCartEntity(
                    productCode = product.product_id.toString(),
                    productName = product.title.toString(),
                    orderQty = cartEntity?.orderQty!! + 1,
                    orderValue = (product.price_variant?.get(position)?.price!!.toDouble() * (cartEntity?.orderQty!! + 1)),
                    productWight = product.price_variant[position].product_weight.toString(),
                    productURI = product.price_variant[position].image!!,
                    priceId = product.price_variant[position].id.toString(),
                    user_id = "",
                    discountAmount = ParseDouble(product.price_variant[position].discount),
                    subtotal = ParseDouble(product.price_variant[position].price),
                    tax = 0.0
                )
*/

//                db?.insertMyCard(myCard)
        val addCartRequest = AddCartRequest()
        addCartRequest.productPriceId = product.price_variant!![position].id.toString()
        if (PreferenceManager(requireContext()).getIsLoggedIn())
            addCartRequest.userId = PreferenceManager(requireContext()).getCustomerId()
        else
            addCartRequest.deviceToken =
                PreferenceManager(requireContext()).getDeviceToken()
        vmProduct.addCart(addCartRequest,
            {
                requireActivity().toast(it.message ?: "")
                (requireActivity() as DashBoardActivity).setCartCount(it.cartCount ?: 0)
            },
            {
                requireActivity().toast(it.toString())
            })
    }
    /*   } catch (e: Exception) {
           e.printStackTrace()
       }*/
//    }

    fun ParseDouble(value: String?): Double {
        return if (value != null && value.isNotEmpty()) {
            try {
                value.toDouble()
            } catch (e: java.lang.Exception) {
                (-1).toDouble()
            }
        } else 0.0
    }
}