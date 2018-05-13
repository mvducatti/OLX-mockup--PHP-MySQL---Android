package com.example.marcos.unasp_phpmysql.Vendedor;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.marcos.unasp_phpmysql.Model.User;
import com.example.marcos.unasp_phpmysql.PHP.Constants;
import com.example.marcos.unasp_phpmysql.PHP.RequestHandler;
import com.example.marcos.unasp_phpmysql.R;
import com.example.marcos.unasp_phpmysql.SharedPreferences.SharedPrefManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PostProduct extends AppCompatActivity {

    private Button btnpostar;
    private EditText editTextCDProductTitle, EditTextProductPrice;
    SharedPrefManager sharedPrefManager;
    private RadioButton rbNovo, rbUsado;
    private RadioGroup grupo;
    private Spinner spinner;

    public void alert(String titulo, String txt) {
        AlertDialog alertDialog = new AlertDialog.Builder(PostProduct.this).create();
        alertDialog.setTitle(titulo);
        alertDialog.setMessage(txt);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_product);

        btnpostar = findViewById(R.id.btn_postar);

        editTextCDProductTitle = findViewById(R.id.EditTextProductTitle);
        EditTextProductPrice = findViewById(R.id.EditTextProductPrice);
        rbNovo = findViewById(R.id.radioButtonNovo);
        rbUsado = findViewById(R.id.radioButtonUsado);
        grupo = findViewById(R.id.radioGroup);
        spinner = findViewById(R.id.spinner);
        editTextCDProductTitle.requestFocus();

        grupo.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                try {
                    if (checkedId == rbNovo.getId()) {
                        rbUsado.setEnabled(true);
                    }
                    if (checkedId == rbUsado.getId()) {
                        rbNovo.setEnabled(true);
                    }
                }
                catch (Exception e){
                    alert("Erro", e.getMessage());
                }
            }
        });



    }

    public void salvarNoticia(View view) {

        final User user = sharedPrefManager.getInstance(this).getUser();
        final String post = editTextCDProductTitle.getText().toString();
        final String price = EditTextProductPrice.getText().toString();
        final String radiovalue = ((RadioButton)findViewById(grupo.getCheckedRadioButtonId())).getText().toString();
        final String origin = spinner.getSelectedItem().toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST,
                    Constants.URL_REGISTER_NEWS,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {

                                JSONObject jsonObject = new JSONObject(response);
                                if (!jsonObject.getBoolean("error")){

                                Toast.makeText(getApplicationContext(), jsonObject.getString("message"),
                                        Toast.LENGTH_LONG).show();

                                    editTextCDProductTitle.setText("");
                                    editTextCDProductTitle.requestFocus();
                                    EditTextProductPrice.setText("");
                                    spinner.setSelection(0);


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
                        params.put("product_name", post);
                        params.put("product_price", price);
                        params.put("product_origin", origin);
                        params.put("product_status", radiovalue);
                        params.put("seller_fk", String.valueOf(user.getId()));
                        return params;
                }
            };

            RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

    }
}
