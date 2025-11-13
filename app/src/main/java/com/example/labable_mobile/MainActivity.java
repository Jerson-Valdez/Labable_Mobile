package com.example.labable_mobile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private final HashMap<Object, HashMap<String, Object>> accounts = new HashMap<>();
    HashMap<String, Object> loggedInAccount = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void goToDashboard(View view){
        Intent toDashboard = new Intent(this, CustomerDashboard.class);

        loggedInAccount = addAccount("Janver Flores", "janver@gmail.com", "123456", "09876543210", new HashMap<>());

        toDashboard.putExtra("account", loggedInAccount);
        startActivity(toDashboard);
    }


    public HashMap<String, Object> addAccount(String name, String email, String password, String number, HashMap<String, HashMap<String, Object>> orders) {
        HashMap<String, Object> account = new HashMap<>();
        account.put("name", name);
        account.put("email", email);
        account.put("password", password);
        account.put("phoneNumber", number);
        account.put("orders", orders);

        accounts.put("ACC-" + accounts.size() + 1, account);
        return account;
    }

    public static int dpToPx(Context ctx, int px) {
        return (int) ctx.getResources().getDisplayMetrics().density * px;
    }
}