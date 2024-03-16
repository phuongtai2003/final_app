package com.example.final_project.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.final_project.repository.AuthenticationRepository;
import com.example.final_project.utils.FireStoreResults;

public class LoginViewModel extends ViewModel {
    private MutableLiveData<String> email = new MutableLiveData<>();
    private MutableLiveData<String> password = new MutableLiveData<>();
    private AuthenticationRepository authenticationRepository;

    public LoginViewModel() {
        authenticationRepository = AuthenticationRepository.getInstance();
        String email  = authenticationRepository.getEmail();
        String password = authenticationRepository.getPassword();
        if(email != null){
            setEmail(email);
        }
        if(password != null){
            setPassword(password);
        }
    }

    public void loginUser(String email, String password, FireStoreResults results) {
        authenticationRepository.loginUser(email, password, results);
    }

    public MutableLiveData<String> getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email.setValue(email);
    }

    public MutableLiveData<String> getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password.setValue(password);
    }
}
