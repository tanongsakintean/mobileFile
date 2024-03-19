package com.example.demofile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Notification;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

public class Products extends AppCompatActivity implements AdapterView.OnItemClickListener , SearchView.OnQueryTextListener {
    ListView listView;
    SearchView search;
    String [] items = {
            "Bitcoin","Ethereum","LiteCoin","Dash","Neo","Nano","Bitcoin Cash",
            "Verge","Pipple","Bitcoin diamond","Iconomi","Stellar Lumens"};

    Files files;
    ArrayList<String> product = new ArrayList<String>();
    ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

        try {
            files = new Files(getApplicationContext()) ;
            for (String[] item: files.selectAll()) {
               product.add(""+item[0]+item[1]+item[2]) ;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        search = (SearchView) findViewById(R.id.search);
        search.setOnQueryTextListener(this);

        listView = (ListView) findViewById(R.id.listView);
         adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, product);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.product) {
            Intent product = new Intent(this, Products.class);
            startActivity(product);
        } else if (item.getItemId() == R.id.exit) {
            finishAffinity();
        } else if (item.getItemId() == R.id.home) {

        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        int itemPos = position;
        String itemValue = (String) listView.getItemAtPosition(itemPos);
        Toast.makeText(this,

                "Position: "+itemPos + " Item clicked : "+itemValue,

                Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
       adapter.getFilter().filter(newText);
        return false;
    }
}