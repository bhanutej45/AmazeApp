package shopping.api.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

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

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProductsAdapter productAdapter;
    private List<Product> productList = new ArrayList<>();

    private List<EbayModel> ebayList = new ArrayList<>();
    private EbayAdapter ebayAdapter;

    EditText inputSearch;

    DatabaseReference reference;
    FirebaseUser user;
    PreferenceManager preferenceManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        preferenceManager = new PreferenceManager(MainActivity.this);

        inputSearch = findViewById(R.id.inputSearch);
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference().child("Users");
//        recyclerView.setLayoutManager(new GridLayoutManager(this,2));

//        productAdapter = new ProductsAdapter(productList); // Initialize adapter with empty list
//        recyclerView.setAdapter(productAdapter);

//        fetchAndParseData();

//        ebayAdapter = new EbayAdapter(ebayList);
//        recyclerView.setAdapter(ebayAdapter);
//
//        fetchEbayProducts();

        getUserData();

        inputSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (EditorInfo.IME_ACTION_GO == i){
                    String text = inputSearch.getText().toString();
                    if (text.isEmpty()){
                        Toast.makeText(MainActivity.this, "Enter input for search", Toast.LENGTH_SHORT).show();
                    }else {
                        Intent intent = new Intent(MainActivity.this,ItemsActivity.class);
                        intent.putExtra("search",text);
                        startActivity(intent);
                    }
                }
                return false;
            }
        });

        ImageView btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                preferenceManager.clear();
                Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        ImageView btnMenu = findViewById(R.id.btnMenu);
        DrawerLayout drawerLayout = findViewById(R.id.drawerLyt);

        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });


//        Intent intent = new Intent(MainActivity.this,ItemsActivity.class);
//        intent.putExtra("search","samsung");
//        startActivity(intent);

//        checkData();

    }


    private void checkData(){
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://google-data-scraper.p.rapidapi.com/search/shop/samsung?api_key=1670b6309dbd087cfd2a5eca7ef1a104")
                .get()
                .addHeader("X-RapidAPI-Key", "0f7e397e25mshd4505846673bbbap164a52jsnff7a130a56e8")
                .addHeader("X-RapidAPI-Host", "google-data-scraper.p.rapidapi.com")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
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

                        String title = productObject.get("title").getAsString();

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, "Data hai!: "+title, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }






                }else{
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "Data nahi!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });
    }

    private void getUserData(){
        NavigationView navigationView = findViewById(R.id.navigationView);
        View view = navigationView.getHeaderView(0);
        TextView username = view.findViewById(R.id.username);
        TextView email = view.findViewById(R.id.email);
        reference.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    UserModel model = snapshot.getValue(UserModel.class);
                    if (model !=null){
                        username.setText(model.getUsername());
                        email.setText(model.getEmail());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Error: "+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

//    private void fetchAndParseData() {
//        OkHttpClient client = new OkHttpClient();
//
//        Request request = new Request.Builder()
//                .url("https://amazon23.p.rapidapi.com/product-search?query=iphone&page=2&country=IN")
//                .get()
//                .addHeader("X-RapidAPI-Key", "0f7e397e25mshd4505846673bbbap164a52jsnff7a130a56e8")
//                .addHeader("X-RapidAPI-Host", "amazon23.p.rapidapi.com")
//                .build();
//
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(MainActivity.this, "Failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
//                    }
//                });
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                if (response.isSuccessful()) {
//                    String responseData = response.body().string();
//                    Gson gson = new Gson();
//                    JsonObject jsonResponse = gson.fromJson(responseData, JsonObject.class);
//                    JsonArray resultArray = jsonResponse.getAsJsonArray("result");
//
//                    for (JsonElement productElement : resultArray) {
//                        JsonObject productObject = productElement.getAsJsonObject();
//                        Product product = new Product();
//                        // Parse and set other fields
//                        product.setTitle(productObject.get("title").getAsString());
//                        product.setThumbnail(productObject.get("thumbnail").getAsString());
//                        product.setUrl(productObject.get("url").getAsString());
//                        product.setCurrentPrice(productObject.get("price").getAsJsonObject().get("current_price").getAsDouble());
//                        productList.add(product);
//                    }
//
//                    // Sort productList by price
//                    Collections.sort(productList, new Comparator<Product>() {
//                        @Override
//                        public int compare(Product product1, Product product2) {
//                            return Double.compare(product1.getCurrentPrice(), product2.getCurrentPrice());
//                        }
//                    });
//
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            productAdapter.notifyDataSetChanged();
//                        }
//                    });
//                }
//            }
//        });
//    }
//
//    private void fetchEbayProducts(){
//        OkHttpClient client = new OkHttpClient();
//
//        Request request = new Request.Builder()
//                .url("https://ebay-products-search-scraper.p.rapidapi.com/products?query=iphone&page=1&Item_Location=asia")
//                .get()
//                .addHeader("X-RapidAPI-Key", "0f7e397e25mshd4505846673bbbap164a52jsnff7a130a56e8")
//                .addHeader("X-RapidAPI-Host", "ebay-products-search-scraper.p.rapidapi.com")
//                .build();
//
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(@NonNull Call call, @NonNull IOException e) {
//
//            }
//
//            @Override
//            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
//                if (response.isSuccessful()){
//                    String responseData = response.body().string();
//                    Gson gson = new Gson();
//                    JsonObject jsonResponse = gson.fromJson(responseData, JsonObject.class);
//                    JsonArray resultArray = jsonResponse.getAsJsonArray("products");
//
//                    for (JsonElement productElement : resultArray) {
//                        JsonObject productObject = productElement.getAsJsonObject();
//                        EbayModel product = new EbayModel();
//                        // Parse and set other fields
//                        product.setTitle(productObject.get("title").getAsString());
//                        product.setImage(productObject.get("image").getAsString());
//                        product.setProductLink(productObject.get("productLink").getAsString());
//
//                        String price = productObject.get("price").getAsString();
//
//                        if (!price.contains("to")){
//                            String rex = price.replace("$","");
//
//                            product.setPrice(Double.parseDouble(rex));
//                            ebayList.add(product);
//                        }
//
//
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                Toast.makeText(MainActivity.this, productObject.toString(), Toast.LENGTH_SHORT).show();
//
//                            }
//                        });
//
//                    }
//                    // Sort productList by price
//                    Collections.sort(ebayList, new Comparator<EbayModel>() {
//                        @Override
//                        public int compare(EbayModel product1, EbayModel product2) {
//                            return Double.compare(product1.getPrice(), product2.getPrice());
//                        }
//                    });
//
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            ebayAdapter.notifyDataSetChanged();
//                        }
//                    });
//                }
//
//            }
//        });
//    }
}