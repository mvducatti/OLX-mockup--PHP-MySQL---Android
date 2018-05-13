package com.example.marcos.unasp_phpmysql.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.marcos.unasp_phpmysql.Model.Product;
import com.example.marcos.unasp_phpmysql.Model.User;
import com.example.marcos.unasp_phpmysql.R;

import java.util.ArrayList;

/**
 * Created by marcos on 19/03/2018.
 */

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.NewsAdapterViewHolder> {

    private Context context;
    private ArrayList<Product> productArrayList;
    //creating a listener for the interface with the same name as our interface
    private OnItemClickListener xListener;

    //making our own custom onClickListener
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnClickListener(OnItemClickListener listener){
        //used to simulate the onItemClick of a listview
        xListener = listener;
    }

    public ProductAdapter(Context context, ArrayList<Product> productArrayList) {
        this.context = context;
        this.productArrayList = productArrayList;
    }

    @Override
    public NewsAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.news_list, parent, false);
        return new NewsAdapterViewHolder(v);
    }

    @Override
    public void onBindViewHolder(NewsAdapterViewHolder holder, int position) {
        Product currentProduct = productArrayList.get(position);

        String post = currentProduct.getProduct_name();
        String preco = String.valueOf(currentProduct.getProduct_price());
        String origin = currentProduct.getProduct_origin();
        String status = currentProduct.getProduct_status();

            holder.txtNoticia.setText(post);
            holder.txtOrigin.setText(origin);
            holder.txtPreco.setText("R$" + preco + ",00");
            holder.txtStatus.setText(status);

    }

    @Override
    public int getItemCount() {
        return productArrayList.size();
    }

    public class NewsAdapterViewHolder extends RecyclerView.ViewHolder{
        public TextView txtNoticia;
        public TextView txtPreco;
        public TextView txtOrigin;
        public TextView txtStatus;

        public NewsAdapterViewHolder(View itemView) {
            super(itemView);
            txtNoticia = itemView.findViewById(R.id.txtnewsPost);
            txtPreco = itemView.findViewById(R.id.txtPreco);
            txtOrigin = itemView.findViewById(R.id.txtOrigin);
            txtStatus = itemView.findViewById(R.id.txtStatus);

            //creating the option for when we click on something to work
            //-------------------------WARNING--------------------------//
            /*THIS IS A TRICK TO USE onItemClick AS YOU WOULD USE IN A LIST VIEW
            * IT'S BETTER TO USE HERE (ACCORDING TO THE TUTORIAL THAN USING ON THE *-onBindViewHolder-*)*/
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (xListener != null) {
                        int position = getAdapterPosition();
                        //noposition to make sure the position is still valid
                        if (position != RecyclerView.NO_POSITION){
                            //onItemclick is the method that we created on the interface
                            xListener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

}
