package com.example.labable_mobile;

import static android.view.View.TEXT_ALIGNMENT_CENTER;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class CustomerDashboard extends AppCompatActivity {
    private HashMap<String, HashMap<String, Object>> account = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_customer_dashboard);

        Bundle extras = getIntent().getExtras();
        account = (HashMap<String, HashMap<String, Object>>) extras.get("account");

        Button createOrderBtn = findViewById(R.id.dashboardCreateOrder);
        createOrderBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, CreateOrder.class);
            intent.putExtra("account", account);

            startOrderActivityLauncher.launch(intent);
        });

        TextView profileHeader = findViewById(R.id.userFullName);
        profileHeader.setText(String.valueOf(account.get("name")));

        loadDashboard();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void loadDashboard() {
        HashMap<String, Object> calculations = getCalculations();

        TextView activeOrders = findViewById(R.id.dashboardActiveOrders);
        activeOrders.setText(String.valueOf(calculations.get("activeOrders")));

        TextView completedOrders = findViewById(R.id.dashboardCompletedOrders);
        completedOrders.setText(String.valueOf(calculations.get("completedOrders")));

        TextView totalSpent = findViewById(R.id.dashboardTotalSpent);
        totalSpent.setText("Php " + String.format("%,.2f", calculations.get("totalSpent")));

        listOrders();
    }

    private HashMap<String, Object> getCalculations(){
        HashMap<String, Object> calculations = new HashMap<>();

        int active = 0;
        int completed = 0;
        double total = 0.00;

        Collection<Object> orders =  (Objects.requireNonNull(account.get("orders"))).values();

        for (Object order: orders)  {
             String[] invalid = {"rejected", "canceled"};

            Order o = (Order) order;
            String status = o.getStatus();

            boolean isInvalid = false;

            for (String bad: invalid) {
                if (bad.equalsIgnoreCase(status.trim())) {
                    isInvalid = true;
                    break;
                }
            }

            if (status.equalsIgnoreCase("completed")) {
                total += ((Order) order).getTotalPrice();
                completed++;
            }

            else if (!isInvalid) {
                active++;
            }
        }

        calculations.put("activeOrders", active);
        calculations.put("completedOrders", completed);
        calculations.put("totalSpent", total);

        return calculations;
    }

    private void listOrders() {
        Collection<Object> orders =  (Objects.requireNonNull(account.get("orders"))).values();
        LinearLayout orderContainer = findViewById(R.id.dashboardOrderContainer);

        orderContainer.removeAllViews();

        if (orders.isEmpty()) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 0, 0, MainActivity.dpToPx(this, 15));
            TextView label = new TextView(this);
            label.setLayoutParams(params);
            label.setTextAlignment(TEXT_ALIGNMENT_CENTER);
            label.setTextColor(ContextCompat.getColor(this, R.color.secondary_font_color));

            label.setText("You have no orders yet.");

            orderContainer.addView(label);
        } else {
            for (Object order: orders) {
                orderContainer.addView(createOrderView((Order) order));
            }
        }
    }

    private View createOrderView(Order order) {
        LinearLayout.LayoutParams orderParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        orderParams.setMargins(0, MainActivity.dpToPx(this, 10), 0, 10);
        LinearLayout orderLayout = new LinearLayout(this);
        orderLayout.setLayoutParams(orderParams);
        orderLayout.setOrientation(LinearLayout.VERTICAL);
        orderLayout.setBackground(ContextCompat.getDrawable(this, R.drawable.custom_shape));
        orderLayout.setPadding(MainActivity.dpToPx(this, 10),MainActivity.dpToPx(this, 10),MainActivity.dpToPx(this, 10),MainActivity.dpToPx(this, 10));

        LinearLayout.LayoutParams iconParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        iconParams.gravity = Gravity.CENTER;

        ImageView icon = new ImageView(this);
        icon.setLayoutParams(iconParams);
        icon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.order_vector1));
        icon.setBackground(ContextCompat.getDrawable(this, R.drawable.gray_circle));
        icon.setPadding(MainActivity.dpToPx(this, 12), MainActivity.dpToPx(this, 12), MainActivity.dpToPx(this, 12), MainActivity.dpToPx(this, 12));

        LinearLayout.LayoutParams idLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        idLayoutParams.setMargins(0, MainActivity.dpToPx(this, 10), 0, 0);

        LinearLayout idLayout = new LinearLayout(this);
        idLayout.setLayoutParams(idLayoutParams);

        LinearLayout.LayoutParams idParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        idParams.setMarginEnd(MainActivity.dpToPx(this, 10));

        TextView id = new TextView(this);
        id.setLayoutParams(idParams);
        id.setText(order.getOrderId());
        id.setTextColor(ContextCompat.getColor(this, R.color.black));
        id.setTypeface(ResourcesCompat.getFont(this, R.font.poppins_bold));

        TextView status = new TextView(this);
        status.setLayoutParams(idParams);
        status.setText(order.getStatus());
        status.setTextColor(ContextCompat.getColor(this, R.color.yellow_dark));
        status.setBackground(ContextCompat.getDrawable(this, R.drawable.rounded_yellow));
        status.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        status.setPadding(MainActivity.dpToPx(this, 10), MainActivity.dpToPx(this, 5), MainActivity.dpToPx(this, 10) , MainActivity.dpToPx(this, 5));
        status.setTypeface(ResourcesCompat.getFont(this, R.font.poppins_regular));

        LinearLayout.LayoutParams serviceParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        TextView service = new TextView(this);
        service.setLayoutParams(serviceParams);
        service.setText(order.getServiceType() + " (" + order.getOrderItems().size() + " item/s)");
        service.setTextColor(ContextCompat.getColor(this, R.color.black));
        service.setTypeface(ResourcesCompat.getFont(this, R.font.poppins_regular));

        LinearLayout.LayoutParams dateParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        TextView date = new TextView(this);
        date.setLayoutParams(dateParams);
        date.setText("Order Date: " + order.getTransferDate());
        date.setTextColor(ContextCompat.getColor(this, R.color.black));
        date.setTypeface(ResourcesCompat.getFont(this, R.font.poppins_regular));

        LinearLayout.LayoutParams actionParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        actionParams.setMargins(0, MainActivity.dpToPx(this, 15), 0, 0);
        actionParams.gravity = Gravity.END;

        LinearLayout action = new LinearLayout(this);
        action.setLayoutParams(actionParams);
        action.setOrientation(LinearLayout.HORIZONTAL);
        action.setGravity(Gravity.CENTER_VERTICAL);

        LinearLayout.LayoutParams priceParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        priceParams.setMarginEnd(MainActivity.dpToPx(this, 10));

        TextView price = new TextView(this);
        price.setLayoutParams(priceParams);
        price.setText("Php " + String.format("%,.2f", order.getTotalPrice()));
        price.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        price.setTextColor(ContextCompat.getColor(this, R.color.black));
        price.setTypeface(ResourcesCompat.getFont(this, R.font.poppins_bold));
        price.setTextAlignment(TEXT_ALIGNMENT_CENTER);
        price.setGravity(Gravity.CENTER_VERTICAL);

        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        Button viewBtn = new Button(this);
        viewBtn.setLayoutParams(buttonParams);
        viewBtn.setText("View Details");
        viewBtn.setBackground(ContextCompat.getDrawable(this, R.drawable.custom_shape));
        viewBtn.setPadding(MainActivity.dpToPx(this, 25), MainActivity.dpToPx(this, 10), MainActivity.dpToPx(this, 25), MainActivity.dpToPx(this, 10));
        viewBtn.setTextColor(ContextCompat.getColor(this, R.color.black));
        viewBtn.setTypeface(ResourcesCompat.getFont(this, R.font.poppins_regular));

        viewBtn.setOnClickListener(v -> {
            viewOrder(order);
        });

        idLayout.addView(id);
        idLayout.addView(status);

        action.addView(price);
        action.addView(viewBtn);

        orderLayout.addView(icon);
        orderLayout.addView(idLayout);
        orderLayout.addView(service);
        orderLayout.addView(date);
        orderLayout.addView(action);

        return orderLayout;
    }

    private void viewOrder(Order order){
        Intent intent = new Intent(this, OrderView.class);
        intent.putExtra("account", account);
        intent.putExtra("order", order);

        startActivity(intent);
    }

    ActivityResultLauncher<Intent> startOrderActivityLauncher = registerForActivityResult(
        new ActivityResultContracts.StartActivityForResult(),
        result -> {
            System.out.println("RETURN FROM CREATE");
            if (result.getResultCode() == Activity.RESULT_OK) {
                Intent data = result.getData();
                if (data != null) {
                    account = (HashMap<String, HashMap<String, Object>>) data.getSerializableExtra("account");
                    loadDashboard();
                }
            }
        });
}