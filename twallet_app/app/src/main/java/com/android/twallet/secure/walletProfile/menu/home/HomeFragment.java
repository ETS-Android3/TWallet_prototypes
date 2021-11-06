package com.android.twallet.secure.walletProfile.menu.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.android.twallet.secure.databinding.FragmentHomeBinding;
import com.android.twallet.secure.web3J.Web3jHandler;

import org.web3j.utils.Convert;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

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
        sendButton.setOnClickListener(view -> {
            /*
             * startActivity method for for starting new activity
             * @param Intent
             */
            startActivity(new Intent(getActivity(), SendTransactionActivity.class));
        });

        refreshButton.setOnClickListener(view -> setBalanceAsync());

        setBalanceAsync();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    void setBalanceAsync() {
        HandlerThread ht = new HandlerThread("TransactionHandler");
        ht.start();
        Handler asyncHandler = new Handler(ht.getLooper());

        Runnable runnable = () -> {
            /* getting balance from wallet account */
            bigInteger = Web3jHandler.getBalance();

            requireActivity().runOnUiThread(() -> {
                try {
                    BigDecimal eth = Convert.toWei(bigInteger.toString(), Convert.Unit.WEI).divide(new BigDecimal("1000000000000000000"));
                    /* setting balance to edit text */
                    etherTextView.setText(eth + " ETH");
                    ethTextView.setText(Web3jHandler.getBalanceUSD(eth) + " USD");
                } catch (IOException ignored) {
                }
            });
        };
        asyncHandler.post(runnable);
    }

}