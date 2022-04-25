package com.example.emongani;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class product_adapter extends FirebaseRecyclerAdapter<ProductHelper,product_adapter.product_viewholder>
{

    public product_adapter(@NonNull FirebaseRecyclerOptions<ProductHelper> options) {
        super(options);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onBindViewHolder(@NonNull product_viewholder holder, int position, @NonNull ProductHelper model) {
        holder.productname.setText(model.getProdName());
        holder.productlocation.setText(model.getProdLocation());
        holder.productcategory.setText(model.getProdCategory());
        holder.productkg.setText(model.getCatKg()+" kg");
        holder.productdescription.setText(model.getProdDesc());
        holder.productsb.setText(model.getStartingBid() + ".00");
        holder.productdate.setText(model.getDate());
        holder.producttime.setText(model.getTime());
        Glide.with(holder.imageView.getContext()).load(model.getProdImg()).into(holder.imageView);
    }

    @NonNull
    @Override
    public product_viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item,parent,false);
        return new product_viewholder(view);
    }

    public class product_viewholder extends RecyclerView.ViewHolder
        {
            ImageView imageView;
            TextView productname, productlocation, productcategory, productkg, productdescription, productsb, productdate, producttime;
            Button sbbtn;

            public product_viewholder(@NonNull View itemView) {
                super(itemView);

                imageView = itemView.findViewById(R.id.prodimg);
                productname = itemView.findViewById(R.id.prodname);
                productlocation = itemView.findViewById(R.id.prodlocation);
                productcategory = itemView.findViewById(R.id.prodcategory);
                productkg = itemView.findViewById(R.id.prodkg);
                productdescription = itemView.findViewById(R.id.proddesc);
                productsb = itemView.findViewById(R.id.prodsb);
                productdate = itemView.findViewById(R.id.proddate);
                producttime = itemView.findViewById(R.id.prodtime);

            }
        }


    }



