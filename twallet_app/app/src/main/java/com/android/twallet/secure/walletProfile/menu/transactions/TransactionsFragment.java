package com.android.twallet.secure.walletProfile.menu.transactions;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.android.twallet.secure.R;
import com.android.twallet.secure.databinding.FragmentTransactionsBinding;
import com.android.twallet.secure.web3J.Web3jHandler;

import java.util.List;
import java.util.Map;


public class TransactionsFragment extends Fragment {

    private TransactionsViewModel transactionsViewModel;
    private FragmentTransactionsBinding binding;

    /* Variable for store transaction history */
    ListView txView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        transactionsViewModel =
                new ViewModelProvider(this).get(TransactionsViewModel.class);

        binding = FragmentTransactionsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        /* initializing transaction history list */
        txView = binding.transactionHistories;
        setTransactionHistoryAsync();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    void setTransactionHistoryAsync() {
        HandlerThread ht = new HandlerThread("TransactionHandler");
        ht.start();
        Handler asyncHandler = new Handler(ht.getLooper());

        Runnable runnable = () -> {
            List<Map<String, String>> list = Web3jHandler.getPastTransactions();
            requireActivity().runOnUiThread(() ->
                    txView.setAdapter(new SimpleAdapter(getActivity(), list, R.layout.tx_item, new String[]{"status", "date", "address", "value"}, new int[]{R.id.txStatus, R.id.txDate, R.id.txAddress, R.id.txValue})));

        };
        asyncHandler.post(runnable);
    }
}