package com.qbrains.tampcolapp.ui.fragments

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.qbrains.tampcolapp.databinding.FragmentWebViewBinding
import com.qbrains.tampcolapp.ui.extension.isConnected
//import kotlinx.android.synthetic.main.fragment_web_view.*

class WebViewFragment : Fragment() {
    var url=""
    var mTitle=""

    private var _binding : FragmentWebViewBinding?=null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentWebViewBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        url = arguments?.getString("url", "")!!
        mTitle = arguments?.getString("title", "")!!
        initAppBar()
        if (isConnected(requireActivity())) {
            webHandling()
        }

    }
    private fun initAppBar() {
        binding.tbWebView.apply {
            binding.tbWebView.title=mTitle
            (activity as AppCompatActivity).setSupportActionBar(binding.tbWebView)
            (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
            (activity as AppCompatActivity).supportActionBar?.title=mTitle
            setNavigationOnClickListener { findNavController().navigateUp() }
        }
    }

    @SuppressLint("JavascriptInterface", "SetJavaScriptEnabled")
    private fun webHandling() {
        binding.webViewFragment.settings.javaScriptEnabled = true
        binding.webViewFragment.settings.loadWithOverviewMode = true
        binding.webViewFragment.webViewClient = WebView()
        binding.webViewFragment.loadUrl(url)
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
            if (binding.pbWebView != null) {
                binding.pbWebView.max = 100
                binding.pbWebView.visibility = View.VISIBLE
                binding.pbWebView.progress = 0
            }
        }

        override fun onPageFinished(view: android.webkit.WebView?, url: String?) {
            if (binding.pbWebView != null) {
                binding.pbWebView.visibility = View.GONE
            }
        }
    }
}