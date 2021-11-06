package com.android.twallet.vanilla.walletUnlock;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.twallet.vanilla.R;
import com.android.twallet.vanilla.walletProfile.WalletMain;
import com.android.twallet.vanilla.web3J.Web3jHandler;

import org.web3j.crypto.CipherException;

import java.io.IOException;
import java.util.Objects;

public class WalletUnlock extends AppCompatActivity {

    /* Variable button for unlock wallet */
    Button unlockButton;

    /* Variable password edit text for password input */
    EditText passwordEditText;

    /* Variable dialog for loading */
    Dialog loadingDialog;

    /**
     * onCreate method
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unlock_wallet);

        /* initializing unlockButton */
        unlockButton = findViewById(R.id.unlockWallet);

        /* initializing password edit text for user input */
        passwordEditText = findViewById(R.id.passwordEditText);


        /**
         * method on click action performed for unlock wallet
         * @param OnClickListener
         */
        unlockButton.setOnClickListener(new View.OnClickListener() {
            /**
             * @override onClick
             * @param view
             * @return void
             */
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(WalletUnlock.this);
                builder.setView(R.layout.progress);
                loadingDialog = builder.create();
                loadingDialog.show();

                /* background async task execute */
                doAsyncTask();
            }
        });
    }

    /* This method is used to execute task */
    public void doAsyncTask() {
        HandlerThread ht = new HandlerThread("Web3jHandler");
        ht.start();
        Handler asyncHandler = new Handler(ht.getLooper());
        Runnable runnable = () -> {
            try {

                /**
                 * This method is loading credential file of your wallet from phone internal storage
                 * *Warning* the folder must only have one wallet file!
                 * @param password
                 * @param path path to the Wallet file
                 * @throws CipherException
                 * @throws IOException
                 */
                Web3jHandler.loadCredentials(passwordEditText.getText().toString(), Objects.requireNonNull(getFilesDir().listFiles())[0].getPath());
                passwordEditText.setText("");
                /**
                 * starting wallet profile activity
                 * @param Intent
                 */
                startActivity(new Intent(WalletUnlock.this, WalletMain.class));
            } catch (CipherException e) {
                Toast.makeText(WalletUnlock.this, "Invalid Password", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                Toast.makeText(WalletUnlock.this, "Connection Error", Toast.LENGTH_SHORT).show();
            } catch (Exception ignored) {
            }

            loadingDialog.dismiss();
        };
        asyncHandler.post(runnable);
    }
}
