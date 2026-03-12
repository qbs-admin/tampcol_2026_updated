package com.qbrains.tampcolapp.ui.fragments.drawerFragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.qbrains.tampcolapp.R
import com.qbrains.tampcolapp.data.dbhelper.LaaFreshDAO
import com.qbrains.tampcolapp.data.dbhelper.LaaFreshDB
import com.qbrains.tampcolapp.data.dbhelper.table.MyCartEntity
import com.qbrains.tampcolapp.data.dbhelper.table.ProductPageEntity
import com.qbrains.tampcolapp.data.network.api.reponse.blockItem
import com.qbrains.tampcolapp.data.network.api.request.AddCartRequest
import com.qbrains.tampcolapp.data.preference.PreferenceManager
import com.qbrains.tampcolapp.databinding.BlockBusterFragmentBinding
import com.qbrains.tampcolapp.ui.DashBoardActivity
import com.qbrains.tampcolapp.ui.adapter.BlockbusterAdapter
import com.qbrains.tampcolapp.ui.adapter.CategoryAdapter
import com.qbrains.tampcolapp.ui.adapter.ProductAdapter
import com.qbrains.tampcolapp.ui.extension.toast
//import kotlinx.android.synthetic.main.block_buster_fragment.*

class BlockBusterFragment : Fragment() {
    var getIndividualProductsList = ArrayList<ProductPageEntity>()
    var db: LaaFreshDAO? = null
    lateinit var products: List<ProductPageEntity>
    lateinit var title: String

    private var _binding:BlockBusterFragmentBinding?=null
    private val binding get() = _binding!!

    val viewModel: BlockBusterViewModel by lazy {
        this.let {
            ViewModelProvider(it)
                .get(BlockBusterViewModel::class.java)
        }
    }

    companion object {
        fun newInstance() = BlockBusterFragment()
        var mBLockBusterOffer = arrayListOf<blockItem>()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = BlockBusterFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
         }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        db = LaaFreshDB.getInstance(context = requireContext())?.laaFreshDAO()

        products = db?.getProductItems() ?: listOf()

        val bundle = this.arguments
        if (bundle != null)
            title = bundle.getString("title")!!

        binding.tbBlockbuster.title = title

        binding.tbBlockbuster.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        setProductAdapter()
    }

    private fun setProductAdapter() {
        if (title != "Block Buster offer") {
            binding.rvProduct.apply {
                binding.rvProduct.adapter =
                    ProductAdapter { productId, priceVarientPosition, moveToCart, varientID, product ->
                        if (moveToCart) {
                            addToCart(
                                productId,
                                priceVarientPosition,
                                varientID.toString(),
                                product
                            )
//                            Toast.makeText(
//                                context,
//                                "Product Added Successfully",
//                                Toast.LENGTH_SHORT
//                            ).show()

                        } else {
                            findNavController().navigate(
                                R.id.action_bbusterFragment_to_productDetailsFragment,
                                bundleOf(
                                    "productId" to productId,
                                    "blockbuster" to true,
                                    "varientId" to varientID.toString()
                                )
                            )
                        }

                    }
            }
            (binding.rvProduct.adapter as ProductAdapter).addProduct(products)
        } else {

            binding.rvProduct.apply {
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                adapter = BlockbusterAdapter { productId, item, addtoCart, varientPosition ->
                    if (addtoCart) {
                        addToCart(item, varientPosition)
//                        Toast.makeText(context, "Product successfully added", Toast.LENGTH_SHORT)
//                            .show()
                    } else
                        findNavController().navigate(
                            R.id.action_bbusterFragment_to_productDetailsFragment,
                            bundleOf(
                                "productId" to productId,
                                "blockbuster" to true,
                                "varientId" to item.price_variant!![0].id

                            )
                        )
                }
                val spacingInPixels = resources.getDimensionPixelSize(R.dimen.margin_4)
                addItemDecoration(
                    CategoryAdapter.ItemCategoryAdapterDecoration(
                        spacingInPixels
                    )
                )
            }
            (binding.rvProduct.adapter as BlockbusterAdapter).addDashBoard(mBLockBusterOffer)
        }
    }

    fun addToCart(
        productId: String,
        position: Int,
        priceVarientId: String,
        product: ProductPageEntity
    ) {
        getIndividualProductsList.clear()
        try {
            getIndividualProductsList.clear()
            getIndividualProductsList =
                db?.getProductItemsDetails(productId) as ArrayList<ProductPageEntity>

            val cartEntity = db?.getQtyAndQtyValue(priceVarientId)
            val cartCount = db?.getCartCount(priceVarientId)
            if (cartCount == 0) {
                val myCard = MyCartEntity(
                    productCode = product.product_id.toString(),
                    productName = product.title,
                    orderQty = 1,
                    orderValue = product.pricevariant[position].price!!.toDouble(),
                    productWight = product.pricevariant[position].product_weight!!.toString(),
                    productURI = product.pricevariant[position].image!!,
                    priceId = product.pricevariant[position].id.toString(),
                    user_id = "",
                    discountAmount = ParseDouble(product.pricevariant[position].discount),
                    subtotal = ParseDouble(product.pricevariant[position].price),
                    tax = 0.0
                )
//                db?.insertMyCard(myCard)
                val addCartRequest = AddCartRequest()
                addCartRequest.productPriceId = product.pricevariant[0].id.toString()
                if (PreferenceManager(requireContext()).getIsLoggedIn())
                    addCartRequest.userId = PreferenceManager(requireContext()).getCustomerId()
                else
                    addCartRequest.deviceToken =
                        PreferenceManager(requireContext()).getDeviceToken()
                viewModel.addCart(addCartRequest,
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
                    productName = product.title,
                    orderQty = cartEntity?.orderQty!! + 1,
                    orderValue = (product.pricevariant[position].price!!.toDouble() * (cartEntity?.orderQty!! + 1)),
                    productWight = product.pricevariant[position].product_weight.toString(),
                    productURI = product.pricevariant[position].image!!,
                    priceId = product.pricevariant[position].id.toString(),
                    user_id = "",
                    discountAmount = ParseDouble(product.pricevariant[position].discount),
                    subtotal = ParseDouble(product.pricevariant[position].price),
                    tax = 0.0
                )
//                db?.insertMyCard(myCard)
                val addCartRequest = AddCartRequest()
                addCartRequest.productPriceId = product.pricevariant[0].id.toString()
                if (PreferenceManager(requireContext()).getIsLoggedIn())
                    addCartRequest.userId = PreferenceManager(requireContext()).getCustomerId()
                else
                    addCartRequest.deviceToken =
                        PreferenceManager(requireContext()).getDeviceToken()
                viewModel.addCart(addCartRequest,
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


    fun addToCart(product: blockItem, position: Int) {
        try {
            getIndividualProductsList.clear()
            getIndividualProductsList =
                db?.getProductItemsDetails(product.product_id.toString()) as ArrayList<ProductPageEntity>

            val cartEntity =
                db?.getQtyAndQtyValue(product.price_variant?.get(position)?.id.toString())
            val cartCount = db?.getCartCount(product.price_variant?.get(position)?.id.toString())
            if (cartCount == 0) {
                val myCard = MyCartEntity(
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
                viewModel.addCart(addCartRequest,
                    {
                        requireActivity().toast(it.message ?: "")
                    },
                    {
                        requireActivity().toast(it.toString())
                    })
            } else {
                val myCard = MyCartEntity(
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

//                db?.insertMyCard(myCard)
                val addCartRequest = AddCartRequest()
                addCartRequest.productPriceId = product.price_variant[0].id.toString()
                if (PreferenceManager(requireContext()).getIsLoggedIn())
                    addCartRequest.userId = PreferenceManager(requireContext()).getCustomerId()
                else
                    addCartRequest.deviceToken =
                        PreferenceManager(requireContext()).getDeviceToken()
                viewModel.addCart(addCartRequest,
                    {
                        requireActivity().toast(it.message ?: "")
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

    override fun onDestroy() {
        mBLockBusterOffer.clear()
        super.onDestroy()
    }

}