package com.android.twallet.secure.walletProfile.menu.settings;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.android.twallet.secure.MainActivity;
import com.android.twallet.secure.databinding.FragmentSettingsBinding;
import com.android.twallet.secure.web3J.Web3jHandler;

public class SettingsFragment extends Fragment {

    private SettingsViewModel settingsViewModel;
    private FragmentSettingsBinding binding;

    /* Variables for showing balances in ether */
    TextView addressTextView;

    /* variables for copy address of wallet, export wallet file, retrieve log, trigger monitoring, delete wallet, and logout */
    Button copyWalletAddressButton, exportWalletButton, retrieveLogButton, triggerMonitoringButton, deleteWalletButton, logoutButton;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        settingsViewModel =
                new ViewModelProvider(this).get(SettingsViewModel.class);

        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        addressTextView = binding.addressText;
        addressTextView.setText(Web3jHandler.getWalletAddress());

        copyWalletAddressButton = binding.codyAddress;

        exportWalletButton = binding.exportWallet;

        retrieveLogButton = binding.eventLogButton;

        triggerMonitoringButton = binding.monitoringButton;

        deleteWalletButton = binding.deleteWallet;

        logoutButton = binding.logout;

        /**
         * setting copy wallet setOnClickListener
         * @param OnClickListener
         *
         */
        copyWalletAddressButton.setOnClickListener(new View.OnClickListener() {
            /**
             * method for performing specified logic for copying wallet address
             * @param view
             * */
            @Override
            public void onClick(View view) {
                /* copying wallet address to clipboard */
                final ClipData clipData = ClipData.newPlainText("text label", Web3jHandler.getWalletAddress());
                ((ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE)).setPrimaryClip(clipData);
                Toast.makeText(getActivity(), "Copied to clipboard", Toast.LENGTH_SHORT).show();
            }
        });

        /**
         * setting export wallet setOnClickListener
         * @param OnClickListener
         *
         */
        exportWalletButton.setOnClickListener(new View.OnClickListener() {
            /**
             * method for performing specified logic for exporting wallet file
             * @param view
             * */
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ExportWalletActivity.class));
            }
        });

        /**
         * setting retrieve log setOnClickListener
         * @param OnClickListener
         *
         */
        retrieveLogButton.setOnClickListener(new View.OnClickListener() {
            /**
             * method for performing specified logic for retrieving log
             * @param view
             * */
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), RetrieveLogActivity.class));
            }
        });

        /**
         * setting trigger monitoring wallet setOnClickListener
         * @param OnClickListener
         *
         */
        triggerMonitoringButton.setOnClickListener(new View.OnClickListener() {
            /**
             * method for performing specified logic for triggering monitoring
             * @param view
             * */
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), MonitorWalletActivity.class));
            }
        });

        /**
         * setting delete wallet setOnClickListener
         * @param OnClickListener
         *
         */
        deleteWalletButton.setOnClickListener(new View.OnClickListener() {
            /**
             * method for performing specified logic for deleting wallet file
             * @param view
             * */
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), DeleteWalletActivity.class));
            }
        });

        /**
         * setting logout setOnClickListener
         * @param OnClickListener
         *
         */
        logoutButton.setOnClickListener(new View.OnClickListener() {
            /**
             * method for performing specified logic for logout operation
             * @param view
             * */
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), MainActivity.class));
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}