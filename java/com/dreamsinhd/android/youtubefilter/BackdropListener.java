package com.dreamsinhd.android.youtubefilter;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;

// Executes animation to show backdrop menu
public class BackdropListener implements MenuItem.OnMenuItemClickListener{

    private final AnimatorSet animatorSet = new AnimatorSet();
    private Context context;
    private View sheet;
    private int height;
    private boolean backdropShown = false;

    BackdropListener(Context context, View sheet) {
        this.context = context;
        this.sheet = sheet;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;
    }

    public boolean isBackdropShown() {
        return backdropShown;
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        executeAnimation();

        return true;
    }

    public void executeAnimation() {
        backdropShown = !backdropShown;

        // Cancel the existing animations
        animatorSet.removeAllListeners();
        animatorSet.end();
        animatorSet.cancel();

        final int translateY = height -
                context.getResources().getDimensionPixelSize(R.dimen.backdrop_reveal_height);

        ObjectAnimator animator = ObjectAnimator.ofFloat(sheet, "translationY", backdropShown ? translateY : 0);
        animator.setDuration(500);

        animatorSet.play(animator);
        animator.start();
    }
}
