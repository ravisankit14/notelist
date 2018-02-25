package com.note.views;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.note.MainActivity;
import com.note.NoteApp;
import com.note.R;
import com.note.database.db.DaoSession2;
import com.note.database.db.Note;
import com.squareup.picasso.Picasso;


import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.app.Activity.RESULT_OK;
import static android.os.Environment.getExternalStoragePublicDirectory;

public class AddNoteFragment extends Fragment implements View.OnClickListener{
    private static final String TAG = "AddNoteFragment";
    static final int REQUEST_IMAGE_CAPTURE = 1;

    TextView title;
    TextView desc;
    Button mSubmit,submitUpdate;
    ImageView addImage;

    String sTitle;
    String sText;
    Note note;

    String mCurrentPhotoPath;

    public AddNoteFragment() {
        // Required empty public constructor
    }

    public void getEditData(long id){
        note =  getAppDaoSession().getNoteDao().loadByRowId(id);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_note, container, false);
        initialize(view);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Picasso.with(getContext()).load(R.mipmap.upload).into(addImage);

        if(note != null){
            updateValues();
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getContext(),
                        "com.note.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            addImage.setImageBitmap(imageBitmap);
        }
    }

    private void updateValues() {
        title.setText(note.getTitle());
        desc.setText(note.getText());
        if (note.getImage_path() != null){
            Glide.with(getContext()).load(note.getImage_path()).into(addImage);
        }

        submitUpdate.setVisibility(View.VISIBLE);
        mSubmit.setVisibility(View.GONE);
    }

    public boolean addData(){

        sTitle = title.getText().toString();
        sText = desc.getText().toString();
        Log.e(TAG, sTitle +" "+ sText +" "+mCurrentPhotoPath);
        if(sTitle.length() > 0 && sText.length() > 0){
            Note note = new Note(sTitle,
                    sText,getDate(),mCurrentPhotoPath);

            getAppDaoSession().getNoteDao().insert(note);
            return true;
        }
        return false;
    }

    public boolean addDataEdit(long id){

        sTitle = title.getText().toString();
        sText = desc.getText().toString();
        Log.e(TAG, sTitle +" "+ sText +" "+mCurrentPhotoPath);
        if(sTitle.length() > 0 && sText.length() > 0){
            Note note = new Note(id, sTitle,
                    sText,getDate(),mCurrentPhotoPath);

            getAppDaoSession().getNoteDao().update(note);
            return true;
        }
        return false;
    }

    public String getDate(){

        String timeStamp = new SimpleDateFormat("EEE, d MMM yyyy HH:mm").format(new Date());
        return timeStamp;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.submit:
                if(addData()){
                    Intent intent  = new Intent(getContext(), MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }else{
                    Toast.makeText(getContext(),"Title and Description cannot be empty",Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.addImage:
                dispatchTakePictureIntent();
                break;

            case R.id.submitUpdate:
                if(addDataEdit(note.getId())){
                    Intent intent  = new Intent(getContext(), MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }else{
                    Toast.makeText(getContext(),"Title and Description cannot be empty",Toast.LENGTH_SHORT).show();
                }
        }
    }

    private void initialize(View view){
        title = (TextView) view.findViewById(R.id.addTitle);
        desc = (TextView) view.findViewById(R.id.addDesc);
        mSubmit = (Button) view.findViewById(R.id.submit);
        submitUpdate = (Button) view.findViewById(R.id.submitUpdate);
        addImage = (ImageView) view.findViewById(R.id.addImage);
        addImage.setOnClickListener(this);
        mSubmit.setOnClickListener(this);
        submitUpdate.setOnClickListener(this);
    }

    private DaoSession2 getAppDaoSession() {
        return ((NoteApp)getActivity().getApplication()).getDaoSession2();
    }
}
