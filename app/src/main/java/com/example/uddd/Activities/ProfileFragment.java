package com.example.uddd.Activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.uddd.API.RetrofitClient;
import com.example.uddd.Models.User;
import com.example.uddd.R;

import java.math.RoundingMode;
import java.text.DecimalFormat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {
    private static final int PICK_IMAGE_REQUEST_CODE = 1;
    private static final int REQUEST_PERMISSION_CODE = 2;
    private ImageButton changeAvatarButton, changeNameButton,changeDobButton;
    private EditText nameBar,dobBar,emailBar;
    private ImageView avatar;
    private User user;
    private TextView totalComment, reliability;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.profile_page, container, false);

        user = MainActivity.getUser();

        avatar = view.findViewById(R.id.imv_avatar);
        totalComment = view.findViewById(R.id.tv_comment);
        reliability  = view.findViewById(R.id.tv_reliability);

        changeAvatarButton = view.findViewById(R.id.btn_change_avatar);
        changeNameButton = view.findViewById(R.id.btn_change_name);
        changeDobButton = view.findViewById(R.id.btn_change_dob);

        nameBar = view.findViewById(R.id.name_bar);
        dobBar = view.findViewById(R.id.dob_bar);
        emailBar = view.findViewById(R.id.email_bar);

        nameBar.setText(user.getName());
        dobBar.setText(user.getDob());
        emailBar.setText(user.getEmail());
        totalComment.setText(String.valueOf(user.getTotalComment()));
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.HALF_UP);
        String roundedValue = df.format(user.getReliability()*100);
        reliability.setText(roundedValue+"%");
        Uri imageUri = Uri.parse(user.getAvatar());
        Glide.with(this).load(imageUri).into(avatar);

        changeNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameBar.setEnabled(true);
            }
        });
        nameBar.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)
                {
                    // Hide the keyboard
                    InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(getActivity().getWindow().getDecorView().getWindowToken(), 0);

                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Confirmation")
                            .setMessage("Save changes?")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    nameBar.setEnabled(false);
                                    dialog.dismiss();

                                    //Update to database
                                    Call<Void> call = RetrofitClient.getInstance().getAPI().editName(user.getUserID(),nameBar.getText().toString());
                                    call.enqueue(new Callback<Void>() {
                                        @Override
                                        public void onResponse(Call<Void> call, Response<Void> response) {
                                            Toast.makeText(getContext(),"Update successful.",Toast.LENGTH_LONG).show();

                                            //Update local
                                            MainActivity.UpdateUser();
                                        }

                                        @Override
                                        public void onFailure(Call<Void> call, Throwable t) {
                                            Toast.makeText(getContext(),t.toString(),Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                    return true;
                }
                return false;
            }
        });
        changeDobButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext());
                datePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month++;
                        dobBar.setText(dayOfMonth+"/"+month+"/"+year);

                        //Update to database
                        Call<Void> call = RetrofitClient.getInstance().getAPI().editDob(user.getUserID(),dobBar.getText().toString());
                        call.enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                Toast.makeText(getContext(),"Update successful.",Toast.LENGTH_LONG).show();
                                //Update local
                                MainActivity.UpdateUser();
                            }
                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Toast.makeText(getContext(),"Update Fail",Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });
                datePickerDialog.show();
            }
        });
        changeAvatarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageFileDialog();
            }
        });

        return view;
    }
    public void openImageFileDialog()
    {
        if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION_CODE);
        } else {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            startActivityForResult(intent, PICK_IMAGE_REQUEST_CODE);
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            if (imageUri != null) {
                confirmChangeAvatarDialog(imageUri);
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openImageFileDialog();
            } else {
                // Permission denied
            }
        }
    }

    public void confirmChangeAvatarDialog(Uri imageUri)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Confirmation")
                .setMessage("Change avatar with this image?")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Glide.with(getContext()).load(imageUri).into(avatar);

                        //Update to database
                        Call<Void> call = RetrofitClient.getInstance().getAPI().editAvatar(user.getUserID(),Uri.decode(imageUri.toString()));
                        call.enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                Toast.makeText(getContext(),"Update successful.",Toast.LENGTH_LONG).show();

                                //Update local
                                MainActivity.UpdateUser();
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Toast.makeText(getContext(),"Update fail.",Toast.LENGTH_LONG).show();
                            }
                        });

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
        ;
        AlertDialog dialog = builder.create();
        dialog.show();

    }

}