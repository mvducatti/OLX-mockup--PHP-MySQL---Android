package com.example.marcos.unasp_phpmysql;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.marcos.unasp_phpmysql.Vendedor.Estoque;
import com.example.marcos.unasp_phpmysql.Comprador.GetAllItems;
import com.example.marcos.unasp_phpmysql.Comprador.MinhasReservas;
import com.example.marcos.unasp_phpmysql.SharedPreferences.SharedPrefManager;
import com.example.marcos.unasp_phpmysql.Vendedor.MyBiddedItems;
import com.example.marcos.unasp_phpmysql.Vendedor.PostProduct;

public class MainMenu extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (!SharedPrefManager.getInstance(this).isLoggedIn()){
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            logout();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_perfil) {
            Intent intent = new Intent(getApplicationContext(), UserProfile.class);
            startActivity(intent);
        } else if (id == R.id.nav_comprar) {
            Intent intent = new Intent(getApplicationContext(), GetAllItems.class);
            startActivity(intent);
        } else if (id == R.id.nav_vender) {
            Intent intent = new Intent(getApplicationContext(), PostProduct.class);
            startActivity(intent);
        } else if (id == R.id.nav_estoque) {
            Intent intent = new Intent(getApplicationContext(), Estoque.class);
            startActivity(intent);
        } else if (id == R.id.nav_pedidos) {
            Intent intent = new Intent(getApplicationContext(), MinhasReservas.class);
            startActivity(intent);
        } else if (id == R.id.nav) {
            Intent intent = new Intent(getApplicationContext(), MyBiddedItems.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_bought) {
            Intent intent = new Intent(getApplicationContext(), MyBoughtItems.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_sold) {
            Intent intent = new Intent(getApplicationContext(), SoldItems.class);
            startActivity(intent);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void logout(){
        SharedPrefManager.getInstance(this).logout();
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}
