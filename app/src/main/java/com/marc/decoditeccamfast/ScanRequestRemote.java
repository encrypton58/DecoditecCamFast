package com.marc.decoditeccamfast;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.google.gson.JsonIOException;
import com.marc.decoditeccamfast.clases.dialogs.SuccessOrErrorDialog;
import com.marc.decoditeccamfast.clases.http.SocketHandler;

import org.json.JSONException;
import org.json.JSONObject;

public class ScanRequestRemote extends AppCompatActivity {

    private CodeScannerView scannerView;
    private CodeScanner codeScanner;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_request_remote);

        String code =  getIntent().getExtras().getString("code");
        Toast.makeText(this, code, Toast.LENGTH_SHORT).show();

        textView = findViewById(R.id.scan_remote_text);
        scannerView = findViewById(R.id.scan_remote_view);
        codeScanner = new CodeScanner(getApplicationContext(), scannerView);

        codeScanner.setDecodeCallback(result -> {
            runOnUiThread(() -> new SuccessOrErrorDialog(ScanRequestRemote.this, true, v -> finishAffinity()).show());
            runOnUiThread(() -> textView.setText(result.getText()));
            try{
                JSONObject json = new JSONObject();
                json.put("codeStr", result.getText());
                json.put("codeSecure", code);
                SocketHandler.getSocket().emit("handleResults", json);
                getIntent().putExtra("code", "");
            }catch (JSONException e){
                System.out.println(e.getMessage());
            }

        });

        scannerView.setOnClickListener(v -> codeScanner.startPreview());

    }
}