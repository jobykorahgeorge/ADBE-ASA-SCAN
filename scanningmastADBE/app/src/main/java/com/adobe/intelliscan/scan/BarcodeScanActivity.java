package com.adobe.intelliscan.scan;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.adobe.intelliscan.R;
import com.adobe.intelliscan.utils.Constants;
import com.adobe.intelliscan.utils.Preferences;
import com.google.android.gms.vision.barcode.Barcode;

import java.util.List;

import info.androidhive.barcode.BarcodeReader;
import info.androidhive.barcode.ScannerOverlay;

public class BarcodeScanActivity extends AppCompatActivity implements BarcodeReader.BarcodeReaderListener /*,ProductFragment.OnInteractionListener*/ {

    private static final String TAG = "BarcodeScanActivity";
    private ScannerOverlay viewFinderLayout;
    private TextView titleTextView;
    private TextView messageTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_scan);

        viewFinderLayout = findViewById(R.id.barcode_scanner_line);
        titleTextView = findViewById(R.id.scan_title_txt);
        messageTextView = findViewById(R.id.scan_message_text);
        titleTextView.setText(R.string.app_name);
        titleTextView.setText(R.string.app_name);
        messageTextView.setText("Barcode Scanner: Scanning for products...");

    }

    @Override   // BarcodeReader.BarcodeReaderListener
    public void onScanned(final Barcode barcode) {
        Log.d(TAG, "Scanned: " + barcode.rawValue);
        Intent i = new Intent(this,ReturnActivity.class);
        i.putExtra("barcode",barcode.rawValue);
        startActivity(i);
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
        finish();
    }

    @Override   // BarcodeReader.BarcodeReaderListener
    public void onScannedMultiple(List<Barcode> barcodes) {

    }

    @Override   // BarcodeReader.BarcodeReaderListener
    public void onBitmapScanned(SparseArray<Barcode> sparseArray) {

    }

    @Override   // BarcodeReader.BarcodeReaderListener
    public void onScanError(String errorMessage) {

    }

    @Override   // BarcodeReader.BarcodeReaderListener
    public void onCameraPermissionDenied() {
        Toast.makeText(this, "No Camera Permission: Please enable camera permission for IntelliScan.", Toast.LENGTH_LONG).show();
    }

    public void onConfigClick(View view) {
        Intent intent = new Intent(this, ConfigActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }
}
