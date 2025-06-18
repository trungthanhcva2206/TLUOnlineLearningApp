package com.nhom1.tlulearningonline.ui.account;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.button.MaterialButton;
import com.nhom1.tlulearningonline.R;

public class AddAccountFileFragment extends Fragment {

    private static final int FILE_SELECT_CODE = 1001;
    private TextView tvSelectedFile;

    public AddAccountFileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_account_file, container, false);
        tvSelectedFile = view.findViewById(R.id.tvSelectedFile);

        tvSelectedFile.setOnClickListener(v -> openFileChooser());

        return view;
    }

    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"); // .xlsx
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intent, "Chọn file Excel"), FILE_SELECT_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == FILE_SELECT_CODE) {
            getActivity();
            if (resultCode == Activity.RESULT_OK && data != null) {
                Uri fileUri = data.getData();
                assert fileUri != null;
                String fileName = fileUri.getLastPathSegment(); // Tên đơn giản
                tvSelectedFile.setText(fileName != null ? fileName : "Đã chọn file");
                Toast.makeText(getContext(), "Đã chọn: " + fileName, Toast.LENGTH_SHORT).show();

                // TODO: xử lý fileUri nếu bạn muốn upload hay đọc nội dung Excel
            }
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Thêm các xử lý khác nếu cần
        MaterialButton addAccountButton = view.findViewById(R.id.btnCreateAccount);
        addAccountButton.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(view);
            navController.navigate(R.id.action_AddAccountFileFragment_to_accountSuccessFragment);
            Log.d("MyDebug", "Button clicked");
        });

        MaterialButton cancelButton = view.findViewById(R.id.btnCancel);
        cancelButton.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(view);
            navController.navigate(R.id.action_AddAccountFileFragment_to_addAccountMethodFragment);
            Log.d("MyDebug", "Cancel button clicked");
        });
    }
}
