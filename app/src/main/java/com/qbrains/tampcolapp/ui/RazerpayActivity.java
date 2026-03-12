//package com.qbrains.harina.ui;
//
//
//import android.app.Activity;
//import android.os.Bundle;
//import android.view.MenuItem;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.qbrains.harina.R;
//import com.qbrains.harina.ui.fragments.CheckOutFragment;
//import com.razorpay.Checkout;
//import com.razorpay.PaymentResultListener;
//
//import org.json.JSONObject;
//
//
//
//
//public class RazerpayActivity extends AppCompatActivity implements PaymentResultListener {
//
//    int amount = 1;
////    User user;
////    PaymentItem paymentItem;
//    @Override
//    public void onCreate(Bundle bundle) {
//        super.onCreate(bundle);
//        setContentView(R.layout.activity_razorpay);
//
////        sessionManager = new SessionManager(this);
////        user = sessionManager.getUserDetails("");
//        amount =  getIntent().getIntExtra("amount", 0);
////        paymentItem = (PaymentItem) getIntent().getSerializableExtra("detail");
//        startPayment(String.valueOf(amount));
//    }
//
//    public void startPayment(String amount) {
//        final Activity activity = this;
//        final Checkout co = new Checkout();
////        co.setKeyID(paymentItem.getCredValue());
//        co.setKeyID("rzp_test_IqcE0gbHhJizpn");
//        try {
//            JSONObject options = new JSONObject();
//            options.put("name", getResources().getString(R.string.app_name));
//            options.put("currency", "INR");
//            double total = Double.parseDouble(amount);
//            total = total * 100;
//            options.put("amount", total);
//            JSONObject preFill = new JSONObject();
//            preFill.put("email", "vr999411@gmail.com");
//            preFill.put("contact", "9994113892");
//            options.put("prefill", preFill);
//            co.open(activity, options);
//        } catch (Exception e) {
//            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId() == android.R.id.home) {
//            finish();
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//    @Override
//    public void onPaymentSuccess(String s) {
//        CheckOutFragment.tragectionID = s;
//        CheckOutFragment.paymentsucsses = String.valueOf(1);
//        finish();
//    }
//
//    @Override
//    public void onPaymentError(int i, String s) {
//        finish();
//    }
//}