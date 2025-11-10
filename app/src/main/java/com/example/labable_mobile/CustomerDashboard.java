package com.example.labable_mobile;

import android.app.Activity;
import android.app.ComponentCaller;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class CustomerDashboard extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_customer_dashboard);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void createOrder(View view){
        Intent intent = new Intent(this, CreateOrder.class);
        startOrderActivityLauncher.launch(intent); // diko ginaya yung kay sir kasi ayaw gumana
    }

    ActivityResultLauncher<Intent> startOrderActivityLauncher = registerForActivityResult(
        new ActivityResultContracts.StartActivityForResult(),
        result -> {
            System.out.println("RETURN FROM CREATE");
            if (result.getResultCode() == Activity.RESULT_OK) {
                Intent data = result.getData();
                if (data != null && data.hasExtra("order")) {
                    Order order;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        order = data.getSerializableExtra("order", Order.class);
                    } else {
                        order = (Order) data.getSerializableExtra("order");
                    }
                    if (order != null) {
                        TextView recentOrderIdView = findViewById(R.id.recent_order_id);
                        TextView serviceItemsView = findViewById(R.id.service_items);
                        TextView orderDatesView = findViewById(R.id.recent_order_dates);
                        TextView orderPriceView = findViewById(R.id.recent_order_price);
                        recentOrderIdView.setText(order.getOrderId());
                        orderDatesView.setText("Ordered: " + order.getTransferDate());
                        String service = order.getServiceType();
                        int itemsCount = 0;
                        for (int i = 0; i < order.getOrderItems().size(); i++) {
                            itemsCount += order.getOrderItems().get(i).getQuantity();
                        }
                        serviceItemsView.setText(service + " • " + itemsCount + " items");
                        orderPriceView.setText("Php "+order.getTotalPrice());
                    }
                }
            }
        });



}