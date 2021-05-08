package com.bignerdranch.android.photogallery

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity

private const val ARG_URI = "photo_page_url"
class PhotoPageFragment : VisibleFragment() {
    private lateinit var KTuri: Uri
    private lateinit var KTwebView: WebView
    private lateinit var KTprogressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        KTuri = arguments?.getParcelable(ARG_URI) ?: Uri.EMPTY
    }
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val KTview = inflater.inflate(R.layout.fragment_photo_page, container, false)

        KTprogressBar = KTview.findViewById(R.id.progress_bar)
        KTprogressBar.max = 100

        KTwebView = KTview.findViewById(R.id.web_view)
        KTwebView.settings.javaScriptEnabled = true
        KTwebView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(webView: WebView, newProgress: Int) {
                if (newProgress == 100) {
                    KTprogressBar.visibility = View.GONE
                } else {
                    KTprogressBar.visibility = View.VISIBLE
                    KTprogressBar.progress = newProgress
                }
            }
            override fun onReceivedTitle(view: WebView?, title: String?) {
                (activity as AppCompatActivity).supportActionBar?.subtitle = title
            }
        }
        KTwebView.webViewClient = WebViewClient()
        KTwebView.loadUrl(KTuri.toString())
        return KTview
    }
    companion object {
        fun newInstance(uri: Uri?): PhotoPageFragment {
            return PhotoPageFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_URI, uri)
                }
            }
        }
    }
}

class PhotoPageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_page)
        val KTfm = supportFragmentManager
        val KTcurrentFragment = KTfm.findFragmentById(R.id.fragment_container)
        if (KTcurrentFragment == null) {
            val KTfragment = PhotoPageFragment.newInstance(intent.data)
            KTfm.beginTransaction()
                    .add(R.id.fragment_container, KTfragment)
                    .commit()
        }
    }
    companion object {
        fun newIntent(context: Context, photoPageUri: Uri): Intent {
            return Intent(context, PhotoPageActivity::class.java).apply {
                data = photoPageUri
            }
        }
    }
}