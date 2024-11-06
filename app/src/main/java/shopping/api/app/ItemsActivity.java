package shopping.api.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ItemsActivity extends AppCompatActivity {


    ProductsAdapter productsAdapter;
    List<Product> productList = new ArrayList<>();

    EbayAdapter ebayAdapter;
    List<EbayModel> ebayModelList = new ArrayList<>();

    AllAdapter adapter;
    List<AllModel> allModels = new ArrayList<>();

    RecyclerView amazonRecycler,ebayRecycler,randomRecycler;
    ProgressBar progressBar;
    ScrollView contentLyt;

    String search;

    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);

        amazonRecycler = findViewById(R.id.amazonRecyclerView);
        ebayRecycler = findViewById(R.id.ebayRecyclerView);
        randomRecycler = findViewById(R.id.randomRecycler);
        contentLyt = findViewById(R.id.lytContent);
        progressBar = findViewById(R.id.progressBar);
        search = getIntent().getStringExtra("search").toLowerCase();

        toolbar = findViewById(R.id.toolbar);
        progressBar.setVisibility(View.VISIBLE);
        contentLyt.setVisibility(View.GONE);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Results for: "+search);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        getAmazonData();

        getEbayData();

        getRandomData();

    }

    private void getAmazonData(){
        amazonRecycler.setHasFixedSize(true);
        amazonRecycler.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));

        productsAdapter = new ProductsAdapter(productList);

        amazonRecycler.setAdapter(productsAdapter);

//        OkHttpClient client = new OkHttpClient();
//
//        Request request = new Request.Builder()
//                .url("https://amazon23.p.rapidapi.com/product-search?query="+search+"&page=2&country=IN")
//                .get()
//                .addHeader("X-RapidAPI-Key", "0f7e397e25mshd4505846673bbbap164a52jsnff7a130a56e8")
//                .addHeader("X-RapidAPI-Host", "amazon23.p.rapidapi.com")
//                .build();

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://amazon23.p.rapidapi.com/product-search?query="+search+"&page=1&country=IN")
                .get()
                .addHeader("X-RapidAPI-Key", "0fa18e27c2mshef8136c7307f7a1p1c55f9jsn069a364e1515")
                .addHeader("X-RapidAPI-Host", "amazon23.p.rapidapi.com")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(ItemsActivity.this, "Failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {

                    String responseData = response.body().string();
                    Gson gson = new Gson();
                    JsonObject jsonResponse = gson.fromJson(responseData, JsonObject.class);
                    JsonArray resultArray = jsonResponse.getAsJsonArray("result");

                    for (JsonElement productElement : resultArray) {
                        JsonObject productObject = productElement.getAsJsonObject();
                        Product product = new Product();
                        // Parse and set other fields
                        product.setTitle(productObject.get("title").getAsString());
                        product.setThumbnail(productObject.get("thumbnail").getAsString());
                        product.setUrl(productObject.get("url").getAsString());
                        product.setCurrentPrice(productObject.get("price").getAsJsonObject().get("current_price").getAsDouble());

                        double price = product.getCurrentPrice();
                        if (price !=0){
                            productList.add(product);

                        }

                    }

                    // Sort productList by price
                    Collections.sort(productList, new Comparator<Product>() {
                        @Override
                        public int compare(Product product1, Product product2) {
                            return Double.compare(product1.getCurrentPrice(), product2.getCurrentPrice());
                        }
                    });

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setVisibility(View.GONE);
                            contentLyt.setVisibility(View.VISIBLE);
                            productsAdapter.notifyDataSetChanged();
                        }
                    });
                }
            }
        });

    }

    private void getEbayData(){
        ebayRecycler.setHasFixedSize(true);
        ebayRecycler.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));

        ebayAdapter = new EbayAdapter(ebayModelList,ItemsActivity.this);
        ebayRecycler.setAdapter(ebayAdapter);

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://ebay-products-search-scraper.p.rapidapi.com/products?query="+search+"&page=1&Item_Location=asia")
                .get()
                .addHeader("X-RapidAPI-Key", "0f7e397e25mshd4505846673bbbap164a52jsnff7a130a56e8")
                .addHeader("X-RapidAPI-Host", "ebay-products-search-scraper.p.rapidapi.com")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()){
                    String responseData = response.body().string();
                    Gson gson = new Gson();
                    JsonObject jsonResponse = gson.fromJson(responseData, JsonObject.class);
                    try {
                        JsonArray resultArray = jsonResponse.getAsJsonArray("products");

                        for (JsonElement productElement : resultArray) {
                            JsonObject productObject = productElement.getAsJsonObject();
                            EbayModel product = new EbayModel();
                            // Parse and set other fields
                            product.setTitle(productObject.get("title").getAsString());
                            product.setImage(productObject.get("image").getAsString());
                            product.setProductLink(productObject.get("productLink").getAsString());

                            String price = productObject.get("price").getAsString();

                            if (!price.contains("to")){
                                String rex = price.replace("$","");
                                String rex1 = rex.replace(",","");

                                product.setPrice(Double.parseDouble(rex1));
                                ebayModelList.add(product);
                            }


//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//
//                            }
//                        });

                        }
                        // Sort productList by price
                        Collections.sort(ebayModelList, new Comparator<EbayModel>() {
                            @Override
                            public int compare(EbayModel product1, EbayModel product2) {
                                return Double.compare(product1.getPrice(), product2.getPrice());
                            }
                        });
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ebayAdapter.notifyDataSetChanged();
                            }
                        });

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }else{
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(ItemsActivity.this, "No Data available:", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });
    }

    private void getRandomData(){

        randomRecycler.setHasFixedSize(true);
        randomRecycler.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));

        adapter = new AllAdapter(allModels,ItemsActivity.this);
        randomRecycler.setAdapter(adapter);




        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://google-data-scraper.p.rapidapi.com/search/shop/"+search+"?api_key=1670b6309dbd087cfd2a5eca7ef1a104")
                .get()
                .addHeader("X-RapidAPI-Key", "0f7e397e25mshd4505846673bbbap164a52jsnff7a130a56e8")
                .addHeader("X-RapidAPI-Host", "google-data-scraper.p.rapidapi.com")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                if (response.isSuccessful()){
                    String responseData = response.body().string();
                    Gson gson = new Gson();
                    JsonObject jsonResponse = gson.fromJson(responseData, JsonObject.class);
                    JsonArray resultArray = jsonResponse.getAsJsonArray("shopping_results");

                    for (JsonElement productElement : resultArray) {
                        JsonObject productObject = productElement.getAsJsonObject();
                        AllModel model = new AllModel();
                        // Parse and set other fields
                        model.setTitle(productObject.get("title").getAsString());
                        model.setThumbnail(productObject.get("thumbnail").getAsString());
                        model.setLink(productObject.get("link").getAsString());
                        model.setExtracted_price(productObject.get("extracted_price").getAsDouble());
                        model.setSource(productObject.get("source").getAsString());
                        double price = productObject.get("extracted_price").getAsDouble();
                        if (price !=0){
                            allModels.add(model);

                        }

//
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                Toast.makeText(ItemsActivity.this, productObject.toString(), Toast.LENGTH_SHORT).show();
//                            }
//                        });


                    }
                    // Sort productList by price
                    Collections.sort(allModels, new Comparator<AllModel>() {
                        @Override
                        public int compare(AllModel product1, AllModel product2) {
                            return Double.compare(product1.getExtracted_price(), product2.getExtracted_price());
                        }
                    });

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
                        }
                    });
                }









            }
        });

    }
}