package com.hungphamcom.mellowcourse.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.Timestamp;
import com.hungphamcom.mellowcourse.MainScreen;
import com.hungphamcom.mellowcourse.R;
import com.hungphamcom.mellowcourse.add_new_item;
import com.hungphamcom.mellowcourse.model.Item;
import com.squareup.picasso.Picasso;

import java.util.List;

public class shopItemRecyclerAdapter  extends RecyclerView.Adapter<shopItemRecyclerAdapter.ViewHolder> {
    private Context context;
    private List<Item> itemList;
    private RecyclerViewClickListener listener;
    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private LayoutInflater inflater;

    public shopItemRecyclerAdapter(Context context,List<Item> itemList,RecyclerViewClickListener listener){
        this.context=context;
        this.itemList =  itemList;
        this.listener=listener;
    }

    public shopItemRecyclerAdapter(List<Item> itemList,RecyclerViewClickListener listener){
        this.itemList=itemList;
        this.listener=listener;
    }


    @NonNull
    @Override
    public shopItemRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context)
                .inflate(R.layout.item_row_shop_fragment,parent,false);
        return new ViewHolder(view,context);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull shopItemRecyclerAdapter.ViewHolder holder, int position) {

        Item item=itemList.get(position);
        String imageUrl;

        holder.title.setText(item.getName());
        holder.seller_name.setText(item.getUsername());
        holder.price.setText("$"+item.getPrice());
        holder.pplReview.setText(" ("+item.getReview()+") ");
        holder.pplBuy.setText(" ("+item.getPurchase()+") ");
        imageUrl=item.getImageUrl();

        switch (item.getReview()){
            case 0:
                holder.star1.setBackgroundResource(R.drawable.ic_emptystar_review);
                holder.star2.setBackgroundResource(R.drawable.ic_emptystar_review);
                holder.star3.setBackgroundResource(R.drawable.ic_emptystar_review);
                holder.star4.setBackgroundResource(R.drawable.ic_emptystar_review);
                holder.star5.setBackgroundResource(R.drawable.ic_emptystar_review);
                break;
            case 1:
                holder.star1.setBackgroundResource(R.drawable.ic_fullstar_review);
                holder.star2.setBackgroundResource(R.drawable.ic_emptystar_review);
                holder.star3.setBackgroundResource(R.drawable.ic_emptystar_review);
                holder.star4.setBackgroundResource(R.drawable.ic_emptystar_review);
                holder.star5.setBackgroundResource(R.drawable.ic_emptystar_review);
                break;
            case 2:
                holder.star1.setBackgroundResource(R.drawable.ic_fullstar_review);
                holder.star2.setBackgroundResource(R.drawable.ic_fullstar_review);
                holder.star3.setBackgroundResource(R.drawable.ic_emptystar_review);
                holder.star4.setBackgroundResource(R.drawable.ic_emptystar_review);
                holder.star5.setBackgroundResource(R.drawable.ic_emptystar_review);
                break;
            case 3:
                holder.star1.setBackgroundResource(R.drawable.ic_fullstar_review);
                holder.star2.setBackgroundResource(R.drawable.ic_fullstar_review);
                holder.star3.setBackgroundResource(R.drawable.ic_fullstar_review);
                holder.star4.setBackgroundResource(R.drawable.ic_emptystar_review);
                holder.star5.setBackgroundResource(R.drawable.ic_emptystar_review);
                break;
            case 4:
                holder.star1.setBackgroundResource(R.drawable.ic_fullstar_review);
                holder.star2.setBackgroundResource(R.drawable.ic_fullstar_review);
                holder.star3.setBackgroundResource(R.drawable.ic_fullstar_review);
                holder.star4.setBackgroundResource(R.drawable.ic_fullstar_review);
                holder.star5.setBackgroundResource(R.drawable.ic_emptystar_review);
                break;
            case 5:
                holder.star1.setBackgroundResource(R.drawable.ic_fullstar_review);
                holder.star2.setBackgroundResource(R.drawable.ic_fullstar_review);
                holder.star3.setBackgroundResource(R.drawable.ic_fullstar_review);
                holder.star4.setBackgroundResource(R.drawable.ic_fullstar_review);
                holder.star5.setBackgroundResource(R.drawable.ic_fullstar_review);
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + item.getReview());
        }

        Picasso.get().load(imageUrl)
                .placeholder(R.drawable.ic_launcher_foreground)
                .fit()
                .into(holder.image);




    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{
        public TextView title,seller_name,price,pplReview,pplBuy;
        public ImageView image,star1,star2,star3,star4,star5;
        ConstraintLayout item;
        String userId;
        String username;
        public ViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);
            context=ctx;
            title=itemView.findViewById(R.id.item_title_shop_fragment);
            image=itemView.findViewById(R.id.item_image_shop_fragment);
            seller_name=itemView.findViewById(R.id.sellerName_shop_fragment);
            price=itemView.findViewById(R.id.item_price_shop_fragment);
            pplReview=itemView.findViewById(R.id.numberOfPeopleRated_shop_fragment);
            pplBuy=itemView.findViewById(R.id.numberOfPeopleBuy_shop_fragment);
            star1=itemView.findViewById(R.id.star1_review_shop_fragment);
            star2=itemView.findViewById(R.id.star2_review_shop_fragment);
            star3=itemView.findViewById(R.id.star3_review_shop_fragment);
            star4=itemView.findViewById(R.id.star4_review_shop_fragment);
            star5=itemView.findViewById(R.id.star5_review_shop_fragment);
            item=itemView.findViewById(R.id.item_item_detail);
            item.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            listener.onClick(view,getAdapterPosition());
        }
    }

    public interface RecyclerViewClickListener{
        void onClick(View v,int position);
    }
}
