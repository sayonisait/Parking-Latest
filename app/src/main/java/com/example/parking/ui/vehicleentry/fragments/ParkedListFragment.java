package com.example.parking.ui.vehicleentry.fragments;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.parking.R;
import com.example.parking.model.Entry;
import com.example.parking.ui.vehicleentry.adapters.ParkedVehicleListAdapter;
import com.example.parking.ui.vehicleentry.viewmodels.EntryListViewModel;
import com.example.parking.ui.vehicleentry.viewmodels.VehicleEntryViewModel;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.List;

public class ParkedListFragment extends Fragment {

    private EntryListViewModel mViewModel;
    RecyclerView recyclerView;

    public static ParkedListFragment newInstance() {
        return new ParkedListFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.parked_list_fragment, container, false);
        recyclerView=view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);

        EditText editText= view.findViewById(R.id.edit_query);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                mViewModel.setQuery(editable.toString()).observe(ParkedListFragment.this,s->{
                    entries.clear();
                    entries.addAll(s);
                    recyclerView.getAdapter().notifyDataSetChanged();
                });
            }
        });
        return view;
    }

    List<Entry> entries;
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(EntryListViewModel.class);

        mViewModel.getParkedEntries().observe(this, s->{
            entries=s;
            recyclerView.setAdapter(new ParkedVehicleListAdapter(entries, (FragmentActivity) mContext));
        });




    }

    Context mContext;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext=context;
    }
}
