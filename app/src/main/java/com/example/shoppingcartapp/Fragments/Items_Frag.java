// Items_Frag.java
package com.example.shoppingcartapp.Fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoppingcartapp.Data.Items;
import com.example.shoppingcartapp.Data.ItemsAdapter;
import com.example.shoppingcartapp.Models.ItemsModel;
import com.example.shoppingcartapp.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Items_Frag extends Fragment {

    private RecyclerView recyclerView;
    private ItemsAdapter adapter;
    private ArrayList<ItemsModel> itemsList;
    private TextView connectedUser;
    private Button viewCartButton;
    private Map<String, Integer> cart;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_items_, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.items_Frag);
        connectedUser = view.findViewById(R.id.connectedUser);
        viewCartButton = view.findViewById(R.id.viewCartButton);
        cart = new HashMap<>();

        setupUserDisplay();
        setupRecyclerView();
        setupCartButton();
    }

    private void setupUserDisplay() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            connectedUser.setText("Welcome, " + mAuth.getCurrentUser().getEmail());
        } else {
            connectedUser.setText("Welcome, Guest");
        }
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        itemsList = generateItemsList();

        if (itemsList.isEmpty()) {
            Toast.makeText(getContext(), "No items available", Toast.LENGTH_SHORT).show();
        } else {
            adapter = new ItemsAdapter(itemsList, this::addItemToCart, this::removeItemFromCart);
            recyclerView.setAdapter(adapter);
        }
    }

    private void setupCartButton() {
        viewCartButton.setOnClickListener(v -> {
            StringBuilder cartSummary = new StringBuilder("Your Cart:\n\n");
            int totalItems = 0;

            for (Map.Entry<String, Integer> entry : cart.entrySet()) {
                cartSummary.append(entry.getKey()).append(" x ").append(entry.getValue()).append("\n");
                totalItems += entry.getValue();
            }

            cartSummary.append("\nTotal Items: ").append(totalItems);

            AlertDialog.Builder cartDialog = new AlertDialog.Builder(getContext());
            cartDialog.setTitle("Shopping Cart");
            cartDialog.setMessage(cartSummary.toString());
            cartDialog.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
            cartDialog.show();
        });
    }

    private void addItemToCart(int position) {
        ItemsModel selectedItem = itemsList.get(position);

        // Create and show the dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_item, null);
        builder.setView(dialogView);

        EditText productNameInput = dialogView.findViewById(R.id.productNameInput);
        EditText productQuantityInput = dialogView.findViewById(R.id.productQuantityInput);
        Button addProductButton = dialogView.findViewById(R.id.addProductButton);

        // Pre-fill product name and disable editing
        productNameInput.setText(selectedItem.getName());
        productNameInput.setEnabled(false);

        AlertDialog dialog = builder.create();

        addProductButton.setOnClickListener(v -> {
            String quantityText = productQuantityInput.getText().toString().trim();

            if (quantityText.isEmpty()) {
                Toast.makeText(getContext(), "Please enter a quantity", Toast.LENGTH_SHORT).show();
                return;
            }

            int quantity;
            try {
                quantity = Integer.parseInt(quantityText);
            } catch (NumberFormatException e) {
                Toast.makeText(getContext(), "Invalid quantity", Toast.LENGTH_SHORT).show();
                return;
            }

            // Add item to the cart
            cart.put(selectedItem.getName(), cart.getOrDefault(selectedItem.getName(), 0) + quantity);
            Toast.makeText(getContext(), "Added " + quantity + " x " + selectedItem.getName() + " to cart", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

        dialog.show();
    }

    private void removeItemFromCart(int position) {
        String itemName = itemsList.get(position).getName();
        if (cart.containsKey(itemName) && cart.get(itemName) > 0) {
            cart.put(itemName, cart.get(itemName) - 1);
            if (cart.get(itemName) == 0) {
                cart.remove(itemName);
            }
            Toast.makeText(getContext(), itemName + " removed from cart", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), itemName + " is not in the cart", Toast.LENGTH_SHORT).show();
        }
    }

    private ArrayList<ItemsModel> generateItemsList() {
        ArrayList<ItemsModel> items = new ArrayList<>();

        if (Items.item_name != null && Items.images != null && Items.itemID != null) {
            for (int i = 0; i < Items.item_name.length; i++) {
                items.add(new ItemsModel(
                        Items.item_name[i],
                        Items.images[i],
                        Items.itemID[i]
                ));
            }
        }

        return items;
    }
}
