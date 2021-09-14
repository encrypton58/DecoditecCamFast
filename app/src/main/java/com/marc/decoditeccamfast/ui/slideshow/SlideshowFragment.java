package com.marc.decoditeccamfast.ui.slideshow;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.marc.decoditeccamfast.R;

public class SlideshowFragment extends Fragment {

    private SlideshowViewModel slideshowViewModel;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        slideshowViewModel = new ViewModelProvider(this).get(SlideshowViewModel.class);

        View root = inflater.inflate(R.layout.fragment_slideshow, container, false);
        final TextView textView = root.findViewById(R.id.scan_remote_text);
        slideshowViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);



        return root;
    }
}