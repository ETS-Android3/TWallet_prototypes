package com.android.twallet.vanilla.walletCreate;

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
import com.android.twallet.vanilla.exception.InvalidPasswordException;
import com.android.twallet.vanilla.web3J.Web3jHandler;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

public class WalletCreate extends AppCompatActivity {

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
        setContentView(R.layout.activity_create_wallet);

        /* initializing unlockButton */
        unlockButton = findViewById(R.id.createWallet);
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
                AlertDialog.Builder builder = new AlertDialog.Builder(WalletCreate.this);
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
                 * This method is create a new wallet file and loading its credentials
                 * @param password
                 * @throws CipherException
                 * @throws IOException
                 * @throws InvalidAlgorithmParameterException
                 * @throws NoSuchAlgorithmException
                 * @throws NoSuchProviderException
                 */
                Web3jHandler.createWallet(passwordEditText.getText().toString(), getFilesDir().getPath());

                /**
                 * starting wallet profile activity
                 * @param Intent
                 */
                startActivity(new Intent(WalletCreate.this, WalletMain.class));
            } catch (InvalidPasswordException ignored) {
                Toast.makeText(WalletCreate.this, "Invalid Password", Toast.LENGTH_SHORT).show();
            } catch (InvalidAlgorithmParameterException | NoSuchAlgorithmException | NoSuchProviderException ignored) {
                Toast.makeText(WalletCreate.this, "Internal Processing Error", Toast.LENGTH_SHORT).show();
            } catch (Exception ignored) {
            }

            loadingDialog.dismiss();
        };
        asyncHandler.post(runnable);
    }

}
