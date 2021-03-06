package com.example.marcos.unasp_phpmysql.Vendedor;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.marcos.unasp_phpmysql.Adapters.ProductAdapter;
import com.example.marcos.unasp_phpmysql.Model.Product;
import com.example.marcos.unasp_phpmysql.Model.User;
import com.example.marcos.unasp_phpmysql.PHP.Constants;
import com.example.marcos.unasp_phpmysql.PHP.RequestHandler;
import com.example.marcos.unasp_phpmysql.R;
import com.example.marcos.unasp_phpmysql.SharedPreferences.SharedPrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyBiddedItems extends AppCompatActivity implements ProductAdapter.OnItemClickListener {

    public static final String EXTRA_PRODUCT_NAME = "productname";
    public static final String EXTRA_ID = "product_id";
    int productId;
    String productName;

    ArrayList<Product> productArrayList;
    private LinearLayoutManager mLayoutManager;
    RecyclerView recyclerView;
    SharedPrefManager sharedPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bidded_items);

        recyclerView = findViewById(R.id.recyclerViewBiddenItems);

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

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, "http://10.2.7.50:8080/android/v1/myBiddedItems.php?vendedor=" + id + "" , null,
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
                            ProductAdapter adapter = new ProductAdapter(MyBiddedItems.this, productArrayList);
                            recyclerView.setAdapter(adapter);
                            adapter.setOnClickListener(MyBiddedItems.this);

                        } catch (JSONException e) {
                            Toast.makeText(getApplicationContext(), "Parou aqui: " + e, Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Deu ruim: " + error, Toast.LENGTH_LONG).show();
                    }
                });

        //adding our stringrequest to queue
        Volley.newRequestQueue(this).add(request);
    }

    @Override
    public void onItemClick(int position) {

        Product clickedProduct = productArrayList.get(position);

        productName = clickedProduct.getProduct_name();
        productId = clickedProduct.getProductId();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("O que deseja fazer com o produto: " + productName + "")
                .setCancelable(false)
                .setPositiveButton("Vender", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        salvarNoticia();

                    }
                })
                .setNegativeButton("Cancelar reserva", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        cancelarReserva();
                    }
                }).setNeutralButton("Voltar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
                }});

        AlertDialog alert = builder.create();
        alert.show();

    }

    public void salvarNoticia() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.URL_SELL_PRODUCT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            if (!jsonObject.getBoolean("error")){

                                Toast.makeText(getApplicationContext(), jsonObject.getString("message"),
                                        Toast.LENGTH_LONG).show();

                                productArrayList.clear();
                                loadProducts();

                            }else{
                                Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(), "Erro: " + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = null;
                params = new HashMap<>();
                params.put("product_id", String.valueOf(productId));
                return params;
            }
        };

        RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

    }

    public void cancelarReserva() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.URL_CANCEL_RESERVATION,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            if (!jsonObject.getBoolean("error")){

                                Toast.makeText(getApplicationContext(), jsonObject.getString("message"),
                                        Toast.LENGTH_LONG).show();

                                productArrayList.clear();
                                loadProducts();


                            }else{
                                Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(), "Erro: " + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = null;
                params = new HashMap<>();
                params.put("product_id", String.valueOf(productId));
                return params;
            }
        };

        RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

    }

}
