package com.elevenetc.raytracer.utils;


import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewTreeObserver;

/**
 * Created by Eugene Levenetc on 16/07/2016.
 */

public class ViewUtils {

    public static void getMeasuredSize(View view, Size listener) {
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                listener.size(view.getWidth(), view.getHeight());
            }
        });
    }

    public static float dpToPx(float dp) {
        if (dp <= 0) return 0;
        Resources r = Resources.getSystem();
        return dpToPx(dp, r);
    }

    public static float dpToPx(Context context, float dp) {
        if (dp <= 0) return 0;
        Resources r = context.getResources();
        return dpToPx(dp, r);
    }

    public static float dpToPx(float dp, Context context) {
        if (dp <= 0) return 0;
        Resources r = context.getResources();
        return dpToPx(dp, r);
    }

    private static float dpToPx(float dp, Resources resources) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.getDisplayMetrics());
    }

    public interface Size {
        void size(int width, int height);
    }
}
