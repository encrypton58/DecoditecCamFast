package com.marc.decoditeccamfast.ui.home;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.marc.decoditeccamfast.R;
import com.marc.decoditeccamfast.clases.models.Constants;
import com.marc.decoditeccamfast.clases.services.ListenScanerRequest;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private final int CAMERA_REQUEST_CODE = 101;
    private final int READ_EXTERNAL_CODE = 102;
    private final int WRITER_EXTERNAL_CODE = 103;
    TextView textView;
    CodeScanner mCodeScanner;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
       textView = root.findViewById(R.id.text_home);

        setUpPermissions();
        codeScanner(root);

        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        Intent service = new Intent(requireContext(), ListenScanerRequest.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            requireActivity().startForegroundService(service);
        }

        IntentFilter filter = new IntentFilter(Constants.ACTION_RUN_SERVICE);
        filter.addAction(Constants.ACTION_EXIT_SERVICE);

        ResponseReceiver receiver = new ResponseReceiver();
        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(receiver, filter);

        return root;
    }

    private void codeScanner(View root) {
        final CodeScannerView scannerView = root.findViewById(R.id.dashboard_scanner_view);
        mCodeScanner = new CodeScanner(requireActivity(), scannerView);
        mCodeScanner.setDecodeCallback(result ->
                requireActivity().runOnUiThread(() -> Toast.makeText(getActivity(), result.getText(), Toast.LENGTH_SHORT).show())
        );
        scannerView.setOnClickListener(v -> mCodeScanner.startPreview());
    }

    private void setUpPermissions(){
        int permission = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA);
        int storagePer = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int storage = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE);

        if(permission != PackageManager.PERMISSION_GRANTED &&
                storage != PackageManager.PERMISSION_GRANTED && storagePer != PackageManager.PERMISSION_GRANTED){
            String[] permissions = new String[3];
            permissions[0] = Manifest.permission.CAMERA;
            permissions[1] = Manifest.permission.READ_EXTERNAL_STORAGE;
            permissions[2] = Manifest.permission.WRITE_EXTERNAL_STORAGE;

            requestPermissions(permissions, CAMERA_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == CAMERA_REQUEST_CODE){
            if(grantResults[0] != PackageManager.PERMISSION_GRANTED){
                Toast.makeText(getActivity(), "Se Requiere La Camara", Toast.LENGTH_SHORT).show();
            }
        }
        if(requestCode == READ_EXTERNAL_CODE){
            if(grantResults[1] != PackageManager.PERMISSION_GRANTED){
                Toast.makeText(getActivity(), "Se Requiere La Lectura", Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == WRITER_EXTERNAL_CODE){
            if(grantResults[2] != PackageManager.PERMISSION_GRANTED){
                Toast.makeText(getActivity(), "Se Requiere La Escritura", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }

    private class ResponseReceiver extends BroadcastReceiver {

        // Sin instancias
        private ResponseReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            /*switch (intent.getAction()) {
                case Constants.ACTION_RUN_SERVICE:
                    requireActivity().runOnUiThread(() -> Toast.makeText(context, intent.getStringExtra(Constants.EXTRA_REQUEST), Toast.LENGTH_SHORT).show());
                    break;
                case Constants.ACTION_EXIT_SERVICE:
                    System.out.println("Stop_Service");
                    break;
            }*/
        }
    }
}