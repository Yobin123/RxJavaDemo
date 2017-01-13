package com.yobin.stee.rxjavasimple;

import android.app.AlertDialog;
import android.app.Fragment;

import butterknife.OnClick;
import rx.Subscription;

/**
 * Created by yobin_he on 2017/1/13.
 */

public abstract class BaseFragment extends Fragment {
    protected Subscription subscription;

    @OnClick(R.id.tipBt)
    void tip() {
        new AlertDialog.Builder(getActivity())
                .setTitle(getTitleRes())
                .setView(getActivity().getLayoutInflater().inflate(getDialogRes(), null))
                .show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unsubscribe();
    }

    //用于解除相关的注册
    protected void unsubscribe() {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }

    //必须被重写的
    protected abstract int getDialogRes();

    protected abstract int getTitleRes();
}
