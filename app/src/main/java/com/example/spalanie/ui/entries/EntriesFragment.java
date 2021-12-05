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
import java.util.Collections;

public class EntriesFragment extends Fragment {

    private EntriesViewModel entriesViewModel;
    private FragmentEntriesBinding binding;

    private ListView listView;
    private RefuelAdapter adapter;

    ArrayList<JSONObject> dataList = new ArrayList<>();
    JSONArray array = new JSONArray();

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
        refreshData();

        return root;
    }

    void refreshData(){
        try {

            array = FileHelper.openFile(getContext());

            //Gson gson = new Gson();
            //for (int i = array.length() - 1; i >= 0; i--) {
            dataList.clear();
            for (int i = 0; i < array.length() ; i++) {

                dataList.add(array.getJSONObject(i));
            }
            adapter = new RefuelAdapter(getContext(), dataList, this);

            listView.setAdapter(adapter);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Collections.reverse(dataList);
    }

    public void deleteElem(int position){
        int realPosition = (dataList.size() - position) - 1;
        dataList.remove(position);
        array.remove(realPosition);
        FileHelper.saveFile(array, getContext());
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}