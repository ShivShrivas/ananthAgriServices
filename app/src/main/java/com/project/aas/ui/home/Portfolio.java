package com.project.aas.ui.home;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.aas.HomePage;
import com.project.aas.R;
import com.project.aas.ui.slideshow.Blogs;

public class Portfolio extends AppCompatActivity {

    WebView webView;
    private String webUrl="https://aboutaas.online/";
    ImageView back;
    TextView backt;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portfolio);

        webView= findViewById(R.id.myWebView);
        WebSettings webSettings=webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new Portfolio.MyWebViewClient());

        webView.loadUrl(webUrl);
        back=findViewById(R.id.backk);
        back.setOnClickListener(v -> startActivity(new Intent(Portfolio.this, HomePage.class)));
        backt=findViewById(R.id.backkk);
        backt.setOnClickListener(v -> startActivity(new Intent(Portfolio.this, HomePage.class)));
    }

    private class MyWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            pd = new ProgressDialog(Portfolio.this);
            pd.setMessage("Please wait ...");
            pd.show();
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if(pd!=null){
                pd.dismiss();
            }
        }
    }

}