package ir.afraapps.tagview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;

import androidx.annotation.Nullable;
import androidx.customview.widget.ViewDragHelper;

/**
 * Author: lujun(http://blog.lujun.co)
 * Date: 2015-12-31 11:47
 */
@SuppressLint("ViewConstructor")
public class TagView extends View {

    /**
     * Border width
     */
    private float mBorderWidth = 0.0f;

    /**
     * Border radius
     */
    private float mBorderRadius;

    /**
     * Text size
     */
    private float mTextSize;

    /**
     * Horizontal padding for this view, include left & right padding(left & right padding are equal
     */
    private int mHorizontalPadding;

    /**
     * Vertical padding for this view, include top & bottom padding(top & bottom padding are equal)
     */
    private int mVerticalPadding;

    /**
     * TagView border color
     */
    private int mBorderColor;

    /**
     * TagView background color
     */
    private int mBackgroundColor;

    /**
     * TagView background color state
     */
    private ColorStateList mBackgroundColorStateList;

    /**
     * TagView text color
     */
    private int mTextColor;

    /**
     * Whether this view clickable
     */
    private boolean isViewClickable;

    /**
     * The max length for this tagview view
     */
    private int mTagMaxLength;

    /**
     * OnTagClickListener for click action
     */
    private OnTagClickListener mOnTagClickListener;

    /**
     * Move slop(default 40px)
     */
    private int mMoveSlop = 40;

    /**
     * Scroll slop threshold
     */
    private int mSlopThreshold = 4;

    /**
     * How long trigger long click callback(default 900ms)
     */
    private int mLongPressTime = 900;

    private int iconSize;
    private int iconPadding;


    private Paint mPaint;

    private RectF mRectF;
    private RectF iconBound;
    private Rect TextBound;

    private TagItem mTag;
    private String mAbstractText, mOriginText;

    private boolean isUp, isMoved;

    private int mLastX, mLastY;

    private float textH, textW, textY;

    private Typeface mTypeface;

    private LayoutMode mMode = LayoutMode.DEFAULT;
    private TagGroup mNotification;

    private boolean useDrawable = false;
    private boolean flag_on, preset_flag_on;

    private boolean usedLongClick;

    private Runnable mLongClickHandle = new Runnable() {
        @Override
        public void run() {
            if (!isMoved && !isUp) {
                int state = ((TagGroup) getParent()).getTagViewState();
                if (state == ViewDragHelper.STATE_IDLE) {
                    usedLongClick = mOnTagClickListener.onTagLongClick(getPosition(), mTag);
                    if (usedLongClick) {
                        sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_LONG_CLICKED);
                    }
                }
            }
        }
    };

    public TagView(Context context, TagItem tag) {
        super(context);
        init(context, tag);
    }

    private void init(Context context, TagItem tag) {
        mTag = tag;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mRectF = new RectF();
        iconBound = new RectF();
        TextBound = new Rect();
        mOriginText = tag == null ? "" : tag.getText();
        preset_flag_on = flag_on = isViewClickable = false;
        iconSize = TagUtil.toDIP(context, 15);
        iconPadding = TagUtil.toDIP(context, 4);
    }

    public void setNotification(TagGroup ly) {
        mNotification = ly;
    }

    private Drawable
            background_drawable_0,
            background_drawable_1,
            background_drawable_2,
            background_drawable_3,
            background_drawable_4,
            background_drawable_5;

    /**
     * setting of using drawable instead of using command attributes
     *
     * @param d0 this cannot be null if the drawable usage is active
     * @param d1 this can be null
     * @param d2 this can be null
     */
    public void setItemDrawableStates(@Nullable Drawable d0, @Nullable Drawable d1, @Nullable Drawable d2) {
        background_drawable_0 = d0;
        background_drawable_1 = d1;
        background_drawable_2 = d2;
        if (d0 != null) {
            useDrawable = true;
            if (d1 == null) {
                background_drawable_1 = d0;
            }
            if (d1 == null) {
                background_drawable_2 = d0;
            }
        }
    }

    public void setItemDrawableHardStates(@Nullable Drawable d0, @Nullable Drawable d1, @Nullable Drawable d2) {
        background_drawable_3 = d0;
        background_drawable_4 = d1;
        background_drawable_5 = d2;
        if (d0 != null) {
            useDrawable = true;
            if (d1 == null) {
                background_drawable_3 = d0;
            }
            if (d1 == null) {
                background_drawable_4 = d0;
            }
        }
    }

    private void onDealText() {
        if (!TextUtils.isEmpty(mOriginText)) {
            mAbstractText = mOriginText.length() <= mTagMaxLength ? mOriginText
                    : mOriginText.substring(0, mTagMaxLength - 3) + "...";
        } else {
            mAbstractText = "";
        }
        mPaint.setTypeface(mTypeface);
        mPaint.setTextSize(mTextSize);
        // final Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
        // float textp = fontMetrics.descent - fontMetrics.ascent;
        mPaint.getTextBounds(mAbstractText, 0, mAbstractText.length(), TextBound);

        textH = TextBound.height();
        textW = TextBound.width();
        textY = TextBound.exactCenterY();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int iconWidth = background_drawable_0 == null ? 0 : iconPadding + iconSize;
        //int iconWidth = background_drawable_0 == null ? 0 : (int) iconBound.width() + (iconPadding * 2);
        setMeasuredDimension(mHorizontalPadding * 2 + (int) mBorderWidth + (int) textW + iconWidth,
                mVerticalPadding * 2 + (int) mBorderWidth + (int) Math.max(textH, iconSize));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mRectF.set(mBorderWidth, mBorderWidth, w - mBorderWidth, h - mBorderWidth);
        calculateIconBound(w, h);
    }

    private void setDrawableBound(Drawable d, Canvas c, RectF rec) {
        if (d != null) {
            d.setBounds((int) rec.left, (int) rec.top, (int) rec.right, (int) rec.bottom);
            d.draw(c);
        }
    }

    public void setIconSize(int iconSize) {
        this.iconSize = iconSize;
        calculateIconBound(getWidth(), getHeight());
        invalidate();
    }

    private void calculateIconBound(int w, int h) {
        int nh = ((h - iconSize) / 2);
        float left = w - mHorizontalPadding - iconSize;
        iconBound.set(left, nh, left + iconSize, h - nh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();

        mPaint.setStyle(Paint.Style.FILL);
        if (mBackgroundColorStateList != null) {
            int[] state = getDrawableState();
            mPaint.setColor(mBackgroundColorStateList.getColorForState(state, mBackgroundColor));
        } else {
            mPaint.setColor(mBackgroundColor);
        }
        canvas.drawRoundRect(mRectF, mBorderRadius, mBorderRadius, mPaint);

        if (mBorderWidth > 0.0f) {
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeWidth(mBorderWidth);
            mPaint.setColor(mBorderColor);
            canvas.drawRoundRect(mRectF, mBorderRadius, mBorderRadius, mPaint);
        }

        if (isFlag_on()) {
            if (isPresetFlag_on()) {
                setDrawableBound(background_drawable_4, canvas, iconBound);
            } else {
                setDrawableBound(background_drawable_1, canvas, iconBound);
            }
        } else {
            if (isPresetFlag_on()) {
                setDrawableBound(background_drawable_3, canvas, iconBound);
            } else {
                setDrawableBound(background_drawable_0, canvas, iconBound);
            }
        }


        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mTextColor);

        if (background_drawable_0 == null) {
            mPaint.setTextAlign(Paint.Align.CENTER);
            // canvas.drawText(mAbstractText, getWidth() / 2f - textW / 2, getHeight() / 2f + fontH / 2 + bdDistance, mPaint);
            canvas.drawText(mAbstractText, getWidth() / 2f, getHeight() / 2f - textY, mPaint);

        } else {
            mPaint.setTextAlign(Paint.Align.RIGHT);
            canvas.drawText(mAbstractText, iconBound.left - iconPadding, getHeight() / 2f - textY, mPaint);
        }

        canvas.restore();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (isViewClickable) {
            int y = (int) event.getY();
            int x = (int) event.getX();
            int action = event.getAction();
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    getParent().requestDisallowInterceptTouchEvent(true);
                    mLastY = y;
                    mLastX = x;
                    isMoved = false;
                    break;

                case MotionEvent.ACTION_MOVE:
                    if (Math.abs(mLastY - y) > mSlopThreshold || Math.abs(mLastX - x) > mSlopThreshold) {
                        getParent().requestDisallowInterceptTouchEvent(false);
                        isMoved = true;
                        return false;
                    }
                    break;
            }
        }
        return super.dispatchTouchEvent(event);
    }

    private long register_down_time;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isViewClickable && mOnTagClickListener != null) {
            int x = (int) event.getX();
            int y = (int) event.getY();
            int action = event.getAction();
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    setPressed(true);
                    usedLongClick = false;
                    mLastY = y;
                    mLastX = x;
                    isMoved = false;
                    isUp = false;
                    postDelayed(mLongClickHandle, mLongPressTime);
                    register_down_time = System.currentTimeMillis();
                    break;

                case MotionEvent.ACTION_MOVE:
                    if (isMoved) {
                        break;
                    }
                    /*if (Math.abs(mLastX - x) > mMoveSlop || Math.abs(mLastY - y) > mMoveSlop) {
                        isMoved = true;
                    }*/
                    if (Math.abs(mLastX - x) > (getX() + getWidth()) || Math.abs(mLastY - y) > (getY() + getHeight())) {
                        isMoved = true;
                    }
                    break;

                case MotionEvent.ACTION_CANCEL:
                    setPressed(false);
                    break;
                case MotionEvent.ACTION_UP:
                    setPressed(false);
                    isUp = true;
                    if (!isMoved) {
                        if (!usedLongClick) {
                            mNotification.notifyInternal(getPosition());
                            playSoundEffect(SoundEffectConstants.CLICK);
                            mOnTagClickListener.onTagClick(getPosition(), mTag);
                        }
                    }

                    break;
            }
            return true;
        }
        return true;
    }

    public String getText() {
        return mOriginText;
    }

    public TagItem getTagObject() {
        return mTag;
    }

    public boolean getIsViewClickable() {
        return isViewClickable;
    }

    public void setTagMaxLength(int maxLength) {
        this.mTagMaxLength = maxLength;
        onDealText();
    }

    private int getPosition() {
        return (int) getTag();
    }


    public void setOnTagClickListener(@Nullable OnTagClickListener listener) {
        this.mOnTagClickListener = listener;
        if (listener != null) {
            this.isViewClickable = true;
        }
    }

    public void setTagBackgroundColor(int color) {
        this.mBackgroundColor = color;
    }

    public ColorStateList getBackgroundColorStateList() {
        return mBackgroundColorStateList;
    }

    public void setBackgroundColorStateList(ColorStateList colorStateList) {
        this.mBackgroundColorStateList = colorStateList;
    }

    public void setTagBorderColor(int color) {
        this.mBorderColor = color;
    }

    public void setTagTextColor(int color) {
        this.mTextColor = color;
    }

    public void setBorderWidth(float width) {
        this.mBorderWidth = width;
    }

    public void setBorderRadius(float radius) {
        this.mBorderRadius = radius;
    }

    public void setTextSize(float size) {
        this.mTextSize = size;
        onDealText();
    }


    public void applyProfile(int[] profile) {
        setTagTextColor(profile[2]);
        if (useDrawable) return;
        setTagBackgroundColor(profile[1]);
        setTagBorderColor(profile[0]);
    }

    public void setMode(LayoutMode mode) {
        this.mMode = mode;
    }

    public void setHorizontalPadding(int padding) {
        this.mHorizontalPadding = padding;
    }

    public void setVerticalPadding(int padding) {
        this.mVerticalPadding = padding;
    }

    public interface OnTagClickListener<T> {
        void onTagClick(int position, TagItem<T> tag);

        boolean onTagLongClick(int position, TagItem<T> tag);
    }

    public void setTypeface(Typeface typeface) {
        this.mTypeface = typeface;
        onDealText();
    }


    public boolean isFlag_on() {
        return flag_on;
    }

    public boolean isPresetFlag_on() {
        return preset_flag_on;
    }

    public void setFlag_on(boolean b) {
        if (useDrawable) {
            //setSelected(b);
        }
        this.flag_on = b;
    }

    public void setPresetFlag(boolean b) {
        this.preset_flag_on = b;
    }


    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        invalidate();
    }


}
