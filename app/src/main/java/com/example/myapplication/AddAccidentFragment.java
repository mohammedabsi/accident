package com.example.myapplication;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import org.w3c.dom.Text;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddAccidentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddAccidentFragment extends Fragment {
    AppCompatButton addAccident;
    TextView scannerBtn ,generate_time;
    EditText idScanner, time, acident_address, carplate;
    ImageView userAvatarIv ;
  //  private SharedViewModel viewModel;
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    String currentemail = firebaseAuth.getCurrentUser().getEmail();

    ProgressBar addAccident_prog ;



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddAccidentFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddAccidentFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddAccidentFragment newInstance(String param1, String param2) {
        AddAccidentFragment fragment = new AddAccidentFragment();
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
        View v = inflater.inflate(R.layout.add_aacident, container, false);
        addAccident = v.findViewById(R.id.addAccident);
        scannerBtn = v.findViewById(R.id.scannerBtn);
        userAvatarIv = v.findViewById(R.id.userAvatarIv);

        idScanner = v.findViewById(R.id.idScanner);
        time = v.findViewById(R.id.time);
        carplate = v.findViewById(R.id.carplate);
        acident_address = v.findViewById(R.id.acident_address);
        generate_time = v.findViewById(R.id.generate_time);
        addAccident_prog = v.findViewById(R.id.addAccident_prog);

        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setTitle("Alert !!!");
        alertDialog.setMessage("I certify and pledge that the statment is all true !!");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                        CreateAccident();

                    }
                });


        //initializing MultiFormatWriter for QR code
        MultiFormatWriter mWriter = new MultiFormatWriter();

        try {
            //BitMatrix class to encode entered text and set Width &amp; Height
            BitMatrix mMatrix = mWriter.encode(currentemail, BarcodeFormat.QR_CODE, 120,120);

            BarcodeEncoder mEncoder = new BarcodeEncoder();
            Bitmap mBitmap = mEncoder.createBitmap(mMatrix);//creating bitmap of code
            userAvatarIv.setImageBitmap(mBitmap);//Setting generated QR code to imageView


        } catch (WriterException e) {
            e.printStackTrace();
        }

        setHasOptionsMenu(false);

        scannerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                QrScannerFragment fragmentB = new QrScannerFragment();

                Bundle args = new Bundle();
                args.putString("add", "1");
                fragmentB .setArguments(args);
                getParentFragmentManager().beginTransaction().replace(R.id.container,fragmentB).addToBackStack(null).commit();


            }
        });

        generate_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date today = new Date();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd , hh:mm:ss a");
                String dateToStr = format.format(today);
                System.out.println(dateToStr);
                time.setText(dateToStr);
            }
        });

        addAccident.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                alertDialog.show();





            }
        });


        return v;
    }




    private void CreateAccident() {
        addAccident_prog.setVisibility(View.VISIBLE);
        String acc_time = time.getText().toString().trim();
        String acc_add = acident_address.getText().toString().trim();
        String car_plt = carplate.getText().toString().trim();
        String second_id = idScanner.getText().toString().trim();

        AccidentDetails accidentDetails = new AccidentDetails(acc_time, acc_add, car_plt, second_id, currentemail);

        if (!acc_time.isEmpty()){
            if (!acc_add.isEmpty()){
                if (!car_plt.isEmpty()){
                    if (!second_id.isEmpty()){

                        firestore.collection("Accident").document(currentemail + "," + second_id).collection(currentemail).document().set(accidentDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()) {


                                    //jump to next fragment
                                    Bundle bundle = new Bundle();
                                    bundle.putString("idkey", idScanner.getText().toString()); // Put anything what you want
                                    bundle.putString("id", "1"); // Put anything what you want
                                    bundle.putString("dialog", "1"); // Put anything what you want

                                    ChatFragment fragment2 = new ChatFragment();
                                    fragment2.setArguments(bundle);
                                    addAccident_prog.setVisibility(View.GONE);

                                    getParentFragmentManager().beginTransaction().replace(R.id.container, fragment2).commit();

                                } else {
                                    Toast.makeText(getActivity(), "Something went wrong , try again later ...", Toast.LENGTH_SHORT).show();
                                    addAccident_prog.setVisibility(View.GONE);

                                }
                            }
                        });


                    }else {
                        idScanner.setError("Fill Empty Field");
                        addAccident_prog.setVisibility(View.GONE);

                    }
                }else {
                    carplate.setError("Fill Empty Field");
                    addAccident_prog.setVisibility(View.GONE);

                }
            }else {
                acident_address.setError("Fill Empty Field");
                addAccident_prog.setVisibility(View.GONE);

            }
        }else {
            time.setError("Fill Empty field");
            addAccident_prog.setVisibility(View.GONE);

        }



    }

    @Override
    public void onStart() {
        super.onStart();
        Bundle args = getArguments();
        if (args != null) {
          idScanner.setText(args.getString("qrscan"));
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        idScanner.getText().clear();
        time.getText().clear();
    }
}