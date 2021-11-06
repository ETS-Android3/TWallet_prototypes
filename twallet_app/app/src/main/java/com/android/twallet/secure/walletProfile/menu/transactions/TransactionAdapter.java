package com.android.twallet.secure.walletProfile.menu.transactions;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.android.twallet.secure.R;


public class TransactionAdapter extends ArrayAdapter<String> {

    Context context;

    public TransactionAdapter(Context context) {
        super(context, R.layout.tx_item);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View txItem = convertView;
        TransactionViewHolder holder = null;
        if (txItem == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            txItem = layoutInflater.inflate(R.layout.tx_item, parent, false);
            holder = new TransactionViewHolder(txItem);
            txItem.setTag(holder);
        } else
            holder = (TransactionViewHolder) txItem.getTag();

        holder.txAddress.setText("From Someone");
        holder.txValue.setText("1.0423423");

        return txItem;
    }
}
