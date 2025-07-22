package com.example.tradeupappmoi.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.algolia.search.saas.Query;
import com.example.tradeupappmoi.R;
import com.example.tradeupappmoi.adapters.ItemAdapter;
import com.example.tradeupappmoi.models.Item;
import com.example.tradeupappmoi.utils.AlgoliaHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    private ItemAdapter adapter;
    private final List<Item> itemList = new ArrayList<>();
    private final Handler searchHandler = new Handler();
    private final int SEARCH_DELAY = 200;  // Delay time for search (in milliseconds)

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);

        EditText edtSearch = rootView.findViewById(R.id.edtSearch);
        RecyclerView recyclerView = rootView.findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ItemAdapter(itemList);
        recyclerView.setAdapter(adapter);

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Remove any previous pending search actions
                searchHandler.removeCallbacksAndMessages(null);

                // Run search after the delay
                searchHandler.postDelayed(() -> performSearch(s.toString()), SEARCH_DELAY);
            }
        });

        return rootView;
    }

    @SuppressLint("NotifyDataSetChanged")
    private void performSearch(String keyword) {
        // Show a loading message or indication here (e.g., progress bar)

        if (keyword.isEmpty()) {
            itemList.clear();
            adapter.notifyDataSetChanged();
            return;  // Do nothing if keyword is empty
        }

        Query query = new Query(keyword).setHitsPerPage(20);

        AlgoliaHelper.index.searchAsync(query, (content, error) -> {
            if (error != null) {
                assert getActivity() != null;
                getActivity().runOnUiThread(() -> Toast.makeText(getContext(), "Search error", Toast.LENGTH_SHORT).show());
                return;
            }

            try {
                assert content != null;
                JSONArray hits = content.getJSONArray("hits");
                itemList.clear();

                if (hits.length() == 0) {
                    assert getActivity() != null;
                    getActivity().runOnUiThread(() -> Toast.makeText(getContext(), "No results found", Toast.LENGTH_SHORT).show());
                }

                for (int i = 0; i < hits.length(); i++) {
                    JSONObject obj = hits.getJSONObject(i);
                    Item item = new Item();
                    item.setTitle(obj.optString("title"));
                    item.setPrice(obj.optDouble("price"));
                    item.setCategory(obj.optString("category"));
                    item.setImage(obj.optString("image"));
                    item.setLocation(obj.optString("location"));  // Lấy location từ Algolia
                    itemList.add(item);
                }

                assert getActivity() != null;
                getActivity().runOnUiThread(() -> adapter.notifyDataSetChanged());

            } catch (Exception e) {
                assert getActivity() != null;
                getActivity().runOnUiThread(() -> Toast.makeText(getContext(), "Parse error", Toast.LENGTH_SHORT).show());
            }
        });
    }
}
