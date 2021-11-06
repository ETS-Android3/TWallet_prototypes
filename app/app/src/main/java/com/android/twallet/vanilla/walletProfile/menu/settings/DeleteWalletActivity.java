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

package com.android.twallet.vanilla.walletProfile.menu.settings;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.twallet.vanilla.MainActivity;
import com.android.twallet.vanilla.R;
import com.android.twallet.vanilla.walletProfile.WalletMain;
import com.android.twallet.vanilla.exception.InvalidPasswordException;
import com.android.twallet.vanilla.exception.WalletDeleteException;
import com.android.twallet.vanilla.web3J.Web3jHandler;

/**
 * This class is written for basic functions of Ethereum and web3j integration
 * Because it's handling all the basic function of web3j so that's why we named
 * it as a web3Handler.
 *
 * @author Zain-Ul-Abedin
 * @version 1.10 24 Aug 2017
 */

public class DeleteWalletActivity extends AppCompatActivity {

    /* Variable button for delete wallet confirmation, and cancel*/
    Button deleteButton, cancelButton;

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
         * @param R.layout.activity_export_wallet
         */
        setContentView(R.layout.activity_delete_wallet);

        /* initializing deleteButton */
        deleteButton = findViewById(R.id.deleteButton);
        /* initializing cancelButton */
        cancelButton = findViewById(R.id.cancelButton);
        /* initializing password edit text for user input */
        passwordEditText = findViewById(R.id.deletePasswordConfirm);

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

                startActivity(new Intent(DeleteWalletActivity.this, WalletMain.class));
            }
        });

        /**
         * method on click action performed for delete wallet
         * @param OnClickListener
         */
        deleteButton.setOnClickListener(new View.OnClickListener() {
            /**
             * @param view
             */
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DeleteWalletActivity.this);
                builder.setView(R.layout.progress);
                loadingDialog = builder.create();
                loadingDialog.show();

                try {
                    Web3jHandler.deleteWallet(passwordEditText.getText().toString(), getFilesDir().getPath());
                    loadingDialog.cancel();
                    startActivity(new Intent(DeleteWalletActivity.this, MainActivity.class));
                } catch (InvalidPasswordException ignored) {
                    Toast.makeText(DeleteWalletActivity.this, "Invalid Password", Toast.LENGTH_SHORT).show();
                } catch (WalletDeleteException ignored) {
                    Toast.makeText(DeleteWalletActivity.this, "Error happened while closing account.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


}
