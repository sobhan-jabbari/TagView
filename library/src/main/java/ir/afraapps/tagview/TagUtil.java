package ir.afraapps.tagview;

import android.content.Context;
import android.graphics.Typeface;
import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import androidx.core.graphics.ColorUtils;
import android.text.TextUtils;
import android.util.TypedValue;

public class TagUtil {

  @ColorInt
  public static int changeLightness(@ColorInt int color, float value) {
    float[] hsl = new float[3];
    ColorUtils.colorToHSL(color, hsl);
    hsl[2] += value;
    hsl[2] = Math.min(Math.max(hsl[2], 0.0f), 1.0f);
    return ColorUtils.HSLToColor(hsl);
  }

  @ColorInt
  public static int setLightness(@ColorInt int color, float value) {
    float[] hsl = new float[3];
    ColorUtils.colorToHSL(color, hsl);
    hsl[2] = value;
    hsl[2] = Math.min(Math.max(hsl[2], 0.0f), 1.0f);
    return ColorUtils.HSLToColor(hsl);
  }

  public static Typeface getFromAsset(Context context, @Nullable String fontName) {
    if (TextUtils.isEmpty(fontName)) return Typeface.DEFAULT;
    return Typeface.createFromAsset(context.getAssets(), fontName);
  }

  public static int toDIP(Context context, int size) {
    return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, size, context.getResources().getDisplayMetrics());
  }
}
