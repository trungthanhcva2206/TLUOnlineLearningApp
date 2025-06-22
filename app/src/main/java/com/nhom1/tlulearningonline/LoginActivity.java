package com.nhom1.tlulearningonline;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText etEmail, etPassword;
    private Button btnLogin;
    private TextView tvForgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);
        tvForgotPassword = findViewById(R.id.tv_forgot_password);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptLogin();
            }
        });

        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LoginActivity.this, "Chức năng quên mật khẩu đang được phát triển!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void attemptLogin() {
        etEmail.setError(null);
        etPassword.setError(null);

        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Mật khẩu không được để trống.");
            focusView = etPassword;
            cancel = true;
        } else if (!isPasswordValid(password)) {
            etPassword.setError("Mật khẩu quá ngắn hoặc không hợp lệ.");
            focusView = etPassword;
            cancel = true;
        }

        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Email không được để trống.");
            focusView = etEmail;
            cancel = true;
        } else if (!isEmailValid(email)) {
            etEmail.setError("Email không hợp lệ.");
            focusView = etEmail;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            showProgress(true);
            if (email.equals("student@tlu.edu.vn") && password.equals("password123")) {
                showProgress(false);
                Toast.makeText(LoginActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class); // Replace MainActivity with your actual homepage activity
                startActivity(intent);
                finish();
            } else if (email.equals("lecturer@tlu.edu.vn") && password.equals("password123")) {
                showProgress(false);
                Toast.makeText(LoginActivity.this, "Đăng nhập giảng viên thành công!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
            else {
                showProgress(false);
                etPassword.setError("Email hoặc mật khẩu không đúng.");
                focusView = etEmail;
                focusView.requestFocus();
            }
        }
    }

    private boolean isEmailValid(String email) {
        return email.contains("@") && (email.endsWith("@e.tlu.edu") || email.endsWith("@tlu.edu")); // Based on doc
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    private void showProgress(boolean show) {
        if (show) {
            Toast.makeText(this, "Đang đăng nhập...", Toast.LENGTH_SHORT).show();
        }
    }
}