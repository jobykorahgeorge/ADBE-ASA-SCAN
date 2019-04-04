package com.adobe.intelliscan.scan;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.adobe.intelliscan.R;
import com.adobe.intelliscan.SplashActivity;
import com.adobe.intelliscan.utils.Preferences;

public class ConfigActivity extends AppCompatActivity {

    private static final String AR_CONFIG_TEXT = "Product One responds to the color Blue." +
            "\nProduct Two responds to the color Red.";

    String scanMode = "";
    private Preferences preferences;
    private ScrollView scrollView;
    private EditText urlInput;
    private EditText prodOnePathInput;
    private EditText baseUrlMagento;
    private RadioGroup scanModeGroup;
    private RadioButton radioAR;
    private RadioButton radioBarcode;
    private LinearLayout arConfigLayout;
    private LinearLayout barcodeConfigLayout;
    private EditText userName;
    private EditText password;
    private TextView arConfigText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        preferences = new Preferences(this);
        initViews();
        loadData();
    }

    private void initViews() {

        scrollView = (ScrollView) findViewById(R.id.config_scrollview);
        urlInput = (EditText) findViewById(R.id.app_server_url_input);
        baseUrlMagento = (EditText) findViewById(R.id.magento_config);
        prodOnePathInput = (EditText) findViewById(R.id.prod_one_path_input);
        scanModeGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioAR = (RadioButton) findViewById(R.id.radioColorScan);
        radioBarcode = (RadioButton) findViewById(R.id.radioBarcodeScan);
        arConfigLayout = (LinearLayout) findViewById(R.id.ar_config_layout);
        barcodeConfigLayout = (LinearLayout) findViewById(R.id.barcode_config_layout);
        userName = (EditText)findViewById(R.id.user_name);
        password = (EditText) findViewById(R.id.password);
        arConfigText = (TextView) findViewById(R.id.ar_config_text);

        scanModeGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                if (checkedId == R.id.radioColorScan) {
                    arConfigLayout.setVisibility(View.GONE);
                    barcodeConfigLayout.setVisibility(View.GONE);

                    scanMode = Preferences.SCAN_MODE_AR;
                } else if (checkedId == R.id.radioBarcodeScan) {

                    scanMode = Preferences.SCAN_MODE_BARCODE;
                }

                if (radioAR.isPressed() || radioBarcode.isPressed()) {
                    scrollView.post(new Runnable() {
                        public void run() {
                            scrollView.fullScroll(View.FOCUS_DOWN);
                        }
                    });
                }
            }
        });

        arConfigText.setText(AR_CONFIG_TEXT);
    }


    private void loadData() {
        scanMode = preferences.getData(Preferences.KEY_SCAN_MODE);
        String url = preferences.getData(Preferences.KEY_APP_SERVER_URL);
        String prodOnePath = preferences.getData(Preferences.KEY_PROD_ONE_PATH);
        String username =preferences.getData(Preferences.USER_NAME);
        String passwd = preferences.getData(Preferences.PASSWORD);

        urlInput.setText(url);
        prodOnePathInput.setText(prodOnePath);
        userName.setText(username);
        password.setText(passwd);


        if (scanMode == null || scanMode.equals(Preferences.SCAN_MODE_BARCODE) ) {
            radioAR.setChecked(false);
            radioBarcode.setChecked(true);
        } else if (scanMode.equals(Preferences.SCAN_MODE_AR)) {
            scanMode = Preferences.SCAN_MODE_AR;
            radioAR.setChecked(true);
            radioBarcode.setChecked(false);
        }
    }

    public void fabSave(View view) {

        String url = urlInput.getText().toString();
        String prodOnePath = prodOnePathInput.getText().toString();

        preferences.setData(Preferences.KEY_SCAN_MODE, scanMode);
        preferences.setData(Preferences.KEY_APP_SERVER_URL, url);
        preferences.setData(Preferences.KEY_PROD_ONE_PATH, prodOnePath);
        preferences.setData(Preferences.API_END_TOKEN,"/integration/customer/token");
        preferences.setData(Preferences.API_END_QUOTE_ID,"/carts/mine");
        preferences.setData(Preferences.API_END_CART_ITEM,"/carts/mine/items");
        preferences.setData(Preferences.API_BASE_URL_CONFIG,baseUrlMagento.getText().toString().trim());
        preferences.setData(Preferences.USER_NAME,userName.getText().toString());
        preferences.setData(Preferences.PASSWORD,password.getText().toString());

        Toast.makeText(this, "Config Saved!", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, SplashActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
