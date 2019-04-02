package com.inc.soft.devers.ventasonlineapp.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.inc.soft.devers.ventasonlineapp.R;

public class ProductDetailsActivity extends AppCompatActivity {

    TextView productName, productPrice, productDescription;
    ImageView productPicture;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        this.setTitle("Detalle de producto");
        productName = findViewById(R.id.product_name);
        productPrice = findViewById(R.id.product_price);
        productDescription = findViewById(R.id.product_description);
        productPicture = findViewById(R.id.product_picture);    
        Intent i = getIntent();

        productName.setText(i.getStringExtra("name"));
        productPrice.setText(i.getStringExtra("price"));
        productDescription.setText(i.getStringExtra("description"));
//        productPicture.setImageURI(Uri.parse(i.getStringExtra("productPicture")));

        Glide.with(this).load(Uri.parse(i.getStringExtra("productPicture"))).into(productPicture);
    }
}

