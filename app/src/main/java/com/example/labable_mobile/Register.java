package com.example.labable_mobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import java.util.HashMap;

public class Register extends AppCompatActivity {
    private AccountManager accountManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        accountManager = (AccountManager) getIntent().getExtras().getSerializable("accountManager");

        LinearLayout firstNameContainer = findViewById(R.id.registerFirstNameContainer);
        EditText firstName = findViewById(R.id.registerFirstName);
        LinearLayout lastNameContainer = findViewById(R.id.registerLastNameContainer);
        EditText lastName = findViewById(R.id.registerLastName);
        LinearLayout emailContainer = findViewById(R.id.registerEmailContainer);
        EditText email = findViewById(R.id.registerEmail);
        LinearLayout phoneContainer = findViewById(R.id.registerPhoneContainer);
        EditText phone = findViewById(R.id.registerPhone);
        LinearLayout addressContainer = findViewById(R.id.registerAddressContainer);
        EditText address = findViewById(R.id.registerAddress);
        LinearLayout passwordContainer = findViewById(R.id.registerPasswordContainer);
        EditText password = findViewById(R.id.registerPassword);
        LinearLayout confirmPasswordContainer = findViewById(R.id.registerConfirmPasswordContainer);
        EditText confirmPassword = findViewById(R.id.registerConfirmPassword);
        CheckBox agreement = findViewById(R.id.registerTerms);
        Button registerBtn = findViewById(R.id.registerButton);
        LinearLayout googleBtn = findViewById(R.id.registerGoogleButton);


        firstName.addTextChangedListener(Form.nameListener(firstNameContainer));
        lastName.addTextChangedListener(Form.nameListener(lastNameContainer));
        email.addTextChangedListener(Form.emailListener(false, accountManager, emailContainer));
        phone.addTextChangedListener(Form.phoneListener(phoneContainer));
        address.addTextChangedListener(Form.addressListener(addressContainer));
        password.addTextChangedListener(Form.passwordListener(false, passwordContainer));
        confirmPassword.addTextChangedListener(Form.confirmPasswordListener(confirmPasswordContainer, password));
        agreement.setOnClickListener(v -> {
            if (agreement.isChecked()) {
                agreement.setButtonDrawable(R.drawable.check_vector);
            } else {
                agreement.setButtonDrawable(R.drawable.form_circle_field);
                Form.registerFormValid = false;
            }
        });
        firstNameContainer.setOnClickListener(v -> {
            firstName.requestFocus();
        });
        lastNameContainer.setOnClickListener(v -> {
            lastName.requestFocus();
        });
        emailContainer.setOnClickListener(v -> {
            email.requestFocus();
        });
        phoneContainer.setOnClickListener(v -> {
            phone.requestFocus();
        });
        addressContainer.setOnClickListener(v -> {
            address.requestFocus();
        });
        passwordContainer.setOnClickListener(v -> {
            password.requestFocus();
        });
        confirmPasswordContainer.setOnClickListener(v -> {
            confirmPassword.requestFocus();
        });
        registerBtn.setOnClickListener(v -> {
            if (!Form.registerFormValid || !agreement.isChecked()) {
                if (!agreement.isChecked()) {
                    new AlertDialog.Builder(this)
                            .setTitle("Registration Failed")
                            .setNegativeButton("Close", null)
                            .setMessage("You must agree to the terms of service first!")
                            .show();
                    return;
                }
                new AlertDialog.Builder(this)
                        .setTitle("Registration Failed")
                        .setNegativeButton("Close", null)
                        .setMessage("Fill out every field first!")
                        .show();
                return;
            }
            Account account = new Account(
                    accountManager.generateId(),
                    new HashMap<>(),
                    address.getText().toString(),
                    phone.getText().toString(),
                    password.getText().toString(),
                    email.getText().toString(),
                    firstName.getText().toString() + ' ' + lastName.getText().toString());
            account.setRemember(false);

            accountManager.addAccount(account);

            new AlertDialog.Builder(this)
                    .setTitle("Account Created")
                    .setPositiveButton("Okay", (dialog, which) -> {
                        accountManager.login(account);
                        accountManager.addAccount(account);
                        Intent intent = new Intent(this, CustomerDashboard.class);
                        intent.putExtra("accountManager", accountManager);
                        startActivity(intent);
                        finish();
                    })
                    .setMessage("You have successfully created an account! You will be redirected to the dashboard.")
                    .show();
        });
        googleBtn.setOnClickListener(Form.alternativeAccessListener());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void goToLogin(View view) {
        Intent intent = new Intent(this, Login.class);
        intent.putExtra("accountManager", accountManager);
        startActivity(intent);
    }
}
