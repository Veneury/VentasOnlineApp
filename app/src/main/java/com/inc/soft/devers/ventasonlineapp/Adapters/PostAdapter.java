package com.inc.soft.devers.ventasonlineapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
//import com.inc.soft.devers.ventasonlineapp.Activities.PostDetailActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.inc.soft.devers.ventasonlineapp.Activities.ProductDetailsActivity;
import com.inc.soft.devers.ventasonlineapp.Models.Post;

import com.inc.soft.devers.ventasonlineapp.R;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyViewHolder> {

    Context mContext;
    List<Post> mData ;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;


    public PostAdapter(Context mContext, List<Post> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(mContext).inflate(R.layout.row_post_item,parent,false);
        return new MyViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tvTitle.setText(mData.get(position).getTitle());
        holder.tvPrice.setText(mData.get(position).getPrice());
        Glide.with(mContext).load(mData.get(position).getPicture()).into(holder.imgPost);
        Glide.with(mContext).load(mData.get(position).getUserPhoto()).into(holder.imgPostProfile);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }



    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle,tvPrice;
        ImageView imgPost;
        ImageView imgPostProfile;
        ToggleButton buttonFavorite;

        ScaleAnimation scaleAnimation;
        BounceInterpolator bounceInterpolator;

        public MyViewHolder(View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.row_post_title);
            tvPrice = itemView.findViewById(R.id.row_post_price);
            imgPost = itemView.findViewById(R.id.row_post_img);
            imgPostProfile = itemView.findViewById(R.id.row_post_profile_img);
            buttonFavorite = itemView.findViewById(R.id.button_favorite);

            scaleAnimation = new ScaleAnimation(0.7f, 1.0f, 0.7f, 1.0f, Animation.RELATIVE_TO_SELF, 0.7f, Animation.RELATIVE_TO_SELF, 0.7f);
            scaleAnimation.setDuration(500);
            bounceInterpolator = new BounceInterpolator();
            scaleAnimation.setInterpolator(bounceInterpolator);

            buttonFavorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){

                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    //animation

                    compoundButton.startAnimation(scaleAnimation);
                    Post post = mData.get(getAdapterPosition());

                     if(buttonFavorite.isChecked()){
                        Toast.makeText(mContext, "Añadido a la lista de favoritos.", Toast.LENGTH_SHORT).show();
                    }

                    else if(!buttonFavorite.isChecked()){
                        Toast.makeText(mContext, "Eliminado de la lista de favoritos.", Toast.LENGTH_SHORT).show();
                    }
                }

            });

           itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    onItemClicked(mData.get(position));
                }

               public void onItemClicked(Post post) {
                   Intent intent = new Intent(mContext, ProductDetailsActivity.class);
                   String productName = post.getTitle();
                   String productPrice = post.getPrice();
                   String productDescription = post.getDescription();
                   String productPicture = post.getPicture();

                   intent.putExtra("name", productName);
                   intent.putExtra("price", productPrice);
                   intent.putExtra("description", productDescription);
                   intent.putExtra("productPicture", productPicture);
                   mContext.startActivity(intent);
               }
            });

        }


    }
}