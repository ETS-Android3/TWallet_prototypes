package com.android.twallet.vanilla;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.StrictMode;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.twallet.vanilla.databinding.ActivityMainBinding;
import com.android.twallet.vanilla.walletCreate.WalletCreate;
import com.android.twallet.vanilla.walletUnlock.WalletUnlock;
import com.android.twallet.vanilla.web3J.Web3jHandler;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        /* setting strict mode thread policy */
        final StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        /* Web3j Connection */
        if (!Web3jHandler.web3Connection()) {
            Toast.makeText(this, "Connection Error", Toast.LENGTH_SHORT).show();
            finish();
        }

        /* Setup Transaction History */
        if (!Web3jHandler.setTxHistory()) {
            Toast.makeText(this, "Internal Error", Toast.LENGTH_SHORT).show();
            finish();
        }

        Runnable r = () -> {
            if (Objects.requireNonNull(getFilesDir().listFiles()).length > 0) {
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
        // The Runnable will be executed after the given delay time
        h.postDelayed(r, 1500);


    }
}