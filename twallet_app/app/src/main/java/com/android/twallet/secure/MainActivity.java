package com.android.twallet.secure;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.StrictMode;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.twallet.secure.databinding.ActivityMainBinding;
import com.android.twallet.secure.utils.TWalletUtils;
import com.android.twallet.secure.walletCreate.WalletCreate;
import com.android.twallet.secure.walletUnlock.WalletUnlock;
import com.android.twallet.secure.web3J.Web3jHandler;


import java.util.Arrays;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    /* TwalletUtils used to access all functions inside the framework TWallet */
    private static TWalletUtils tWalletUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tWalletUtils = new TWalletUtils();
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        /* setting strict mode thread policy */
        final StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Runnable r = () -> {
            /* Web3j Connection */
            if (!Web3jHandler.web3Connection()) {
                runOnUiThread(() -> Toast.makeText(this, "Connection Error", Toast.LENGTH_SHORT).show());
                finish();
            }

            if (!tWalletUtils.attestComponents(12)) {
                runOnUiThread(() -> Toast.makeText(this, "Error while verifying Application files", Toast.LENGTH_SHORT).show());
                finish();
            }

            String[] credentials = tWalletUtils.loadCredentials();
            if (credentials != null && credentials.length > 0) {
                /**
                 * starting wallet unlock activity
                 * @param Intent
                 */
                startActivity(new Intent(MainActivity.this, WalletUnlock.class));
            } else {
                /**
                 * starting wallet unlock activity
                 * @param Intent
                 */
                startActivity(new Intent(MainActivity.this, WalletCreate.class));
            }
        };

        HandlerThread ht = new HandlerThread("MainHandler");
        ht.start();
        Handler h = new Handler(ht.getLooper());
        // The Runnable will be executed after a small delay time
        h.postDelayed(r, 500);
    }
}