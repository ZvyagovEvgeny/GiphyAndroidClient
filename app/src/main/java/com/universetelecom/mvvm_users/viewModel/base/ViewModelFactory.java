package com.universetelecom.mvvm_users.viewModel.base;


public interface ViewModelFactory<T extends ViewModel> {

    T create();

}
