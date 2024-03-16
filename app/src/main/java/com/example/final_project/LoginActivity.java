package com.example.final_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.final_project.data.SharedPreferencesDataSource;
import com.example.final_project.databinding.ActivityLoginBinding;
import com.example.final_project.models.Company;
import com.example.final_project.models.User;
import com.example.final_project.utils.FireStoreResults;
import com.example.final_project.utils.UIHelpers;
import com.example.final_project.viewmodel.LoginViewModel;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding activityLoginBinding;
    private LoginViewModel loginViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityLoginBinding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(activityLoginBinding.getRoot());

        initViewModel();
        initUI();
    }

    private void initViewModel(){
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        loginViewModel.getEmail().observe(this, email -> {
            activityLoginBinding.emailEditText.setText(email);
        });

        loginViewModel.getPassword().observe(this, password -> {
            activityLoginBinding.passwordEditText.setText(password);
        });
    }

    private void initUI(){
        activityLoginBinding.registerButton.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
        activityLoginBinding.loginButton.setOnClickListener(v ->{
            String email = activityLoginBinding.emailEditText.getText().toString();
            String password = activityLoginBinding.passwordEditText.getText().toString();
            if(email.isEmpty() || password.isEmpty()){
                UIHelpers.showSnackBar(activityLoginBinding.getRoot(), "Please fill all fields");
                return;
            }
            if(!email.matches(UIHelpers.emailRegex)){
                UIHelpers.showSnackBar(activityLoginBinding.getRoot(), "Invalid email");
                return;
            }
            if(password.length() < 6){
                UIHelpers.showSnackBar(activityLoginBinding.getRoot(), "Password must be at least 6 characters");
                return;
            }
            runOnUiThread(
                    new Runnable() {
                        @Override
                        public void run() {
                            activityLoginBinding.progressIndicator.setVisibility(View.VISIBLE);
                            activityLoginBinding.loginLayout.setVisibility(View.GONE);
                        }
                    }
            );
            loginViewModel.loginUser(email, password, new FireStoreResults() {
                @Override
                public void onResult(boolean result) {

                }

                @Override
                public void onLoginResult(boolean result, User user, Company company) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            activityLoginBinding.progressIndicator.setVisibility(View.GONE);
                            activityLoginBinding.loginLayout.setVisibility(View.VISIBLE);
                        }
                    });
                    if(!result){
                        UIHelpers.showSnackBar(activityLoginBinding.getRoot(), "Invalid email or password");
                    }
                    else{
                        if(user != null){
                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }
                        else if(company != null){
                            Intent intent = new Intent(LoginActivity.this, CompanyHomeActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }
                    }
                }
            });
        });
    }
}