package com.example.labable_mobile;

import static android.view.View.TEXT_ALIGNMENT_CENTER;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class OrderView extends AppCompatActivity {
    private AccountManager accountManager;
    private Account account;
    private Order order;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_order_view);

        Bundle extras = getIntent().getExtras();
        accountManager = (AccountManager) extras.get("accountManager");
        account = accountManager.getLoggedInAccount();

        order = (Order) extras.get("order");

        TextView price = findViewById(R.id.orderViewPrice);
        price.setText("Php " + String.format("%,.2f", order.getTotalPrice()));

        TextView status = findViewById(R.id.orderViewStatus);
        status.setText(order.getStatus());
        status.setTextColor(ContextCompat.getColor(this, order.getStatus().equalsIgnoreCase("pending") ? R.color.yellow_dark : R.color.red_dark));
        status.setBackground(ContextCompat.getDrawable(this, order.getStatus().equalsIgnoreCase("pending") ? R.drawable.rounded_yellow : R.drawable.rounded_red));

        TextView address = findViewById(R.id.orderViewAddress);
        address.setText(order.getAddress());

        listOrderItems(order);

        TextView transferDate = findViewById(R.id.orderViewTransferDateTime);
        transferDate.setText(order.getTransferDate() + " - " + order.getTransferTime());

        TextView service = findViewById(R.id.orderViewServiceType);
        service.setText(order.getServiceType() + " (" + order.getOrderItems().size() + " item/s)");

        TextView paymentMethod = findViewById(R.id.orderViewPaymentMethod);
        paymentMethod.setText(order.getPaymentMethod());

        TextView claimMode = findViewById(R.id.orderViewClaimMode);
        claimMode.setText(order.getClaimMode());

        TextView transferMode = findViewById(R.id.orderViewTransferMode);
        transferMode.setText(order.getTransferMode());

        TextView notes = findViewById(R.id.orderViewNotes);
        if (order.getNotes() != null && !order.getNotes().isEmpty()) {
            notes.setText(order.getNotes());
        } else {
            notes.setText("No additional notes.");
        }

        LinearLayout buttonContainer = findViewById(R.id.bottom_bar);

        Button cancelBtn = findViewById(R.id.orderViewCancelBtn);

        if (order.getStatus().equalsIgnoreCase("canceled")) {
            buttonContainer.removeView(cancelBtn);
        } else {
            cancelBtn.setOnClickListener(v -> {
            if (!order.getStatus().equalsIgnoreCase("pending")) {
                new AlertDialog.Builder(this)
                        .setTitle("Failed to Cancel Order")
                        .setMessage("This order is already being processed.")
                        .setNegativeButton("Close", null)
                        .show();
                return;
            }
            new AlertDialog.Builder(this)
                    .setTitle("Cancel Order")
                    .setMessage("You are about to cancel this order. Are you sure you want to proceed?")
                    .setNegativeButton("Cancel", null)
                    .setPositiveButton("Confirm", (dialog, which) -> {
                        order.setStatus("Canceled");
                        status.setTextColor(ContextCompat.getColor(this, order.getStatus().equalsIgnoreCase("pending") ? R.color.yellow_dark : R.color.red_dark));
                        status.setBackground(ContextCompat.getDrawable(this, order.getStatus().equalsIgnoreCase("pending") ? R.drawable.rounded_yellow : R.drawable.rounded_red));
                        accountManager.updateAccountOrder(order);
                        status.setText(order.getStatus());

                        new AlertDialog.Builder(this)
                                .setTitle("Order Canceled")
                                .setMessage("This order has been canceled.")
                                .setNegativeButton("Close", null)
                                .show();
                    })
                    .show();
        });
        }

        Button backBtn = findViewById(R.id.orderViewBackBtn);
        backBtn.setOnClickListener(v -> {
            finishWithResult();
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void finishWithResult() {
        Intent res = new Intent();
        res.putExtra("order", order);
        res.putExtra("accountManager", accountManager);
        setResult(-1, res);
        finish();
    }

    @Override
    public void onBackPressed() {
        finishWithResult();
    }

    private void listOrderItems(Order order) {
        LinearLayout container = findViewById(R.id.orderViewOrderItemContainer);

        for (OrderItem orderItem: order.getOrderItems()) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.CENTER_HORIZONTAL;
            params.setMargins(0, MainActivity.dpToPx(this, 10), 0, 0);

            LinearLayout layout = new LinearLayout(this);
            layout.setLayoutParams(params);

            LinearLayout.LayoutParams itemParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
            itemParams.weight = 1;

            TextView itemName = new TextView(this);
            itemName.setLayoutParams(itemParams);
            itemName.setText(orderItem.getName());
            itemName.setTextAlignment(TEXT_ALIGNMENT_CENTER);
            itemName.setTextColor(ContextCompat.getColor(this, R.color.black));
            itemName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            itemName.setTypeface(ResourcesCompat.getFont(this, R.font.poppins_regular));

            TextView itemQuantity = new TextView(this);
            itemQuantity.setLayoutParams(itemParams);
            itemQuantity.setText("×" + orderItem.getQuantity());
            itemQuantity.setTextAlignment(TEXT_ALIGNMENT_CENTER);
            itemQuantity.setTextColor(ContextCompat.getColor(this, R.color.black));
            itemQuantity.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            itemQuantity.setTypeface(ResourcesCompat.getFont(this, R.font.poppins_regular));

            layout.addView(itemName);
            layout.addView(itemQuantity);

            container.addView(layout);
        }
    }
}
