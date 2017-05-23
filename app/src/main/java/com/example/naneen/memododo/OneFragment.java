package com.example.naneen.memododo;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class OneFragment extends Fragment{
    Button addBtn;
    EditText wordEdit, meaningEdit;
//    String word, meaning;

    public OneFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_one, container, false);
        addBtn = (Button) view.findViewById(R.id.addBtn);
        wordEdit   = (EditText) view.findViewById(R.id.wordEdit);
        meaningEdit   = (EditText) view.findViewById(R.id.meaningEdit);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String word = wordEdit.getText().toString().trim();
                if (word.isEmpty()) {
                    Toast.makeText(getActivity().getApplicationContext(), "Please enter title", Toast.LENGTH_SHORT).show();
                    return;
                }
//                Firebase childRef_name = mRootRef.child("rd, Toast.LENGImage_Title");
//                childRef_name.setValue(mName);
                Toast.makeText(getActivity().getApplicationContext(), "Updated word: " + word, Toast.LENGTH_LONG).show();
                InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });

//        addBtn.setOnClickListener((View.OnClickListener) this);

        return view;
    }
//    @Override
//    public void onClick(View v) {
//        word = wordEdit.getText().toString();
//    }
}
