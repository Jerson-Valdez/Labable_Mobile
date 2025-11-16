package com.example.labable_mobile;

import android.app.AlertDialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Form {
    private static final String PASSWORD_PATTERN = "^(?=.*\\d).{6,}$";
    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,6}$";
    private static final String NAME_PATTERN = "^[A-Za-z\\s]{2,}$";
    private static final String PHONE_PATTERN = "^(09|\\+639)\\d{9}$";
    private static final String ADDRESS_PATTERN = "^.*,.+$";

    static boolean loginFormValid = false;
    static boolean registerFormValid = false;

    private static TextView getErrorView(View child) {
        ViewGroup group = (ViewGroup) child.getParent();
        int childCount = group.getChildCount();

        int elIndex = group.indexOfChild(child);

        if (elIndex >= 0 && elIndex < childCount - 1) return (TextView) group.getChildAt(elIndex + 1);

        return null;
    }

    private static boolean isEmailValid(String email) {
        return email.matches(EMAIL_PATTERN);
    }

    private static boolean isNameValid(String name) {
        return name.matches(NAME_PATTERN);
    }

    private static boolean isPhoneValid(String phone) {
        return phone.matches(PHONE_PATTERN);
    }

    private static boolean isAddressValid(String address) {
        return address.matches(ADDRESS_PATTERN);
    }

    public static TextWatcher nameListener(LinearLayout container) {
        TextView errorView = getErrorView(container);
        assert  errorView != null;

        return new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if (s.isEmpty()) {
                    errorView.setText("");
                    registerFormValid = false;
                } else {
                    boolean valid = Form.isNameValid(s.toString());

                    if (!valid) {
                        errorView.setText("Name must be at least 2 characters and only letters");
                    } else {
                        errorView.setText("");
                        registerFormValid = true;
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        };
    }

    public static TextWatcher emailListener(boolean login, AccountManager accountManager, LinearLayout container) {
        TextView errorView = getErrorView(container);
        assert  errorView != null;

        return new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if (s.isEmpty()) {
                    errorView.setText("");
                    if (login) loginFormValid = false;
                    else registerFormValid = false;
                } else {
                    boolean valid = Form.isEmailValid(s.toString());
                    boolean exists = accountManager.emailExists(s.toString());

                    if (!valid) {
                        errorView.setText("Email is invalid");
                    } else if (!exists && login) {
                        errorView.setText("Unable to find account");
                    } else if (exists && !login) {
                        errorView.setText("Email is already in use");
                    } else {
                        errorView.setText("");

                        if (login) loginFormValid = true;
                        else registerFormValid = true;
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        };
    }

    public static TextWatcher phoneListener(LinearLayout container) {
        TextView errorView = getErrorView(container);
        assert  errorView != null;

        return new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if (s.isEmpty()) {
                    errorView.setText("");
                    registerFormValid = false;
                } else {
                    boolean valid = Form.isPhoneValid(s.toString());

                    if (!valid) {
                        errorView.setText("Number must be 11 digits long");
                    } else {
                        errorView.setText("");
                        registerFormValid = true;
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        };
    }

    public static TextWatcher addressListener(LinearLayout container) {
        TextView errorView = getErrorView(container);
        assert  errorView != null;

        return new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if (s.isEmpty()) {
                    errorView.setText("");
                    registerFormValid = false;
                } else {
                    boolean valid = Form.isAddressValid(s.toString());

                    if (!valid) {
                        errorView.setText("At least include your municipality");
                    } else {
                        errorView.setText("");
                        registerFormValid = true;
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        };
    }

    public static TextWatcher passwordListener(boolean login, LinearLayout container) {
        TextView errorView = getErrorView(container);
        assert  errorView != null;

        return new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if (s.isEmpty()) {
                    errorView.setText("");
                    if (login) loginFormValid = false;
                    else registerFormValid = false;
                } else if (!s.toString().matches(PASSWORD_PATTERN)) {
                    errorView.setText("Password must be at least 6 characters long with a digit");
                } else {
                    errorView.setText("");
                    if (login) loginFormValid = true;
                    else registerFormValid = true;
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        };
    }

    public static TextWatcher confirmPasswordListener( LinearLayout container, EditText password) {
        TextView errorView = getErrorView(container);
        assert  errorView != null;

        return new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if (s.isEmpty()) {
                    errorView.setText("");
                    registerFormValid = false;
                } else if (!s.toString().equals(password.getText().toString())) {
                    errorView.setText("Passwords do not match");
                } else {
                    errorView.setText("");
                    registerFormValid = true;
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        };
    }

    public static View.OnClickListener alternativeAccessListener() {
        return v -> new AlertDialog.Builder(v.getContext())
                .setTitle("Feature not Available")
                .setMessage("Google login is not yet possible! Please sit tight as we work on it.")
                .setNegativeButton("Close", null)
                .show();
    }
}
