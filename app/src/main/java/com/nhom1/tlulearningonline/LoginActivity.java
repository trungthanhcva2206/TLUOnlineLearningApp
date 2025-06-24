package com.nhom1.tlulearningonline;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText etUsername, etPassword;
    private Button btnLogin;
    private TextView tvForgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize UI elements
        etUsername = findViewById(R.id.et_username); // ID vẫn là et_email trong layout
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);
        tvForgotPassword = findViewById(R.id.tv_forgot_password);

        btnLogin.setOnClickListener(v -> attemptLogin());

        tvForgotPassword.setOnClickListener(v ->
                Toast.makeText(LoginActivity.this, "Chức năng quên mật khẩu đang được phát triển!", Toast.LENGTH_SHORT).show()
        );
    }

    private void attemptLogin() {
        etUsername.setError(null);
        etPassword.setError(null);

        String username = etUsername.getText().toString().trim();
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

        if (TextUtils.isEmpty(username)) {
            etUsername.setError("Tên đăng nhập không được để trống.");
            focusView = etUsername;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            showProgress(true);
            loginWithAPI(username, password);
        }
    }

    private void loginWithAPI(String username, String password) {
        String url = "http://14.225.207.221:6060/mobile/auth/log-in";

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    showProgress(false);
                    Log.d("LOGIN", "Response: " + response);
                    try {
                        JSONObject json = new JSONObject(response);
                        int code = json.getInt("code");
                        Log.d("LOGIN", "Code: " + code);

                        if (code == 0) {
                            JSONObject result = json.getJSONObject("result");
                            boolean authenticated = result.getBoolean("authenticated");
                            String role = result.optString("role", "");
                            String status = result.optString("status", "");
                            Log.d("LOGIN", "Authenticated: " + authenticated + ", Role: " + role);

                            if (!"ACTIVE".equalsIgnoreCase(status)) {
                                Toast.makeText(this, "Tài khoản của bạn đã bị vô hiệu hóa!", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if (authenticated) {
                                String userId = result.optString("id", "");
                                SessionManager sessionManager = new SessionManager(this);
                                sessionManager.saveLogin(userId);
                                Toast.makeText(this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                                Intent intent;

                                if ("TEACHER".equalsIgnoreCase(role)) {
                                    intent = new Intent(this, HomeGVActivity.class);
                                } else {
                                    intent = new Intent(this, HomeActivity.class);
                                }

                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(this, "Tên đăng nhập hoặc mật khẩu không đúng!", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(this, "Đăng nhập thất bại. Mã lỗi: " + code, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Lỗi khi xử lý phản hồi!", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    showProgress(false);
                    Log.e("LOGIN", "Volley Error: " + error.toString());
                    Toast.makeText(this, "Lỗi kết nối đến server!", Toast.LENGTH_SHORT).show();
                }) {
            @Override
            public byte[] getBody() {
                JSONObject params = new JSONObject();
                try {
                    params.put("username", username);
                    params.put("password", password);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return params.toString().getBytes();
            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
        };

        queue.add(postRequest);
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 7;
    }

    private void showProgress(boolean show) {
        if (show) {
            Toast.makeText(this, "Đang đăng nhập...", Toast.LENGTH_SHORT).show();
        }
    }
}
