package com.android.twallet.vanilla.walletProfile.menu.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.android.twallet.vanilla.databinding.FragmentHomeBinding;
import com.android.twallet.vanilla.web3J.Web3jHandler;

import org.web3j.utils.Convert;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;

    /* Variables for showing balances in ether */
    TextView etherTextView, ethTextView;

    /* Variable for store big integer */
    BigInteger bigInteger;

    /* variables for refresh balance, send and copy address of wallet */
    Button refreshButton, sendButton;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        /* initializing ether text view for displaying ether balance */
        etherTextView = binding.etherBalanceTextView;

        /* initializing ether text view for displaying ether balance */
        ethTextView = binding.ethBalanceTextView;

        /* initializing send button */
        refreshButton = binding.refreshButton;

        /* initializing send button */
        sendButton = binding.sendButton;

        /* setUp send on click button for transaction */
        sendButton.setOnClickListener(view -> send());

        refreshButton.setOnClickListener(view -> setBalance());

        /**
         * uiThread for setting up current balance
         * @param Runnable
         */
        getActivity().runOnUiThread(new Runnable() {
            /**
             * run method for setUp
             * balances in ether
             */
            @Override
            public void run() {
                setBalance();
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    void setBalance() {

        /* getting balance from wallet account */
        bigInteger = Web3jHandler.getBalance();

        try {
            BigDecimal eth = Convert.toWei(bigInteger.toString(), Convert.Unit.WEI).divide(new BigDecimal("1000000000000000000"));
            /* setting balance to edit text */
            etherTextView.setText(eth + " ETH");
            ethTextView.setText(Web3jHandler.getBalanceUSD(eth) + " USD");
        } catch (IOException ignored) {
        }
    }

    /**
     * this method is start send activity for taking required inputs for transaction
     */
    void send() {
        /*
         * startActivity method for for starting new activity
         * @param Intent
         */
        startActivity(new Intent(getActivity(), SendTransactionActivity.class));
    }
}