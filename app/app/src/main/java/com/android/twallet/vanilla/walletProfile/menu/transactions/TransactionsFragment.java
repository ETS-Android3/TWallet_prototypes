package com.android.twallet.vanilla.walletProfile.menu.transactions;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.android.twallet.vanilla.R;
import com.android.twallet.vanilla.databinding.FragmentTransactionsBinding;
import com.android.twallet.vanilla.web3J.Web3jHandler;

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

        setTransactionHistory();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    void setTransactionHistory() {

        List<Map<String, String>> list = Web3jHandler.getPastTransactions();

        txView.setAdapter(new SimpleAdapter(getActivity(), list, R.layout.tx_item, new String[]{"status", "date", "address", "value"}, new int[]{R.id.txStatus, R.id.txDate, R.id.txAddress, R.id.txValue}));
    }
}