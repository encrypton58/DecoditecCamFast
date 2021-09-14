package com.marc.decoditeccamfast;


import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.marc.decoditeccamfast.clases.dialogs.LoginDialog;
import com.marc.decoditeccamfast.clases.login.Login;
import com.marc.decoditeccamfast.clases.models.User;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private TextInputLayout layCorreo, layPassword;
    private EditText eCorreo, ePass;
    private boolean correctEmailFormat = false, correctPassFormat = false;
    private LoginDialog dialogCharge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setTheme(R.style.Theme_DecoditecCamFast);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       //hooks UI
        layCorreo = findViewById(R.id.correo);
        layPassword = findViewById(R.id.password);
        eCorreo = findViewById(R.id.inputCorreo);
        ePass = findViewById(R.id.inputPass);
        Button bLogin = findViewById(R.id.loginButton);
        dialogCharge = new LoginDialog(MainActivity.this, "", false, false);
        eCorreo.addTextChangedListener(watcherCorreo);
        ePass.addTextChangedListener(watcherPass);

        bLogin.setOnClickListener(v -> loguear(correctEmailFormat, correctPassFormat));

    }

    TextWatcher watcherCorreo = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            final Pattern VALID_EMAIL_ADDRESS_REGEX =
                    Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
            Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(s);

            if(!s.toString().isEmpty() && !matcher.find()){
                layCorreo.setError("No Es Una Direccion de Correo");
                correctEmailFormat = false;
            }else{
                layCorreo.setError(null);
                correctEmailFormat = true;
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    TextWatcher watcherPass = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(s.length() < 8){
                correctPassFormat = false;
                layPassword.setError("La contraseña debe ser minimo de 8 caracteres");
            }else{
                layPassword.setError(null);
                correctPassFormat = true;
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private void loguear(boolean correctEmailFormat, boolean correctPassFormat){
        Login login = new Login();
        String correo = eCorreo.getText().toString();
        String pass = ePass.getText().toString();
        if(correctEmailFormat && correctPassFormat){
            dialogCharge.show();
            login.loginUser(correo,pass, this, msg -> {
                dialogCharge.dismiss();
                LoginDialog dialog;
                if(msg.equals("")){
                    User user = new Gson().fromJson(getSharedPreferences("user", MODE_PRIVATE).getString("userJson", ""), User.class);
                    if(user != null) {
                        if (!user.getToken().isEmpty()) {
                            startActivity(new Intent(getApplicationContext(), Dashboard.class));
                        }else{
                            dialog = new LoginDialog(MainActivity.this, "No se Encontro El Token", true, true);
                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            dialog.show();
                        }
                    }else{
                        dialog = new LoginDialog(MainActivity.this, "Inicia Sesion de Nuevo", true, true);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog.show();
                    }
                }else{
                    dialog = new LoginDialog(MainActivity.this, msg, true, true);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.show();
                }
            });
        }
    }
}