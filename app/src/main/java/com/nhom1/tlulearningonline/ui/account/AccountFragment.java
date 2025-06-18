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

public class AccountFragment extends Fragment {
    public AccountFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_account, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button btnGo = view.findViewById(R.id.btnCreateAccount);
        btnGo.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(view);
            navController.navigate(R.id.action_accountFragment_to_addAccountMethodFragment);
            Log.d("MyDebug", "Button clicked");
        });

        Button btnList = view.findViewById(R.id.btnListAccount);
        btnList.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(view);
            navController.navigate(R.id.action_accountFragment_to_listAccountFragment);
            Log.d("MyDebug", "Button clicked");
        });
    }
}
