package com.nhom1.tlulearningonline.ui.account;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.button.MaterialButton;
import com.nhom1.tlulearningonline.R;
import com.nhom1.tlulearningonline.model.Account;

public class AccountDetailFragment extends Fragment {
    private EditText etMsv, etFullName, etEmail, etPhone, etRole;
    private Button btnUpdate;
    private Account account;

    public AccountDetailFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_account_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etMsv = view.findViewById(R.id.etMsv);
        etFullName = view.findViewById(R.id.etFullName);
        etEmail = view.findViewById(R.id.etEmail);
        etPhone = view.findViewById(R.id.etPhone);
        etRole = view.findViewById(R.id.roleDropdown);
        btnUpdate = view.findViewById(R.id.btnUpdate);

        // Nhận dữ liệu
        Account account = (Account) getArguments().getSerializable("account");
        if (account != null) {
            etMsv.setText(account.getMsv());
            etFullName.setText(account.getFullName());
            etEmail.setText(account.getEmail());
            etPhone.setText(account.getPhoneNumber());
            etRole.setText(account.getRole());
        }

        btnUpdate.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "Đã cập nhật!", Toast.LENGTH_SHORT).show();
            NavController navController = Navigation.findNavController(view);
            navController.navigate(R.id.action_accountDetailFragment_to_listAccountFragment);
            Log.d("MyDebug", "Button clicked");

        });

        MaterialButton btnBack = view.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(view);
            navController.navigate(R.id.action_accountDetailFragment_to_listAccountFragment);
            Log.d("MyDebug", "Button clicked");
        });

    }


    private void showAccountInfo(Account acc) {
        etMsv.setText(acc.getMsv());
        etFullName.setText(acc.getFullName());
        etEmail.setText(acc.getEmail());
        etPhone.setText(acc.getPhoneNumber());
        etRole.setText(acc.getRole());
    }
}
