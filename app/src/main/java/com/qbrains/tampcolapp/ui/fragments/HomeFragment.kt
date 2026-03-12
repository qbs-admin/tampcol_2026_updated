package com.qbrains.tampcolapp.ui.fragments

import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListView
import androidx.core.os.bundleOf
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView
import com.qbrains.tampcolapp.BuildConfig
import com.qbrains.tampcolapp.R
import com.qbrains.tampcolapp.data.dbhelper.LaaFreshDAO
import com.qbrains.tampcolapp.data.dbhelper.LaaFreshDB
import com.qbrains.tampcolapp.data.dbhelper.table.CategoriesItemEntity
import com.qbrains.tampcolapp.data.dbhelper.table.ProductPageEntity
import com.qbrains.tampcolapp.data.dbhelper.table.SubCategoriesItemEntity
import com.qbrains.tampcolapp.data.network.api.reponse.*
import com.qbrains.tampcolapp.data.network.api.request.AddCartRequest
import com.qbrains.tampcolapp.data.network.api.request.CartListRequest
import com.qbrains.tampcolapp.data.preference.PreferenceManager
import com.qbrains.tampcolapp.data.viewmodel.VMUser
import com.qbrains.tampcolapp.databinding.FragmentHomeBinding
import com.qbrains.tampcolapp.ui.DashBoardActivity
import com.qbrains.tampcolapp.ui.adapter.*
import com.qbrains.tampcolapp.ui.extension.isConnected
import com.qbrains.tampcolapp.ui.extension.toast
import com.qbrains.tampcolapp.ui.fragments.drawerFragment.BlockBusterFragment
import com.smarteist.autoimageslider.SliderView
//import kotlinx.android.synthetic.main.fragment_home.*
//import kotlinx.android.synthetic.main.fragment_home.view.*
import java.util.*
import kotlin.concurrent.timerTask
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class HomeFragment : Fragment(), NavigationView.OnNavigationItemSelectedListener {

    private var _binding: FragmentHomeBinding?=null
    private val binding get() = _binding!!

    var getIndividualProductsList = ArrayList<ProductPageEntity>()
    var db: LaaFreshDAO? = null
    var getSubCategoriesList = ArrayList<SubCategoriesItemEntity>()
    var getCategoriesList = ArrayList<CategoriesItemEntity>()
    lateinit var homepageResponse: HomePageResponse
    private lateinit var drawer: DrawerLayout

    lateinit var expandableLV: ExpandableListView
    lateinit var expandableLVadopter: MyExpandableListAdapter

    lateinit var group: ArrayList<String>
    lateinit var collection: HashMap<String, List<String>>


    val vmUser: VMUser by lazy {
        this.let {
            ViewModelProvider(it, VMUser.Factory(requireContext()))
                .get(VMUser::class.java)
        }
    }
    val time: Long = 6000

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        db = LaaFreshDB.getInstance(context = requireContext())?.laaFreshDAO()

        drawer(view)

        if (isConnected(requireActivity())) {
            getSlides()
            Timer().schedule(timerTask {
                getad1()
            }, 1000)

            getMiniOrder()

        }
        loadCategoryList()

        if (getCategoriesList.isNotEmpty()) {
            setCategoryAdapter(getCategoriesList as List<CategoriesItemEntity>)
        }
        if (isConnected(requireActivity())) {
            getCategories()
        } else {
            showLoading(false)
            if (getCategoriesList.size == 0)
                activity?.toast(getString(R.string.no_internet))
        }


        getHomepageData()
        getCartList()

        lifecycleScope.launch {
            delay(2000)
            checkVersionUpdate()
        }

    }


    fun updateProduct(products: List<blockItem>?) {
        db?.deleteAllProducts()
        val productList = ArrayList<ProductPageEntity>()
        for (productsItem: blockItem in products!!) {
            productList.add(
                ProductPageEntity(
                    productsItem.product_id!!.toInt(),
                    productsItem.title.toString(),
                    productsItem.category.toString(),
                    "productsItem..toString()",
                    "productsItem.sub_category.toString()",
                    productsItem.deal.toString(),
                    productsItem.price_variant!! as List<varient>
                )
            )
        }
        db?.insertProductItems(productList)
    }

    private fun getMiniOrder() {
        vmUser.getMiniOrder(onSuccess = {

        }, onError = {

        })
    }


    private fun drawer(view: View) {

        drawer = view.findViewById(R.id.drawer_layout)
        drawer.setScrimColor(Color.TRANSPARENT)
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
        binding.menu.setOnClickListener {
            drawer.open()
        }

        group = ArrayList()
        group.add("Category")

        loadCategoryList()
        getCategoriesList

        val child1: ArrayList<String> = ArrayList()
        for (i in getCategoriesList) {
            child1.add(i.categoryName)
        }

        collection = HashMap()

        collection[group[0]] = child1

        expandableLVadopter = context?.let {
            MyExpandableListAdapter(
                it,
                group, collection
            ) {
                drawer.close()
                val id: String = getCategoriesList[it].categoryId.toString()
                findNavController().navigate(
                    R.id.action_homeFragment_to_productFragment,
                    bundleOf(
                        "productId" to id
                    )
                )
            }
        }!!

        binding.drawerRv.setAdapter(expandableLVadopter)


        binding.drawerHome.setOnClickListener {
            drawer.close()
        }
        binding.draweContactus.setOnClickListener {
            drawer.close()

            findNavController().navigate(
                R.id.action_homeFragment_to_ContactusFragment
            )
        }

        binding.drawerFeatured.setOnClickListener {
            drawer.close()

            updateProduct(homepageResponse.featured)
            findNavController().navigate(
                R.id.action_homeFragment_to_blockBusterFragment,
                bundleOf(
                    "title" to "Featured Products ",
                )
            )
        }

        binding.drawerBlkBuster.setOnClickListener {
            drawer.close()

            updateProduct(homepageResponse.block)
            homepageResponse.block?.let { offers ->
                BlockBusterFragment.mBLockBusterOffer.addAll(
                    offers
                )
            }

            findNavController().navigate(
                R.id.action_homeFragment_to_blockBusterFragment,
                bundleOf(
                    "title" to "Block Buster offer",
                )
            )
        }

        binding.drawerTopSeller.setOnClickListener {
            drawer.close()

            updateProduct(homepageResponse.top)

            findNavController().navigate(
                R.id.action_homeFragment_to_blockBusterFragment,
                bundleOf(
                    "title" to "Top Sellers",
                )
            )
        }

        binding.ivSearch.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_productSearchFragment)
        }

    }

    private fun getSlides() {
        vmUser.getSlides(onSuccess = {
            if (it.success == 1) {
                Log.e("tagslide", it.slides?.get(0).toString())

                setSlidesAdapter((it.slides as List<SlidesItem>?)!!)
            }
        }, onError = {

        })
    }

    private fun getad1() {
        vmUser.getad1(onSuccess = {
            if (it.success == 1) {
                Glide.with(this)
                    .load(it.slides?.last()?.images.toString())
                    .into(binding.ad2)

            }
        }, onError = {

        })
    }

    private fun getCategories() {
        if (getCategoriesList.size == 0)
            showLoading(true)
        vmUser.getCategories(
            onSuccess = {
                showLoading(false)
                val categoriesList = ArrayList<CategoriesItemEntity>()
                for (categoriesItem: CategoriesItem in it.categories!!) {
                    categoriesList.add(
                        CategoriesItemEntity(
                            Integer.valueOf(categoriesItem.categoryId!!),
                            categoriesItem.categoryName.toString(),
                            categoriesItem.banner.toString(),
                        )
                    )
                }
                db?.insertCategoriesItem(categoriesList)
                if (getCategoriesList.isNullOrEmpty()) {
                    loadSubCategoryList()
                    setCategoryAdapter(getCategoriesList as List<CategoriesItemEntity>)
                }
            },
            onError = {
                showLoading(false)

            }
        )
    }

    private fun getHomepageData() {
        vmUser.getHomepageData(
            onSuccess = {
                showLoading(false)

                homepageResponse = it
                it.top?.let { it1 -> setFeaturedAdapter(it1) }

                setTopSellerAdapter(it.featured!!)

                it.block?.let { it1 ->
                    blockBuster(it1)
                }


            },
            onError = {
                showLoading(false)

            }
        )
    }

    private fun blockBuster(block: List<blockItem>?) {

        try {
            if (block!!.isNotEmpty()) {
                binding.rlBlockbuster.visibility = View.VISIBLE
            }


            val linearLayoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            binding.rvBlockbuster.layoutManager = linearLayoutManager

            binding.rvBlockbuster.apply {
                adapter = BlockbusterAdapter { productId, item, addtoCart, varientPosition ->
                    if (addtoCart) {
                        addToCart(item, varientPosition)
                        /*  Toast.makeText(context, "product successfully added", Toast.LENGTH_SHORT)
                              .show()*/
                    } else
                        findNavController().navigate(
                            R.id.action_homeFragment_to_productDetailsFragment,
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
            (binding.rvBlockbuster.adapter as BlockbusterAdapter).addDashBoard(block)

            val linearSnapHelper = LinearSnapHelper()
            linearSnapHelper.attachToRecyclerView(binding.rvBlockbuster)

            Timer().schedule(object : TimerTask() {
                override fun run() {
                    try {
                        binding.rvBlockbuster.let {
                            if (linearLayoutManager.findLastCompletelyVisibleItemPosition() < block.size - 1) {
                                linearLayoutManager.smoothScrollToPosition(
                                    binding.rvBlockbuster,
                                    RecyclerView.State(),
                                    linearLayoutManager.findLastCompletelyVisibleItemPosition() + 1
                                )
                            } else if (linearLayoutManager.findLastCompletelyVisibleItemPosition() == block.size - 1) {
                                linearLayoutManager.smoothScrollToPosition(
                                    binding.rvBlockbuster,
                                    RecyclerView.State(),
                                    0
                                )
                            }
                        }
                    } catch (e: Exception) {
                        // handler
                    }
                }
            }, 4000, time)
        } catch (e: Exception) {
            // handler
        }

    }


    private fun loadSubCategoryList() {
        getSubCategoriesList = db?.getSubCategoriesItem() as ArrayList<SubCategoriesItemEntity>
    }

    private fun loadCategoryList() {
        getCategoriesList = db?.getCategoriesItem() as ArrayList<CategoriesItemEntity>
    }


    private fun setSlidesAdapter(slides: List<SlidesItem>) {
        binding.slider.apply {
            val adapter = SliderAdapter()
            adapter.addSlides(slides)
            binding.slider.autoCycleDirection = SliderView.LAYOUT_DIRECTION_LTR
            binding.slider.setSliderAdapter(adapter)
            binding.slider.scrollTimeInSec = 3
            binding.slider.isAutoCycle = true
            binding.slider.startAutoCycle()
        }


    }


    private fun setCategoryAdapter(Category: List<CategoriesItemEntity>) {
        binding.rvSubcategories.apply {
            adapter = CategoryAdapter {

                Log.e("tagadopter", it)
                findNavController().navigate(
                    R.id.action_homeFragment_to_productFragment,
                    bundleOf(
                        "productId" to it
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
        (binding.rvSubcategories.adapter as CategoryAdapter).addDashBoardCategory(
            Category
        )
    }

    private fun setTopSellerAdapter(topsellerItems: List<blockItem>) {

        if (topsellerItems!!.isNotEmpty()) {
            binding.rlFeatured.visibility = View.VISIBLE
        }

        binding.rvTopSeller.apply {
            adapter = TopSellerAdapter { productId, item, addtoCart, varientPosition ->
                if (addtoCart) {
                    Handler().postDelayed({
                        addToCart(item, varientPosition)
                        /*Toast.makeText(context, "product successfully added", Toast.LENGTH_SHORT)
                            .show()*/
                    }, 500)
                } else
                    findNavController().navigate(
                        R.id.action_homeFragment_to_productDetailsFragment,
                        bundleOf(
                            "productId" to productId,
                            "blockbuster" to true,
                            "varientId" to item.price_variant!![varientPosition].id,
//                            "product" to item
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
        (binding.rvTopSeller.adapter as TopSellerAdapter).addDashBoard(
            topsellerItems
        )


    }

    private fun setFeaturedAdapter(featuredItems: List<blockItem>) {
        if (featuredItems.isNotEmpty()) {
            binding.rlTopSeller.visibility = View.VISIBLE
        }
        binding.rvFeatured.apply {
            adapter = FeaturedAdapter { productId, item, addtoCart, varientPosition ->
                if (addtoCart) {
                    addToCart(item, varientPosition)
//                    Toast.makeText(context, "product successfully added", Toast.LENGTH_SHORT).show()
                } else
                    findNavController().navigate(
                        R.id.action_homeFragment_to_productDetailsFragment,
                        bundleOf(
                            "productId" to productId,
                            "blockbuster" to true,
                            "varientId" to item.price_variant!![0].id,
//                            "product" to item
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
        (binding.rvFeatured.adapter as FeaturedAdapter).addDashBoard(
            featuredItems
        )

    }


    private fun showLoading(isLoading: Boolean) {
        if (!isAdded || _binding == null) return

        _binding?.pbHome?.visibility =
            if (isLoading) View.VISIBLE else View.GONE
    }

    fun addToCart(product: blockItem, position: Int) {
        val addCartRequest = AddCartRequest()
        addCartRequest.productPriceId = product.price_variant?.get(position)?.id.toString()
        if (PreferenceManager(requireContext()).getIsLoggedIn())
            addCartRequest.userId = PreferenceManager(requireContext()).getCustomerId()
        else
            addCartRequest.deviceToken =
                PreferenceManager(requireContext()).getDeviceToken()
        vmUser.addCart(addCartRequest,
            {
                requireActivity().toast(it.message ?: "")
                (requireActivity() as DashBoardActivity).setCartCount(it.cartCount ?: 0)
            },
            {
                requireActivity().toast(it.toString())
            })
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

    fun checkVersionUpdate() {

        Log.d("TAG", "Check version -- xx");
        vmUser.checkVersion(onSuccess = {
            Log.d("TAG", it.version.toString());
            Log.d("TAG", BuildConfig.VERSION_NAME);
            if (it.version != BuildConfig.VERSION_NAME) {//BuildConfig.VERSION_NAME
                AlertDialog.Builder(context)
                    .setMessage("Tampcol App update available please update to latest version")
                    .setPositiveButton("Update") { _, _ ->
                        showLoading(true)
                        val appPackageName = "com.qbrains.tampcolapp"
                        try {
                            startActivity(
                                Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse("market://details?id=$appPackageName")
                                )
                            )
                        } catch (anfe: ActivityNotFoundException) {
                            startActivity(
                                Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")
                                )
                            )
                        }
                    }.show()

            }
        }, onError = {

        })
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return true
    }

    private fun getCartList() {
        val cartListRequest = CartListRequest()
        if (PreferenceManager(requireContext()).getIsLoggedIn())
            cartListRequest.userId = PreferenceManager(requireContext()).getCustomerId()
        else
            cartListRequest.deviceToken =
                PreferenceManager(requireContext()).getDeviceToken()

        vmUser.getCartList(cartListRequest,
            { (requireActivity() as DashBoardActivity).setCartCount(it.products.size) }, {})
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}

