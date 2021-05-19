package com.example.mynestedscrollingdemo;

import android.os.Bundle;

import androidx.annotation.Nullable;

public class DispatchEventNestedScrollActivity extends BaseTestActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dispatch);
        initRecyclerView();
    }
}
