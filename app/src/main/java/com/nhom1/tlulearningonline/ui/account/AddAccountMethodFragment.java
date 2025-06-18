package com.nhom1.tlulearningonline.ui.account;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.nhom1.tlulearningonline.R;

public class AddAccountMethodFragment extends Fragment {
    public AddAccountMethodFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize any data or state here if needed
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_account_method, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button btnGo = view.findViewById(R.id.btnCreateForm);
        Button btnCreateFile = view.findViewById(R.id.btnCreateFile);
        btnGo.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(view);
            navController.navigate(R.id.action_addAccountMethodFragment_to_addAccountManualFragment);
            Log.d("MyDebug", "Button clicked");
        });

        Button btnBack = view.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(view);
            navController.navigate(R.id.action_addAccountMethodFragment_to_accountFragment);
            Log.d("MyDebug", "Back button clicked");
        });

        btnCreateFile.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(view);
            navController.navigate(R.id.action_addAccountMethodFragment_to_AddAccountFileFragment);
            Log.d("MyDebug", "Create File button clicked");
        });
    }
}
