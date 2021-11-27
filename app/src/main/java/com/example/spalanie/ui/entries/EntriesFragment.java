package com.example.spalanie.ui.entries;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.spalanie.FileHelper;
import com.example.spalanie.R;
import com.example.spalanie.RefuelAdapter;
import com.example.spalanie.databinding.FragmentEntriesBinding;
import com.example.spalanie.databinding.FragmentHomeBinding;
import com.example.spalanie.model.RefuelData;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class EntriesFragment extends Fragment {

    private EntriesViewModel entriesViewModel;
    private FragmentEntriesBinding binding;

    private ListView listView;
    private RefuelAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        entriesViewModel =
                new ViewModelProvider(this).get(EntriesViewModel.class);

        binding = FragmentEntriesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

//        final TextView textView = binding.textHome;
//        entriesViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });

        listView = binding.entriesList;
        try {
            ArrayList<JSONObject> dataList = new ArrayList<>();
            JSONArray array = FileHelper.openFile(getContext());

            //Gson gson = new Gson();
            for (int i = 0; i < array.length(); i++) {

                dataList.add(array.getJSONObject(i));
            }
            adapter = new RefuelAdapter(getContext(), dataList);

            listView.setAdapter(adapter);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return root;
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}