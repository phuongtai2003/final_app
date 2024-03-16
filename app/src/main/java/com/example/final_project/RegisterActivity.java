package com.example.final_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.example.final_project.adapter.StringSpinnerAdapter;
import com.example.final_project.databinding.ActivityRegisterBinding;
import com.example.final_project.models.Company;
import com.example.final_project.models.User;
import com.example.final_project.utils.FireStoreResults;
import com.example.final_project.utils.GlobalConstants;
import com.example.final_project.utils.UIHelpers;
import com.example.final_project.viewmodel.RegisterViewModel;

public class RegisterActivity extends AppCompatActivity {
    private RegisterViewModel registerViewModel;
    private ActivityRegisterBinding registerBinding;
    private StringSpinnerAdapter stringSpinnerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerBinding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(registerBinding.getRoot());

        registerViewModel = new ViewModelProvider(this).get(RegisterViewModel.class);
        initUI();
    }

    private void initUI(){
        registerBinding.backButton.setOnClickListener(v -> {
            finish();
        });
        stringSpinnerAdapter = new StringSpinnerAdapter(GlobalConstants.accountTypes, this);
        registerBinding.userTypeSpinner.setAdapter(stringSpinnerAdapter);
        registerBinding.userTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i == 0){
                    registerBinding.nameEditText.setHint("Name");
                }
                else{
                    registerBinding.nameEditText.setHint("Company Name");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        registerBinding.registerButton.setOnClickListener(v -> {
            String email = registerBinding.emailEditText.getText().toString();
            String password = registerBinding.passwordEditText.getText().toString();
            String confirmPassword = registerBinding.confirmPasswordEditText.getText().toString();
            String name = registerBinding.nameEditText.getText().toString();
            String phone = registerBinding.phoneEditText.getText().toString();
            String accountType = registerBinding.userTypeSpinner.getSelectedItem().toString();
            if(email.isEmpty() || password.isEmpty() || name.isEmpty() || phone.isEmpty()){
                UIHelpers.showSnackBar(registerBinding.getRoot(), "Please fill all fields");
                return;
            }
            if(password.length() < 6){
                UIHelpers.showSnackBar(registerBinding.getRoot(), "Password must be at least 6 characters");
                return;
            }
            if(phone.length() < 10){
                UIHelpers.showSnackBar(registerBinding.getRoot(), "Phone number must be at least 10 characters");
                return;
            }
            if(!password.equals(confirmPassword)){
                UIHelpers.showSnackBar(registerBinding.getRoot(), "Passwords do not match");
                return;
            }
            if(!email.matches(UIHelpers.emailRegex)){
                UIHelpers.showSnackBar(registerBinding.getRoot(), "Invalid email");
                return;
            }
            if(!phone.matches(UIHelpers.phoneRegex)){
                UIHelpers.showSnackBar(registerBinding.getRoot(), "Invalid phone number");
                return;
            }

            runOnUiThread(new Runnable() {
                              @Override
                              public void run() {
                                  registerBinding.registerLayout.setVisibility(View.GONE);
                                  registerBinding.progressIndicator.setVisibility(View.VISIBLE);
                              }
                          }
            );
            registerViewModel.register(email, password, name, phone, accountType , new FireStoreResults() {
                @Override
                public void onResult(boolean result) {
                    if(result){
                        runOnUiThread(new Runnable() {
                                          @Override
                                          public void run() {
                                              UIHelpers.showSnackBar(registerBinding.getRoot(), "User registered successfully");
                                              registerBinding.registerLayout.setVisibility(View.VISIBLE);
                                              registerBinding.progressIndicator.setVisibility(View.GONE);
                                          }
                                      }
                        );
                    } else{
                        runOnUiThread(new Runnable() {
                                          @Override
                                          public void run() {
                                              UIHelpers.showSnackBar(registerBinding.getRoot(), "User registration failed");
                                              registerBinding.registerLayout.setVisibility(View.VISIBLE);
                                              registerBinding.progressIndicator.setVisibility(View.GONE);
                                          }
                                      }
                        );
                    }
                }

                @Override
                public void onLoginResult(boolean result, User user, Company company) {

                }
            });
        });
    }
}