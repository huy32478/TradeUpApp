package com.example.tradeupappmoi.utils;

import com.algolia.search.saas.Client;
import com.algolia.search.saas.Index;

public class AlgoliaHelper {
    private static final String ALGOLIA_APP_ID = "AUU05QTDD9";
    private static final String ALGOLIA_SEARCH_API_KEY = "60ca8863da0b0c41a03ffe3f6b9b001d";
    private static final String INDEX_NAME = "products"; // Tên index đã tạo

    private static final Client client = new Client(ALGOLIA_APP_ID, ALGOLIA_SEARCH_API_KEY);
    public static final Index index = client.getIndex(INDEX_NAME);
}
