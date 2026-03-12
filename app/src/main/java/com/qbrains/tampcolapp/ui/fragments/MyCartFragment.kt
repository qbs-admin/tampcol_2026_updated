package com.qbrains.tampcolapp.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.qbrains.tampcolapp.R
import com.qbrains.tampcolapp.data.dbhelper.LaaFreshDAO
import com.qbrains.tampcolapp.data.dbhelper.LaaFreshDB
import com.qbrains.tampcolapp.data.network.api.reponse.Product
import com.qbrains.tampcolapp.data.network.api.request.CartListRequest
import com.qbrains.tampcolapp.data.network.api.request.RemoveCartRequest
import com.qbrains.tampcolapp.data.network.api.request.UpdateCartRequest
import com.qbrains.tampcolapp.data.preference.PreferenceManager
import com.qbrains.tampcolapp.data.viewmodel.VMCart
import com.qbrains.tampcolapp.databinding.ContentOrderTotalBinding
import com.qbrains.tampcolapp.ui.DashBoardActivity
import com.qbrains.tampcolapp.ui.adapter.CartViewAdapterNew
import com.qbrains.tampcolapp.ui.extension.toast
import com.qbrains.tampcolapp.databinding.FragmentMyCartBinding
import kotlin.math.roundToInt

class MyCartFragment : Fragment() {
    private var db: LaaFreshDAO? = null
    private var getCartList = ArrayList<Product>()
    private var total: Double = 0.0
    private var subTotal: Double = 0.0
    private var shippingCost: Double = 0.0
    private var discount: Double = 0.0

    private val TAG = MyCartFragment::class.java.simpleName


    private val vmCart: VMCart by lazy {
        this.let {
            ViewModelProvider(it, VMCart.Factory(requireContext()))
                .get(VMCart::class.java)
        }
    }

    private var _binding: FragmentMyCartBinding? = null
    private val binding get() = _binding!!

    private lateinit var contentOrderBind: ContentOrderTotalBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyCartBinding.inflate(inflater, container, false)

        contentOrderBind = ContentOrderTotalBinding.bind(binding.contentOrder.root)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        db = LaaFreshDB.getInstance(context = requireContext())?.laaFreshDAO()
        getCartList()

        binding.btnCheckout.setOnClickListener {
            if (PreferenceManager(requireContext()).getIsLoggedIn()) {
                if (PreferenceManager(requireContext()).getMiniAmount().toInt()
                    <= total.roundToInt()
                ) {
                    val bundle = Bundle().apply {
                        putDouble("total", total)
                        putDouble("shippingCost", shippingCost)
                        putDouble("subTotal", subTotal)
                        putDouble("discount", discount)
                    }
                    findNavController().navigate(
                        R.id.action_myCartFragment_to_checkOutFragment,
                        bundle
                    )
                }
            } else {
                findNavController().navigate(R.id.loginFragment)
            }
        }

        contentOrderBind.tvCouponCodeLabel.visibility = View.GONE
        contentOrderBind.tvCouponCodeValue.visibility = View.GONE
    }

    private fun getCartProducts() {
        if (getCartList.isNotEmpty()) {
            setCartViewAdapter(getCartList)
        } else {
            contentOrderBind.root.visibility = View.GONE
            binding.btnCheckout.visibility = View.GONE
            binding.tvEmptyCart.visibility = View.VISIBLE
        }
    }

    private fun setCartViewAdapter(cartItems: List<Product>) {
        binding.rvCart.apply {
            adapter = CartViewAdapterNew { qty, qtyvalue, productid, isdelete, productPriceId ->
                if (isdelete) {
                    val removeCartRequest = RemoveCartRequest().apply {
                        if (PreferenceManager(requireContext()).getIsLoggedIn()) {
                            userId = PreferenceManager(requireContext()).getCustomerId()
                        } else {
                            deviceToken = PreferenceManager(requireContext()).getDeviceToken()
                        }
                        this.productPriceId = productPriceId
                    }
                    vmCart.removeCart(removeCartRequest, {
                        getCartList()
                    }, {
                        requireActivity().toast(it.toString())
                    })
                } else {
                    val updateCartRequest = UpdateCartRequest().apply {
                        if (PreferenceManager(requireContext()).getIsLoggedIn()) {
                            userId = PreferenceManager(requireContext()).getCustomerId()
                        } else {
                            deviceToken = PreferenceManager(requireContext()).getDeviceToken()
                        }
                        this.productPriceId = productPriceId
                        this.qty = qty.toString()
                    }
                    vmCart.updateCart(updateCartRequest, {
                        getCartList()
                    }, {
                        requireActivity().toast(it.toString())
                    })
                }
            }
        }
        (binding.rvCart.adapter as CartViewAdapterNew).addCartItem(cartItems)
        setOnDisplayOrderDetails()
    }

    private fun setOnDisplayOrderDetails() {
        val subTotal = db?.getSubTotal() ?: 0.0
        val total = db?.getTotal() ?: 0.0
        val discount = db?.getDiscountTotal().toString()
        contentOrderBind.tvSubTotal.text = subTotal.toString()
        contentOrderBind.tvTotal.text = "%.2f".format(total).toDouble().toString()
        contentOrderBind.tvDiscount.text = discount
        Log.i(TAG, "setOnDisplayOrderDetails: Subtotal : $subTotal Total : $total Discount : $discount")
    }

    @SuppressLint("SetTextI18n")
    private fun checkMiniPrice(total: Double) {
        if (PreferenceManager(requireContext()).getMiniAmount().toInt() > total.toInt()) {
            contentOrderBind.relativeAwayPrice.visibility = View.VISIBLE
            contentOrderBind.tvAwayDescription.text =
                "Your order away from ₹ ${
                    PreferenceManager(requireContext()).getMiniAmount().toInt() - total.toInt()
                }"
        } else {
            contentOrderBind.relativeAwayPrice.visibility = View.GONE
        }
    }

    private fun getCartList() {
        val cartListRequest = CartListRequest().apply {
            if (PreferenceManager(requireContext()).getIsLoggedIn()) {
                userId = PreferenceManager(requireContext()).getCustomerId()
            } else {
                deviceToken = PreferenceManager(requireContext()).getDeviceToken()
            }
        }
        vmCart.getCartList(cartListRequest,
            {
                getCartList.clear()
                getCartList.addAll(it.products)
                (requireActivity() as DashBoardActivity).setCartCount(it.products.size)
                getCartProducts()
                PreferenceManager(requireContext()).setCartCout(getCartList.size.toString())
                checkMiniPrice(it.total)
                total = it.total
                subTotal = it.sub_total
                shippingCost = it.shipping_cost
                discount = it.total_discount
                contentOrderBind.tvSubTotal.text = "%.2f".format(it.sub_total)
                contentOrderBind.tvTotal.text = "%.2f".format(it.total)
                contentOrderBind.tvShippingCost.visibility = View.GONE
                contentOrderBind.tvShippingCostLabel.visibility = View.GONE
                contentOrderBind.tvShippingCost.text = "%.2f".format(it.shipping_cost)
                contentOrderBind.tvDiscount.text = "%.2f".format(it.total_discount)
            },
            {}
        )

        Log.e("Shipping Cost", shippingCost.toString())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
