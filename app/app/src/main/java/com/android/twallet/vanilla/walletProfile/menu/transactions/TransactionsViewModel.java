package com.android.twallet.vanilla.walletProfile.menu.transactions;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TransactionsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public TransactionsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Transaction fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}