package ir.afraapps.tagview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.annotation.StyleRes;


public class ColorFactory {


  private static final String BG_COLOR_ALPHA = "33";
  private static final String BD_COLOR_ALPHA = "88";

  private static final String RED         = "F44336";
  private static final String LIGHT_BLUE  = "03A9F4";
  private static final String AMBER       = "FFC107";
  private static final String ORANGE      = "FF9800";
  private static final String YELLOW      = "FFEB3B";
  private static final String LIME        = "CDDC39";
  private static final String BLUE        = "2196F3";
  private static final String INDIGO      = "3F51B5";
  private static final String LIGHT_GREEN = "8BC34A";
  private static final String GREY        = "9E9E9E";
  private static final String DEEP_PURPLE = "673AB7";
  private static final String TEAL        = "009688";
  private static final String CYAN        = "00BCD4";

  public enum PURE_COLOR {CYAN, TEAL}

  public static final int NONE      = -1;
  public static final int RANDOM    = 0;
  public static final int PURE_CYAN = 1;
  public static final int PURE_TEAL = 2;

  private static final int SHARP666666 = Color.parseColor("#FF666666");
  private static final int SHARP727272 = Color.parseColor("#FF727272");

  private static final String[] COLORS = new String[]{RED, LIGHT_BLUE, AMBER, ORANGE, YELLOW,
    LIME, BLUE, INDIGO, LIGHT_GREEN, GREY, DEEP_PURPLE, TEAL, CYAN};

  public static int[] onRandomBuild() {
    int random  = (int) (Math.random() * COLORS.length);
    int bgColor = Color.parseColor("#" + BG_COLOR_ALPHA + COLORS[random]);
    int bdColor = Color.parseColor("#" + BD_COLOR_ALPHA + COLORS[random]);
    int tColor  = SHARP666666;
    return new int[]{bgColor, bdColor, tColor};
  }

  public static int[] onPureBuild(PURE_COLOR type) {
    String color   = type == PURE_COLOR.CYAN ? CYAN : TEAL;
    int    bdColor = Color.parseColor("#" + BD_COLOR_ALPHA + color);
    int    bgColor = Color.parseColor("#" + BG_COLOR_ALPHA + color);
    return new int[]{bdColor, bgColor, SHARP727272};
  }


  public ColorFactory() {

  }


  private int[] theme_style_config;
  boolean usingStyle = false;

  public ColorFactory(@StyleRes int theme, @NonNull Context context) {


    int[] attrs = {
      R.attr.tg_border_color,
      R.attr.tg_background_color,
      R.attr.tg_text_color
    };

    // Parse MyCustomStyle, using Context.obtainStyledAttributes()
    TypedArray ta = context.obtainStyledAttributes(theme, attrs);
    usingStyle = ta.hasValue(0);


    int borderColor = ta.getColor(R.styleable.TagViewColorTheme_tg_border_color, Color.WHITE);
    int bgColor     = ta.getColor(R.styleable.TagViewColorTheme_tg_background_color, Color.WHITE);
    int textColor   = ta.getColor(R.styleable.TagViewColorTheme_tg_text_color, Color.WHITE);

    /*TypedValue outValue = new TypedValue();
    boolean hasBorderColorValue = context.getTheme()
      .resolveAttribute(R.attr.ptc_tag_border_color, outValue, true);
    if (hasBorderColorValue) {
      borderColor = outValue.data;
    }
    boolean hasBgColorValue = context.getTheme()
      .resolveAttribute(R.attr.ptc_tag_background_color, outValue, true);
    if (hasBgColorValue) {
      bgColor = outValue.data;
    }
    boolean hasTextColorValue = context.getTheme()
      .resolveAttribute(R.attr.ptc_tag_text_color, outValue, true);
    if (hasTextColorValue) {
      textColor = outValue.data;
    }*/

    theme_style_config = new int[]{borderColor, bgColor, textColor};

    ta.recycle();

  }

  public boolean isUsingStyleable() {
    return usingStyle;
  }

  public int[] getConfig() {
    return theme_style_config;
  }
}
