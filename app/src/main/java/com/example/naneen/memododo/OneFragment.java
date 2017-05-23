package com.example.naneen.memododo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.naneen.memododo.R;

public class OneFragment extends Fragment{
    Button addBtn;
    EditText word, meanign;

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
        addBtn = (Button) getView().findViewById(R.id.add);
        word   = (EditText) getView().findViewById(R.id.word);
        meanign   = (EditText) getView().findViewById(R.id.meaning);

        return view;
    }
}
