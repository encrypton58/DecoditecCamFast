package com.marc.decoditeccamfast.clases.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.airbnb.lottie.LottieAnimationView;
import com.marc.decoditeccamfast.R;

public class SuccessOrErrorDialog extends Dialog implements View.OnClickListener {

    private int setAnimation;
    View.OnClickListener listener;

    public SuccessOrErrorDialog(Activity a, boolean succcesOrError) {
        super(a);
        this.setAnimation = succcesOrError ? R.raw.success_animation : R.raw.error_animation;
    }


    public SuccessOrErrorDialog(Activity a, boolean succcesOrError, View.OnClickListener listener) {
        super(a);
        this.listener = listener;
        this.setAnimation = succcesOrError ? R.raw.success_animation : R.raw.error_animation;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_success_or_error);
        this.setCancelable(false);
        Button accept = findViewById(R.id.dialog_succes_accept);
        LottieAnimationView animationView = findViewById(R.id.dialog_success_animation);
        accept.setOnClickListener( ( listener == null) ? this : listener);
        animationView.setAnimation(setAnimation);
        animationView.playAnimation();
    }

    @Override
    public void onClick(View v) {
        this.dismiss();
    }
}
