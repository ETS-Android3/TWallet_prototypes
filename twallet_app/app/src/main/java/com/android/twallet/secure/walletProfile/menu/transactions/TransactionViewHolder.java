package com.android.twallet.secure.walletProfile.menu.transactions;

import android.view.View;
import android.widget.TextView;

import com.android.twallet.secure.R;

public class TransactionViewHolder {

    TextView txAddress, txValue;

    public TransactionViewHolder(View view) {
        txAddress = view.findViewById(R.id.txAddress);
        txValue = view.findViewById(R.id.txValue);
    }

}
