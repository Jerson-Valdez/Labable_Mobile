package com.example.labable_mobile;

import static android.view.View.TEXT_ALIGNMENT_CENTER;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import java.util.Collection;
import java.util.HashMap;

public class CustomerDashboard extends AppCompatActivity {
    private AccountManager accountManager;
    private Account account;

    private boolean backPressedOnce = false;

    private final ActivityResultLauncher<Intent> launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            res -> {
                if (res.getResultCode() == -1 && res.getData() != null) {
                    Object orderObj = res.getData().getSerializableExtra("order");
                    if (orderObj instanceof Order) {
                        Order updatedOrder = (Order) orderObj;

                        if (account != null && account.getOrders() != null) {
                            HashMap<String, Order> orders = account.getOrders();
                            orders.put(updatedOrder.getOrderId(), updatedOrder);
                            account.setOrders(orders);
                        }

                        Object managerObj = res.getData().getSerializableExtra("accountManager");
                        if (managerObj instanceof AccountManager) {
                            accountManager = (AccountManager) managerObj;
                        }

                        loadDashboard();
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_customer_dashboard);

        accountManager = (AccountManager) getIntent().getExtras().getSerializable("accountManager");
        account = accountManager.getLoggedInAccount();

        View header = findViewById(R.id.header);
        new ProfileHeader(this, header, accountManager);

        Button createOrderBtn = findViewById(R.id.dashboardCreateOrder);
        createOrderBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, CreateOrder.class);
            intent.putExtra("accountManager", accountManager);
            launcher.launch(intent);
        });

        TextView profileHeader = findViewById(R.id.userFullName);
        profileHeader.setText(account.getName());

        loadDashboard();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);

        if (intent != null && intent.getExtras() != null) {
            Object managerObj = intent.getExtras().getSerializable("accountManager");
            if (managerObj instanceof AccountManager) {
                accountManager = (AccountManager) managerObj;
                account = accountManager.getLoggedInAccount();

                TextView profileHeader = findViewById(R.id.userFullName);
                profileHeader.setText(account.getName());

                loadDashboard();
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (backPressedOnce) {
            super.onBackPressed();
            return;
        }

        backPressedOnce = true;

        new AlertDialog.Builder(this)
                .setTitle("Exit app")
                .setMessage("The app will close with another back press.")
                .setNegativeButton("Cancel", (dialog, which) -> backPressedOnce = false)
                .setPositiveButton("Okay", (dialog, which) -> super.onBackPressed())
                .show();
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

        Collection<Order> orders =  (account.getOrders().values());

        for (Order order: orders)  {
             String[] invalid = {"rejected", "canceled"};

            String status = order.getStatus();

            boolean isInvalid = false;

            for (String bad: invalid) {
                if (bad.equalsIgnoreCase(status.trim())) {
                    isInvalid = true;
                    break;
                }
            }

            if (status.equalsIgnoreCase("completed")) {
                total += order.getTotalPrice();
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
        Collection<Order> orders = account.getOrders().values();
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
            for (Order order: orders) {
                orderContainer.addView(createOrderView(order));
            }
        }
    }

    private View createOrderView(Order order) {
        LayoutInflater inflater = getLayoutInflater();
        LinearLayout container = findViewById(R.id.dashboardOrderContainer);

        View view = inflater.inflate(R.layout.order_card, container, false);
        view.setTag(order);

        TextView id = view.findViewById(R.id.orderCardId);
        TextView status = view.findViewById(R.id.orderCardStatus);
        TextView date = view.findViewById(R.id.orderCardOrderDate);
        TextView service = view.findViewById(R.id.orderCardService);
        TextView price = view.findViewById(R.id.orderCardPrice);

        id.setText(order.getOrderId());
        status.setText(order.getStatus());

        if (order.getStatus().equalsIgnoreCase("pending")) {
            status.setTextColor(ContextCompat.getColor(this, R.color.yellow_dark));
            status.setBackground(ContextCompat.getDrawable(this, R.drawable.rounded_yellow));
        } else {
            status.setTextColor(ContextCompat.getColor(this, R.color.red_dark));
            status.setBackground(ContextCompat.getDrawable(this, R.drawable.rounded_red));
        }

        date.setText("Order Date: " + order.getTransferDate());
        service.setText(order.getServiceType() + " (" + order.getOrderItems().size() + " item/s)");
        price.setText("Php " + String.format("%,.2f", order.getTotalPrice()));

        view.setOnClickListener(v -> {
            viewOrder(order);
        });

        return view;
    }

    private void viewOrder(Order order){
        Intent intent = new Intent(this, OrderView.class);
        intent.putExtra("accountManager", accountManager);
        intent.putExtra("order", order);
        launcher.launch(intent);
    }

}
