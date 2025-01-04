package com.example.shoppingcartapp.Data;

import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shoppingcartapp.Models.ItemsModel;
import com.example.shoppingcartapp.R;

import java.util.ArrayList;
import java.util.List;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.MyViewHolder> implements Filterable {

    private final List<ItemsModel> itemsModels;
    private final List<ItemsModel> itemsModelFull; // Backup list for filtering
    private final OnItemClickListener addClickListener;
    private final OnItemClickListener removeClickListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public ItemsAdapter(List<ItemsModel> itemsModels, OnItemClickListener addClickListener, OnItemClickListener removeClickListener) {
        this.itemsModels = new ArrayList<>(itemsModels);
        this.itemsModelFull = new ArrayList<>(itemsModels); // Backup list
        this.addClickListener = addClickListener;
        this.removeClickListener = removeClickListener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView itemName;
        Button addButton;
        Button removeButton;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            itemName = itemView.findViewById(R.id.item_name);
            addButton = itemView.findViewById(R.id.addProductButton);
            removeButton = itemView.findViewById(R.id.removeProductButton);
        }
    }

    @NonNull
    @Override
    public ItemsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ItemsModel currItem = itemsModels.get(position);
        holder.itemName.setText(currItem.getName());

        // Set the image resource
        if (currItem.getImage() != 0) {
            holder.imageView.setImageResource(currItem.getImage());
        } else {
            holder.imageView.setImageResource(R.drawable.brad); // Default image
        }

        holder.addButton.setOnClickListener(v -> addClickListener.onItemClick(position)); // Callback to open dialog
        holder.removeButton.setOnClickListener(v -> removeClickListener.onItemClick(position));
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<ItemsModel> filteredList = new ArrayList<>();
                if (constraint == null || constraint.length() == 0) {
                    filteredList.addAll(itemsModelFull);
                } else {
                    String filterPattern = constraint.toString().toLowerCase().trim();
                    for (ItemsModel item : itemsModelFull) {
                        if (item.getName().toLowerCase().contains(filterPattern)) {
                            filteredList.add(item);
                        }
                    }
                }

                FilterResults results = new FilterResults();
                results.values = filteredList;
                results.count = filteredList.size();
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                // Safely cast results.values to List<ItemsModel>
                if (results.values instanceof List) {
                    @SuppressWarnings("unchecked")
                    List<ItemsModel> resultList = (List<ItemsModel>) results.values;
                    itemsModels.clear();
                    itemsModels.addAll(resultList);
                    notifyDataSetChanged();
                }
            }
        };
    }

    @Override
    public int getItemCount() {
        return itemsModels.size();
    }
}
