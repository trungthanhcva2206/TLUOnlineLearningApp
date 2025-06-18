package com.nhom1.tlulearningonline.ui.account;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.nhom1.tlulearningonline.R;
import com.nhom1.tlulearningonline.adapter.AccountAdapter;
import com.nhom1.tlulearningonline.model.Account;

import java.util.ArrayList;
import java.util.List;

public class ListAccountFragment extends Fragment {
    private RecyclerView recyclerView;
    private List<Account> accountList;
    private AccountAdapter accountAdapter;

    public ListAccountFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list_account, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewAccounts);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Tạo dữ liệu mẫu
        accountList = new ArrayList<>();
        accountList.add(new Account("001", "Nguyễn Văn A", "SV", "64CNTT.NB"));
        accountList.add(new Account("002", "Trần Thị B", "GV", "Khoa CNTT"));
        accountList.add(new Account("003", "Phạm Văn C", "Admin", "Phòng Đào tạo"));

        accountAdapter = new AccountAdapter(accountList, new AccountAdapter.OnAccountClickListener() {
            @Override
            public void onChiTietClick(Account account) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("account", account);

                NavController navController = Navigation.findNavController(requireView());
                navController.navigate(R.id.action_listAccountFragment_to_accountDetailFragment, bundle);
            }
        });


        recyclerView.setAdapter(accountAdapter);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Thêm các xử lý khác nếu cần

        MaterialButton btnBack = view.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(view);
            navController.navigate(R.id.action_listAccountFragment_to_accountFragment);
            Log.d("MyDebug", "Button clicked");
        });

    }
}
