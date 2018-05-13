package com.example.marcos.unasp_phpmysql;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.marcos.unasp_phpmysql.SharedPreferences.SharedPrefManager;

public class UserProfile extends AppCompatActivity {

    TextView profileName;
    TextView edtEmail;
    TextView edtPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        profileName = findViewById(R.id.edtNameProfile);
        profileName.setText(SharedPrefManager.getInstance(this).getUser().getUsername());

        edtEmail = findViewById(R.id.edtEmail);
        edtEmail.setText(SharedPrefManager.getInstance(this).getUser().getEmail());

        edtPass = findViewById(R.id.edtPass);
        edtPass.setText("Senha: " + SharedPrefManager.getInstance(this).getUser().getPassword());
    }
}
