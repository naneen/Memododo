package com.example.naneen.memododo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import static android.R.id.list;
import static android.app.Activity.RESULT_OK;

public class AddFragment extends Fragment{
    Button addBtn, selectImage;
    EditText wordEdit, meaningEdit;
    ImageView imageView;
    View view;
    public static final int READ_EXTERNAL_STORAGE = 0;
    private static final int GALLERY_INTENT = 2;
    private ProgressDialog mProgressDialog;
    private Firebase mRootRef;
    private Uri mImageUri = null;
    private DatabaseReference mDatabaseRef;
    private StorageReference mStorage;

    public AddFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_add, container, false);
        addBtn = (Button) view.findViewById(R.id.addBtn);
        wordEdit   = (EditText) view.findViewById(R.id.wordEdit);
        meaningEdit   = (EditText) view.findViewById(R.id.meaningEdit);
        selectImage = (Button) view.findViewById(R.id.selectImg);
        imageView = (ImageView) view.findViewById(R.id.imageView);

//      initialise firebase
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        mRootRef = new Firebase("https://memododo-d46c0.firebaseio.com/").child("Word_list").push();
        mStorage = FirebaseStorage.getInstance().getReferenceFromUrl("gs://memododo-d46c0.appspot.com");

        //initialise progress bar
        mProgressDialog = new ProgressDialog(getActivity());

        //select image from storage
        selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check the permission
                if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getActivity().getApplication(), "Call for permission", Toast.LENGTH_SHORT).show();
                    Log.d("Debug", Build.VERSION.SDK_INT + " >= " + Build.VERSION_CODES.N);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        Log.d("Debug", "Build.VERSION.SDK_INT >= Build.VERSION_CODES.N");
                        requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTERNAL_STORAGE);
                    }
                }
                else {
                    Log.d("Debug", "else --> call gal()");
                    Toast.makeText(getActivity().getApplicationContext(), "else --> call gal()", Toast.LENGTH_SHORT).show();
                    callGallery();
                }
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String word = wordEdit.getText().toString().trim();
                final String meaning = meaningEdit.getText().toString().trim();
                if (word.isEmpty()) {
                    Toast.makeText(getActivity().getApplicationContext(), "Word is empty!", Toast.LENGTH_SHORT).show();
                    return;
                }
                mRootRef.child("Word").setValue(word);
                mRootRef.child("Meaning").setValue(meaning);
                Toast.makeText(getActivity().getApplicationContext(), "Added word: " + word, Toast.LENGTH_LONG).show();
                InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                mRootRef = new Firebase("https://memododo-d46c0.firebaseio.com/").child("Word_list").push();

                wordEdit.getText().clear();
                meaningEdit.getText().clear();
                imageView.setImageResource(android.R.drawable.ic_menu_gallery);

//                TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabs);
//                int tabCount= tabLayout.getTabCount();
//                Log.d("Debug", "tabcount : "+tabCount);
////                TabLayout.Tab tab = tabLayout.getTabAt(2);
////                tab.select();
            }
        });
        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d("Debug", (grantResults.length > 0)+"");
        Log.d("Debug", (grantResults[0] == PackageManager.PERMISSION_GRANTED)+"");
        switch (requestCode){
            case READ_EXTERNAL_STORAGE:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    callGallery();
                }
                return;
        }
        Toast.makeText(getActivity().getApplicationContext(), "...", Toast.LENGTH_SHORT).show();
    }

    //if access is grant gallery will be opened
    private void callGallery(){
        Log.d("debug", "call gall");
        Intent intent = new Intent(Intent.ACTION_PICK);
        startActivityForResult(intent, 2);
        intent.setType("image/*");
        startActivityForResult(intent, GALLERY_INTENT);
    }

    //after selecting image, it will be opened to firebase
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("Debug", "requestCode: " + requestCode + " , resultCode: " + resultCode);

        if (requestCode == GALLERY_INTENT && resultCode == RESULT_OK) {
            Log.d("Debug", "rrequestCode == GALLERY_INTENT && resultCode == RESULT_OK");
            mImageUri = data.getData();
            imageView.setImageURI(mImageUri);
            StorageReference filePath = mStorage.child("User_Images").child(mImageUri.getLastPathSegment());

            mProgressDialog.setMessage("Uploading...");
            mProgressDialog.show();

            filePath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri downloadUri = taskSnapshot.getDownloadUrl(); //ignore this error
                    mRootRef.child("Image_URL").setValue(downloadUri.toString());
                    Glide.with(getActivity().getApplicationContext())
                            .load(downloadUri)
                            .crossFade()
                            .diskCacheStrategy(DiskCacheStrategy.RESULT)
                            .into(imageView);
                    Toast.makeText(getActivity().getApplicationContext(), "Loaded image", Toast.LENGTH_SHORT).show();
                    mProgressDialog.dismiss();
                }
            });
        }
    }
}
