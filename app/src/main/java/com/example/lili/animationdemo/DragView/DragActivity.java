package com.example.lili.animationdemo.DragView;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.SimpleAdapter;

import com.example.lili.animationdemo.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * @author li.li
 * @Description:
 * @date 18-6-8
 * @copyright MIBC
 */
public class DragActivity extends AppCompatActivity implements DraggableGridView.ItemSwapListener {
    private DraggableGridView mGridView;
    private SimpleAdapter mSimpleAdapter;
    private List<HashMap<String, Object>> dataSourceList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dragview);
        initData();
        initView();
    }

    private void initView() {
        mGridView = findViewById(R.id.drag_grid_view);

        mSimpleAdapter = new SimpleAdapter(this, dataSourceList,
                R.layout.layout_grid_view_item, new String[]{"item_image", "item_text"},
                new int[]{R.id.item_image, R.id.item_text});
        mGridView.setAdapter(mSimpleAdapter);
        mGridView.setItemSwapListener(this);
    }

    private void initData() {
        dataSourceList = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            HashMap<String, Object> itemHashMap = new HashMap<>();
            itemHashMap.put("item_image", R.mipmap.ic_launcher_round);
            itemHashMap.put("item_text", "拖拽 " + Integer.toString(i));
            dataSourceList.add(itemHashMap);
        }
    }

    @Override
    public void onSwapItem(int fromItem, int toItem) {
        HashMap<String, Object> item = dataSourceList.get(fromItem);
        if (fromItem < toItem) {
            for (int i = fromItem; i < toItem; i++) {
                /*dataSourceList.get(i+1)-->dataSourceList.get(i)*/
                Collections.swap(dataSourceList, i, i + 1);
            }
        } else if (fromItem > toItem) {
            for (int i = fromItem; i > toItem; i--) {
                /*dataSourceList.get(i-1)-->dataSourceList.get(i)*/
                Collections.swap(dataSourceList, i, i - 1);
            }
        }
        dataSourceList.set(toItem, item);
        mSimpleAdapter.notifyDataSetChanged();
    }
}
