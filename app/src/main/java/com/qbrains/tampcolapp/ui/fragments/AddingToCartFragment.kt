package com.qbrains.tampcolapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.qbrains.tampcolapp.R
import com.qbrains.tampcolapp.data.dbhelper.LaaFreshDAO
import com.qbrains.tampcolapp.data.dbhelper.LaaFreshDB
import com.qbrains.tampcolapp.data.dbhelper.table.ProductPageEntity
import com.qbrains.tampcolapp.data.viewmodel.VMProduct
import com.qbrains.tampcolapp.databinding.AddingCartFragmentBinding
import com.qbrains.tampcolapp.databinding.ContentCartActionBinding
import com.qbrains.tampcolapp.ui.extension.isConnected
import com.qbrains.tampcolapp.ui.extension.toSpanned
import com.qbrains.tampcolapp.ui.extension.toast
//import kotlinx.android.synthetic.main.adding_cart_fragment.*
//import kotlinx.android.synthetic.main.content_cart_action.*
//import kotlinx.android.synthetic.main.fragment_product_details.ivProductDetailsImage
//import kotlinx.android.synthetic.main.fragment_product_details.pbIndividualProduct
//import kotlinx.android.synthetic.main.fragment_product_details.tbProductsDetails
//import kotlinx.android.synthetic.main.fragment_product_details.tvDescriptionTitle
//import kotlinx.android.synthetic.main.fragment_product_details.tvProductDescription

class AddingToCartFragment : Fragment() {

    private var _binding: AddingCartFragmentBinding?=null
    private val binding get() = _binding!!

    private val cartContentBind = ContentCartActionBinding.bind(binding.contentCartView.root)


    var db: LaaFreshDAO? = null
    var getProductsList = ArrayList<ProductPageEntity>()
    var productId = ""
    var priceVariantId = ""
    val vmProduct: VMProduct by lazy {
        ViewModelProvider(this, VMProduct.Factory(requireContext()))
            .get(VMProduct::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
      _binding = AddingCartFragmentBinding.inflate(layoutInflater);

      return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val addLinearProductQty = view.findViewById<LinearLayoutCompat>(R.id.addLinearProductQty)
        addLinearProductQty.visibility = View.VISIBLE
        db = LaaFreshDB.getInstance(context = requireContext())?.laaFreshDAO()
        val bundle = this.arguments
        initAppBar()
        if (bundle != null) {
            productId = bundle.getString("productId")!!
            priceVariantId = bundle.getString("varientId")!!
        }
        getProductOffline(productId, priceVariantId)
        updateCartCount()

        binding.tvItemReduceProduct.setOnClickListener {
            decrementCartItem()
        }

        binding.tvItemAddProduct.setOnClickListener {
            incrementCartItem()
        }

        cartContentBind.root.setOnClickListener {
            findNavController().navigate(R.id.action_addAddressFragment_to_mycartFragment, null,
                navOptions {
                    popUpTo(R.id.homeFragment) {
                        inclusive = true
                    }
                })
        }
    }

    private fun getProductOffline(productId: String, priceVariantId: String) {
        getProductsList.clear()
        getProductsList = db?.getProductItemsDetails(productId) as ArrayList<ProductPageEntity>
        if (getProductsList.size == 0) {
            activity?.toast(getString(R.string.no_internet))
            if (isConnected(requireActivity())) {
                getProductOnline()
            }
        }
        // Load and display product details
        loadProductDetails()
    }

    private fun loadProductDetails() {
        Glide.with(this)
            .load(getProductsList[0].pricevariant[0].image)
            .diskCacheStrategy(DiskCacheStrategy.DATA)
            .into(binding.ivProductDetailsImage)
        binding.tvDescriptionTitle.text = getProductsList[0].title
        binding.tvProductDescription.text = getProductsList[0].description.toSpanned()
    }

    private fun getProductOnline() {
        vmProduct.getIndividualProduct(productId, onSuccess = {
            binding.pbIndividualProduct.visibility = View.GONE
            // Load and display product details
            loadProductDetails()
        }, onError = {
            binding.pbIndividualProduct.visibility = View.GONE
        })
    }

    private fun initAppBar() {
        binding.tbProductsDetails.apply {
            (activity as AppCompatActivity).setSupportActionBar(binding.tbProductsDetails)
            (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
            setNavigationOnClickListener { findNavController().navigateUp() }
        }
    }

    private fun decrementCartItem() {
        val qty = db?.getQtyAndQtyValue(priceVariantId)
        val myCartEntity = db?.checkItemAlreadyExist(priceVariantId)
        if (myCartEntity != null && qty != null) {
            if (myCartEntity.orderQty == 1) {
                db?.deleteMyCardItem(priceVariantId)
                updateCartCount()
                // Navigate to cart when the item is removed
                findNavController().navigate(R.id.action_addAddressFragment_to_mycartFragment, null,
                    navOptions {
                        popUpTo(R.id.productFragment) {
                            inclusive = true
                        }
                    })
            } else {
                db?.updateMyCart(
                    qty.orderQty - 1,
                    qty.subtotal * (qty.orderQty - 1),
                    priceVariantId
                )
                updateCartCount()
            }
        }
    }

    private fun incrementCartItem() {
        val qty = db?.getQtyAndQtyValue(priceVariantId)
        if (qty != null) {
            db?.updateMyCart(
                qty.orderQty + 1,
                qty.subtotal * (qty.orderQty + 1),
                priceVariantId
            )
            updateCartCount()
        }
    }

    private fun updateCartCount() {
        val count = db?.getAllCartCount()
        cartContentBind.tvCartCount.text = "Items - ${count ?: 0}"
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null

    }
}
