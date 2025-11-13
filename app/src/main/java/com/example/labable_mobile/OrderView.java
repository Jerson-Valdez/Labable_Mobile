package com.example.labable_mobile;

import static android.view.View.TEXT_ALIGNMENT_CENTER;

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

import java.util.HashMap;

public class OrderView extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_order_view);

        Bundle extras = getIntent().getExtras();
        HashMap<String, HashMap<String, Object>> account = (HashMap<String, HashMap<String, Object>>) extras.get("account");

        TextView profileHeader = findViewById(R.id.userFullName);
        profileHeader.setText(String.valueOf(account.get("name")));

        Order order = (Order) extras.get("order");

        TextView price = findViewById(R.id.orderViewPrice);
        price.setText("Php " + String.format("%,.2f", order.getTotalPrice()));

        TextView status = findViewById(R.id.orderViewStatus);
        status.setText(order.getStatus());
        status.setTextColor(ContextCompat.getColor(this, R.color.yellow_dark));
        status.setBackground(ContextCompat.getDrawable(this, R.drawable.rounded_yellow));
        status.setTypeface(ResourcesCompat.getFont(this, R.font.poppins_semibold));
        status.setPadding(MainActivity.dpToPx(this, 10), MainActivity.dpToPx(this, 5), MainActivity.dpToPx(this, 10) , MainActivity.dpToPx(this, 5));

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
        notes.setText(order.getNotes());

        Button backBtn = findViewById(R.id.orderViewBackBtn);
        backBtn.setOnClickListener(v -> {
            finish();
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
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
            itemName.setTextColor(ContextCompat.getColor(this, R.color.secondary_font_color));
            itemName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            itemName.setTypeface(ResourcesCompat.getFont(this, R.font.poppins_regular));

            TextView itemQuantity = new TextView(this);
            itemQuantity.setLayoutParams(itemParams);
            itemQuantity.setText("×" + orderItem.getQuantity());
            itemQuantity.setTextAlignment(TEXT_ALIGNMENT_CENTER);
            itemQuantity.setTextColor(ContextCompat.getColor(this, R.color.secondary_font_color));
            itemQuantity.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            itemQuantity.setTypeface(ResourcesCompat.getFont(this, R.font.poppins_regular));

            layout.addView(itemName);
            layout.addView(itemQuantity);

            container.addView(layout);
        }
    }
}
