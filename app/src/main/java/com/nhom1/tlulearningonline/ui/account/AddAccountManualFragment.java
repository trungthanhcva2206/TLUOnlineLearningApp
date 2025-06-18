package com.nhom1.tlulearningonline.ui.account;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.button.MaterialButton;
import com.nhom1.tlulearningonline.R;

import java.util.Arrays;
import java.util.List;

public class AddAccountManualFragment extends Fragment {

    public AddAccountManualFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_account_manual, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        AutoCompleteTextView roleDropdown = view.findViewById(R.id.roleDropdown);
        List<String> items = Arrays.asList("SV - Sinh viên", "GV - Giảng viên", "Admin - Quản trị viên");



        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                items
        );

        roleDropdown.setAdapter(adapter);

        AutoCompleteTextView classDropdown = view.findViewById(R.id.classDropdown);
        List<String> classItems = Arrays.asList("CNTT - Công nghệ thông tin", "KT - Kỹ thuật", "QL - Quản lý", "KHXH - Khoa học xã hội");
        ArrayAdapter<String> classAdapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                classItems
        );
        classDropdown.setAdapter(classAdapter);

        MaterialButton addAccountButton = view.findViewById(R.id.btnCreateAccount);
        addAccountButton.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(view);
            navController.navigate(R.id.action_addAccountManualFragment_to_accountSuccessFragment);
            Log.d("MyDebug", "Button clicked");
        });

        MaterialButton cancelButton = view.findViewById(R.id.btnCancel);
        cancelButton.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(view);
            navController.navigate(R.id.action_addAccountManualFragment_to_addAccountMethodFragment);
            Log.d("MyDebug", "Cancel button clicked");
        });

    }
}
