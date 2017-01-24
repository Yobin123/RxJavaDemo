package com.yobin.stee.rxjavasimple.module.cache_6;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.yobin.stee.rxjavasimple.BaseFragment;
import com.yobin.stee.rxjavasimple.R;
import com.yobin.stee.rxjavasimple.adapter.ItemListAdapter;
import com.yobin.stee.rxjavasimple.model.Item;
import com.yobin.stee.rxjavasimple.module.cache_6.data.Data;

import java.util.List;

import rx.Observer;

/**
 * Created by yobin_he on 2017/1/24.
 */

public class CacheFragment extends BaseFragment {
    private TextView loadingTimeTv;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView cacheRv;
    private Button clearMemoryCacheBt;
    private Button clearMemoryAndDiskCacheBt;
    private Button loadBt;

    private long startingTime;
    private ItemListAdapter adapter = new ItemListAdapter(getContext());

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cache,container,false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        loadingTimeTv = (TextView) view.findViewById(R.id.loadingTimeTv);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        cacheRv = (RecyclerView) view.findViewById(R.id.cacheRv);
        tipBt = (Button) view.findViewById(R.id.tipBt);
        clearMemoryCacheBt = (Button) view.findViewById(R.id.clearMemoryCacheBt);
        clearMemoryAndDiskCacheBt = (Button) view.findViewById(R.id.clearMemoryAndDiskCacheBt);
        loadBt = (Button) view.findViewById(R.id.loadBt);

        cacheRv.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        cacheRv.setAdapter(adapter);
        swipeRefreshLayout.setColorSchemeColors(Color.BLUE, Color.GREEN, Color.RED, Color.YELLOW);
        swipeRefreshLayout.setEnabled(false);

        tipBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(getTitleRes(),getDialogRes());
            }
        });

        clearMemoryCacheBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearMemoryCache();
            }
        });

        clearMemoryAndDiskCacheBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearMemoryAndDiskCache();
            }
        });

        loadBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                load();
            }
        });
    }

    private void load() {
        swipeRefreshLayout.setRefreshing(true);
        startingTime = System.currentTimeMillis();
        unsubscribe();
        subscription = Data.getInstance()
                .subscribeData(new Observer<List<Item>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        swipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(getActivity(), R.string.loading_failed, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(List<Item> items) {
                        swipeRefreshLayout.setRefreshing(false);
                        int loadingTime = (int) (System.currentTimeMillis() - startingTime);
                        loadingTimeTv.setText(getString(R.string.loading_time_and_source, String.valueOf(loadingTime), Data.getInstance().getDataSourceText()));
                        adapter.setItems(items);
                    }
                });
    }

    private void clearMemoryAndDiskCache() {
        Data.getInstance().clearMemoryAndDiskCache();
        adapter.setItems(null);
        Toast.makeText(getActivity(), R.string.memory_and_disk_cache_cleared, Toast.LENGTH_SHORT).show();
    }

    private void clearMemoryCache() {
        Data.getInstance().clearMemoryCache();
        adapter.setItems(null);
        Toast.makeText(getActivity(), R.string.memory_cache_cleared, Toast.LENGTH_SHORT).show();
    }



    @Override
    protected int getDialogRes() {
        return R.layout.dialog_cache;
    }

    @Override
    protected int getTitleRes() {
        return R.string.title_cache;
    }
}
