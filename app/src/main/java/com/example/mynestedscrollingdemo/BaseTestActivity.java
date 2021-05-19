package com.example.mynestedscrollingdemo;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class BaseTestActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void initRecyclerView(){
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        List<String> data = new ArrayList<>();
        for(int i = 0; i < 40; i++){
            data.add("测试数据--->" + i );
        }
        MyAdapter adapter = new MyAdapter(data);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter.notifyDataSetChanged();
    }


    static class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>{
        private List<String> data;
        private int count;
        public MyAdapter(List<String> data){
            this.data = data;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            count++;
            Log.e("viewHolder->count","----->" + count);
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_of_adapter, parent,false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            holder.testTv.setText(data.get(position));
        }

        @Override
        public int getItemCount() {
            return data == null ? 0 : data.size();
        }

        static class MyViewHolder extends RecyclerView.ViewHolder{

            TextView testTv;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                testTv = itemView.findViewById(R.id.item_tv);
            }
        }

    }
}
