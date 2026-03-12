    package com.qbrains.tampcolapp.ui.fragments

    import android.os.Bundle
    import android.util.Log
    import android.view.LayoutInflater
    import android.view.View
    import android.view.ViewGroup
    import android.widget.AdapterView
    import android.widget.ArrayAdapter
    import androidx.core.os.bundleOf
    import androidx.fragment.app.Fragment
    import androidx.lifecycle.ViewModelProvider
    import androidx.navigation.fragment.findNavController
    import com.bumptech.glide.Glide
    import com.bumptech.glide.load.engine.DiskCacheStrategy
    import com.qbrains.tampcolapp.R
    import com.qbrains.tampcolapp.data.dbhelper.LaaFreshDAO
    import com.qbrains.tampcolapp.data.dbhelper.LaaFreshDB
    import com.qbrains.tampcolapp.data.dbhelper.table.MyCartEntity
    import com.qbrains.tampcolapp.data.dbhelper.table.ProductPageEntity
    import com.qbrains.tampcolapp.data.network.api.reponse.ProductItem
    import com.qbrains.tampcolapp.data.network.api.reponse.varient
    import com.qbrains.tampcolapp.data.network.api.request.AddCartRequest
    import com.qbrains.tampcolapp.data.preference.PreferenceManager
    import com.qbrains.tampcolapp.data.viewmodel.VMProduct
    import com.qbrains.tampcolapp.databinding.ContentCartActionBinding
    import com.qbrains.tampcolapp.databinding.FragmentProductBinding
    import com.qbrains.tampcolapp.databinding.FragmentProductDetailsBinding
    import com.qbrains.tampcolapp.ui.DashBoardActivity
    import com.qbrains.tampcolapp.ui.adapter.ProductAdapter
    import com.qbrains.tampcolapp.ui.extension.isConnected
    import com.qbrains.tampcolapp.ui.extension.toast

//    import kotlinx.android.synthetic.main.content_cart_action.*
//    import kotlinx.android.synthetic.main.fragment_product.*
//    import kotlinx.android.synthetic.main.fragment_product_details.ivProductDetailsImage
//    import kotlinx.android.synthetic.main.fragment_product_details.seller_discount_layout
//    import kotlinx.android.synthetic.main.fragment_product_details.spinner
//    import kotlinx.android.synthetic.main.fragment_product_details.ts_discount
//    import kotlinx.android.synthetic.main.fragment_product_details.ts_price

    class ProductFragment : Fragment() {

        private var _binding:FragmentProductBinding?=null
        private val binding get() = _binding!!


        private lateinit var productDetailsBind : FragmentProductDetailsBinding

        private lateinit var cartBinding:ContentCartActionBinding

        var db: LaaFreshDAO? = null
        var getProductsList = ArrayList<ProductPageEntity>()
        var getIndividualProductsList = ArrayList<ProductPageEntity>()
        var productId = "";
        var varientId = ""
        var position = 0
        val vmProduct: VMProduct by lazy {
            this.let {
                ViewModelProvider(it, VMProduct.Factory(requireContext()))
                    .get(VMProduct::class.java)
            }
        }

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View {
            _binding = FragmentProductBinding.inflate(layoutInflater, container, false)
            cartBinding = ContentCartActionBinding.bind(binding.contentProductCartView.root)

            productDetailsBind = FragmentProductDetailsBinding.inflate(layoutInflater, container, false)
            return binding.root
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            db = LaaFreshDB.getInstance(context = requireContext())?.laaFreshDAO()
            val bundle = this.arguments
            if (bundle != null) {
                productId = bundle.getString("productId")!!
                varientId = bundle.getString("varientId")?: ""


            }
            binding.tbProducts.setNavigationOnClickListener {
                findNavController().navigateUp()
            }
            loadProductList()
            if (getProductsList.isNotEmpty()) {
                setProductAdapter(getProductsList as List<ProductPageEntity>)
            }
            if (getProductsList.size == 0)
                showLoading(true)
            if (isConnected(requireActivity())) {
                getProducts()
            } else {
                showLoading(false)
                if (getProductsList.size == 0)
                    activity?.toast(getString(R.string.no_internet))
            }

            /** todo have to check
            var count = db?.getAllCartCount()
            if (count!! > 0) {
            content_ProductCart_view.visibility = View.VISIBLE
            tvCartCount.text = "Items- ${count}"
            content_ProductCart_view.setOnClickListener {
            findNavController().navigate(R.id.action_productFragment_to_mycartFragment,
            null,
            navOptions {
            popUpTo(R.id.homeFragment) {
            inclusive = true
            }
            })
            }
            }
             */
            Log.e("taggetproductid: ", productId)
        }

        private fun setSpinnerAdapter(priceVariant: List<varient>?) {

            var spinnerList = ArrayList<String>()
            for (spinnerItem in priceVariant!!) {
                spinnerList.add(spinnerItem.product_weight.toString())
            }

            val adapter = ArrayAdapter<String>(
                requireContext(),
                R.layout.spinner_item, spinnerList
            )

            productDetailsBind.spinner.adapter = adapter
            productDetailsBind.spinner?.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(parent: AdapterView<*>?) {}

                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        positio: Int,
                        id: Long
                    ) {
                        productDetailsBind.tsPrice.text =
                            activity?.getString(R.string.Rs) + priceVariant!![positio].price.toString()
                        if (priceVariant[positio].discount_percent != "") {
                            val discountPrice =
                                priceVariant!![positio].discount?.toDouble()!! +
                                        priceVariant!![positio].price?.toDouble()!!
                            productDetailsBind.tsDiscount.text = discountPrice.toString()
                            position = positio
                            varientId = priceVariant[positio].id.toString()
                        } else {
                            productDetailsBind.sellerDiscountLayout.visibility = View.GONE
                        }
                        Glide.with(productDetailsBind.ivProductDetailsImage)
                            .load(priceVariant[positio].image)
                            .diskCacheStrategy(DiskCacheStrategy.DATA)
                            .into(productDetailsBind.ivProductDetailsImage)
                        position = positio
                    }


                }
        }


        fun getProducts() {
            showLoading(true)
            vmProduct.getProductDetails(productId, onSuccess = {
                showLoading(false)
                if (it.products != null) {
                    binding.rvProduct.visibility = View.VISIBLE
                    binding.tvEmptyProducts.visibility = View.GONE
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
    //            setProductAdapter(productList as List<ProductPageEntity>)
                    db?.deleteAllProducts()
                    db?.insertProductItems(productList)
    //            if (getProductsList.isNullOrEmpty()) {
                    loadProductList()
                    setProductAdapter(getProductsList as List<ProductPageEntity>)
    //                rvProduct.adapter?.notifyDataSetChanged()
    //            }

                    setSpinnerAdapter(productList[0].pricevariant)
                } else {
                    binding.rvProduct.visibility = View.GONE
                    binding.tvEmptyProducts.visibility = View.VISIBLE
                }
            }, onError = {
                showLoading(false)
            })
        }

        private fun loadProductList() {
            getProductsList.clear()
            getProductsList = db?.getProductItems() as ArrayList<ProductPageEntity>
        }

        private fun setProductAdapter(productItem: List<ProductPageEntity>) {
            binding.rvProduct.apply {
                binding.rvProduct.adapter =
                    ProductAdapter { productId, priceVarientPosition, moveToCart, varientID, product ->
                        if (moveToCart) {
                            System.out.println("Before: "+varientID.toString());
                            addToCart(productId, priceVarientPosition, varientID.toString(), product)
                            //Toast.makeText(context, "Product Added Successfully", Toast.LENGTH_SHORT)
                               // .show()

//                        findNavController().navigate(
//                            R.id.action_productFragment_to_mycartFragment,
//                            bundleOf(
//                                "productId" to productId
//                            )
//                        )
//                       findNavController().navigate(
//                           R.id.action_productFragment_to_mycartFragment,
//                           bundleOf(
//                                "productId" to productId,
//                                "varientId" to product.pricevariant[priceVarientPosition].id
//                            ) )
                        } else {
                            findNavController().navigate(
                                R.id.action_productFragment_to_productDetailsFragment,
                                bundleOf(
                                    "productId" to productId,
                                    "blockbuster" to true,
                                    "varientId" to varientID.toString()
                                )
                            )
                        }

                    }
            }
            (binding.rvProduct.adapter as ProductAdapter).addProduct(productItem)
        }

        //function made by Ashok kumar
        fun addToCart(
            productId: String,
            position: Int,
            priceVarientId: String,
            product: ProductPageEntity
        ) {
            try {
                val cartEntity = db?.getQtyAndQtyValue(priceVarientId)


                val cartCount = db?.getCartCount(priceVarientId)

                val orderQty = cartEntity?.orderQty ?: 0
                val orderValue = (orderQty + 1) * product.pricevariant[position].price!!.toDouble()

                val myCard = MyCartEntity(
                    productCode = product.product_id.toString(),
                    productName = product.title,
                    orderQty = orderQty + 1,
                    orderValue = orderValue,
                    productWight = product.pricevariant[position].product_weight.toString(),
                    productURI = product.pricevariant[position].image!!,
                    priceId = priceVarientId,
                    user_id = "",
                    discountAmount = ParseDouble(product.pricevariant[position].discount),
                    subtotal = ParseDouble(product.pricevariant[position].price),
                    tax = 0.0
                )

                db?.insertMyCard(myCard)
//                updateCartCount()

                val addCartRequest = AddCartRequest().apply {
//                    productPriceId = product.pricevariant[0].id.toString()
                    productPriceId = priceVarientId.toString();
                    System.out.println("Product Varient: "+productPriceId);
                    if (PreferenceManager(requireContext()).getIsLoggedIn()) {
                        userId = PreferenceManager(requireContext()).getCustomerId()
                    } else {
                        deviceToken = PreferenceManager(requireContext()).getDeviceToken()
                    }
                }
                vmProduct.addCart(
                    addCartRequest,
                    {
                        requireActivity().toast(it.message ?: "")
                        (requireActivity() as DashBoardActivity).setCartCount(it.cartCount ?: 0)
                    },
                    {
                        requireActivity().toast(it.toString())
                    }
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        /*
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

                val cartEntity = db?.getQtyAndQtyValue(priceVarientId) //as MyCartEntity
                val cartCount = db?.getCartCount(priceVarientId)
//                orderValue =getIndividualProductsList[0].salePrice.toDouble()
//                tax =ParseDouble(getIndividualProductsList[0].tax)
//                subtotal = ParseDouble(getIndividualProductsList[0].salePrice)
//                discountAmount = ParseDouble(getIndividualProductsList[0].discount)
                //     TODO(.pricevariant[0] -> .pricevariant[spinner position])
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

                    System.out.println(myCard);
                    db?.insertMyCard(myCard)
                    val addCartRequest = AddCartRequest()
                    addCartRequest.productPriceId = product.pricevariant[0].id.toString()
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
    //                orderValue =(getIndividualProductsList[0].salePrice.toDouble() * (cartEntity?.orderQty!!+1)) + getIndividualProductsList[0].tax.toDouble(),

    //                discountAmount = getIndividualProductsList[0].discount.toDouble(),
    //                subtotal = getIndividualProductsList[0].salePrice.toDouble(),
    //                tax =getIndividualProductsList[0].tax.toDouble()
    //                db?.insertMyCard(myCard)

                    val addCartRequest = AddCartRequest()
                    addCartRequest.productPriceId = product.pricevariant[0].id.toString()
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
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
*/ /*
   fun addToCart(productId: String,position:Int,priceVarientId :String) {
        getIndividualProductsList.clear()
        try{
        getIndividualProductsList.clear()
        getIndividualProductsList = db?.getProductItemsDetails(productId) as ArrayList<ProductPageEntity>

        val cartEntity=db?.getQtyAndQtyValue(priceVarientId) //as MyCartEntity
        val cartCount=db?.getCartCount(priceVarientId)
        //            orderValue =getIndividualProductsList[0].salePrice.toDouble()
        //            tax =ParseDouble(getIndividualProductsList[0].tax)
        //            subtotal = ParseDouble(getIndividualProductsList[0].salePrice)
        //            discountAmount = ParseDouble(getIndividualProductsList[0].discount)
        //     TODO(.pricevariant[0] -> .pricevariant[spinner position])
        if(cartCount==0){
        val myCard = MyCartEntity(
        productCode = getIndividualProductsList[0].product_id.toString(),
        productName = getIndividualProductsList[0].title,
        orderQty = 1,
        orderValue = getIndividualProductsList[0].pricevariant[position].price!!.toDouble(),
        productWight = getIndividualProductsList[0].pricevariant[position].product_weight!!.toString(),
        productURI = getIndividualProductsList[0].pricevariant[position].image!!,
        priceId = priceVarientId,
        user_id ="",
        discountAmount = ParseDouble(getIndividualProductsList[0].pricevariant[position].discount),
        subtotal = ParseDouble(getIndividualProductsList[0].pricevariant[position].price),
        tax =0.0
        )
        db?.insertMyCard(myCard)
        }
        else{
        Log.e("cartdata>>>",(cartEntity?.orderQty!!+1).toString())
        //                Log.e("cartdata>>>",(getIndividualProductsList[0].salePrice.toDouble() * (cartEntity?.orderQty!!+1)).toString())
        var myCard = MyCartEntity(
        productCode = getIndividualProductsList[0].product_id.toString(),
        productName = getIndividualProductsList[0].title,
        orderQty = cartEntity?.orderQty!!+1,
        orderValue =(getIndividualProductsList[0].pricevariant[position].price!!.toDouble() * (cartEntity?.orderQty!!+1)),
        productWight = "",
        productURI = getIndividualProductsList[0].pricevariant[position].image!!,
        priceId = priceVarientId,
        user_id ="",
        discountAmount = ParseDouble(getIndividualProductsList[0].pricevariant[position].discount),
        subtotal = ParseDouble(getIndividualProductsList[0].pricevariant[position].price),
        tax =0.0
        )
        //                orderValue =(getIndividualProductsList[0].salePrice.toDouble() * (cartEntity?.orderQty!!+1)) + getIndividualProductsList[0].tax.toDouble(),

        //                discountAmount = getIndividualProductsList[0].discount.toDouble(),
        //                subtotal = getIndividualProductsList[0].salePrice.toDouble(),
        //                tax =getIndividualProductsList[0].tax.toDouble()
        db?.insertMyCard(myCard)


        }
        }
        catch (e:Exception){
        e.printStackTrace()
        }
        }*/

    //-> add topseller btn --- priceId null


        fun ParseDouble(value: String?): Double {
            return if (value != null && value.isNotEmpty()) {
                try {
                    value.toDouble()
                } catch (e: java.lang.Exception) {
                    (-1).toDouble()
                }
            } else 0.0
        }


        private fun showLoading(isLoading: Boolean) {

            val pbProduct = binding.pbProduct

            if (pbProduct != null) {
                if (isLoading)
                    pbProduct.visibility = View.VISIBLE
                else
                    pbProduct.visibility = View.GONE
            }
        }

        override fun onDestroyView() {
            super.onDestroyView()

            _binding = null
        }
    }