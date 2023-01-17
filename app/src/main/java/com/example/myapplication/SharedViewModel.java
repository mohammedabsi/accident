package com.example.myapplication;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {
    private MutableLiveData<CharSequence> text = new MutableLiveData<>();

public void setText(CharSequence input){
    text.postValue(input);
}
public LiveData<CharSequence> getText(){
    return text;
}
}
