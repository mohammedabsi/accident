package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link QrScannerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QrScannerFragment extends Fragment {
    CodeScanner codeScanner;
    CodeScannerView codeScannerView;
    private SharedViewModel viewModel;
    TextView tv;
    String x = null;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public QrScannerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment QrScannerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static QrScannerFragment newInstance(String param1, String param2) {
        QrScannerFragment fragment = new QrScannerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_qr_scanner, container, false);
        codeScannerView =  v.findViewById(R.id.scanner_view);
        tv =  v.findViewById(R.id.resulttv);
        codeScanner = new CodeScanner(getActivity(), codeScannerView);


        codeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull Result result) {
               getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                       viewModel.setText();
                        Bundle args = new Bundle();



                            if (x!= null && x.equalsIgnoreCase("1") ){


                                if (Patterns.EMAIL_ADDRESS.matcher(result.getText().toString().trim()).matches()){
                                    AddAccidentFragment fragmentc = new AddAccidentFragment();
                                    args.putString("qrscan", result.getText());
                                    fragmentc .setArguments(args);
                                    getParentFragmentManager().beginTransaction().replace(R.id.container, fragmentc).commit();



                                }else {
                                    getParentFragmentManager().beginTransaction().replace(R.id.container, new AddAccidentFragment()).commit();
                                    Toast.makeText(getActivity(), "Invalid Id , Scan correct Id provided by the app !", Toast.LENGTH_SHORT).show();
                                }




                            }else
                            if (x!= null && x.equalsIgnoreCase("2")){

                                if (Patterns.EMAIL_ADDRESS.matcher(result.getText().toString().trim()).matches()){

                                    ChatFragment fragmentd = new ChatFragment();
                                    args.putString("qrscan", result.getText());
                                    args.putString("id", "2");
                                    args.putString("dialog", "3");
                                    fragmentd.setArguments(args);
                                    getParentFragmentManager().beginTransaction().replace(R.id.container, fragmentd).commit();


                                }else {
                                    getParentFragmentManager().beginTransaction().replace(R.id.container, new ChatFragment()).commit();
                                    Toast.makeText(getActivity(), "Invalid Id , Scan correct Id provided by the app !", Toast.LENGTH_SHORT).show();
                                }





                            }



                    }
                });
            }
        });

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        requestCamera();
    }

    @Override
    public void onStart() {
        super.onStart();
        Bundle args = getArguments();
        if (args != null) {
           x =   args.getString("add");
        }
    }

    private void requestCamera() {
        codeScanner.startPreview();
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(getActivity()).get(SharedViewModel.class);
        viewModel.getText().observe(getViewLifecycleOwner(), new Observer<CharSequence>() {
            @Override
            public void onChanged(@Nullable CharSequence charSequence) {

                    tv.setText(charSequence);



            }
        });
    }

}