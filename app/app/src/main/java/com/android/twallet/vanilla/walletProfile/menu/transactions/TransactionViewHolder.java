package com.android.twallet.vanilla.walletProfile.menu.transactions;

import android.view.View;
import android.widget.TextView;

import com.android.twallet.vanilla.R;

public class TransactionViewHolder {

    TextView txAddress, txValue;

    public TransactionViewHolder(View view) {
        txAddress = view.findViewById(R.id.txAddress);
        txValue = view.findViewById(R.id.txValue);
    }

}
