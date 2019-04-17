package com.example.anggarisky.fidashboard.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.anggarisky.fidashboard.model.DemoItem;
import com.example.anggarisky.fidashboard.R;
import com.example.anggarisky.fidashboard.utils.BlinkingLoader;
import com.example.anggarisky.fidashboard.utils.Space;
import com.example.anggarisky.fidashboard.adapters.EnglishAdapter;
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
    private EnglishAdapter englishAdapter;
    private String className;
    private StaggeredGridLayoutManager mLayoutManager;
    private int arrayInt[] = new int[]{R.drawable.placeholder_1,R.drawable.placeholder_2,R.drawable.placeholder_3,R.drawable.placeholder_4,R.drawable.placeholder_5,R.drawable.placeholder_6,R.drawable.placeholder_7,R.drawable.placeholder_8};
    private int c=0;
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
        View view = inflater.inflate(R.layout.english_demo, container, false);
        className = getArguments().getString("className");
        RecyclerView recyclerViewDemo = view.findViewById(R.id.recyclerViewDemo);
        mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerViewDemo.setLayoutManager(mLayoutManager);
        recyclerViewDemo.addItemDecoration(new Space(10, 1));
        englishAdapter = new EnglishAdapter(demoItemList,getContext(),className);
        recyclerViewDemo.setAdapter(englishAdapter);
        fetchData();
        return view;
    }

    private void fetchData() {
        c=0;
        databaseReference = FirebaseDatabase.getInstance().getReference("books");
        databaseReference.child("CBSE").child("English").child(className).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    BlinkingLoader blinkingLoader = (BlinkingLoader) getActivity().findViewById(R.id.dotLoading1);
                    blinkingLoader.setVisibility(View.INVISIBLE);
                    demoItemList.add(new DemoItem(dataSnapshot1.getKey(),arrayInt[c]));
                    englishAdapter.notifyDataSetChanged();
                    c++;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Opsss.... Something is wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
