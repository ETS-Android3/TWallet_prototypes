/*
 * %W% %E% Zain-Ul-Abedin
 *
 * Copyright (c) 2017-2018 Miranz Technology. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Miranz
 * technology. You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with Miranz.
 *
 */

package com.android.twallet.secure.walletProfile.menu.settings;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.twallet.secure.R;
import com.android.twallet.secure.walletProfile.WalletMain;
import com.android.twallet.secure.exception.InvalidPasswordException;
import com.android.twallet.secure.web3J.Web3jHandler;

/**
 * This class is written for basic functions of Ethereum and web3j integration
 * Because it's handling all the basic function of web3j so that's why we named
 * it as a web3Handler.
 *
 * @author Zain-Ul-Abedin
 * @version 1.10 24 Aug 2017
 */

public class MonitorWalletActivity extends AppCompatActivity {

    /* Variable button for trigger monitoring wallet confirmation, and cancel*/
    Button triggerButton, cancelButton;

    /* Variable password edit text for password input */
    EditText passwordEditText;

    /* Variable dialog for loading */
    Dialog loadingDialog;

    /**
     * @param savedInstanceState
     * @override onCreate method calls when activity start
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        /** passing savedInstanceState to super onCreate method
         * @param savedInstanceState
         */
        super.onCreate(savedInstanceState);

        /** set content layout
         * @param R.layout.activity_lock_wallet
         */
        setContentView(R.layout.activity_lock_wallet);

        /* initializing deleteButton */
        triggerButton = findViewById(R.id.triggerButton);
        /* initializing cancelButton */
        cancelButton = findViewById(R.id.cancelButton);
        /* initializing password edit text for user input */
        passwordEditText = findViewById(R.id.lockPasswordConfirm);

        /**
         * method on click action performed for cancel operation
         * @param OnClickListener
         */
        cancelButton.setOnClickListener(new View.OnClickListener() {
            /**
             * @param view
             */
            @Override
            public void onClick(View view) {

                startActivity(new Intent(MonitorWalletActivity.this, WalletMain.class));
            }
        });

        /**
         * method on click action performed for delete wallet
         * @param OnClickListener
         */
        triggerButton.setOnClickListener(new View.OnClickListener() {
            /**
             * @param view
             */
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MonitorWalletActivity.this);
                builder.setView(R.layout.progress);
                loadingDialog = builder.create();
                loadingDialog.show();

                try {
                    Web3jHandler.monitorWallet(passwordEditText.getText().toString());
                    loadingDialog.cancel();
                    Toast.makeText(MonitorWalletActivity.this, "Operations Filtering disabled", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MonitorWalletActivity.this, WalletMain.class));
                } catch (InvalidPasswordException ignored) {
                    Toast.makeText(MonitorWalletActivity.this, "Invalid Password", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


}
