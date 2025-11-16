package com.example.labable_mobile;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class CreateOrder extends AppCompatActivity {
    EditText address, additionalNotes;
    Spinner washableItems;

    ImageButton addToCartButton;
    Button reviewOrder, back;
    LinearLayout orderListContainer;
    RadioGroup serviceGroup, transferGroup, claimingGroup, paymentGroup;
    TextView transferDate, transferTime, cost ;
    List<WashableItem> washableItemsList = new ArrayList<>();
    double superbService, goodService, budgetService, studentPackService, dryCleaningService, superbThickService;
    LayoutInflater inflater;
    String meridian = "am";

    private AccountManager accountManager;
    private Account account = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_order);

        accountManager = (AccountManager) getIntent().getExtras().getSerializable("accountManager");
        account = accountManager.getLoggedInAccount();

        initialize();
        addToCart();
        transferDate();
        transferTime();
        updateEstimatedCost();
        setupReviewOrderButton();
        back();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @SuppressLint("WrongViewCast")
    private void initialize() {
        address = findViewById(R.id.address);
        additionalNotes = findViewById(R.id.additionalNotes);
        washableItems = findViewById(R.id.washableItems);
        addToCartButton = findViewById(R.id.addToCartButton);
        orderListContainer = findViewById(R.id.orderListContainer);
        reviewOrder = findViewById(R.id.reviewOrder);
        back = findViewById(R.id.back);
        serviceGroup = findViewById(R.id.serviceGroup);
        transferGroup = findViewById(R.id.transferGroup);
        claimingGroup = findViewById(R.id.claimingGroup);
        paymentGroup = findViewById(R.id.paymentGroup);
        transferDate = findViewById(R.id.transferDate);
        transferTime = findViewById(R.id.transferTime);
        cost = findViewById(R.id.cost);

        address.setText(account.getAddress());

        // Prices
        superbService = 180.00;
        goodService = 140.00;
        budgetService = 100.00;
        studentPackService = 160.00;
        dryCleaningService = 120.00;
        superbThickService = 200.00;

        inflater = LayoutInflater.from(this);

        // Item List
        washableItemsList.add(new WashableItem("T-Shirts", 5.0));
        washableItemsList.add(new WashableItem("Polo Shirts", 4.0));
        washableItemsList.add(new WashableItem("Blouses", 6.67));
        washableItemsList.add(new WashableItem("Sando", 10.0));
        washableItemsList.add(new WashableItem("Undergarments", 14.28));
        washableItemsList.add(new WashableItem("Shorts", 3.33));
        washableItemsList.add(new WashableItem("Jeans", 1.25));
        washableItemsList.add(new WashableItem("Trousers", 2));
        washableItemsList.add(new WashableItem("Bath Towels", 2));
        washableItemsList.add(new WashableItem("Hoodies", 1.43));
        washableItemsList.add(new WashableItem("Jackets(Leather)", 1.20));
        washableItemsList.add(new WashableItem("Blankets", 0.67));

        // Spinner Adapter
        ArrayAdapter<WashableItem> spinnerAdapter = new ArrayAdapter<>(this,
                R.layout.spinner_item,
                washableItemsList
        );

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        washableItems.setAdapter(spinnerAdapter);
    }

    private void addToCart() {
        addToCartButton.setOnClickListener(v -> {
            WashableItem selectedItem = (WashableItem) washableItems.getSelectedItem();
            addOrderItem(selectedItem);
            update();
        });
    }

    private void addOrderItem(WashableItem item) {
        View noItemMessage = findViewById(R.id.noItemMessage);

        if (noItemMessage.getVisibility() == View.VISIBLE) {
            noItemMessage.setVisibility(View.INVISIBLE);
        }

        int childCount = orderListContainer.getChildCount();

        for (int i = 1; i < childCount; i++) {
            View itemView = orderListContainer.getChildAt(i);
            WashableItem existingItem = (WashableItem) itemView.getTag();
            if (item.getName().equals(existingItem.getName())) {
                EditText quantityText = itemView.findViewById(R.id.quantity);
                int currentQuantity = Integer.parseInt(quantityText.getText().toString());
                currentQuantity+=1;
                quantityText.setText(String.valueOf(currentQuantity));
                update();
                return;
            }
        }

        // Inflate the item layout
        LayoutInflater inflater = LayoutInflater.from(this);
        View itemView = inflater.inflate(R.layout.item_order, orderListContainer, false);

        TextView itemNameText = itemView.findViewById(R.id.item_name);
        ImageButton btnRemove = itemView.findViewById(R.id.btn_delete);
        ImageButton btnAdd = itemView.findViewById(R.id.btn_add);
        ImageButton btnSubtract = itemView.findViewById(R.id.btn_subtract);
        EditText quantity = itemView.findViewById(R.id.quantity);

        itemNameText.setText(item.getName());

        itemView.setTag(item);

        btnAdd.setOnClickListener(v -> {
            int currentQuantity = Integer.parseInt(quantity.getText().toString());
            currentQuantity++;
            quantity.setText(String.valueOf(currentQuantity));
            update();
        });

        btnSubtract.setOnClickListener(v -> {
            int currentQuantity = Integer.parseInt(quantity.getText().toString());
            currentQuantity-=1;
            update();
            if (currentQuantity <= 0) {
                orderListContainer.removeView(itemView);
                if (noItemMessage.getVisibility() == View.INVISIBLE) {
                    noItemMessage.setVisibility(View.VISIBLE);
                }
                update();


            }else {
                quantity.setText(String.valueOf(currentQuantity));
                update();
            }
        });

        btnRemove.setOnClickListener(v -> {
            orderListContainer.removeView(itemView);
            if (noItemMessage.getVisibility() == View.INVISIBLE) {
                noItemMessage.setVisibility(View.VISIBLE);
            }
            update();
        });

        orderListContainer.addView(itemView);
    }

    private void transferDate() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        transferDate.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, selectedYear, selectedMonth, selectedDay) -> {
                String selectedDate = (selectedMonth + 1) + "/" + selectedDay + "/" + selectedYear;
                transferDate.setText(selectedDate);
            }, year, month, day);
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
            datePickerDialog.show();
        });
    }

    private void transferTime(){
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);

        transferTime.setOnClickListener(v -> {
            TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view, selectedHour, selectedMinute) -> {
                if (selectedHour > 12) {
                    selectedHour -= 12;
                    meridian = "pm";
                } else if (selectedHour == 12) {
                    meridian = "pm";
                } else if (selectedHour == 0) {
                    selectedHour += 12;
                } else {
                    meridian = "am";
                }
                String formattedTime = String.format(Locale.getDefault(), "%02d:%02d", selectedHour, selectedMinute);
                transferTime.setText(formattedTime+" "+meridian);
            }, hour, minute, false);
            timePickerDialog.show();

        });
    }

    private void setupReviewOrderButton() {
        reviewOrder.setOnClickListener(v -> {
            int count = orderListContainer.getChildCount();
            if (address.getText().toString().trim().isEmpty()) {
                Toast.makeText(this, "Please enter your address", Toast.LENGTH_SHORT).show();
                return;   
            } else if (count == 0) {
                Toast.makeText(this, "Please add items to your order", Toast.LENGTH_SHORT).show();
                return;
            } else if (serviceGroup.getCheckedRadioButtonId() == -1) {
                Toast.makeText(this, "Please select a service type", Toast.LENGTH_SHORT).show();
                return;
            } else if (transferGroup.getCheckedRadioButtonId() == -1) {
                Toast.makeText(this, "Please select a Mode of Transfer", Toast.LENGTH_SHORT).show();
                return;
            } else if (transferDate.getText().toString().equalsIgnoreCase("mm/dd/yyyy")) {
                Toast.makeText(this, "Please select a Transfer Date", Toast.LENGTH_SHORT).show();
                return;
            } else if (transferTime.getText().toString().equalsIgnoreCase("00:00am/pm")) {
                Toast.makeText(this, "Please select a Transfer Time", Toast.LENGTH_SHORT).show();
                return;
            } else if (claimingGroup.getCheckedRadioButtonId() == -1) {
                Toast.makeText(this, "Please select a Mode of Claiming", Toast.LENGTH_SHORT).show();
                return;
            } else if (paymentGroup.getCheckedRadioButtonId() == -1) {
                Toast.makeText(this, "Please select a Payment Method", Toast.LENGTH_SHORT).show();
                return;
            }

            String selectedService = "";
            String selectedTransfer;
            String selectedClaiming;
            String selectedPayment;

            int selectedServiceId = serviceGroup.getCheckedRadioButtonId();
            int numItems = serviceGroup.getChildCount();
            for (int i = 0; i < numItems; i++) {
                RadioButton radioButton = (RadioButton) serviceGroup.getChildAt(i);
                if (radioButton.getId() == selectedServiceId) {
                    selectedService = radioButton.getText().toString();
                    break;
                }
            }

            if(transferGroup.getCheckedRadioButtonId() == R.id.transfer1) {
                selectedTransfer = "Pickup";
            } else {
                selectedTransfer = "Deliver";
            }

            if (claimingGroup.getCheckedRadioButtonId() == R.id.claim1) {
                selectedClaiming = "Pickup";
            } else {
                selectedClaiming = "Drop-Off (Deliver to your front door)";
            }

            if(paymentGroup.getCheckedRadioButtonId() == R.id.cash){
                selectedPayment = "Cash";
            } else {
                selectedPayment = "GCash";
            }

            ArrayList<OrderItem> orderList = collectOrderData();

            final String finalService = selectedService;
            final String finalTransfer = selectedTransfer;
            final String finalClaiming = selectedClaiming;
            final String finalPayment = selectedPayment;

            try {
                double totalPrice = calculateTotalPrice();
                try {
                    Intent intent = new Intent(this, OrderSummary.class);
                    Order newOrder = new Order("ORD-"+ (account.getOrders().size() + 1), address.getText().toString(), orderList, finalService, finalTransfer, transferDate.getText().toString(), transferTime.getText().toString(), finalClaiming, finalPayment, additionalNotes.getText().toString(), totalPrice);

                    intent.putExtra("accountManager", accountManager);
                    intent.putExtra("order", newOrder);
                    startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(this, "Error calculating price.", Toast.LENGTH_SHORT).show();
                    Log.e("CreateOrder", "Could not calculate price", e);
                }
            } catch (Exception e) {
                Toast.makeText(this, "Error calculating price.", Toast.LENGTH_SHORT).show();
                Log.e("CreateOrder", "Could not calculate price", e);
            }
        });
    }

    private ArrayList<OrderItem> collectOrderData() {
        ArrayList<OrderItem> list = new ArrayList<>();
        int childCount = orderListContainer.getChildCount();

        for (int i = 1; i < childCount; i++) {
            View itemView = orderListContainer.getChildAt(i);

            WashableItem item = (WashableItem) itemView.getTag();
            String itemName = item.getName();

            EditText quantityText = itemView.findViewById(R.id.quantity);
            int quantity = 0;

            try {
                quantity = Integer.parseInt(quantityText.getText().toString());
            } catch (NumberFormatException e) {
                Log.e("CreateOrder", "Invalid quantity for " + itemName);
            }

            if (quantity > 0) {
                list.add(new OrderItem(itemName, quantity));
            }
        }
        return list;
    }

    private void back() {
        back.setOnClickListener(v -> {
            finish();
        });
    }

    private void update(){
        double total = calculateTotalPrice();
        String formattedPrice = String.format("%,.2f", total);
        cost.setText("Estimated Cost: Php " + formattedPrice);
    }

    private void updateEstimatedCost(){
        serviceGroup.setOnCheckedChangeListener((group, checkedId) -> {
            update();
        });
    }

    private double getSelectedServicePrice() {
        int selectedId = serviceGroup.getCheckedRadioButtonId();

        if (selectedId == R.id.superbRadioButton) {
            return superbService;
        } else if (selectedId == R.id.goodRadioButton) {
            return goodService;
        } else if (selectedId == R.id.budgetRadioButton) {
            return budgetService;
        } else if (selectedId == R.id.studentPackRadioButton) {
            return studentPackService;
        } else if (selectedId == R.id.dryCleaningRadioButton) {
            return dryCleaningService;
        } else if (selectedId == R.id.superbThickRadioButton) {
            return superbThickService;
        }

        return 0.0;
    }

    private double calculateTotalPrice() {
        int count = orderListContainer.getChildCount();
        if (count == 1){
            return 0.0;
        }

        double servicePrice = getSelectedServicePrice();
        if (servicePrice == 0.0) {
            return 0.0;
        }

        double totalWeight = 0.0;
        int childCount = orderListContainer.getChildCount();

        if (childCount == 1){
            Toast.makeText(this, "Please add items to your order", Toast.LENGTH_SHORT).show();
            return 0.0;
        }

        for (int i = 1; i < childCount; i++) {
            View itemView = orderListContainer.getChildAt(i);

            WashableItem item = (WashableItem) itemView.getTag();

            EditText quantityText = itemView.findViewById(R.id.quantity);

            int quantity = Integer.parseInt(quantityText.getText().toString());

            if (item.getItemsPerKg() > 0) {
                totalWeight += (double) quantity / item.getItemsPerKg();
            }
        }

        return totalWeight * servicePrice;
    }
}