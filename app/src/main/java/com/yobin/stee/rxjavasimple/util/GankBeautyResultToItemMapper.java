package com.yobin.stee.rxjavasimple.util;

import com.yobin.stee.rxjavasimple.model.GankBeaty;
import com.yobin.stee.rxjavasimple.model.GankBeautyResult;
import com.yobin.stee.rxjavasimple.model.Item;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import rx.functions.Func1;

/**
 * Created by yobin_he on 2017/1/23.
 */

public class GankBeautyResultToItemMapper implements Func1<GankBeautyResult,List<Item>> {
    private static GankBeautyResultToItemMapper INSTANCE = new GankBeautyResultToItemMapper();
    private GankBeautyResultToItemMapper(){

    }
    public static GankBeautyResultToItemMapper getInstance(){
        return INSTANCE;
    }
    @Override
    public List<Item> call(GankBeautyResult gankBeautyResult) {
        //此时beauties是一个list对像，其中这个list对象中，此时的对象中包含相应的时间和imageUrl
        List<GankBeaty> gankBeaties = gankBeautyResult.beauties;
        List<Item> items = new ArrayList<>(gankBeaties.size());
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SS'Z'");
        SimpleDateFormat outputFormat = new SimpleDateFormat("yy/MM/dd HH:mm:ss");
        for(GankBeaty gankBeauty : gankBeaties){
            Item item = new Item();
            try {
                Date date = inputFormat.parse(gankBeauty.createdAt);
                item.description = outputFormat.format(date); //把相应的description换成了相应的时间
            } catch (ParseException e) {
                e.printStackTrace();
                item.description = "unknown date";
            }
            item.imageUrl = gankBeauty.url;
            items.add(item);
        }
        return items;
    }
}
