package com.example.myapplication;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.os.FileUtils;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.util.FileUtil;
import com.squareup.picasso.Picasso;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.stream.IntStream;

import javax.xml.namespace.QName;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChatFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatFragment extends Fragment {
    ImageButton mButtonSend;
    ImageView qr_imageButton, add_Accidentdetails2;
    private ListView mListView;
    private ArrayList<ChatMessage> chatMessageArrayList;
    private EditText mEditTextMessage;
    private ImageView mImageView;

    private ChatMessageAdapter mAdapter;
    private TextView user_id2;
    private SharedViewModel viewModel;
    private int counter = 0;
    ArrayList<String> firstList = new ArrayList<String>();
    List<String> secondList = new ArrayList<String>();
    List<String> thirdList = new ArrayList<String>();
    // private UploadApis uploadApis;
    private ProgressDialog progressDialog;


    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private static final int CAPTURE_REQUEST_CODE = 0;
    private static final int SELECT_REQUEST_CODE = 1;

    String path;


    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference qRef = db.collection("Questions");
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Dialog dialog, details_dialog;
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    String currentemail = firebaseAuth.getCurrentUser().getEmail();

    AlertDialog alertDialog , alertDialog2 ;



    ImageView openCam_img;
   // private String imgtrigger = "0";


    public ChatFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChatFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChatFragment newInstance(String param1, String param2) {
        ChatFragment fragment = new ChatFragment();
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
        View v = inflater.inflate(R.layout.fragment_chat, container, false);

        alertDialog  = new AlertDialog.Builder(getActivity()).create();

        mButtonSend = v.findViewById(R.id.send);
        mEditTextMessage = v.findViewById(R.id.emailEdt);
        mListView = v.findViewById(R.id.mListView);
        mImageView = v.findViewById(R.id.iv_image);
        user_id2 = v.findViewById(R.id.user_id2);
        add_Accidentdetails2 = v.findViewById(R.id.add_Accidentdetails2);
        qr_imageButton = v.findViewById(R.id.qr_imageButton);
        firstList.add("a");
        firstList.add("b");
        firstList.add("c");
        firstList.add("d");

        dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.uploadimg_dialog);
        openCam_img = dialog.findViewById(R.id.openCam_img);

         alertDialog2 = new AlertDialog.Builder(getActivity()).create();
        alertDialog2.setTitle(R.string.alert);
        alertDialog2.setMessage("I certify and pledge that the statment is all true !!");
        alertDialog2.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });

















        add_Accidentdetails2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDetailsDialog();
            }
        });

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.upoadimgprog));

        chatMessageArrayList = new ArrayList<ChatMessage>();


        mAdapter = new ChatMessageAdapter(getActivity(), chatMessageArrayList);
        mListView.setAdapter(mAdapter);




        qr_imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                QrScannerFragment fragmentB = new QrScannerFragment();

                Bundle args = new Bundle();
                args.putString("add", "2");
                fragmentB.setArguments(args);
                getParentFragmentManager().beginTransaction().replace(R.id.container, fragmentB).addToBackStack(null).commit();

            }
        });


        // camera open
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{
                    Manifest.permission.CAMERA
            }, 100);
        }


        mButtonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = mEditTextMessage.getText().toString().trim();
                mEditTextMessage.getText().clear();

                if (!user_id2.getText().toString().trim().isEmpty()) {


                mListView.setSelection(mAdapter.getCount() - 1);
                dialog.dismiss();
                if (message.contains("a") || message.contains("b") || message.contains("c") || message.contains("d")) {
                    counter++;


                } else {
                    mimicOtherMessage(getString(R.string.ddd));

                }
                sendMessage(message, counter);
                } else {
                    alertDialog.setTitle(getString(R.string.alert));
                    alertDialog.setMessage(getString(R.string.alert2));
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();

                                }
                            });
                    alertDialog.show();

                }
            }
        });


        return v;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(getActivity()).get(SharedViewModel.class);
        viewModel.getText().observe(getViewLifecycleOwner(), new Observer<CharSequence>() {
            @Override
            public void onChanged(@Nullable CharSequence charSequence) {
                user_id2.setText(charSequence);


            }
        });
    }

    private void sendMessage(String message, int count) {
        ChatMessage chatMessage = new ChatMessage(message, true, false);
        mAdapter.add(chatMessage);
        //respond as Helloworld


        if (count == 1 && (message.trim().equalsIgnoreCase("a") || message.trim().contains("b"))) {
            mimicOtherMessage(getString(R.string.q2));

        }



            if (count == 2 && message.trim().equalsIgnoreCase("a")) {
                counter = 10;
                mimicOtherMessage(getString(R.string.q3));


            }
            if (count == 2 && message.trim().equalsIgnoreCase("b")) {
                counter = 20;
                mimicOtherMessage(getString(R.string.q4));


            }    if (count == 2 && message.trim().equalsIgnoreCase("c")) {
                counter = 30;
                mimicOtherMessage(getString(R.string.q5));


            }

        if (count == 31 && message.trim().equalsIgnoreCase("a")) {
            mimicOtherMessage(getString(R.string.res1));
            mButtonSend.setEnabled(false);
            mEditTextMessage.setEnabled(false);

        }  if (count == 31 && message.trim().equalsIgnoreCase("b")) {
            mimicOtherMessage(getString(R.string.res2));
            mButtonSend.setEnabled(false);
            mEditTextMessage.setEnabled(false);

        }

        if (count == 21 && message.trim().equalsIgnoreCase("a")) {
            mimicOtherMessage(getString(R.string.res3));
            mButtonSend.setEnabled(false);
            mEditTextMessage.setEnabled(false);

        }
        if (count == 21 && message.trim().equalsIgnoreCase("c")) {
            mimicOtherMessage(getString(R.string.res4));
            mButtonSend.setEnabled(false);
            mEditTextMessage.setEnabled(false);

        }
        if (count == 21 && message.trim().equalsIgnoreCase("b")) {
            mimicOtherMessage(getString(R.string.res5));


        }   if (count == 22 && message.trim().equalsIgnoreCase("a")) {
            mimicOtherMessage(getString(R.string.res6));
            mButtonSend.setEnabled(false);
            mEditTextMessage.setEnabled(false);

        }  if (count == 22 && message.trim().equalsIgnoreCase("b")) {
            mimicOtherMessage(getString(R.string.res7));
            mButtonSend.setEnabled(false);
            mEditTextMessage.setEnabled(false);

        }


            if (count == 11 && message.trim().equalsIgnoreCase("b")) {
                mimicOtherMessage(getString(R.string.res8));
                mButtonSend.setEnabled(false);
            mEditTextMessage.setEnabled(false);

            }


            if (count == 11 && message.trim().equalsIgnoreCase("a")) {
                mimicOtherMessage(getString(R.string.q6));


            }
            if (count == 12 && message.trim().equalsIgnoreCase("a")) {
                mimicOtherMessage(getString(R.string.res9));
                mButtonSend.setEnabled(false);
                mEditTextMessage.setEnabled(false);

            }
            if (count == 12 && message.trim().equalsIgnoreCase("b")) {
                mimicOtherMessage(getString(R.string.res10));
                mButtonSend.setEnabled(false);
                mEditTextMessage.setEnabled(false);

            }
            if (count == 12 && message.trim().equalsIgnoreCase("c")) {
                mimicOtherMessage(getString(R.string.res11));
                mButtonSend.setEnabled(false);
                mEditTextMessage.setEnabled(false);

            }
            if (count == 12 && message.trim().equalsIgnoreCase("d")) {
                mimicOtherMessage(getString(R.string.res12));
                mButtonSend.setEnabled(false);
                mEditTextMessage.setEnabled(false);

            }

        }





    private void mimicOtherMessage(String message) {
        ChatMessage chatMessage = new ChatMessage(message, false, false);
        mAdapter.add(chatMessage);
    }

    private void sendMessage() {
        ChatMessage chatMessage = new ChatMessage(null, true, true);
        mAdapter.add(chatMessage);

        mimicOtherMessage();
    }

    private void mimicOtherMessage() {
        ChatMessage chatMessage = new ChatMessage(null, false, true);
        mAdapter.add(chatMessage);
    }

    private void showDetailsDialog() {
        details_dialog = new Dialog(getActivity());
        details_dialog.setContentView(R.layout.adddetails_dialog);

        details_dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        details_dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        details_dialog.setCancelable(false);
        details_dialog.show();

        TextView generate_time_dialog = details_dialog.findViewById(R.id.generate_time_dialog);
        EditText acident_address_dialog = details_dialog.findViewById(R.id.acident_address_dialog);
        EditText carplate_dialog = details_dialog.findViewById(R.id.carplate_dialog);
        EditText time_dialog = details_dialog.findViewById(R.id.time_dialog);
        Button uploadAcc_btn_dialog = details_dialog.findViewById(R.id.uploadAcc_btn_dialog);
        Button cancel_dialog = details_dialog.findViewById(R.id.cancel_dialog);


        generate_time_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date today = new Date();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd , hh:mm:ss a");
                String dateToStr = format.format(today);
                time_dialog.setText(dateToStr);
            }
        });
        uploadAcc_btn_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!acident_address_dialog.getText().toString().trim().isEmpty()) {
                    if (!time_dialog.getText().toString().trim().isEmpty()) {
                        if (!carplate_dialog.getText().toString().trim().isEmpty()) {
                            AccidentDetails accidentDetails = new AccidentDetails(time_dialog.getText().toString().trim(), acident_address_dialog.getText().toString().trim(), carplate_dialog.getText().toString().trim(), user_id2.getText().toString().trim(), currentemail);


                            //Todo : add accident details for second part

                            firestore.collection("Accident").document(user_id2.getText().toString().trim() + "," + currentemail).collection(currentemail).document().set(accidentDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()) {

                                        Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
                                        details_dialog.dismiss();
                                        add_Accidentdetails2.setVisibility(View.GONE);


                                    } else {
                                        Toast.makeText(getActivity(), R.string.errormsg, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });


                        } else {
                            carplate_dialog.setError("Empty field !");
                        }
                    } else {
                        time_dialog.setError("Empty field , generate time");
                    }
                } else {
                    acident_address_dialog.setError("Empty Field");
                }


            }
        });
        cancel_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                details_dialog.dismiss();
            }
        });


    }

    public void showDialog() {



        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.setCancelable(false);
        dialog.show();

        AppCompatButton cancel = dialog.findViewById(R.id.cancel);
        ImageView openCam_img = dialog.findViewById(R.id.openCam_img);
        TextView img_toimgview = dialog.findViewById(R.id.img_toimgview);
        AppCompatButton uploadAcc_btn = dialog.findViewById(R.id.uploadAcc_btn);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (openCam_img.getDrawable() == null) {
                    dialog.dismiss();

                    getParentFragmentManager().beginTransaction().replace(R.id.container, new MainFragment()).commit();
                    Toast.makeText(getActivity(), R.string.startbotchat, Toast.LENGTH_SHORT).show();

                } else {
                    dialog.dismiss();

                }


            }
        });
        uploadAcc_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                ImageUpload(currentemail);
                dialog.dismiss();
                // progressDialog.show();


                final Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Do something after 100ms
                        progressDialog.dismiss();
                        mimicOtherMessage(getString(R.string.q1));

                        ShowResultDialog();
                    }
                }, 10000);


            }
        });

        img_toimgview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Open camera
                if (ContextCompat.checkSelfPermission(getContext(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(intent, 10);
                } else {
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                }
            }
        });


    }

    private void ShowResultDialog() {

        AlertDialog Result_Dialog = new AlertDialog.Builder(getActivity()).create();
        Result_Dialog.setTitle(getString(R.string.accresults));
        final List<ReturnResult>[] arrayList = new List[]{new ArrayList<ReturnResult>()};


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://64.227.67.56/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ReturnResultApiInterface returnResultApiInterface = retrofit.create(ReturnResultApiInterface.class);



        Call<ReturnResult> call = returnResultApiInterface.getResultList();

        call.enqueue(new Callback<ReturnResult>() {
            @Override
            public void onResponse(Call<ReturnResult> call, Response<ReturnResult> response) {
                if (response.isSuccessful()){


                    Log.d("ListTag", "onResponse: "+response.body().getResult());


                  LinkedList <String> list1 = new LinkedList<String>();
                  list1.add(response.body().getResult());
                    Log.d("ListTag", "onResponse: "+list1.getLast());


                    if (!list1.isEmpty()){
                        if (list1.getLast().equalsIgnoreCase("'1'")) {
                            Result_Dialog.setMessage(getString(R.string.thereisacc));


                        } else {
                            Result_Dialog.setMessage(getString(R.string.noaccedent));

                        }
                    }else {
                        Result_Dialog.setMessage(getString(R.string.noresult));
                    }




                    Result_Dialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                    if (!response.body().getResult().isEmpty() && response.body().getResult().equalsIgnoreCase("'1'")) {
                                        dialog.dismiss();
                                        alertDialog2.show();

                                    } else {
                                        dialog.dismiss();
                                        getParentFragmentManager().beginTransaction().replace(R.id.container, new MainFragment()).commit();


                                    }


                                }
                            });


                    Result_Dialog.show();
                }
            }



            @Override
            public void onFailure(Call<ReturnResult> call, Throwable t) {
                Toast.makeText(getActivity(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });




    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == 10 && resultCode == RESULT_OK) {

            Uri uri = data.getData();
            Context context = getActivity();
            path = RealPathUtil.getRealPath(context, uri);

            Bitmap bitmap = BitmapFactory.decodeFile(path);
            openCam_img.setImageBitmap(bitmap);


        }

    }

    private void ImageUpload(String email) {

        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://64.227.67.56/api/").addConverterFactory(GsonConverterFactory.create()).build();


        File file = new File(path);
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);

        MultipartBody.Part body = MultipartBody.Part.createFormData("photo", file.getName(), requestFile);
        RequestBody req_email = RequestBody.create(MediaType.parse("multipart/form-data"), email);
        UploadApis uploadApis = retrofit.create(UploadApis.class);


        Call<AddPhoto> call = uploadApis.uploadImage(body, req_email);

        call.enqueue(new Callback<AddPhoto>() {
            @Override
            public void onResponse(Call<AddPhoto> call, Response<AddPhoto> response) {
                if (response.isSuccessful()) {
                    if (response.body().getCode().toString().equalsIgnoreCase("200")) {
                        Toast.makeText(getActivity(), "Upload success", Toast.LENGTH_SHORT).show();
                        //  progressDialog.dismiss();

                    } else {
                        Toast.makeText(getActivity(), "Upload failed", Toast.LENGTH_SHORT).show();
                        //  progressDialog.dismiss();

                    }
                }
            }

            @Override
            public void onFailure(Call<AddPhoto> call, Throwable t) {
                Toast.makeText(getActivity(), t.getLocalizedMessage().toString(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();

                Log.d("TAG", "failedresponse: " + t.toString());

            }
        });


    }


    @Override
    public void onStart() {
        super.onStart();
        mButtonSend.setEnabled(true);
        mEditTextMessage.setEnabled(true);


        Bundle args = getArguments();


        if (args != null) {
            if (args.getString("id").equalsIgnoreCase("1")) {
                user_id2.setText(args.getString("idkey"));
                add_Accidentdetails2.setVisibility(View.GONE);


            } else if (args.getString("id").equalsIgnoreCase("2")) {
                user_id2.setText(args.getString("qrscan"));
                add_Accidentdetails2.setVisibility(View.VISIBLE);

                Log.d("moham", "onCreateView: " + args.getString("qrscan"));
            }
        }


        if (user_id2.getText().toString().equals("")) {
            qr_imageButton.setVisibility(View.VISIBLE);

        } else {
            qr_imageButton.setVisibility(View.GONE);
       //    imgtrigger = "1";
            showDialog();



        }


    }


}