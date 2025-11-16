package com.example.labable_mobile;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.HashMap;

public class OrderSummary extends AppCompatActivity {
    Order order;
    TextView summaryAddress, summaryTransferDateTime, summaryServiceType,
            summaryPaymentMethod, summaryClaimMode, summaryTransferMode,
            summaryNotes, costSummary;
    LinearLayout orderItems;
    Button checkoutbtn, backbtn;
    AccountManager accountManager;
    Account account = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_order_summary);

        accountManager = (AccountManager) getIntent().getExtras().getSerializable("accountManager");
        account = accountManager.getLoggedInAccount();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if (getIntent().getExtras() != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                order = getIntent().getSerializableExtra("order", Order.class);
            } else {
                order = (Order) getIntent().getSerializableExtra("order");
            }
        }

        initialize();

        if (order != null) {
            populateOrderDetails();
        }

        setupButtonListeners();
    }

    private void initialize() {
        summaryAddress = findViewById(R.id.summaryAddress);
        summaryTransferDateTime = findViewById(R.id.summaryTransferDateTime);
        summaryServiceType = findViewById(R.id.summaryServiceType);
        summaryPaymentMethod = findViewById(R.id.summaryPaymentMethod);
        summaryClaimMode = findViewById(R.id.summaryClaimMode);
        summaryTransferMode = findViewById(R.id.summaryTransferMode);
        summaryNotes = findViewById(R.id.summaryNotes);
        costSummary = findViewById(R.id.cost_summary);
        orderItems = findViewById(R.id.orderItems);
        checkoutbtn = findViewById(R.id.checkoutbtn);
        backbtn = findViewById(R.id.backbtn);
    }

    private void populateOrderDetails() {
        summaryAddress.setText(order.getAddress());
        summaryTransferDateTime.setText(order.getTransferDate() + " " + order.getTransferTime());
        summaryServiceType.setText(order.getServiceType());
        summaryPaymentMethod.setText(order.getPaymentMethod());
        summaryClaimMode.setText(order.getClaimMode());
        summaryTransferMode.setText(order.getTransferMode());

        if (order.getNotes() != null && !order.getNotes().isEmpty()) {
            summaryNotes.setText(order.getNotes());
        } else {
            summaryNotes.setText("No additional notes.");
        }

        costSummary.setText("Estimated Cost: Php " + String.format("%,.2f", order.getTotalPrice()));

        orderItems.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(this);
        for (OrderItem item : order.getOrderItems()) {
            View itemView = inflater.inflate(R.layout.item_order_summary_row, orderItems, false);
            TextView itemName = itemView.findViewById(R.id.item_name_summary);
            TextView itemQuantity = itemView.findViewById(R.id.item_quantity_summary);

            itemName.setText(item.getName());
            itemQuantity.setText("×" + item.getQuantity());

            orderItems.addView(itemView);
        }
    }

    private void setupButtonListeners() {
        backbtn.setOnClickListener(v -> {
            finish();
        });

        checkoutbtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, CustomerDashboard.class);
            HashMap<String, Order> orders = account.getOrders();
            orders.put(order.getOrderId(), order);

            account.setOrders(orders);
            intent.putExtra("accountManager", accountManager);

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            
            startActivity(intent);
            finish();
        });
    }
}
