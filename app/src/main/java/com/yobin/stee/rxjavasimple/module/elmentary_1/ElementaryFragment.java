package com.yobin.stee.rxjavasimple.module.elmentary_1;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.Toast;

import com.yobin.stee.rxjavasimple.BaseFragment;
import com.yobin.stee.rxjavasimple.R;
import com.yobin.stee.rxjavasimple.adapter.ZhuangbiListAdapter;
import com.yobin.stee.rxjavasimple.model.ZhuangbiImage;

import java.util.List;

import butterknife.OnCheckedChanged;
import rx.Observer;

/**
 * Created by yobin_he on 2017/1/13.
 */
public class ElementaryFragment extends BaseFragment {
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView gridRv;
    private ZhuangbiListAdapter adapter = new ZhuangbiListAdapter();

    Observer<List<ZhuangbiImage>> observer = new Observer<List<ZhuangbiImage>>() {
        @Override
        public void onCompleted() {
            
        }

        @Override
        public void onError(Throwable e) {
            swipeRefreshLayout.setRefreshing(false);
            Toast.makeText(getActivity(), R.string.loading_failed, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onNext(List<ZhuangbiImage> images) {
            swipeRefreshLayout.setRefreshing(false);
            adapter.setImages(images);
        }
    };

    @OnCheckedChanged({R.id.searchRb1,R.id.searchRb2,R.id.searchRb3,R.id.searchRb4})
    void onTagChecked(RadioButton searchRb, boolean checked){
        if(checked){
            unsubscribe();
            adapter.setImages(null);
            swipeRefreshLayout.setRefreshing(false);
            search(searchRb.getText().toString());
        }
    }

    private void search(String key) {

    }

    @Override
    protected int getDialogRes() {
        return R.layout.dialog_elementary;
    }

    @Override
    protected int getTitleRes() {
        return R.string.title_elementary;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_elementary,container,false);
        initView(view);
        return view;
    }

    private void initView(View view) {

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        gridRv = (RecyclerView) view.findViewById(R.id.gridRv);

        gridRv.setLayoutManager(new GridLayoutManager(getActivity(),2));
        gridRv.setAdapter(adapter);
        swipeRefreshLayout.setColorSchemeColors(Color.BLUE, Color.GREEN, Color.RED, Color.YELLOW);
        swipeRefreshLayout.setEnabled(false);
    }
}
