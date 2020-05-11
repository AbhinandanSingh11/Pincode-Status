package com.abhinandan.findallindiapincode.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.abhinandan.findallindiapincode.Model.Pincode;
import com.abhinandan.findallindiapincode.R;


import java.util.ArrayList;

public class AdapterRecyclerViewLocation extends RecyclerView.Adapter<AdapterRecyclerViewLocation.ViewHolder> {
    private ArrayList<Pincode> pincodes = new ArrayList<>();
    private Context context;

    public AdapterRecyclerViewLocation(ArrayList<Pincode> pincodes, Context context) {
        this.pincodes = pincodes;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_location,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.block.setText(pincodes.get(position).getBlock());
        holder.state.setText(pincodes.get(position).getState());
        holder.pincode.setText(pincodes.get(position).getPincode());
    }

    @Override
    public int getItemCount() {
        return pincodes.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder{
        private TextView block,state,pincode;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            block = itemView.findViewById(R.id.blockLocation);
            state = itemView.findViewById(R.id.stateLocation);
            pincode = itemView.findViewById(R.id.pincodeLocation);
        }
    }
}
