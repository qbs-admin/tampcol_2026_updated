package com.qbrains.tampcolapp.ui.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.qbrains.tampcolapp.BuildConfig
import com.qbrains.tampcolapp.R
import com.qbrains.tampcolapp.data.dbhelper.LaaFreshDAO
import com.qbrains.tampcolapp.data.dbhelper.LaaFreshDB
import com.qbrains.tampcolapp.data.dbhelper.table.MyCartEntity
import com.qbrains.tampcolapp.data.interfaces.ProductionDataCallback
import com.qbrains.tampcolapp.data.network.api.reponse.CouponItem
import com.qbrains.tampcolapp.data.network.api.request.*
import com.qbrains.tampcolapp.data.preference.PreferenceManager
import com.qbrains.tampcolapp.data.viewmodel.VMCheckOut
import com.qbrains.tampcolapp.databinding.ContentOrderTotalBinding
import com.qbrains.tampcolapp.databinding.DefaultAddressBinding
import com.qbrains.tampcolapp.databinding.DialogAvailableCouponBinding
import com.qbrains.tampcolapp.databinding.FragmentCheckOutBinding
import com.qbrains.tampcolapp.ui.DashBoardActivity
import com.qbrains.tampcolapp.ui.adapter.CheckOutViewAdapter
import com.qbrains.tampcolapp.ui.adapter.CouponListAdapter
import com.qbrains.tampcolapp.ui.extension.isConnected
import com.qbrains.tampcolapp.ui.extension.showDialAddAdres
import com.qbrains.tampcolapp.ui.extension.toast
import com.razorpay.Checkout
import com.razorpay.ExternalWalletListener
import com.razorpay.PaymentData
import com.razorpay.PaymentResultWithDataListener
//import kotlinx.android.synthetic.main.content_order_total.*
//import kotlinx.android.synthetic.main.default_address.*
//import kotlinx.android.synthetic.main.dialog_available_coupon.*
//import kotlinx.android.synthetic.main.fragment_check_out.*
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt
import kotlin.reflect.jvm.internal.impl.metadata.ProtoBuf.Visibility


class CheckOutFragment : Fragment(), ProductionDataCallback{

    private var _binding: FragmentCheckOutBinding? = null
    private val binding get() = _binding!!

    private lateinit var orderTotalBind: ContentOrderTotalBinding
    private lateinit var defaultAddressBind: DefaultAddressBinding

    private var email: String = ""
    var db: LaaFreshDAO? = null
    var getCartList = ArrayList<MyCartEntity>()
    var sendCartDataRequest = SendCartDataRequest()
    val productDetails: ArrayList<ProductDetailsItem> = ArrayList()
    var shippingAddress: ShippingAddress? = null
    var paymentType = ""
    var total: Double = 0.0
    var shippingCost = 0.0
    var subtotal: Double = 0.0

    val sdf = SimpleDateFormat("dd-MM-YYYY")

    val currentDate = sdf.format(Date())
    private var transactionId = ""
    private var couponCod = ""
    private var gatewayPayment = ""
    private var orderId = ""
    private var saleId = 0

    private var couponDiscount = 0.0
    var blzRewardsPts: Int = 0
    var discount = 0.0
    var rewardPts = 1
    var rewardPtsRate = 1
    var min_point_reach = 1
    var rewardDiscounts = 0

    private var productionMode: Boolean = true;
    private var customerId: Int = 0;
    private var paymentError: Boolean = false;

    private var productionDataCallback: ProductionDataCallback? = null

    //   val currentDate:SimpleDateFormat
    val vmCheckOut: VMCheckOut by lazy {
        this.let {
            ViewModelProvider(it, VMCheckOut.Factory(requireContext()))
                .get(VMCheckOut::class.java)
        }
    }

    fun setProductionDataCallback(callback: ProductionDataCallback) {
        productionDataCallback = callback
    }

//    val vmUser: VMUser by lazy {
//        this.let {
//            ViewModelProvider(it, VMUser.Factory(this))
//                .get(VMUser::class.java)
//        }
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCheckOutBinding.inflate(inflater, container, false);



        orderTotalBind = ContentOrderTotalBinding.bind(binding.contentPlacementOrder.root)
        defaultAddressBind = DefaultAddressBinding.bind(binding.includeDefaultAddress.root)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        db = LaaFreshDB.getInstance(context = requireContext())?.laaFreshDAO()
        getCartList.clear()
        getCartList = db?.getMyCardList() as ArrayList<MyCartEntity>

        orderTotalBind.relativeAddCoupon.visibility = View.VISIBLE
        orderTotalBind.selectCoupon.visibility = View.VISIBLE
        orderTotalBind.savedRs.visibility = View.VISIBLE

//            vmCheckOut.checkCouponCode(edCouponCode.text.toString(),
//                onSuccess = {
//
//            },
//            onError = {
//
//            })

//        Volley Request Queue
        val requestQueue = Volley.newRequestQueue(context)
        customerId = PreferenceManager(requireContext()).getCustomerId().toInt();
        Log.e("CUSTOMER ID", customerId.toString());

        val productionObjectRequest = JsonObjectRequest(Request.Method.POST, BuildConfig.BASE_URL +
                "check_production", null, { response ->

            if (response.getInt("success") == 1) {
                try {
                    val productionData = JSONObject(response.getString("data"));
                    productionDataCallback?.onDataReceived(productionData)

                    Log.e("Production Data", productionData.toString());
                    Log.e(
                        "Production Data 1",
                        productionData.getBoolean("production").toString()
                    );
                    Log.e(
                        "Production Data 2",
                        productionData.getInt("allowed_user").toString()
                    );


//                    if (!productionData.getBoolean("production")) {
//
//                        paymentError = true;
//
//                        if (customerId == productionData.getInt("allowed_user")) {
//                            productionMode = false
//
//                            binding.rbCashOnDelivery.visibility = View.VISIBLE;
//                            binding.rbCashOnDelivery.isChecked = true
//
//                            binding.rbRazorpayPayment.visibility = View.GONE;
//                        }
//                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

        }, { error ->
            // Handle error response
            Log.e("Volley Error", error.toString())
        });

        requestQueue.add(productionObjectRequest);



        defaultAddressBind.root.setOnClickListener {
            findNavController().navigate(R.id.action_checkOutFragment_to_settingsFragment)
        }

        binding.rgPaymentType.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.rb_cash_on_delivery -> {
                    paymentType = "cash_on_delivery"
                }

                R.id.rb_online_payment -> {
                    paymentType = "payumoney"
                }

                R.id.rb_razorpay_payment -> {
                    paymentType = "razorpay"
                    /*   AlertDialog.Builder(context)
                           .setMessage("Razorpay payment gateway currently stopped. Sorry for inconvenience...")
                           .setPositiveButton("Okay") { dialog, _ ->
                               dialog.dismiss()
                           }.show()*/
                }
            }
        }

        if (isConnected(requireActivity())) {
            showLoading(true)
            vmCheckOut.getDefaultAddress(
                onSuccess = {
                    showLoading(false)
                    if (it.success == "1") {
                        it.address?.get(0)?.display?.let { it1 -> hideRadioButton(it1) }
                        binding.btnPlacement.visibility = View.VISIBLE
                        defaultAddressBind.tvDefaultName.text = it.address?.get(0)?.name
                        defaultAddressBind.tvAddressListDeliveryPhoneNumber.text =
                            it.address?.get(0)?.phone

                        email = it.address?.get(0)?.email.toString()
                        /*if (it.address?.get(0)?.min_point_reach?.toInt()!! < it.address!![0].reward_points!!.toInt()) {
                            claim_rew_pts.visibility = View.VISIBLE

                            rewardPts = it.address[0].reward_points!!.toInt()
                            blzRewardsPts = rewardPts
                            rewardPtsRate = it.address[0].reward_point_rate!!.toInt()
                            min_point_reach = it.address[0].min_point_reach!!.toInt()
                        }*/
                        defaultAddressBind.tvDefaultAddress.text =
                            "${it.address?.get(0)?.address1}\n${it.address?.get(0)?.address2}"
                        orderTotalBind.tvShippingCostLabel.visibility = View.VISIBLE
                        orderTotalBind.tvShippingCost.visibility = View.VISIBLE
                        shippingCost = it.address?.get(0)?.shipping_cost?.toDouble() ?: 0.0
                        orderTotalBind.tvShippingCost.text = "%.2f".format(shippingCost)
                        /* tvTotal.text = "%.0f".format(
                             db?.getTotal()!!.toDouble() + shippingCost.toDouble()
                         )*///.toDouble().toString()
//                        tvTotal.text= Math.round(value * 100) / 100.0((db?.getTotal()!!.toDouble() + shippingCost.toDouble()).toString())//.toDouble().toString()
//                        tvTotal.text=(db?.getTotal()!!.toDouble() + shippingCost.toDouble()).toString()

                        orderTotalBind.tvSubTotal.text = "%.2f".format(subtotal)
                        orderTotalBind.tvTotal.text = "%.2f".format(total.plus(shippingCost))
//        discount = db?.getDiscountTotal()!!
                        Log.e("tagdiscount", discount.toString());
                        orderTotalBind.tvDiscount.text = "%.2f".format(discount)
                        binding.myRewPts.text =
                            "Your Reward Points " + it.address?.get(0)?.reward_points
                        binding.minRewpts.text =
                            "Minimum reward points to redeem " + it.address?.get(0)?.min_point_reach

                        shippingAddress = ShippingAddress(
                            it.address?.get(0)?.zip,
                            it.address?.get(0)?.name,
                            currentDate,
                            paymentType,
                            it.address?.get(0)?.address2,
                            it.address?.get(0)?.city,
                            it.address?.get(0)?.phone,
                            it.address?.get(0)?.address1,
                            "",
                            it.address?.get(0)?.lastName,
                            "",
                            it.address?.get(0)?.state,
                            it.address?.get(0)?.area
                        )

                        scrollToLast()

                    } else {
                        activity?.showDialAddAdres(
                            title = "Please Add delivery Address",
                            adresDialog = {
                                if (it) {
                                    findNavController().navigate(R.id.action_checkOutFragment_to_addAddressFragment)
                                } else
                                    activity?.onBackPressed()
                            })
                    }
                },
                onError = {
                    showLoading(false)
                }
            )
        } else {
            showLoading(false)
            activity?.toast("Please Check the your Internet! ")
        }

        binding.rvOrderDetailsList.apply {
            binding.rvOrderDetailsList.adapter = CheckOutViewAdapter()
            (binding.rvOrderDetailsList.adapter as CheckOutViewAdapter).addCartItem(getCartList)
        }

        total = arguments?.getDouble("total", 0.0) ?: 0.0
        subtotal = arguments?.getDouble("subTotal", 0.0) ?: 0.0
        discount = arguments?.getDouble("discount", 0.0) ?: 0.0
//        shippingCost = arguments?.getDouble("shippingCost", 0.0) ?: 0.0

        /*total = db?.getTotal()!!.toDouble()
        total = "%.0f".format(total).toDouble()
        subtotal = db?.getSubTotal()!!*/

        /**
         * Order Placement*/
        binding.btnPlacement.setOnClickListener {
            it.isEnabled = false

            Log.e("production mode on click", productionMode.toString())
//            if (!productionMode) {
//                placeOrder()
//            }

            if (!paymentType.isNullOrEmpty()) {
                if (paymentType == "cash_on_delivery")
                    placeOrder()
                //TODO disable btn
                else if (paymentType == "razorpay") {



                    var totalString = orderTotalBind.tvTotal.text.toString()
                    var totalDouble = totalString.toDouble()
                    val solution: Double = Math.round(totalDouble * 1.0) / 1.0
                    sendCartDataRequest.buyer = PreferenceManager(requireContext()).getCustomerId()
                    sendCartDataRequest.shippingAddress = shippingAddress
                    sendCartDataRequest.paymentType = paymentType
                    sendCartDataRequest.subTotal = subtotal.toString()
                    sendCartDataRequest.grandTotal = solution.toString()
                    sendCartDataRequest.couponAmount = couponDiscount
                    sendCartDataRequest.couponCode = couponCod
                    sendCartDataRequest.shipping_cost =
                        shippingCost
                    vmCheckOut.createRazorpayOrderId(
                        sendCartDataRequest = sendCartDataRequest,
                        onSuccess = {
                            if (it.success.equals("1")) {
                                orderId = it.orderId ?: ""
                                saleId = it.saleId ?: 0
//                                startRazorPayPayment(it.orderId ?: "", solution)
                                startRazorPayPayment(it.orderId ?: "", totalDouble)
                            }
                        },
                        onError = {
                            activity?.toast(it.toString())
                        })
                } else
                    startPayment()
            } else {
                activity?.toast("Please Select the Payment type ")
                it.isEnabled = true

            }
        }


        orderTotalBind.btnApplyCoupon.setOnClickListener {
            couponCod = orderTotalBind.edCouponCode.text.toString()
            if (orderTotalBind.edCouponCode.text.toString().isEmpty())
                Toast.makeText(context, "not valid", Toast.LENGTH_SHORT).show()
            else
                applyCoupon(couponCod)
        }

        orderTotalBind.selectCoupon.setOnClickListener {
            it.isEnabled = false
            getCouponList()
        }
        orderTotalBind.claimRewPts.setOnClickListener {
            orderTotalBind.claimRewPts.visibility = View.GONE

            if (rewardPts > min_point_reach) {
//                 blzRewardsPts=rewardPts - min_point_reach
//                 rewardDiscounts=min_point_reach *rewardPtsRate

                total = ((db?.getTotal()!!
                    .toInt() - couponDiscount - rewardDiscounts + shippingCost.toInt()).toDouble())
                total = "%.0f".format(total).toDouble()


                applyFullRedeempts(rewardPts * rewardPtsRate)

//                tvTotal.text=(db?.getTotal()!!.toInt() -couponDiscount -rewardDiscounts + shippingCost.toInt()).toString()
                savedAmount()
            }
        }

        savedAmount()


        (requireActivity() as DashBoardActivity).transactionId.observe(requireActivity()) {
            showLoading(true);
            val placeRazorpayOrderRequest = PlaceRazorpayOrderRequest()
            placeRazorpayOrderRequest.buyer = sendCartDataRequest.buyer
            placeRazorpayOrderRequest.couponAmount = sendCartDataRequest.couponAmount
            placeRazorpayOrderRequest.couponCode = sendCartDataRequest.couponCode
            placeRazorpayOrderRequest.orderId = orderId
            placeRazorpayOrderRequest.saleId = saleId
            placeRazorpayOrderRequest.paymentId = it
            vmCheckOut.placeRazorpayOrder(
                placeRazorpayOrderRequest = placeRazorpayOrderRequest,
                onSuccess = {
                    if (it.success.equals("1")) {
//                        requireActivity().toast(it.message ?: "")
                        db?.deleteAllMyCardItem()
                        showLoading(false)
                        //activity?.toast(it.message.toString())
                        alertbox()
                    }
                },
                onError = {
                    showLoading(false)
                    activity?.toast(it.toString())
                }
            )
        }
    }


    fun applyFullRedeempts(availablepts: Int) {
//        var availablepts:Double =230 *100.0

        if (total.toInt() < availablepts) {
            blzRewardsPts = (availablepts - total).toInt()
            rewardDiscounts = total.roundToInt()


            total = 0.0
            orderTotalBind.tvTotal.text = "0"
        } else {
            blzRewardsPts = 0
            rewardDiscounts = availablepts

            total = (db?.getTotal()!!
                .toDouble() + shippingCost.toDouble()) - couponDiscount - availablepts
            total = "%.0f".format(total).toDouble()


            orderTotalBind.tvTotal.text = total.plus(shippingCost).toString()
//              tvTotal.text = ((db?.getTotal()!!.toDouble() + shippingCost.toDouble()) - (it.deductAmt!!.toDouble())).toString()
        }

    }


    private fun scrollToLast() {
        //TODO have to scroll down
//        scrollbar.postDelayed(
//            Runnable { scrollbar.smoothScrollTo(0, scrollbar.height) },
//            2000
//        )
    }

    fun placeOrder() {
        var totalString = orderTotalBind.tvTotal.text.toString()
        var totalDouble = totalString.toDouble()
        val solution: Double = Math.round(totalDouble * 1.0) / 1.0

        Log.e("placeOrder: solution", solution.toString())
        if (isConnected(requireActivity())) {
            showLoading(true)
            getCartList.forEach {

                productDetails.add(
                    ProductDetailsItem(
                        it.productCode,
                        it.productWight,
                        it.priceId,
                        it.orderQty,

                        )
                )
            }

            print(productDetails)


//            sendCartDataRequest.productDetails = productDetails
            sendCartDataRequest.shippingAddress = shippingAddress
            sendCartDataRequest.paymentType = paymentType
            sendCartDataRequest.subTotal = subtotal.toString()
            sendCartDataRequest.grandTotal = solution.toString()
            sendCartDataRequest.couponAmount = couponDiscount
            sendCartDataRequest.couponCode = couponCod
            sendCartDataRequest.shipping_cost =
                shippingCost
//            sendCartDataRequest.gatewayTxnid = transactionId
//            sendCartDataRequest.gatewayPayment = ""// gatewayPayment


//            "productdetails":[
//
//            {
//                "product_id":1933,
//                "product_weight":"100g",
//                "product_price_id":88,
//                "qty":5
//            }
//            ],
//
//            "gateway_payment":{}
//        }


            sendCartDataRequest.buyer = PreferenceManager(requireContext()).getCustomerId()
            Log.e("sendCartDataRequest", sendCartDataRequest.toString())

            vmCheckOut.placingOrders(sendCartDataRequest = sendCartDataRequest, onSuccess = {
                if (it.success.equals("1")) {
                    db?.deleteAllMyCardItem()
                    showLoading(false)

                    //activity?.toast(it.message.toString())
                    alertbox()
                }
            }, onError = {
                activity?.toast(it.toString())
            })


        } else {
            activity?.toast("Please Check the your Internet! ")
        }

    }


    fun getCouponList() {
        var mcouponList: List<CouponItem> = ArrayList<CouponItem>()
        val couponListRequest = CouponListRequest()
        couponListRequest.userId = PreferenceManager(requireContext()).getCustomerId().toInt()
        couponListRequest.grandTotal = total

        vmCheckOut.getCouponList(couponListRequest,
            onSuccess = {
//                if (it.success == 1) {
                mcouponList = it.couponDetails as ArrayList<CouponItem>
                Log.e("tagCouponDi: ", mcouponList[1].code.toString())
                selectCouponDialog(mcouponList as ArrayList<CouponItem>)
//                } else
//                    activity?.toast(it.message.toString())

            },
            onError = {

            })
    }


    private fun selectCouponDialog(mcouponList: ArrayList<CouponItem>) {
//        Log.e( "tagCouponDialog: ",mcouponList[1].code )

        val dialogBind = DialogAvailableCouponBinding.inflate(LayoutInflater.from(requireContext()))

        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)



        dialog.setContentView(dialogBind.root)

        dialogBind.rvCouponList.apply {
            dialogBind.rvCouponList.adapter = CouponListAdapter { couponCode ->
                run {
                    dialog.dismiss()
                    couponCod = couponCode
                    orderTotalBind.selectCoupon.isEnabled = true
                    orderTotalBind.edCouponCode.setText(couponCode)
                    applyCoupon(couponCode)
                }

            }
            (dialogBind.rvCouponList.adapter as CouponListAdapter).addCouponItem(mcouponList)
        }

        dialogBind.closeBtn.setOnClickListener {
            dialog.dismiss()
            orderTotalBind.selectCoupon.isEnabled = true
        }

        dialog.getWindow()
            ?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        dialog.show()

    }


    private fun applyCoupon(couponcode: String) {
        val request = GetCouponDetailsRequest()
        request.couponcode = couponcode
        request.grandTotal = total.toInt()
        request.shippingCost = shippingCost.roundToInt()


        val productArray: ArrayList<CouponProductItem> = ArrayList()


        if (isConnected(requireActivity())) {
            getCartList.forEach {
                productArray.add(
                    CouponProductItem(
                        it.priceId.toInt(),
                        it.orderQty
                    )
                )

                request.products = productArray

            }

        } else {
            activity?.toast("Please Check the your Internet! ")
        }



        vmCheckOut.applyCouponCode(request,
            onSuccess = {
                Log.e("tagapplyCoupon: ", it.deductAmt.toString())
                if (it.success == 1) {
                    couponDiscount = it.deductAmt!!
                    orderTotalBind.tvCouponCodeValue.text =
                        "%.2f".format(couponDiscount).toDouble().toString()
//                    total = ((db?.getTotal()!!
//                        .toDouble() + shippingCost.toDouble()) - (it.deductAmt!!.toDouble()) - rewardDiscounts)
//                    total = "%.0f".format(total).toDouble()
//                tvTotal.text = ((db?.getTotal()!!.toDouble() + shippingCost.toDouble()) - (it.deductAmt!!.toDouble())).toString()
                    orderTotalBind.tvTotal.text =
                        "%.2f".format(total.plus(shippingCost).minus(it.deductAmt)).toDouble()
                            .toString()
                    savedAmount()

                } else
                    activity?.toast(it.message.toString())
            }, onError = {

            })


    }


    private fun showLoading(isLoading: Boolean) {
        if (binding.pbCheckOut != null) {
            if (isLoading)
                binding.pbCheckOut.visibility = View.VISIBLE
            else
                binding.pbCheckOut.visibility = View.GONE
        }
    }


    override fun onResume() {
        super.onResume()
    }


    private val surl = "https://payuresponse.firebaseapp.com/success"
    private val furl = "https://payuresponse.firebaseapp.com/failure"


    //Test Key and Salt

//   private val testKey = "neiLRq"
//    private val testSalt = "9gjdBHreLBqOZkcL5ndQkxa934BTsd4p"


    /**
     * Enter below keys when integrating Multi Currency Payments.
     * To get these credentials, please reach out to your Key Account Manager at PayU
     * */
    private val merchantAccessKey = "<Please_add_your_merchant_access_key>"
    private val merchantSecretKey = "<Please_add_your_merchant_secret_key>"


    //Prod Key and Salt
    private val prodKey = "3TnMpV"
    private val prodSalt = "g0nGFe03"


    // variable to track event time
    private var mLastClickTime: Long = 0

    private val HarinaProductionKey = "2vKDoRip"
    private val HarinaProductionSalt = "ZxQop6Vy0S"


//TODO payment email id

    fun startPayment() {

        Log.e("TAG", "startPayment: $email,")
        getCartList.forEach {


            productDetails.add(
                ProductDetailsItem(
                    it.productCode,
                    it.productWight,
                    it.priceId,
                    it.orderQty,

                    )
            )
        }
        transactionId = System.currentTimeMillis().toString();
        // Preventing multiple clicks, using threshold of 1 second
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return
        }
        mLastClickTime = SystemClock.elapsedRealtime()

//        val paymentParams = preparePayUBizParams()
//        initUiSdk(paymentParams)
    }

    /*fun preparePayUBizParams(): PayUPaymentParams {

//tvTotal.text.toString()
        var totalString = tvTotal.text.toString()
        var totalDouble = totalString.toDouble()
        val solution: Double = Math.round(totalDouble * 1.0) / 1.0



        return PayUPaymentParams.Builder().setAmount(solution.toString())//total.toString()
            .setIsProduction(true)
            .setKey(HarinaProductionKey)
            .setProductInfo(productDetails.toString())
            .setPhone(tvAddressListDeliveryPhoneNumber.text.toString())
//            .setPhone("9876543210")
            .setTransactionId(transactionId)
            .setFirstName(tvDefaultName.text.toString())
            .setEmail(email)
            .setSurl(surl)
            .setFurl(furl)
            .setUserCredential("${HarinaProductionKey}:$email")
//            .setAdditionalParams(additionalParamsMap)
//            .setPayUSIParams(siDetails)
            .build()


    }

    private fun initUiSdk(payUPaymentParams: PayUPaymentParams) {
        PayUCheckoutPro.open(
            requireActivity(),
            payUPaymentParams,
            getCheckoutProConfig(),
            object : PayUCheckoutProListener {

                override fun onPaymentSuccess(response: Any) {
                    response as HashMap<*, *>
//                    processResponse(response)

                    gatewayPayment =
                        "Payu's Data : " + response.get(PayUCheckoutProConstants.CP_PAYU_RESPONSE) + "\n\n\n Merchant's Data: " + response.get(
                            PayUCheckoutProConstants.CP_MERCHANT_RESPONSE
                        )

                    placeOrder()

                }

                override fun onPaymentFailure(response: Any) {
//                    processResponse(response)
                    showSnackBar("Something went wrong please try again later")
                    btnPlacement.isEnabled = true

                }

                override fun onPaymentCancel(isTxnInitiated: Boolean) {
                    showSnackBar(resources.getString(R.string.transaction_cancelled_by_user))
                    btnPlacement.isEnabled = true
                }

                override fun onError(errorResponse: ErrorResponse) {
                    showSnackBar("Something went wrong please try again later")
                    btnPlacement.isEnabled = true
                    Log.e("TAG, onError: ", errorResponse.errorMessage.toString())

                    val errorMessage: String
                    if (errorResponse != null && errorResponse.errorMessage != null && errorResponse.errorMessage!!.isNotEmpty())
                        errorMessage = errorResponse.errorMessage!!
                    else
                        errorMessage = resources.getString(R.string.some_error_occurred)
                    */
    /**   showSnackBar(errorMessage)
     *//*
                }


                override fun generateHash(
                    map: HashMap<String, String?>,
                    hashGenerationListener: PayUHashGenerationListener
                ) {
                    if (map.containsKey(PayUCheckoutProConstants.CP_HASH_STRING)
                        && map.containsKey(PayUCheckoutProConstants.CP_HASH_STRING) != null
                        && map.containsKey(PayUCheckoutProConstants.CP_HASH_NAME)
                        && map.containsKey(PayUCheckoutProConstants.CP_HASH_NAME) != null
                    ) {

                        val hashData = map[PayUCheckoutProConstants.CP_HASH_STRING]
                        val hashName = map[PayUCheckoutProConstants.CP_HASH_NAME]

                        var hash: String? = null

                        //Below hash should be calculated only when integrating Multi-currency support. If not integrating MCP
                        // then no need to have this if check.
                        if (hashName.equals(
                                PayUCheckoutProConstants.CP_LOOKUP_API_HASH,
                                ignoreCase = true
                            )
                        ) {

                            //Calculate HmacSHA1 hash using the hashData and merchant secret key
                            hash = HashGenerationUtils.generateHashFromSDK(
                                hashData!!,
                                HarinaProductionSalt.toString(),
                                merchantSecretKey
                            )
                        } else {
                            //calculate SDH-512 hash using hashData and salt
                            hash = HashGenerationUtils.generateHashFromSDK(
                                hashData!!,
                                HarinaProductionSalt.toString()
                            )
                        }

                        if (!TextUtils.isEmpty(hash)) {
                            val hashMap: HashMap<String, String?> = HashMap()
                            hashMap[hashName!!] = hash!!
                            hashGenerationListener.onHashGenerated(hashMap)
                        }
                    }
                }

                override fun setWebViewProperties(webView: WebView?, bank: Any?) {
                }
            })
    }

    private fun getCheckoutProConfig(): PayUCheckoutProConfig {
        val checkoutProConfig = PayUCheckoutProConfig()
        checkoutProConfig.paymentModesOrder = getCheckoutOrderList()
//        checkoutProConfig.offerDetails = getOfferDetailsList()
//        checkoutProConfig.showCbToolbar = !binding.switchHideCbToolBar.isChecked
//        checkoutProConfig.autoSelectOtp = binding.switchAutoSelectOtp.isChecked
//        checkoutProConfig.autoApprove = binding.switchAutoApprove.isChecked
//        checkoutProConfig.surePayCount = binding.etSurePayCount.text.toString().toInt()
        */
    /**    checkoutProConfig.cartDetails = reviewOrderAdapter?.getOrderDetailsList()
     *//*
        checkoutProConfig.showExitConfirmationOnPaymentScreen = true
//        checkoutProConfig.showExitConfirmationOnCheckoutScreen =
//            !binding.switchDiableUiDialog.isChecked
        checkoutProConfig.merchantName = "Harina Foods"
        checkoutProConfig.merchantLogo = R.drawable.logo
        checkoutProConfig.waitingTime = 3000
        checkoutProConfig.merchantResponseTimeout = 3000
        */
    /**     checkoutProConfig.customNoteDetails = getCustomeNoteDetails()
     *//*
//        checkoutProConfig.enforcePaymentList = getEnforcePaymentList()
        return checkoutProConfig
    }

    private fun getCheckoutOrderList(): ArrayList<PaymentMode> {
        val checkoutOrderList = ArrayList<PaymentMode>()
//        if (binding.switchShowGooglePay.isChecked)
        checkoutOrderList.add(
            PaymentMode(
                PaymentType.UPI,
                PayUCheckoutProConstants.CP_GOOGLE_PAY
            )
        )
//        if (binding.switchShowPhonePe.isChecked)
        checkoutOrderList.add(
            PaymentMode(
                PaymentType.WALLET,
                PayUCheckoutProConstants.CP_PHONEPE
            )
        )
//        if (binding.switchShowPaytm.isChecked)
        checkoutOrderList.add(
            PaymentMode(
                PaymentType.WALLET,
                PayUCheckoutProConstants.CP_PAYTM
            )
        )
        return checkoutOrderList
    }*/

    private fun showSnackBar(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }

    /*
        private fun processResponse(response: Any) {
            response as HashMap<*, *>
            Log.d(
                BaseApiLayerConstants.SDK_TAG,
                "payuResponse ; > " + response[PayUCheckoutProConstants.CP_PAYU_RESPONSE]
                        + ", merchantResponse : > " + response[PayUCheckoutProConstants.CP_MERCHANT_RESPONSE]
            )

            AlertDialog.Builder(context, R.style.Theme_AppCompat_Light_Dialog_Alert)
                .setCancelable(false)
                .setMessage(
                    "Payu's Data : " + response.get(PayUCheckoutProConstants.CP_PAYU_RESPONSE) + "\n\n\n Merchant's Data: " + response.get(
                        PayUCheckoutProConstants.CP_MERCHANT_RESPONSE
                    )
                )
                .setPositiveButton(
                    android.R.string.ok
                ) { dialog, cancelButton -> dialog.dismiss() }.show()
        }
    */


    fun savedAmount() {
        var value = (rewardDiscounts.toDouble() + couponDiscount.toDouble() + discount.toDouble())
        if (value != 0.0) {
            orderTotalBind.savedRs.visibility = View.VISIBLE
            orderTotalBind.savedRs.text =
                "You Have Saved " + "%.2f".format(value).toDouble().toString()
        } else
            orderTotalBind.savedRs.visibility = View.GONE
//        return (rewardDiscounts.toDouble()+couponDiscount.toDouble()+discount.toDouble()).toString()
    }

    fun alertbox() {
        AlertDialog.Builder(context)
            .setCancelable(false)
            .setIcon(R.drawable.logo)
            .setMessage("Thank you for your order, your order has been placed successfully")
            .setPositiveButton(
                "Okay"
            ) { dialog, which ->

                dialog.dismiss()
                findNavController().navigate(R.id.homeFragment, null,
                    navOptions {
                        popUpTo(R.id.homeFragment) {
                            inclusive = true
                        }
                    })

            }.show()
    }

    fun customAlertBox(msg : String) {
        AlertDialog.Builder(context)
            .setCancelable(false)
            .setIcon(R.drawable.logo)
            .setMessage(msg)
            .setPositiveButton(
                "Okay"
            ) { dialog, which ->

                dialog.dismiss()
                findNavController().navigate(R.id.homeFragment, null,
                    navOptions {
                        popUpTo(R.id.homeFragment) {
                            inclusive = true
                        }
                    })

            }.show()

    }

    private fun hideRadioButton(display: Int) {

        Log.e("Hide radio", productionMode.toString());
        if (display != 1 && productionMode) {
            binding.rbCashOnDelivery.visibility = View.GONE
        } else {
            binding.rbCashOnDelivery.visibility = View.VISIBLE;
            binding.rbRazorpayPayment.visibility = View.GONE;
        }

    }

    private fun startRazorPayPayment(orderId: String, grantTotal: Double) {

        Log.e("TOTAL", grantTotal.toString());

//        if(paymentError){
//            Toast.makeText(context, "There is an issue with payment gateway", Toast.LENGTH_LONG).show()
//            customAlertBox("There is an issue with payment gateway!")
//            return
//        }

        /**
         * You need to pass current activity in order to let Razorpay create CheckoutActivity
         */
        val co = Checkout()

        co.setKeyID(BuildConfig.RAZORPAY_KEY)
        try {
            val options = JSONObject()
            options.put("name", getString(R.string.app_name))
            options.put("description", "App Payment")
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://rzp-mobile.s3.amazonaws.com/images/rzp.png")
            options.put("currency", "INR")
            options.put("order_id", orderId)
            // amount is in paise so please multiple it by 100
            //Payment failed Invalid amount (should be passed in integer paise. Minimum value is 100 paise, i.e. ₹ 1)
            var total = grantTotal * 100
//        total = grantTotal * 100
            options.put("amount", total)
            if (PreferenceManager(requireActivity()).getUserPhoneNumber().isNotEmpty()) {
                val preFill = JSONObject()
                preFill.put("email", PreferenceManager(requireActivity()).getUserEmailId())
                preFill.put("contact", PreferenceManager(requireActivity()).getUserPhoneNumber())
                options.put("prefill", preFill)
            }
            co.open(requireActivity(), options)
        }catch (e: Exception){

            Toast.makeText(activity,"Error in payment: "+ e.message,Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }

    override fun onDataReceived(productionData: JSONObject?) {
        TODO("Not yet implemented")
    }


}

//{
//{"country":"","udf10":"","mode":"CC","error_Message":"No Error","state":"","bankcode":"",
//    "txnid":"cdbd1befa9dd7e1dd553","net_amount_debit":"2285.00","lastname":"","zipcode":"",
//    "phone":"8754269423","productinfo":"Harina Foods Products",
//    "hash":"7a766683f777bd498207ac5baa2393671c31b9388a9acdf51378b2d42383eca81897af3439a24bc216477aa32cac176c897908d7dabf533ac4908fd8480e3c9a",
//    "status":"success","firstname":"Sabari Aruna","city":"","isConsentPayment":"",
//    "error":"E000","addedon":"2022-01-31 12:14:44","udf9":"","udf7":"","udf8":"",
//    "encryptedPaymentId":"14641545914","bank_ref_num":"202239694224309","key":"2vKDoRip",
//    "email":"yoaruna@gmail.com","amount":"2285.00","unmappedstatus":"captured",
//    "address2":"","payuMoneyId":"14641545914","address1":"","udf5":"","mihpayid":"14641545914",
//    "udf6":"","udf3":"","udf4":"","udf1":"","udf2":"","giftCardIssued":"","field1":"203168028178",
//    "cardnum":"461119XXXXXX3876","field7":"AUTHPOSITIVE","field6":"00","field9":"No Error",
//    "field8":"Approved or completed successfully",
//    "amount_split":"{\"PAYU\":\"2285.00\"}","field3":"202239694224309","field2":"042040","field5":"05",
//    "PG_TYPE":"CC-PG","field4":"2202239694235653","name_on_card":"Maharajan"}

//{
//    "buyer":641,
//    "payment_type":"cash_on_delivery",
//    "grand_total":3000,
//    "shipping_address":{
//},
//    "productdetails":[
//
//    ],
//    "coupon_amount":200,
//    "coupon_code":"MAX3000",
//    "rewardpoint_amount":300,
//    "reward_rem_points":500,
//    "shipping_cost":40,
//    "gateway_txnid":"",
//    "gateway_payment":{}
//}

//SendCartDataRequest(buyer=737, paymentType=cash_on_delivery, grandTotal=1453.7, shippingAddress=null,
//productDetails=[  ], couponAmount=0, couponCode=,
//rewardpointAmount=0, reward_rem_points=500, shipping_cost=0, gatewayTxnid=, gatewayPayment=, subTotal=1653)