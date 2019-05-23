package com.example.anggarisky.fidashboard.ui;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.example.anggarisky.fidashboard.R;
import com.example.anggarisky.fidashboard.db.DatabaseHelper;
import com.example.anggarisky.fidashboard.model.DemoItem;
import com.example.anggarisky.fidashboard.utils.Constants;
import com.example.anggarisky.fidashboard.utils.DownloadService;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import java.io.File;

public class WebActivity extends AppCompatActivity {

    String output = "";
    private WebView webView;
    ProgressDialog mProgressDialog;
    private static final int REQUEST_WRITE_STORAGE = 112;
    private AdView mAdView;
    private InterstitialAd mInterstitialAd;
    View contextView;
    String chapterName,className;
    DatabaseHelper mDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        webView = findViewById(R.id.webview);
        MobileAds.initialize(this, getString(R.string.admob_app_id));
        final Toolbar toolbar = findViewById(R.id.toolbar);
        final boolean hasPermission = (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
        Intent i = getIntent();
        chapterName = i.getStringExtra("chapterName");
        className = i.getStringExtra("className");
        toolbar.setTitle(chapterName);
        String url = i.getStringExtra("urlStart");
        String urlName = i.getStringExtra("url");
        output = url + "books/books/" + urlName;
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        pdfOpen(output);
        mProgressDialog = new ProgressDialog(WebActivity.this);
        mProgressDialog.setMessage("Downloading");
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setCancelable(false);
        Button downloadButton = findViewById(R.id.action_download);
//        initializeBannerAds();
//        initializeInterstitialAds();
        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!hasPermission) {
                    ActivityCompat.requestPermissions(WebActivity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            REQUEST_WRITE_STORAGE);
                }
                else{
                    mProgressDialog.show();
                    Intent intent = new Intent(WebActivity.this, DownloadService.class);
                    intent.putExtra("url", output);
                    intent.putExtra("receiver", new DownloadReceiver(new Handler()));
                    startService(intent);
                }
            }
        });
        TextView chapterName = (TextView) findViewById(R.id.chapterName);
        chapterName.setText(i.getStringExtra("chapterName"));
        contextView = findViewById(R.id.content_view);
        mDatabaseHelper = new DatabaseHelper(this);
    }

    private void initializeInterstitialAds() {
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_id));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
    }

    private void initializeBannerAds() {
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }
    public void AddData(String name, String url) {
        boolean insertData = mDatabaseHelper.addData(name,url,className);

        if (insertData) {
            toastMessage("Data Successfully Inserted!");
        } else {
            toastMessage("Something went wrong");
        }
    }

    private void pdfOpen(String fileUrl) {

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);

        //---you need this to prevent the webview from
        // launching another browser when a url
        // redirection occurs---
        webView.setWebViewClient(new Callback());

        webView.loadUrl(
                "http://docs.google.com/gview?embedded=true&url=" + fileUrl);

    }

    private class Callback extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(
                WebView view, String url) {
            return (false);
        }
    }

    private class DownloadReceiver extends ResultReceiver {
        public DownloadReceiver(Handler handler) {
            super(handler);
        }


        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            super.onReceiveResult(resultCode, resultData);
            if (resultCode == DownloadService.UPDATE_PROGRESS) {
                int progress = resultData.getInt("progress");
                mProgressDialog.setProgress(progress);
                if (progress == 100) {
                    mProgressDialog.dismiss();
                    final String urlsuffix =output.substring(output.lastIndexOf("/") + 1);
                    Snackbar.make(contextView,"Downloaded Successfully",Snackbar.LENGTH_INDEFINITE).setAction(R.string.open_pdf, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            openPdf(urlsuffix);
                        }
                    }).show();
                    AddData(chapterName,urlsuffix);
//                    if (mInterstitialAd.isLoaded()) {
//                        mInterstitialAd.show();
//
//                    } else {
//                        Log.d("TAG", "The interstitial wasn't loaded yet.");
//                    }
//                    mInterstitialAd.loadAd(new AdRequest.Builder().build());
                }
            }
        }
    }

    private void openPdf(String urlsuffix) {
        File pdfFile = new File(Environment.getExternalStorageDirectory().getPath() + "/NcertBooks/" + urlsuffix);  // -> filename = maven.pdf
        Uri path = FileProvider.getUriForFile(
                WebActivity.this,
                getPackageName() + ".provider", pdfFile);
//        Uri path2 = Uri.fromFile(pdfFile);
        Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
        pdfIntent.setDataAndType(path, "application/pdf");
        pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        pdfIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        try{
            startActivity(pdfIntent);
        }catch(ActivityNotFoundException e){
            Toast.makeText(WebActivity.this, "No Application available to view PDF", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode)
        {
            case REQUEST_WRITE_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    //reload my activity with permission granted or use the features what required the permission
                    mProgressDialog.show();
                    Intent intent = new Intent(WebActivity.this, DownloadService.class);
                    intent.putExtra("url", output);
                    intent.putExtra("receiver", new DownloadReceiver(new Handler()));
                    startService(intent);
                } else
                {
                    Toast.makeText(this, "The app was not allowed to write to your storage. Hence, it cannot function properly. Please consider granting it this permission", Toast.LENGTH_LONG).show();
                }
            }
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        mInterstitialAd.show();
    }
    private void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }
}
