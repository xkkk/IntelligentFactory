package com.cme.corelib.utils;

import android.animation.ObjectAnimator;
import android.view.View;

/**
 * Created by xiaozi on 2017/6/14.
 */

public class AnimatorUtils {
    private static ObjectAnimator anim;
    private static ObjectAnimator scaleX;

    public static void scale(View view) {
        anim = ObjectAnimator.ofFloat(view, "scaleY", 1f, 1.5f, 1f);
        scaleX = ObjectAnimator.ofFloat(view, "scaleX", 1f, 1.5f, 1f);
        anim.setDuration(500);
        scaleX.setDuration(500);
        anim.start();
        scaleX.start();
    }


}
