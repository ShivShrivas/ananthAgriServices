package com.project.aas.ui.slideshow;

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

public class Feedback extends AppCompatActivity {

    WebView webView;
    ProgressDialog pd;
    private final String webUrl="https://mega.nz/folder/Ap5WRAhb#qp4xOEUGXODH8Cx7mmStOQ";
    ImageView back;
    TextView backt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        webView= findViewById(R.id.webView);
        WebSettings webSettings=webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
       // webView.setWebViewClient(new Blogs.Callback());
        webView.setWebViewClient(new Feedback.MyWebViewClient());

        webView.loadUrl(webUrl);
        back=findViewById(R.id.backk);
        back.setOnClickListener(v -> startActivity(new Intent(Feedback.this, HomePage.class)));
        backt=findViewById(R.id.backkk);
        backt.setOnClickListener(v -> startActivity(new Intent(Feedback.this, HomePage.class)));

    }

    @Override
    public void onBackPressed() {
        if(webView.canGoBack()){
            webView.goBack();
        }else
            super.onBackPressed();
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
            pd = new ProgressDialog(Feedback.this);
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