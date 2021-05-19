package com.example.mynestedscrollingdemo;

import android.os.Bundle;

import androidx.annotation.Nullable;

public class NestedScrollParent2Activity extends BaseTestActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nestedparent2);
        initRecyclerView();
    }
}
