package ir.afraapps.tagview;

import android.graphics.drawable.Drawable;

public class TagItem<T> {

  private T tagObject;
  private String text;
  private int textColor;
  private int borderColor;
  private int backgroundColor;
  private Drawable itemDrawable0;
  private Drawable itemDrawable1;
  private Drawable itemDrawable2;

  public TagItem(T tagObject, String text, int textColor, int borderColor, int backgroundColor, Drawable itemDrawable0, Drawable itemDrawable1, Drawable itemDrawable2) {
    this.tagObject = tagObject;
    this.text = text;
    this.textColor = textColor;
    this.borderColor = borderColor;
    this.backgroundColor = backgroundColor;
    this.itemDrawable0 = itemDrawable0;
    this.itemDrawable1 = itemDrawable1;
    this.itemDrawable2 = itemDrawable2;
  }

  public TagItem(T tagObject, String text, int textColor, int borderColor, int backgroundColor, Drawable itemDrawable0) {
    this.tagObject = tagObject;
    this.text = text;
    this.textColor = textColor;
    this.borderColor = borderColor;
    this.backgroundColor = backgroundColor;
    this.itemDrawable0 = itemDrawable0;
  }

  public TagItem(T tagObject, String text, int textColor, int borderColor, int backgroundColor) {
    this.tagObject = tagObject;
    this.text = text;
    this.textColor = textColor;
    this.borderColor = borderColor;
    this.backgroundColor = backgroundColor;
  }

  public TagItem(T tagObject, String text, int textColor) {
    this.tagObject = tagObject;
    this.text = text;
    this.textColor = textColor;
    this.borderColor = -1;
    this.backgroundColor = -1;
  }

  public TagItem(T tagObject, String text) {
    this.tagObject = tagObject;
    this.text = text;
    this.textColor = -1;
    this.borderColor = -1;
    this.backgroundColor = -1;
  }

  public TagItem(T tagObject, String text, int textColor, Drawable itemDrawable0) {
    this.tagObject = tagObject;
    this.text = text;
    this.textColor = textColor;
    this.borderColor = -1;
    this.backgroundColor = -1;
    this.itemDrawable0 = itemDrawable0;
  }

  public TagItem(T tagObject, String text, Drawable itemDrawable0) {
    this.tagObject = tagObject;
    this.text = text;
    this.itemDrawable0 = itemDrawable0;
    this.textColor = -1;
    this.borderColor = -1;
    this.backgroundColor = -1;
  }

  public T getTagObject() {
    return tagObject;
  }

  public void setTagObject(T tagObject) {
    this.tagObject = tagObject;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public int getTextColor() {
    return textColor;
  }

  public void setTextColor(int textColor) {
    this.textColor = textColor;
  }

  public int getBorderColor() {
    return borderColor;
  }

  public void setBorderColor(int borderColor) {
    this.borderColor = borderColor;
  }

  public int getBackgroundColor() {
    return backgroundColor;
  }

  public void setBackgroundColor(int backgroundColor) {
    this.backgroundColor = backgroundColor;
  }

  public Drawable getItemDrawable0() {
    return itemDrawable0;
  }

  public void setItemDrawable0(Drawable itemDrawable0) {
    this.itemDrawable0 = itemDrawable0;
  }

  public Drawable getItemDrawable1() {
    return itemDrawable1;
  }

  public void setItemDrawable1(Drawable itemDrawable1) {
    this.itemDrawable1 = itemDrawable1;
  }

  public Drawable getItemDrawable2() {
    return itemDrawable2;
  }

  public void setItemDrawable2(Drawable itemDrawable2) {
    this.itemDrawable2 = itemDrawable2;
  }

  public boolean hasSetColor() {
    return textColor != -1 || borderColor != -1 || backgroundColor != -1;
  }
}
