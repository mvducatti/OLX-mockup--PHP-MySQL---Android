package com.example.marcos.unasp_phpmysql.Vendedor;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.marcos.unasp_phpmysql.Adapters.ProductAdapter;
import com.example.marcos.unasp_phpmysql.Model.Product;
import com.example.marcos.unasp_phpmysql.Model.User;
import com.example.marcos.unasp_phpmysql.R;
import com.example.marcos.unasp_phpmysql.SharedPreferences.SharedPrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Estoque extends AppCompatActivity {

    ArrayList<Product> productArrayList;
    private LinearLayoutManager mLayoutManager;
    RecyclerView recyclerView;
    SharedPrefManager sharedPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estoque);

        recyclerView = findViewById(R.id.recylerViewEstoque);

        mLayoutManager = new LinearLayoutManager(getApplicationContext());

        //Invert the view of the list
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(mLayoutManager);

        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        productArrayList = new ArrayList<>();
        loadProducts();
    }

    private void loadProducts() {

        final User user = sharedPrefManager.getInstance(this).getUser();
        int id = user.getId();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, "http://10.2.7.50:8080/android/v1/getMyItems.php?fk_user=" + id + "" , null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //converting the string to json array object
                            JSONArray jsonArray = response.getJSONArray("products");

                            //traversing through all the object
                            for (int i = 0; i < jsonArray.length(); i++) {
                                //getting product object from json array
                                JSONObject jsnews = jsonArray.getJSONObject(i);
                                //adding the product to product list
                                try {
                                    String post = jsnews.getString("product_name");
                                    int preco = jsnews.getInt("product_price");
                                    String origin = jsnews.getString("product_origin");
                                    String status = jsnews.getString("product_status");
                                    int id = jsnews.getInt("product_id");
                                    productArrayList.add(new Product(id, post, preco, origin, status));
                                } catch (JSONException e) {
                                    Toast.makeText(getApplicationContext(), "quase: " + e, Toast.LENGTH_LONG).show();
                                }
                            }

                            //creating adapter object and setting it to recyclerview
                            ProductAdapter adapter = new ProductAdapter(Estoque.this, productArrayList);
                            recyclerView.setAdapter(adapter);

                        } catch (JSONException e) {
                            Toast.makeText(getApplicationContext(), "Parou aqui: " + e, Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Não existe nada a venda ainda", Toast.LENGTH_LONG).show();
                    }
                });

        //adding our stringrequest to queue
        Volley.newRequestQueue(this).add(request);
    }
}