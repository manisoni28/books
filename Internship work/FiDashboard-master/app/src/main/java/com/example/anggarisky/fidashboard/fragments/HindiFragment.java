package com.example.anggarisky.fidashboard.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.anggarisky.fidashboard.model.DemoItem;
import com.example.anggarisky.fidashboard.R;
import com.example.anggarisky.fidashboard.utils.Space;
import com.example.anggarisky.fidashboard.adapters.CommonAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class EnglishFragment extends Fragment {
    private List<String> booksList = new ArrayList<>();
    private DatabaseReference databaseReference;
    private List<DemoItem> demoItemList= new ArrayList<>();
    private CommonAdapter englishAdapter;
    private String className;

    public EnglishFragment() {
    }


    public static EnglishFragment newInstance(String className) {
        EnglishFragment fragment = new EnglishFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        args.putString("className",className);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_demo, container, false);
         className = getArguments().getString("className");
        RecyclerView recyclerViewDemo = view.findViewById(R.id.recyclerViewDemo);
        recyclerViewDemo.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewDemo.addItemDecoration(new Space(20, 1));
        englishAdapter = new CommonAdapter(demoItemList,getContext());
        recyclerViewDemo.setAdapter(englishAdapter);
        fetchData();
        return view;
    }

    private void fetchData() {
        databaseReference = FirebaseDatabase.getInstance().getReference("books");
        databaseReference.child("CBSE").child("English").child(className).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    demoItemList.add(new DemoItem(dataSnapshot1.getKey(),dataSnapshot1.getKey(),dataSnapshot1.getValue().toString()));
                    englishAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Opsss.... Something is wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
