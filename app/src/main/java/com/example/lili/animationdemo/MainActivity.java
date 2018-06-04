package com.example.lili.animationdemo;

import java.util.ArrayList;

public class MainActivity extends BaseListActivity {


    public MainActivity() {
        initListItems();
    }

    public void initListItems() {
        initData();
        initDisplayList();
    }

    private void initData(){
        mItemsInfo = new ArrayList<>();

        ItemComponentInfo info;
        info = new ItemComponentInfo("PropertyAnimation", PropertyAnimActivity.class);
        mItemsInfo.add(info);
        info = new ItemComponentInfo("HandWriting", HandwritingActivity.class);
        mItemsInfo.add(info);
    }

    private void initDisplayList() {
        if (mItemsInfo != null && mItemsInfo.size() > 0) {
            mUnits = new String[mItemsInfo.size()];

            for (int i = 0; i < mItemsInfo.size(); i++) {
                mUnits[i] = mItemsInfo.get(i).mDisplayName;
            }
        }
    }
}

