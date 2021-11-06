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

package com.android.twallet.vanilla.walletProfile.menu.home;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.twallet.vanilla.R;
import com.android.twallet.vanilla.web3J.Web3jHandler;

import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.io.IOException;

/**
 * This class is written for basic functions of Ethereum and web3j integration
 * Because it's handling all the basic function of web3j so that's why we named
 * it as a web3Handler.
 *
 * @author Zain-Ul-Abedin
 * @version 1.10 24 Aug 2017
 */

public class SendTransactionActivity extends AppCompatActivity {

    /* variables for input address and ether's for transaction */
    EditText addressEditText, ethEditText;

    /* variable for send button */
    Button sendButton;

    /* textView variables for output of transaction hashes and subDetails */
    TextView transactionHashAddressTextView, subDetailsTextView;

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
         * @param R.layout.activity_send_transaction
         */
        setContentView(R.layout.activity_send_transaction);

        /* initializing address and ether editText for input from user */
        addressEditText = (EditText) findViewById(R.id.addressEditText);

        ethEditText = (EditText) findViewById(R.id.ethEditText);

        /* initializing send button for transaction */
        sendButton = (Button) findViewById(R.id.sendEtherButton);

        /* setUp send on click button for transaction */
        sendButton.setOnClickListener(new View.OnClickListener() {
            /**
             * @override onClick
             * @param view
             * @return void
             */
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SendTransactionActivity.this);
                builder.setView(R.layout.progress);
                loadingDialog = builder.create();
                loadingDialog.show();

                /* background async task execute */
                sendTransactionAsync();
            }
        });
    }

    /**
     * sendTransactionAsync method for calling library functions
     * to transact amount of ether from one to another wallet
     *
     * @throws InterruptedException
     * @throws IOException
     * @throws Exception
     */
    void sendTransactionAsync() {

        /* condition for valid address input */
        if (addressEditText.length() == 0) {
            Toast.makeText(this, "Please Provide Address", Toast.LENGTH_SHORT).show();
            return;
        }

        /* condition for valid transaction amount */
        if (ethEditText.length() == 0) {
            Toast.makeText(this, "Please Provide Balance", Toast.LENGTH_SHORT).show();
            return;
        }

        HandlerThread ht = new HandlerThread("Web3jHandler");
        ht.start();
        Handler asyncHandler = new Handler(ht.getLooper());
        Runnable runnable = () -> {
            try {
                /**
                 * Calling transaction method from Web3jHandler for transferring amount from wallet to another wallet
                 * @param address
                 * @param etherAmount
                 * @throws org.web3j.crypto.CipherException
                 * @throws TransactionTimeoutException
                 * @throws IOException
                 * @return TransactionReceipt after transaction
                 */
                TransactionReceipt transactionReceipt = Web3jHandler.transaction(addressEditText.getText().toString(), Double.parseDouble(ethEditText.getText().toString()));

                /* toast for showing success message */
                Toast.makeText(this, "Successfully Transfer", Toast.LENGTH_SHORT).show();
            } catch (InterruptedException e) {
                Toast.makeText(this, "Interruption occurred during transaction", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            } catch (IOException e) {
                Toast.makeText(this, "Please check your connection", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            } catch (Exception e) {
                Toast.makeText(this, "Transaction Time out", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
            loadingDialog.dismiss();
        };
        asyncHandler.post(runnable);

    }
}
