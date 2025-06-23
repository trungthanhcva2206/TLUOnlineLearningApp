package com.nhom1.tlulearningonline;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText etEmail, etPassword;
    private Button btnLogin;
    private TextView tvForgotPassword;

    // A map to store dummy accounts: email -> password
    private Map<String, String> testAccounts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize UI elements
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);
        tvForgotPassword = findViewById(R.id.tv_forgot_password);

        // Setup dummy test accounts
        setupTestAccounts();

        // Set click listener for login button
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptLogin();
            }
        });

        // Set click listener for forgot password text
        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LoginActivity.this, "Chức năng quên mật khẩu đang được phát triển!", Toast.LENGTH_SHORT).show();
                // In a real app, you would navigate to a password reset activity
            }
        });
    }

    /**
     * Sets up a map of dummy test accounts for login validation.
     * Includes both student and lecturer accounts.
     */
    private void setupTestAccounts() {
        testAccounts = new HashMap<>();
        // Student accounts with format: studentID@e.tlu.edu.vn
        testAccounts.put("2251161942@e.tlu.edu.vn", "password123");
        testAccounts.put("2251061708@e.tlu.edu.vn", "password123"); // Example student ID from doc
        testAccounts.put("2251061885@e.tlu.edu.vn", "password123"); // Example student ID from doc
        testAccounts.put("2251061712@e.tlu.edu.vn", "password123"); // Example student ID from doc
        testAccounts.put("sv.test@e.tlu.edu.vn", "svpass"); // Another student test account

        // Lecturer accounts
        testAccounts.put("lecturer@tlu.edu.vn", "password123");
        testAccounts.put("gv.test@tlu.edu.vn", "gvpass"); // Another lecturer test account
    }


    /**
     * Attempts to sign in the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        // Reset errors
        etEmail.setError(null);
        etPassword.setError(null);

        // Store values at the time of the login attempt.
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Mật khẩu không được để trống.");
            focusView = etPassword;
            cancel = true;
        } else if (!isPasswordValid(password)) {
            etPassword.setError("Mật khẩu quá ngắn hoặc không hợp lệ.");
            focusView = etPassword;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Email không được để trống.");
            focusView = etEmail;
            cancel = true;
        } else if (!isValidEmailFormat(email)) { // Use new validation for email format
            etEmail.setError("Email không hợp lệ (ví dụ: student@e.tlu.edu.vn hoặc lecturer@tlu.edu.vn).");
            focusView = etEmail;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Simulate a login attempt using the testAccounts map
            showProgress(true); // You'd implement a loading spinner or similar

            // Check if the entered email and password match any test account
            if (testAccounts.containsKey(email) && testAccounts.get(email).equals(password)) {
                showProgress(false);
                Toast.makeText(LoginActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();

                // Determine user role based on email domain
                if (email.endsWith("@e.tlu.edu.vn")) {
                    Toast.makeText(LoginActivity.this, "Đăng nhập với vai trò Sinh viên", Toast.LENGTH_SHORT).show();
                    // Navigate to MainActivity or a student-specific dashboard
                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish(); // Close login activity
                } else if (email.endsWith("@tlu.edu.vn")) {
                    Toast.makeText(LoginActivity.this, "Đăng nhập với vai trò Giảng viên", Toast.LENGTH_SHORT).show();
                    // Navigate to MainActivity or a lecturer-specific dashboard
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish(); // Close login activity
                } //else {
//                    // Fallback for other valid domains if any, or a general success
//                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                    startActivity(intent);
//                    finish();
//                }

            } else {
                showProgress(false);
                etPassword.setError("Email hoặc mật khẩu không đúng.");
                focusView = etEmail;
                focusView.requestFocus();
            }
        }
    }

    /**
     * Checks if the email format is valid (e.g., contains @ and ends with specific domains).
     * @param email The email string to validate.
     * @return True if the email has a valid format, false otherwise.
     */
    private boolean isValidEmailFormat(String email) {
        // Use Android's Patterns.EMAIL_ADDRESS for basic email format validation
        // And then check for specific domains as per your requirement
        return Patterns.EMAIL_ADDRESS.matcher(email).matches() &&
                (email.endsWith("@e.tlu.edu.vn") || email.endsWith("@tlu.edu.vn"));
    }

    /**
     * Checks if the password meets the minimum length requirement.
     * @param password The password string to validate.
     * @return True if the password length is greater than 4, false otherwise.
     */
    private boolean isPasswordValid(String password) {
        // You can define more robust password complexity rules here
        return password.length() > 4; // Example: password must be greater than 4 characters
    }

    /**
     * Shows or hides a progress indicator (currently a Toast message).
     * @param show True to show progress, false to hide.
     */
    private void showProgress(boolean show) {
        // Implement a progress bar or spinner here to show/hide loading state
        // For now, it's just a placeholder Toast
        if (show) {
            Toast.makeText(this, "Đang đăng nhập...", Toast.LENGTH_SHORT).show();
        }
    }
}
