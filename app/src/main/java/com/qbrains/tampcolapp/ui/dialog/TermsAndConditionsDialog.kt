package com.qbrains.tampcolapp.ui.dialog

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.fragment.app.DialogFragment
import com.qbrains.tampcolapp.R
import com.qbrains.tampcolapp.databinding.TermsAndConditionBinding
import com.qbrains.tampcolapp.ui.extension.isConnected
import com.qbrains.tampcolapp.ui.extension.toast
//import kotlinx.android.synthetic.main.terms_and_condition.*

typealias terms = (Boolean) -> Unit

class TermsAndConditionsDialog(var terms: terms) : DialogFragment() {

    private var _binding : TermsAndConditionBinding?=null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onResume() {
        super.onResume()
        val width = (resources.displayMetrics.widthPixels * 1)
        val height = (resources.displayMetrics.heightPixels * 0.90).toInt()
        dialog?.setCanceledOnTouchOutside(true)
        dialog?.window?.setLayout(width, height)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = TermsAndConditionBinding.inflate(layoutInflater, container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (isConnected(requireActivity())) {
            webHandling()
        } else
            activity?.toast(getString(R.string.no_internet))
        binding.linearAcceptTerms.setOnClickListener {
            terms.invoke(true)
            dialog?.dismiss()
        }

        binding.linearCancelTerms.setOnClickListener {
            terms.invoke(false)
            dialog?.dismiss()
        }
    }

    @SuppressLint("JavascriptInterface", "SetJavaScriptEnabled")
    private fun webHandling() {
        binding.wvTermsCondition.settings.javaScriptEnabled = true
        binding.wvTermsCondition.settings.loadWithOverviewMode = true
        binding.wvTermsCondition.webViewClient = WebView()
        val url ="https://tampcol.com/public/api/android/v1/staticpages/terms-conditions"
        binding.wvTermsCondition.loadUrl(url)
    }


    inner class WebView : WebViewClient() {
        override fun shouldOverrideUrlLoading(
            view: android.webkit.WebView?,
            url: String?
        ): Boolean {
            view?.loadUrl(url!!)
            return true
        }

        override fun onPageStarted(view: android.webkit.WebView?, url: String?, favicon: Bitmap?) {
            if (binding.pbTermsCondition != null) {
                binding.pbTermsCondition.max = 100
                binding.pbTermsCondition.visibility = View.VISIBLE
                binding.pbTermsCondition.progress = 0
            }
        }

        override fun onPageFinished(view: android.webkit.WebView?, url: String?) {
            if (binding.pbTermsCondition != null) {
                binding.pbTermsCondition.visibility = View.GONE
            }
        }
    }
}