package com.marc.decoditeccamfast.clases.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
import com.marc.decoditeccamfast.R;

public class LoginDialog extends Dialog implements View.OnClickListener {

    public TextView msgBox;
    private final String mesagge;
    private final boolean typeAnimation;
    private final boolean showButtonAccept;

    public LoginDialog(Activity a, String msg, boolean typeAnimation, boolean showButton) {
        super(a);
        this.mesagge = msg;
        this.typeAnimation = typeAnimation;
        this.showButtonAccept = showButton;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Configuraciones
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.login_dialog_error);
        this.setCancelable(false);
        //Hooks UI
        Button accept = findViewById(R.id.login_dialog_button_accept);
        msgBox = findViewById(R.id.login_dialog_boxmsg);
        LottieAnimationView animationView = findViewById(R.id.login_dialog_animation);
        //Set Attibutes UI
        msgBox.setText(mesagge);
        accept.setOnClickListener(this);
        animationView.setRepeatCount(10);
        animationView.setRepeatMode(LottieDrawable.REVERSE);
        animationView.setRepeatMode(LottieDrawable.RESTART);
        //true
        if(typeAnimation){
            animationView.setAnimation(R.raw.login_animation_error);
        }else{
            animationView.setAnimation(R.raw.load_animation);
        }

        if(!showButtonAccept){
            accept.setVisibility(View.INVISIBLE);
        }else{
            accept.setVisibility(View.VISIBLE);
        }

        animationView.playAnimation();
    }


    @Override
    public void onClick(View v) {
        this.dismiss();
    }

}
