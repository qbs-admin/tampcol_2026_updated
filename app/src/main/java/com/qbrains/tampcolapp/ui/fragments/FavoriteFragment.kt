package com.qbrains.tampcolapp.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.qbrains.tampcolapp.data.dbhelper.LaaFreshDAO
import com.qbrains.tampcolapp.data.dbhelper.LaaFreshDB
import com.qbrains.tampcolapp.data.dbhelper.table.MyCartEntity
import com.qbrains.tampcolapp.data.dbhelper.table.ProductPageEntity
import com.qbrains.tampcolapp.data.network.api.reponse.ProductItem
import com.qbrains.tampcolapp.data.network.api.request.AddCartRequest
import com.qbrains.tampcolapp.data.preference.PreferenceManager
import com.qbrains.tampcolapp.data.viewmodel.VMFavorite
import com.qbrains.tampcolapp.databinding.FragmentFavoriteBinding
import com.qbrains.tampcolapp.ui.DashBoardActivity
import com.qbrains.tampcolapp.ui.adapter.FavoriteProductAdapter
import com.qbrains.tampcolapp.ui.extension.toast
//import kotlinx.android.synthetic.main.fragment_favorite.*


class FavoriteFragment : Fragment() {
    var db: LaaFreshDAO? = null
    var getIndividualProductsList = ArrayList<ProductPageEntity>()

    private var _binding : FragmentFavoriteBinding?=null
    private val binding get() = _binding!!

    val vmFavorite: VMFavorite by lazy {
        this.let {
            ViewModelProvider(it, VMFavorite.Factory(requireContext()))
                .get(VMFavorite::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFavoriteBinding.inflate(layoutInflater, container, false)

      return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        db = LaaFreshDB.getInstance(context = requireContext())?.laaFreshDAO()
        if (PreferenceManager(requireContext()).getIsLoggedIn()) {
            binding.pbFavoriteProduct.visibility = View.VISIBLE
            vmFavorite.getFavorite(onSuccess = {
                if (it.products != null) {
                    binding.pbFavoriteProduct.visibility = View.GONE
                    binding.tvEmptyWishList.visibility = View.GONE
                    binding.rvFavoriteProduct.visibility = View.VISIBLE
                    setProductAdapter(it.products)
                } else {
                    binding.pbFavoriteProduct.visibility = View.GONE
                    binding.tvEmptyWishList.visibility = View.VISIBLE
                    binding.rvFavoriteProduct.visibility = View.GONE
                }
            }, onError = {
                binding.pbFavoriteProduct.visibility = View.GONE
                binding.tvEmptyWishList.visibility = View.VISIBLE
            })
        }

    }

    private fun setProductAdapter(productItem: List<ProductItem>) {
        binding.rvFavoriteProduct.apply {
            binding.rvFavoriteProduct.adapter =
                FavoriteProductAdapter { ProductId, delete, position, item ->
                    if (delete) {
                        (binding.rvFavoriteProduct.adapter as FavoriteProductAdapter).delProduct(
                            productItem,
                            position
                        )
                        vmFavorite.delfromWishlist(
                            item.price_variant!![0].id.toString(),
                            onSuccess = {
                                Log.e("addtoWishlist:>>> ", it.success.toString())
                            },
                            onError = {})
                    } else {
                        addToCart(item)
                        /* Toast.makeText(context, "product successfully added", Toast.LENGTH_SHORT)
                             .show()*/

//                    val productFrag= ProductFragment()
//                     productFrag.addToCart(ProductId)
//                    findNavController().navigate(
//                        R.id.action_favoriteFragment_to_cartFragment,
//                        bundleOf(
//                            "productId" to ProductId
//                        )
//                    )
                    }

                }
        }
        (binding.rvFavoriteProduct.adapter as FavoriteProductAdapter).addProduct(productItem)
    }

    override fun onResume() {
        super.onResume()
//        var count = db?.getAllCartCount()
//        if (count!! >= 0) {
//            setCartCount(count)
//        }
//        val productFrag=ProductFragment()
//        productFrag.addToCart()
    }
//    fun setCartCount(cartCount: Int) {
//        (activity as DashBoardActivity).setCartCount(cartCount)
//    }

    fun addToCart(product: ProductItem) {
        try {

            var cartEntity = db?.getQtyAndQtyValue(product.price_variant!![0].id.toString())
            var cartCount = db?.getCartCount(product.price_variant!![0].id.toString())
            //     TODO(.pricevariant[0] -> .pricevariant[spinner position])
            Log.e("cartdata>>>", (ParseDouble(product.price_variant?.get(0)?.discount)).toString())
            /**
             * productWight = product.price_variant[0].product_weight!!.toString(),
             */

            if (cartCount == 0) {
                var myCard = MyCartEntity(
                    productCode = product.product_id.toString(),
                    productName = product.title.toString(),
                    orderQty = 1,
                    orderValue = product.price_variant?.get(0)?.price!!.toDouble(),
                    productWight = product.price_variant[0].product_weight!!.toString(),
                    productURI = product.price_variant[0].image!!,
                    priceId = product.price_variant[0].id.toString(),
                    user_id = "",
                    discountAmount = ParseDouble(product.price_variant[0].discount),
                    subtotal = ParseDouble(product.price_variant[0].price),
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
                vmFavorite.addCart(addCartRequest,
                    {
                        requireActivity().toast(it.message ?: "")
                        (requireActivity() as DashBoardActivity).setCartCount(it.cartCount ?: 0)
                    },
                    {
                        requireActivity().toast(it.toString())
                    })
            } else {
                var myCard = MyCartEntity(
                    productCode = product.product_id.toString(),
                    productName = product.title.toString(),
                    orderQty = cartEntity?.orderQty!! + 1,
                    orderValue = ((product.price_variant?.get(0)?.price!!.toDouble()) * (cartEntity?.orderQty!! + 1)),
                    productWight = product.price_variant[0].product_weight!!.toString(),
                    productURI = product.price_variant[0].image!!,
                    priceId = product.price_variant[0].id.toString(),
                    user_id = "",
                    discountAmount = ParseDouble(product.price_variant[0].discount),
                    subtotal = ParseDouble(product.price_variant[0].price),
                    tax = 0.0
                )
//                orderValue =(getIndividualProductsList[0].salePrice.toDouble() * (cartEntity?.orderQty!!+1)) + getIndividualProductsList[0].tax.toDouble(),

//                discountAmount = getIndividualProductsList[0].discount.toDouble(),
//                subtotal = getIndividualProductsList[0].salePrice.toDouble(),
//                tax =getIndividualProductsList[0].tax.toDouble()
//                db?.insertMyCard(myCard)
                val addCartRequest = AddCartRequest()
                addCartRequest.productPriceId = product.price_variant[0].id.toString()
                if (PreferenceManager(requireContext()).getIsLoggedIn())
                    addCartRequest.userId = PreferenceManager(requireContext()).getCustomerId()
                else
                    addCartRequest.deviceToken =
                        PreferenceManager(requireContext()).getDeviceToken()
                vmFavorite.addCart(addCartRequest,
                    {
                        requireActivity().toast(it.message ?: "")
                        (requireActivity() as DashBoardActivity).setCartCount(it.cartCount ?: 0)
                    },
                    {
                        requireActivity().toast(it.toString())
                    })
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun ParseDouble(value: String?): Double {
        return if (value != null && value.isNotEmpty()) {
            try {
                value.toDouble()
            } catch (e: java.lang.Exception) {
                (-1).toDouble()
            }
        } else 0.0
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

}