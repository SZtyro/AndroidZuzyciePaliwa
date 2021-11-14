package com.example.spalanie.ui;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.spalanie.R;
import com.example.spalanie.databinding.ActivityMainBinding;
import com.example.spalanie.databinding.FragmentNewDataBinding;
import com.google.android.material.snackbar.Snackbar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewDataFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewDataFragment extends Fragment {

    private FragmentNewDataBinding binding;

    public NewDataFragment() {
        super(R.layout.fragment_new_data);
    }

    public static NewDataFragment newInstance() {
        NewDataFragment fragment = new NewDataFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNewDataBinding.inflate(
                inflater
        );

        binding.saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }

        });
        //return inflater.inflate(R.layout.fragment_new_data, container, false);
        return  binding.getRoot();
    }


}