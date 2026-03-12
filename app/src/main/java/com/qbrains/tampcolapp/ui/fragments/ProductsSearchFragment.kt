package com.qbrains.tampcolapp.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.qbrains.tampcolapp.R
import com.qbrains.tampcolapp.data.dbhelper.LaaFreshDAO
import com.qbrains.tampcolapp.data.dbhelper.LaaFreshDB
import com.qbrains.tampcolapp.data.dbhelper.table.ProductEntity
import com.qbrains.tampcolapp.data.dbhelper.table.ProductPageEntity
import com.qbrains.tampcolapp.data.network.api.reponse.ProductItem
import com.qbrains.tampcolapp.data.viewmodel.VMProduct
import com.qbrains.tampcolapp.databinding.FragmentProductsSearchBinding
import com.qbrains.tampcolapp.ui.adapter.ProductAdapter
import com.qbrains.tampcolapp.ui.extension.toast
//import kotlinx.android.synthetic.main.fragment_products_search.*

class ProductsSearchFragment : Fragment() {

    private var _binding : FragmentProductsSearchBinding?=null
    private val binding get() = _binding!!

    private val mAdapter =
        ProductAdapter { ProductId, priceVarientPosition, move, varientId, product ->
/*       if (moveToCart) {
           addToCart(productId)
           findNavController().navigate(
               R.id.action_productSearchFragment_to_addingToCartFragment,
               bundleOf(
                   "productId" to productId
               )
           )
       } else {*/
           findNavController().navigate(
               R.id.action_productSearchFragment_to_productDetailsFragment,
               bundleOf(
                   "productId" to ProductId,
                   "varientId" to varientId.toString()
               )
           )
       }

//    }

    private var db: LaaFreshDAO? = null
    private var individualProductsList = ArrayList<ProductEntity>()

    private val mViewModel: VMProduct by lazy {
        this.let {
            ViewModelProvider(it, VMProduct.Factory(requireContext()))
                .get(VMProduct::class.java)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentProductsSearchBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        db = LaaFreshDB.getInstance(context = requireContext())?.laaFreshDAO()
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query.isNullOrEmpty().not() && query!!.length > 2) {
                    getQueryList(query)
                } else requireActivity().toast("Please enter more than 2 characters!")
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        binding.ivBack.setOnClickListener { findNavController().navigateUp() }

//        searchView.findViewById<AppCompatImageView>(R.id.search_close_btn).setOnClickListener {
//            searchView.setQuery("", false)
//            searchView.clearFocus()
//            hideKeyboard()
//        }

        binding.rvProduct.adapter = mAdapter
    }

    private fun hideKeyboard() {
        if (binding.searchView != null) {
            val imm =
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(binding.searchView.windowToken, 0)
        }
    }


    private fun getQueryList(query: String) {
        showLoading(true)
        mViewModel.getServiceQueryResult(query, {
            showLoading(false)
            if (it.products.isNullOrEmpty().not()) {
                val productList = ArrayList<ProductPageEntity>()
                for (productsItem: ProductItem in it.products!!) {
                    productList.add(
                        ProductPageEntity(
                            productsItem.product_id!!.toInt(),
                            productsItem.title.toString(),
                            productsItem.category.toString(),
                            productsItem.description.toString(),
                            productsItem.sub_category.toString(),
                            productsItem.deal.toString(),
                            productsItem.price_variant!!
                        )
                    )
                }
                mAdapter.addProduct(productList as List<ProductPageEntity>,isSearch = true)
            }
        }, {
            showLoading(false)
        })
    }

    /*private fun addToCart(productId: String) {
        individualProductsList.clear()
        try {
            individualProductsList.clear()
            individualProductsList =
                db?.getProductItemsDetails(productId) as ArrayList<ProductEntity>

            var cartEntity = db?.getQtyAndQtyValue(productId)
            var cartCount = db?.getCartCount(productId)
            if (cartCount == 0) {
                var myCard = MyCartEntity(
                    productCode = individualProductsList[0].productId,
                    productName = individualProductsList[0].title,
                    orderQty = 1,
                    orderValue = individualProductsList[0].salePrice.toDouble(),
                    productWight = "",
                    productURI = individualProductsList[0].image,
                    priceId = "",
                    user_id = "",
                    discountAmount = ParseDouble(individualProductsList[0].discount),
                    tax = ParseDouble(individualProductsList[0].tax),
                    subtotal = ParseDouble(individualProductsList[0].salePrice) - ParseDouble(
                        individualProductsList[0].discount
                    ) + ParseDouble(individualProductsList[0].tax)
                )
                db?.insertMyCard(myCard)
            } else {
                var myCard = MyCartEntity(
                    productCode = individualProductsList[0].productId,
                    productName = individualProductsList[0].title,
                    orderQty = cartEntity.orderQty + 1,
                    orderValue = (individualProductsList[0].salePrice.toDouble() * (cartEntity.orderQty + 1)) + individualProductsList[0].tax.toDouble(),
                    productWight = "",
                    productURI = individualProductsList[0].image,
                    priceId = "",
                    user_id = "",
                    subtotal = (ParseDouble(individualProductsList[0].salePrice) * (cartEntity.orderQty + 1)) - (ParseDouble(
                        individualProductsList[0].discount
                    ) * (cartEntity.orderQty + 1)) + (ParseDouble(individualProductsList[0].tax) * (cartEntity.orderQty + 1)) + (ParseDouble(
                        individualProductsList[0].shipping_cost
                    ) * (cartEntity.orderQty + 1)),
                    discountAmount = (ParseDouble(individualProductsList[0].discount) * (cartEntity.orderQty + 1)),
                    tax = (ParseDouble(individualProductsList[0].tax) * (cartEntity.orderQty + 1)),
                )
                db?.insertMyCard(myCard)


            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }*/


    private fun showLoading(isLoading: Boolean) {
        if (isLoading)
            binding.progressBar.visibility = View.VISIBLE
        else
            binding.progressBar.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}