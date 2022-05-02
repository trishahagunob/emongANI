package com.example.emongani;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class pendingFragment extends Fragment {

    RecyclerView pending_rv;
    product_adapter adapter;
    public pendingFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pending, container, false);
        pending_rv = (RecyclerView) view.findViewById(R.id.pending_rv);
        pending_rv.setLayoutManager(new LinearLayoutManager(getContext()));
       // eAuth = FirebaseAuth.getInstance();
       // currentUserID = eAuth.getCurrentUser().getUid();
       // DatabaseReference pendingref = FirebaseDatabase.getInstance().getReference().child("Pending");

        FirebaseRecyclerOptions<ProductHelper> options =
                new FirebaseRecyclerOptions.Builder<ProductHelper>()
                    .setQuery(FirebaseDatabase.getInstance().getReference("Product").child("Pending"),ProductHelper.class)
                    .build();
        adapter = new product_adapter(options);
        pending_rv.setAdapter(adapter);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
        requireActivity().finish();
    }
}