package com.yobin.stee.rxjavasimple.module.map_2;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
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
import com.yobin.stee.rxjavasimple.network.Network;
import com.yobin.stee.rxjavasimple.util.GankBeautyResultToItemMapper;

import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by yobin_he on 2017/1/23.
 */

public class MapFragment  extends BaseFragment{
    private int page = 0;
    private TextView pageTv;
    private Button previousPageBt;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView gridRv;
    private Button nextPageBt;

    private ItemListAdapter adapter ;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map,container,false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        gridRv = (RecyclerView) view.findViewById(R.id.gridRv);
        pageTv = (TextView) view.findViewById(R.id.pageTv);
        previousPageBt = (Button) view.findViewById(R.id.previousPageBt);
        nextPageBt = (Button) view.findViewById(R.id.nextPageBt);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        tipBt = (Button) view.findViewById(R.id.tipBt);

        adapter = new ItemListAdapter(getActivity());
        gridRv.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        gridRv.setAdapter(adapter);
        swipeRefreshLayout.setColorSchemeColors(Color.BLUE,Color.GREEN,Color.RED);
        swipeRefreshLayout.setEnabled(false);

//        adapter.setOnItemClickListener(new ItemListAdapter.OnRvItemClickListener() {
//            @Override
//            public void onItemClick(View view, String data) {
//                Intent intent = new Intent(getContext(), ImageActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putString("imageUrl",data);
//                intent.putExtras(bundle);
//                getContext().startActivity(intent);
//            }
//        });

        tipBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity())
                        .setTitle(getTitleRes())
                        .setView(getActivity().getLayoutInflater().inflate(getDialogRes(), null))
                        .show();
            }
        });


        nextPageBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               loadPage(++page);
                if(page==2){
                    previousPageBt.setEnabled(true);
                }
            }
        });

        previousPageBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadPage(--page);
                if(page==1){
                    previousPageBt.setEnabled(false);
                }
            }
        });

    }

    private void loadPage(int page){
        swipeRefreshLayout.setRefreshing(true);
        unsubscribe();
        subscription = Network.getGankApi()   //得到GankApi
                .getBeauties(10,page)  //可以获取的页数，和数量
                .map(GankBeautyResultToItemMapper.getInstance())  //转化为在observer中的中的list<Item>
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);

    }

    Observer<List<Item>> observer = new Observer<List<Item>>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            swipeRefreshLayout.setRefreshing(false);
            Toast.makeText(getActivity(), R.string.loading_failed, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onNext(List<Item> images) {
            swipeRefreshLayout.setRefreshing(false);
            pageTv.setText(getString(R.string.page_with_number, String.valueOf(page)));
            adapter.setItems(images);
        }
    };


    @Override
    protected int getDialogRes() {
        return R.layout.dialog_map;
    }

    @Override
    protected int getTitleRes() {
        return R.string.title_map;
    }
}
