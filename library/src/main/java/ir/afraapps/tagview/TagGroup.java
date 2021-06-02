package ir.afraapps.tagview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.customview.widget.ViewDragHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Author: lujun(http://blog.lujun.co)
 * Date: 2015-12-30 17:14
 */
public class TagGroup extends ViewGroup {

    private static final String TAG = TagGroup.class.getSimpleName();

    /**
     * Vertical interval, default 5(dp)
     */
    private int mVerticalInterval;

    /**
     * Horizontal interval, default 5(dp)
     */
    private int mHorizontalInterval;

    /**
     * TagGroup border width(default 0.5dp)
     */
    private float mBorderWidth = 0.0f;

    /**
     * TagGroup border radius(default 10.0dp)
     */
    private float mBorderRadius = 10.0f;

    /**
     * The sensitive of the ViewDragHelper(default 1.0f, normal)
     */
    private float mSensitivity = 1.0f;

    /**
     * TagView average height
     */
    private int mChildHeight;

    /**
     * TagGroup border color(default #22FF0000)
     */
    private int mBorderColor = Color.TRANSPARENT;

    /**
     * TagGroup background color(default #11FF0000)
     */
    private int mBackgroundColor = Color.TRANSPARENT;

    /**
     * The container layout gravity(default left)
     */
    private int mGravity = Gravity.START;

    /**
     * The max length for TagView(default max length 23)
     */
    private int mTagMaxLength = 23;

    /**
     * TagView Border width(default 0.5dp)
     */
    private float mTagBorderWidth = 1.0f;

    /**
     * TagView Border radius(default 15.0dp)
     */
    private float mTagBorderRadius = 15.0f;

    /**
     * TagView Text size(default 14sp)
     */
    private float mTextSize = 14;


    /**
     * Horizontal padding for TagView, include left & right padding(left & right padding are equal, default 20px)
     */
    private int mTagHorizontalPadding = 20;

    /**
     * Vertical padding for TagView, include top & bottom padding(top & bottom padding are equal, default 17px)
     */
    private int mTagVerticalPadding = 17;

    /**
     * TagView border color(default #88F44336)
     */
    private int mTagBorderColor = Color.parseColor("#88F44336");

    /**
     * TagView background color(default #33F44336)
     */
    private int mTagBackgroundColor = Color.TRANSPARENT;

    /**
     * TagView background color state
     */
    private ColorStateList mTagBackgroundColorStateList;

    /**
     * TagView text color(default #FF666666)
     */
    private int mTextColor = Color.parseColor("#FF666666");

    /**
     * TagView typeface
     */
    private Typeface mTypeface = Typeface.DEFAULT;

    /**
     * for alternative selecting drawable
     */
    private Drawable itemDrawable0, itemDrawable1, itemDrawable2, itemDrawable3, itemDrawable4, itemDrawable5;
    /**
     * Preselected positions of the tagview
     */
    private int[] mPreselectedTags;
    /**
     * Tags
     */
    private List<TagItem> mTags;

    /**
     * Can drag TagView(default false)
     */
    private boolean mDragEnable;

    /**
     * TagView drag state(default STATE_IDLE)
     */
    private int mTagViewState = ViewDragHelper.STATE_IDLE;


    /**
     * The interaction mode from the factory
     */
    private LayoutMode mMode = LayoutMode.DEFAULT;
    /**
     * OnTagClickListener for TagView
     */
    private TagView.OnTagClickListener mOnTagClickListener;

    private Paint mPaint;

    private RectF mRectF;

    private ViewDragHelper mViewDragHelper;

    private List<View> mChildViews;

    private int[] mViewPos;
    private int[] profile_normal, profile_active, profile_theme_perpetual_preset, profile_theme_preset_perpetual_active;
    private Context mContext;
    /**
     * View theme default PURE_CYAN)
     */
    private int mTheme = ColorFactory.NONE;

    private int mThemePresetPerpetual = ColorFactory.PURE_CYAN;
    private int mThemePresetPerpetualActive = ColorFactory.RANDOM;
    /**
     * View theme default PURE_CYAN
     */
    private int mThemeActive = ColorFactory.NONE;
    /**
     * Default interval(dp)
     */
    private static final float DEFAULT_INTERVAL = 5;

    /**
     * Default tagview min length
     */
    private static final int TAG_MIN_LENGTH = 3;

    private boolean has_hard_layout_preset = false;

    public TagGroup(Context context) {
        this(context, null);
    }

    public TagGroup(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TagGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        mContext = context;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TagGroup,
                defStyleAttr, 0);
        mVerticalInterval = (int) a.getDimension(R.styleable.TagGroup_vertical_interval,
                dp2px(context, DEFAULT_INTERVAL));
        mHorizontalInterval = (int) a.getDimension(R.styleable.TagGroup_horizontal_interval,
                dp2px(context, DEFAULT_INTERVAL));
        mBorderWidth = a.getDimension(R.styleable.TagGroup_group_border_width,
                dp2px(context, mBorderWidth));
        mBorderRadius = a.getDimension(R.styleable.TagGroup_group_border_radius,
                dp2px(context, mBorderRadius));
        mBorderColor = a.getColor(R.styleable.TagGroup_group_border_color,
                mBorderColor);
        mBackgroundColor = a.getColor(R.styleable.TagGroup_group_background_color,
                mBackgroundColor);
        mDragEnable = a.getBoolean(R.styleable.TagGroup_group_enable_drag, false);
        mSensitivity = a.getFloat(R.styleable.TagGroup_group_drag_sensitivity,
                mSensitivity);
        mGravity = a.getInt(R.styleable.TagGroup_group_gravity, mGravity);
        mTagMaxLength = a.getInt(R.styleable.TagGroup_tag_max_length, mTagMaxLength);
        mTheme = a.getInt(R.styleable.TagGroup_tag_theme, mTheme);
        mTagBorderWidth = a.getDimension(R.styleable.TagGroup_tag_border_width,
                dp2px(context, mTagBorderWidth));
        mTagBorderRadius = a.getDimension(
                R.styleable.TagGroup_tag_corner_radius, dp2px(context, mTagBorderRadius));
        mTagHorizontalPadding = a.getDimensionPixelSize(
                R.styleable.TagGroup_tag_horizontal_padding, mTagHorizontalPadding);
        mTagVerticalPadding = a.getDimensionPixelSize(
                R.styleable.TagGroup_tag_vertical_padding, mTagVerticalPadding);
        mTextSize = a.getDimension(R.styleable.TagGroup_tag_text_size,
                sp2px(context, mTextSize));
        mTagBorderColor = a.getColor(R.styleable.TagGroup_tag_border_color,
                mTagBorderColor);
        mTagBackgroundColor = a.getColor(R.styleable.TagGroup_tag_background_color,
                mTagBackgroundColor);
        mTextColor = a.getColor(R.styleable.TagGroup_tag_text_color, mTextColor);
        itemDrawable0 = a.getDrawable(R.styleable.TagGroup_tag_drawable_state0);
        itemDrawable1 = a.getDrawable(R.styleable.TagGroup_tag_drawable_state1);
        itemDrawable2 = a.getDrawable(R.styleable.TagGroup_tag_drawable_state2);
        itemDrawable3 = a.getDrawable(R.styleable.TagGroup_tag_drawable_state3);
        itemDrawable4 = a.getDrawable(R.styleable.TagGroup_tag_drawable_state4);
        itemDrawable5 = a.getDrawable(R.styleable.TagGroup_tag_drawable_state5);

        if (a.hasValue(R.styleable.TagGroup_tag_typeface)) {
            String font_name = a.getString(R.styleable.TagGroup_tag_typeface);
            mTypeface = Typeface.createFromAsset(mContext.getAssets(), font_name);
        }

        a.recycle();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mRectF = new RectF();
        mChildViews = new ArrayList<>();
        mViewDragHelper = ViewDragHelper.create(this, mSensitivity, new DragHelperCallBack());
        setWillNotDraw(false);
        setTagMaxLength(mTagMaxLength);
        setTagHorizontalPadding(mTagHorizontalPadding);
        setTagVerticalPadding(mTagVerticalPadding);
        if (!isInEditMode()) {
            profile_normal = onUpdateColorFactory(mTheme);
            profile_active = getProfileActive();
        }
    }


    private int[] getProfileActive() {
        if (profile_normal == null) {
            return new int[]{mTagBorderColor, mTagBackgroundColor, mTextColor};
        }

        int borderColor = TagUtil.changeLightness(profile_normal[0], -0.2f);
        int bgColor = TagUtil.changeLightness(profile_normal[1], -0.2f);
        int textColor = TagUtil.setLightness(profile_normal[2], 0.9f);

        return new int[]{borderColor, bgColor, textColor};
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        measureChildren(widthMeasureSpec, heightMeasureSpec);
        final int childCount = getChildCount();
        int lines = childCount == 0 ? 0 : getChildLines(childCount);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
//        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);

        if (childCount == 0) {
            setMeasuredDimension(0, 0);
        } else if (heightSpecMode == MeasureSpec.AT_MOST
                || heightSpecMode == MeasureSpec.UNSPECIFIED) {
            setMeasuredDimension(widthSpecSize, (mVerticalInterval + mChildHeight) * lines
                    - mVerticalInterval + getPaddingTop() + getPaddingBottom());
        } else {
            setMeasuredDimension(widthSpecSize, heightSpecSize);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mRectF.set(0, 0, w, h);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount;
        if ((childCount = getChildCount()) <= 0) {
            return;
        }
        int availableW = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
        int curRight = getMeasuredWidth() - getPaddingRight();
        int curTop = getPaddingTop();
        int curLeft = getPaddingLeft();
        int sPos = 0;
        mViewPos = new int[childCount * 2];

        for (int i = 0; i < childCount; i++) {
            final View childView = getChildAt(i);
            if (childView.getVisibility() != GONE) {
                int width = childView.getMeasuredWidth();
                if (mGravity == Gravity.RIGHT || (mGravity == Gravity.START && getLayoutDirection() == LAYOUT_DIRECTION_RTL)) {
                    if (curRight - width < getPaddingLeft()) {
                        curRight = getMeasuredWidth() - getPaddingRight();
                        curTop += mChildHeight + mVerticalInterval;
                    }
                    mViewPos[i * 2] = curRight - width;
                    mViewPos[i * 2 + 1] = curTop;
                    curRight -= width + mHorizontalInterval;
                } else if (mGravity == Gravity.CENTER) {
                    if (curLeft + width - getPaddingLeft() > availableW) {
                        int leftW = getMeasuredWidth() - mViewPos[(i - 1) * 2]
                                - getChildAt(i - 1).getMeasuredWidth() - getPaddingRight();
                        for (int j = sPos; j < i; j++) {
                            mViewPos[j * 2] = mViewPos[j * 2] + leftW / 2;
                        }
                        sPos = i;
                        curLeft = getPaddingLeft();
                        curTop += mChildHeight + mVerticalInterval;
                    }
                    mViewPos[i * 2] = curLeft;
                    mViewPos[i * 2 + 1] = curTop;
                    curLeft += width + mHorizontalInterval;

                    if (i == childCount - 1) {
                        int leftW = getMeasuredWidth() - mViewPos[i * 2]
                                - childView.getMeasuredWidth() - getPaddingRight();
                        for (int j = sPos; j < childCount; j++) {
                            mViewPos[j * 2] = mViewPos[j * 2] + leftW / 2;
                        }
                    }
                } else {
                    if (curLeft + width - getPaddingLeft() > availableW) {
                        curLeft = getPaddingLeft();
                        curTop += mChildHeight + mVerticalInterval;
                    }
                    mViewPos[i * 2] = curLeft;
                    mViewPos[i * 2 + 1] = curTop;
                    curLeft += width + mHorizontalInterval;
                }
            }
        }

        // layout all child views
        for (int i = 0; i < mViewPos.length / 2; i++) {
            View childView = getChildAt(i);
            childView.layout(mViewPos[i * 2], mViewPos[i * 2 + 1],
                    mViewPos[i * 2] + childView.getMeasuredWidth(),
                    mViewPos[i * 2 + 1] + mChildHeight);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mBackgroundColor);
        canvas.drawRoundRect(mRectF, mBorderRadius, mBorderRadius, mPaint);

        if (mBorderWidth > 0f) {
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeWidth(mBorderWidth);
            mPaint.setColor(mBorderColor);
            canvas.drawRoundRect(mRectF, mBorderRadius, mBorderRadius, mPaint);
        }
    }

    public static class Builder {
        private int
                ic_company, ic_search, ic_back, ic_background,
                tb_textsize = 0, tb_title_color = 0, title_line_config = 1,
                animation_duration_logo = -1,
                animation_duration = -1;
        private Typeface typeface;
        private String title_default;
        private boolean enable_logo_anim = true;
        private boolean save_title_navigation = false;

        public Builder() {

        }
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        // TODO Auto-generated method stub
        try {
            return mViewDragHelper.shouldInterceptTouchEvent(ev);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mViewDragHelper.processTouchEvent(event);
        return true;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mViewDragHelper.continueSettling(true)) {
            requestLayout();
        }
    }

    private int getChildLines(int childCount) {
        int availableW = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
        int lines = 1;
        for (int i = 0, curLineW = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            int dis = childView.getMeasuredWidth() + mHorizontalInterval;
            int height = childView.getMeasuredHeight();
            mChildHeight = i == 0 ? height : Math.min(mChildHeight, height);
            curLineW += dis;
            if (curLineW - mHorizontalInterval > availableW) {
                lines++;
                curLineW = dis;
            }
        }
        return lines;
    }

    private int[] onUpdateColorFactory(int import_theme) {
        Log.i(TAG, "onUpdateColorFactory");
        int[] colors;
        if (import_theme == ColorFactory.RANDOM) {
            Log.d(TAG, "color random");
            colors = ColorFactory.onRandomBuild();
        } else if (import_theme == ColorFactory.PURE_TEAL) {
            Log.d(TAG, "color pure teal");
            colors = ColorFactory.onPureBuild(ColorFactory.PURE_COLOR.TEAL);
        } else if (import_theme == ColorFactory.PURE_CYAN) {
            Log.d(TAG, "color cyan");
            colors = ColorFactory.onPureBuild(ColorFactory.PURE_COLOR.CYAN);
        } else {
            Log.d(TAG, "color none");
            ColorFactory cf = new ColorFactory(import_theme, mContext);
            if (cf.isUsingStyleable()) {
                Log.d(TAG, "is using styleable");
                colors = cf.getConfig();
            } else {
                Log.d(TAG, "is not using styleable");
                colors = new int[]{mTagBorderColor, mTagBackgroundColor, mTextColor};
            }
        }
        return colors;
    }

    private int[] onUpdateColorFactory(TagItem tag) {
        Log.i(TAG, "onUpdateColorFactory");
        int borderColor = tag.getBorderColor() == -1 ? mTagBorderColor : tag.getBorderColor();
        int backgroundColor = tag.getBackgroundColor() == -1 ? mTagBackgroundColor : tag.getBorderColor();
        int textColor = tag.getTextColor() == -1 ? mTextColor : tag.getTextColor();
        return new int[]{borderColor, backgroundColor, textColor};
    }

    private void onSetTag() {
        if (mTags == null || mTags.size() == 0) {
            return;
        }
        for (int i = 0; i < mTags.size(); i++) {
            onAddTag(mTags.get(i), mChildViews.size());
        }
        postInvalidate();
    }

    private void processPreselectedOptions(int position, TagView tag, int[] target_theme, boolean flag) {
        Log.i(TAG, "processPreselectedOptions");
        if (mMode == LayoutMode.MULTIPLE_CHOICE || mMode == LayoutMode.SINGLE_CHOICE) {
            boolean apply_original = true;
            if (mPreselectedTags != null && profile_theme_perpetual_preset != null) {
                for (int i = 0; i < mPreselectedTags.length; i++) {
                    if (mPreselectedTags[i] == position) {
                        if (flag) {
                            tag.applyProfile(profile_theme_perpetual_preset);
                        } else {
                            tag.applyProfile(target_theme);
                        }
                        tag.setFlag_on(flag);
                        apply_original = false;
                    }
                }
            }
            if (apply_original) {
                tag.applyProfile(target_theme);
                tag.setFlag_on(flag);
            }
        }
    }

    private void processPreselectedRender(int position, TagView tag) {
        Log.i(TAG, "processPreselectedRender");
        if (mMode == LayoutMode.MULTIPLE_CHOICE || mMode == LayoutMode.SINGLE_CHOICE) {
            Log.d(TAG, "layout mode is multiple choice or single choice");
            if (mPreselectedTags == null) return;

            Log.d(TAG, "preselected tags are not null");

            for (int mPreselectedTag : mPreselectedTags) {
                if (mPreselectedTag == position) {
                    if (has_hard_layout_preset) {
                        Log.d(TAG, "has hard layout preset");
                        tag.setPresetFlag(true);
                        tag.applyProfile(profile_theme_perpetual_preset);
                    } else {
                        Log.d(TAG, "has not hard layout preset");
                        tag.applyProfile(profile_active);
                    }
                    tag.setFlag_on(true);
                }
            }
        }
    }

    private void onAddTag(@NonNull TagItem tag, int position) {
        if (position < 0 || position > mChildViews.size()) {
            throw new RuntimeException("Illegal position!");
        }
        TagView tagView = new TagView(getContext(), tag);

        initTagView(tagView, tag);

        mChildViews.add(position, tagView);
        if (position < mChildViews.size()) {
            for (int i = position; i < mChildViews.size(); i++) {
                mChildViews.get(i).setTag(i);
            }
        } else {
            tagView.setTag(position);
        }

        processPreselectedRender(position, tagView);
        addView(tagView, position);
    }


    private void initTagView(TagView tagView, TagItem tag) {
        //tag.applyProfile(onUpdateColorFactory(mTheme));
        int[] profile = tag.hasSetColor() ? onUpdateColorFactory(tag) : onUpdateColorFactory(mTheme);
        Drawable icon0 = tag.getItemDrawable0() == null ? itemDrawable0 : tag.getItemDrawable0();
        Drawable icon1 = tag.getItemDrawable1() == null ? itemDrawable1 : tag.getItemDrawable1();
        Drawable icon2 = tag.getItemDrawable2() == null ? itemDrawable2 : tag.getItemDrawable2();
        tagView.applyProfile(profile);
        tagView.setBackgroundColorStateList(mTagBackgroundColorStateList);
        tagView.setTagMaxLength(mTagMaxLength);
        tagView.setTypeface(mTypeface);
        tagView.setBorderWidth(mTagBorderWidth);
        tagView.setBorderRadius(mTagBorderRadius);
        tagView.setTextSize(mTextSize);
        tagView.setHorizontalPadding(mTagHorizontalPadding);
        tagView.setVerticalPadding(mTagVerticalPadding);
        tagView.setOnTagClickListener(mOnTagClickListener);
        tagView.setMode(mMode);
        tagView.setNotification(this);
        tagView.setItemDrawableStates(icon0, icon1, icon2);
        tagView.setItemDrawableHardStates(itemDrawable3, itemDrawable4, itemDrawable5);
    }

    private void processPreselectedOptionsOff(final int position, TagView tag) {
        Log.i(TAG, "processPreselectedOptionsOff");
        if (mPreselectedTags == null) return;
        for (int mPreselectedTag : mPreselectedTags) {
            if (mPreselectedTag == position) {
                tag.applyProfile(profile_theme_perpetual_preset == null ? profile_normal : profile_theme_perpetual_preset);
            } else {
                tag.applyProfile(profile_normal);
            }
        }
        tag.setFlag_on(false);
    }

    /**
     * only communicate from the TagView
     *
     * @param position position of the tagview
     */
    public void notifyInternal(int position) {
        Log.i(TAG, "notifyInternal");
        int onNotePosition = scopePosition(position);
        if (mMode == LayoutMode.DEFAULT) {
            // not happened touch event

        } else if (mMode == LayoutMode.SINGLE_CHOICE) {
            for (int i = 0; i < mChildViews.size(); i++) {
                if (mChildViews.get(i) instanceof TagView) {
                    TagView tag = (TagView) mChildViews.get(i);
                    if (onNotePosition == i) {
                        Log.d(TAG, "is activated tagview");
                        if (has_hard_layout_preset && tag.isPresetFlag_on()) {
                            Log.d(TAG, "has hard layout preset and is preset flag");
                            tag.applyProfile(profile_theme_preset_perpetual_active);
                        } else {
                            Log.d(TAG, "has not hard layout preset or is not preset flag");
                            tag.applyProfile(profile_active);
                        }
                        tag.setFlag_on(true);
                    } else {
                        Log.d(TAG, "is normal tagview");
                        if (has_hard_layout_preset && tag.isPresetFlag_on()) {
                            Log.d(TAG, "has hard layout preset and is preset flag");
                            tag.applyProfile(profile_theme_perpetual_preset);
                        } else {
                            Log.d(TAG, "has not hard layout preset or is not preset flag");
                            tag.applyProfile(profile_normal);
                        }
                        tag.setFlag_on(false);
                    }
                    tag.postInvalidate();
                }
            }
        } else if (mMode == LayoutMode.SINGLE_CHOICE_OVERLAY_PRESET) {
            for (int i = 0; i < mChildViews.size(); i++) {
                if (mChildViews.get(i) instanceof TagView) {
                    TagView tag = (TagView) mChildViews.get(i);
                    if (onNotePosition == i) {
                        tag.applyProfile(profile_theme_preset_perpetual_active);
                        tag.setFlag_on(true);
                    } else {
                        processPreselectedOptionsOff(i, tag);
                    }
                    tag.postInvalidate();
                }
            }
        } else if (mMode == LayoutMode.MULTIPLE_CHOICE) {
            if (mChildViews.get(onNotePosition) instanceof TagView) {
                TagView tag = (TagView) mChildViews.get(onNotePosition);
                if (tag.isFlag_on()) {
                    // setProfile(tagview, profile_normal);
                    processPreselectedOptions(position, tag, profile_normal, false);
                } else {
                    processPreselectedOptions(position, tag, profile_active, true);
                }
                tag.postInvalidate();
            }
        }
    }

    private int scopePosition(int in) {
        int n1 = in >= mChildViews.size() ? mChildViews.size() - 1 : in;
        return Math.max(n1, 0);
    }

    public final void performClick(int position_in_list, boolean isShortClick) {
        int pos = scopePosition(position_in_list);
        if (mChildViews.get(pos) instanceof TagView) {
            TagView cf = (TagView) mChildViews.get(pos);
            if (isShortClick) {
                mOnTagClickListener.onTagClick(pos, cf.getTagObject());
            } else {
                mOnTagClickListener.onTagLongClick(pos, cf.getTagObject());
            }
        }
        notifyInternal(pos);
    }

    public final void performFirstItemClick() {
        performClick(0, true);
    }

    public final void performLastItemClick() {
        performClick(mChildViews.size() - 1, true);
    }

    public final void performFirstItemLongClick() {
        performClick(0, false);
    }

    public final void performLastItemLongClick() {
        performClick(mChildViews.size() - 1, false);
    }


    private void onRemoveTag(int position) {
        if (position < 0 || position >= mChildViews.size()) {
            throw new RuntimeException("Illegal position!");
        }
        mChildViews.remove(position);
        removeViewAt(position);
        for (int i = position; i < mChildViews.size(); i++) {
            mChildViews.get(i).setTag(i);
        }
        // TODO, make removed view null?
    }

    private int[] onGetNewPosition(View view) {
        int left = view.getLeft();
        int top = view.getTop();
        int bestMatchLeft = mViewPos[(int) view.getTag() * 2];
        int bestMatchTop = mViewPos[(int) view.getTag() * 2 + 1];
        int tmpTopDis = Math.abs(top - bestMatchTop);
        for (int i = 0; i < mViewPos.length / 2; i++) {
            if (Math.abs(top - mViewPos[i * 2 + 1]) < tmpTopDis) {
                bestMatchTop = mViewPos[i * 2 + 1];
                tmpTopDis = Math.abs(top - mViewPos[i * 2 + 1]);
            }
        }
        int rowChildCount = 0;
        int tmpLeftDis = 0;
        for (int i = 0; i < mViewPos.length / 2; i++) {
            if (mViewPos[i * 2 + 1] == bestMatchTop) {
                if (rowChildCount == 0) {
                    bestMatchLeft = mViewPos[i * 2];
                    tmpLeftDis = Math.abs(left - bestMatchLeft);
                } else {
                    if (Math.abs(left - mViewPos[i * 2]) < tmpLeftDis) {
                        bestMatchLeft = mViewPos[i * 2];
                        tmpLeftDis = Math.abs(left - bestMatchLeft);
                    }
                }
                rowChildCount++;
            }
        }
        return new int[]{bestMatchLeft, bestMatchTop};
    }

    private int onGetCoordinateReferPos(int left, int top) {
        int pos = 0;
        for (int i = 0; i < mViewPos.length / 2; i++) {
            if (left == mViewPos[i * 2] && top == mViewPos[i * 2 + 1]) {
                pos = i;
            }
        }
        return pos;
    }

    private void onChangeView(View view, int newPos, int originPos) {
        mChildViews.remove(originPos);
        mChildViews.add(newPos, view);
        for (View child : mChildViews) {
            child.setTag(mChildViews.indexOf(child));
        }
        removeViewAt(originPos);
        addView(view, newPos);
    }

    private int ceilTagBorderWidth() {
        return (int) Math.ceil(mTagBorderWidth);
    }

    private class DragHelperCallBack extends ViewDragHelper.Callback {

        @Override
        public void onViewDragStateChanged(int state) {
            super.onViewDragStateChanged(state);
            mTagViewState = state;
        }

        @Override
        public boolean tryCaptureView(@NonNull View child, int pointerId) {
            requestDisallowInterceptTouchEvent(true);
            return mDragEnable;
        }

        @Override
        public int clampViewPositionHorizontal(@NonNull View child, int left, int dx) {
            final int leftX = getPaddingLeft();
            final int rightX = getWidth() - child.getWidth() - getPaddingRight();
            return Math.min(Math.max(left, leftX), rightX);
        }

        @Override
        public int clampViewPositionVertical(@NonNull View child, int top, int dy) {
            final int topY = getPaddingTop();
            final int bottomY = getHeight() - child.getHeight() - getPaddingBottom();
            return Math.min(Math.max(top, topY), bottomY);
        }

        @Override
        public int getViewHorizontalDragRange(@NonNull View child) {
            return getMeasuredWidth() - child.getMeasuredWidth();
        }

        @Override
        public int getViewVerticalDragRange(@NonNull View child) {
            return getMeasuredHeight() - child.getMeasuredHeight();
        }

        @Override
        public void onViewReleased(@NonNull View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
            requestDisallowInterceptTouchEvent(false);
            int[] pos = onGetNewPosition(releasedChild);
            int posRefer = onGetCoordinateReferPos(pos[0], pos[1]);
            onChangeView(releasedChild, posRefer, (int) releasedChild.getTag());
            mViewDragHelper.settleCapturedViewAt(pos[0], pos[1]);
            invalidate();
        }
    }

    /**
     * Get current drag view state.
     *
     * @return switchable state
     */
    public int getTagViewState() {
        return mTagViewState;
    }


    /**
     * Set tags
     *
     * @param tags the array of tags
     */
    public void setTags(List<TagItem> tags) {
        mTags = tags;
        onSetTag();
    }

    /**
     * Set tags
     *
     * @param tags tags in Tag array
     */
    public void setTags(TagItem... tags) {
        mTags = Arrays.asList(tags);
        onSetTag();
    }

    /**
     * Inserts the specified TagView into this ContainerLayout at the end.
     *
     * @param tag the string in tagview
     */
    public void addTag(TagItem tag) {
        addTag(tag, mChildViews.size());
    }

    /**
     * Inserts the specified TagView into this ContainerLayout at the specified location.
     * The TagView is inserted before the current element at the specified location.
     *
     * @param tag      the string
     * @param position the position
     */
    public void addTag(@NonNull TagItem tag, int position) {
        if (tag == null) {

        }
        onAddTag(tag, position);
        postInvalidate();
    }

    /**
     * Remove a TagView in specified position.
     *
     * @param position position of the tagview
     */
    public void removeTag(int position) {
        onRemoveTag(position);
        postInvalidate();
    }

    /**
     * Remove all TagViews.
     */
    public void removeAllTags() {
        mChildViews.clear();
        removeAllViews();
        postInvalidate();
    }

    /**
     * Set OnTagClickListener for TagView.
     *
     * @param listener the listener
     */
    public void setOnTagClickListener(TagView.OnTagClickListener listener) {
        mOnTagClickListener = listener;
    }

    /**
     * Get TagView text.
     *
     * @param position the position
     * @return the string to be returned
     */
    public String getTagText(int position) {
        return ((TagView) mChildViews.get(position)).getText();
    }

    /**
     * Get a string list for all tags in TagGroup.
     *
     * @return the list in string
     */
    public List<String> getTags() {
        List<String> tmpList = new ArrayList<>();
        for (View view : mChildViews) {
            if (view instanceof TagView) {
                tmpList.add(((TagView) view).getText());
            }
        }
        return tmpList;
    }

    /**
     * Set whether the child view can be dragged.
     *
     * @param enable enable to true
     */
    public final void setDragEnable(boolean enable) {
        this.mDragEnable = enable;
    }

    /**
     * Get current view is drag enable attribute.
     *
     * @return true or false
     */
    public final boolean getDragEnable() {
        return mDragEnable;
    }

    /**
     * Set vertical interval
     *
     * @param interval float for interval
     */
    public final void setVerticalInterval(float interval) {
        mVerticalInterval = (int) dp2px(getContext(), interval);
        postInvalidate();
    }

    /**
     * Get vertical interval in this view.
     *
     * @return height unit in hi
     */
    public int getVerticalInterval() {
        return mVerticalInterval;
    }

    /**
     * Set horizontal interval.
     *
     * @param interval width unit for horizontal
     */
    public void setHorizontalInterval(float interval) {
        mHorizontalInterval = (int) dp2px(getContext(), interval);
        postInvalidate();
    }

    /**
     * Get horizontal interval in this view.
     *
     * @return get the unit of width
     */
    public int getHorizontalInterval() {
        return mHorizontalInterval;
    }

    /**
     * Get TagGroup border width.
     *
     * @return get the unit of the border
     */
    public float getBorderWidth() {
        return mBorderWidth;
    }

    /**
     * Set TagGroup border width.
     *
     * @param width get the border of the width unit
     */
    public void setBorderWidth(float width) {
        this.mBorderWidth = dp2px(getContext(), width);
    }

    /**
     * Get TagGroup border radius.
     *
     * @return as it is
     */
    public float getBorderRadius() {
        return mBorderRadius;
    }

    /**
     * Set TagGroup border radius.
     *
     * @param radius as it is
     */
    public void setBorderRadius(float radius) {
        this.mBorderRadius = dp2px(getContext(), radius);
    }

    /**
     * Get TagGroup border color.
     *
     * @return as it is
     */
    public int getBorderColor() {
        return mBorderColor;
    }

    /**
     * Set TagGroup border color.
     *
     * @param color as it is
     */
    public void setBorderColor(int color) {
        this.mBorderColor = color;
    }

    /**
     * Get TagGroup background color.
     *
     * @return as it is
     */
    public int getBackgroundColor() {
        return mBackgroundColor;
    }

    /**
     * Set TagGroup background color.
     *
     * @param color as it is
     */
    public void setBackgroundColor(int color) {
        this.mBackgroundColor = color;
    }

    /**
     * Get container layout gravity.
     *
     * @return as it is
     */
    public int getGravity() {
        return mGravity;
    }

    /**
     * Set container layout gravity.
     *
     * @param gravity as it is
     */
    public void setGravity(int gravity) {
        this.mGravity = gravity;
    }

    /**
     * Get TagGroup ViewDragHelper sensitivity.
     *
     * @return as it is
     */
    public float getSensitivity() {
        return mSensitivity;
    }

    /**
     * Set TagGroup ViewDragHelper sensitivity.
     *
     * @param sensitivity as it is
     */
    public void setSensitivity(float sensitivity) {
        this.mSensitivity = sensitivity;
    }


    /**
     * Set the TagView text max length (must be more or big than 3).
     *
     * @param maxLength the max length of the text inside each tagvie.
     */
    public void setTagMaxLength(int maxLength) {
        mTagMaxLength = Math.max(maxLength, TAG_MIN_LENGTH);
    }

    /**
     * Set the application mode for this
     *
     * @param mode mode code
     */
    public final void setMode(LayoutMode mode) {
        mMode = mode;
        for (int i = 0; i < mChildViews.size(); i++) {
            if (mChildViews.get(i) instanceof TagView) {
                TagView d = (TagView) mChildViews.get(i);
                d.setMode(mode);
               /* if (mode == LayoutMode.SINGLE_CHOICE || mode == LayoutMode.MULTIPLE_CHOICE) {
                    d.setIsViewClickable(true);
                }*/
            }
        }
    }

    public final void setPreselectedTags(int[] list) {
        mPreselectedTags = list;
    }

    /**
     * A helper function that will able to set a single choice of the layout with the preset items as well as making up the preset items theme
     *
     * @param list                collection of positions
     * @param theme_on_reselected the theme pointer for the xml theme configurations
     */
    public final void setPreselectedTags(int[] list, int theme_on_reselected) {
        mPreselectedTags = list;
        profile_theme_perpetual_preset = onUpdateColorFactory(theme_on_reselected);
    }

    /**
     * given the SINGLE_CHOICE_OVERLAY_PRESET as foundation with all other theme configurations
     *
     * @param list                the preset item list
     * @param theme_hard_inactive the theme with inactive preset items
     * @param theme_hard_active   the theme with active preset items
     */
    public final void setPresetHardTags(int[] list, int theme_hard_inactive, int theme_hard_active) {
        mPreselectedTags = list;
        profile_theme_perpetual_preset = onUpdateColorFactory(theme_hard_inactive);
        profile_theme_preset_perpetual_active = onUpdateColorFactory(theme_hard_active);
        has_hard_layout_preset = true;
    }

    /**
     * Get TagView max length.
     *
     * @return the max length of the characters on a single line
     */
    public int getTagMaxLength() {
        return mTagMaxLength;
    }

    /**
     * Set TagView to use this theme when the click is active
     *
     * @param theme as it is
     */
    public void setThemeOnActive(int theme) {
        mThemeActive = theme;
        profile_active = onUpdateColorFactory(theme);
    }

    /**
     * Set TagView theme.
     *
     * @param theme as it is
     */
    public void setTheme(int theme) {
        mTheme = theme;
        profile_normal = onUpdateColorFactory(theme);
    }

    public void setPresetPerpetualTheme(int theme) {
        mThemePresetPerpetual = theme;
        profile_theme_perpetual_preset = onUpdateColorFactory(theme);
    }

    public void setThemePresetPerpetualActive(int theme) {
        mThemePresetPerpetualActive = theme;
        profile_theme_preset_perpetual_active = onUpdateColorFactory(theme);
    }

    /**
     * Get TagView theme.
     *
     * @return as it is
     */
    public int getTheme() {
        return mTheme;
    }

    /**
     * Get TagView is clickable.
     *
     * @return as it is

    public boolean getIsTagViewClickable() {
    return isTagViewClickable;
    }
     */
    /**
     * Set TagView is clickable
     *
     * @param clickable as it is

    public void setIsTagViewClickable(boolean clickable) {
    this.isTagViewClickable = clickable;
    }
     */
    /**
     * Get TagView border width.
     *
     * @return as it is
     */
    public float getTagBorderWidth() {
        return mTagBorderWidth;
    }

    /**
     * Set TagView border width.
     *
     * @param width as it is
     */
    public void setTagBorderWidth(float width) {
        this.mTagBorderWidth = dp2px(getContext(), width);
    }

    /**
     * Get TagView border radius.
     *
     * @return as it is
     */
    public float getTagBorderRadius() {
        return mTagBorderRadius;
    }

    /**
     * Set TagView border radius.
     *
     * @param radius as it is
     */
    public void setTagBorderRadius(float radius) {
        this.mTagBorderRadius = dp2px(getContext(), radius);
    }

    /**
     * Get TagView text size.
     *
     * @return as it is
     */
    public float getTextSize() {
        return mTextSize;
    }

    /**
     * Set TagView text size.
     *
     * @param size as it is
     */
    public void setTextSize(float size) {
        this.mTextSize = sp2px(getContext(), size);
    }

    /**
     * Get TagView horizontal padding.
     *
     * @return as it is
     */
    public int getTagHorizontalPadding() {
        return mTagHorizontalPadding;
    }

    /**
     * Set TagView horizontal padding.
     *
     * @param padding as it is
     */
    public void setTagHorizontalPadding(int padding) {
        int ceilWidth = ceilTagBorderWidth();
        this.mTagHorizontalPadding = Math.max(padding, ceilWidth);
    }

    /**
     * Get TagView vertical padding.
     *
     * @return as it is
     */
    public int getTagVerticalPadding() {
        return mTagVerticalPadding;
    }

    /**
     * Set TagView vertical padding.
     *
     * @param padding as it is
     */
    public void setTagVerticalPadding(int padding) {
        int ceilWidth = ceilTagBorderWidth();
        this.mTagVerticalPadding = Math.max(padding, ceilWidth);
    }

    /**
     * Get TagView border color.
     *
     * @return as it is
     */
    public int getTagBorderColor() {
        return mTagBorderColor;
    }

    /**
     * Set TagView border color.
     *
     * @param color as it is
     */
    public void setTagBorderColor(int color) {
        this.mTagBorderColor = color;
    }

    /**
     * Get TagView background color.
     *
     * @return as it is
     */
    public int getTagBackgroundColor() {
        return mTagBackgroundColor;
    }

    /**
     * Set TagView background color.
     *
     * @param color as it is
     */
    public void setTagBackgroundColor(int color) {
        this.mTagBackgroundColor = color;
    }


    public void setTagBackgroundColorStateList(ColorStateList colorStateList) {
        this.mTagBackgroundColorStateList = colorStateList;
    }

    /**
     * Get TagView text color.
     *
     * @return as it is
     */
    public int getTextColor() {
        return mTextColor;
    }


    /**
     * Get TagView typeface.
     *
     * @return as it is
     */
    public Typeface getTypeface() {
        return mTypeface;
    }

    /**
     * Set TagView typeface.
     *
     * @param typeface as it is
     */
    public void setTypeface(Typeface typeface) {
        this.mTypeface = typeface;
    }


    /**
     * Set TagView text color.
     *
     * @param color as it is
     */
    public void setTextColor(int color) {
        this.mTextColor = color;
    }

    public float dp2px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return dp * scale + 0.5f;
    }

    public float sp2px(Context context, float sp) {
        final float scale = context.getResources().getDisplayMetrics().scaledDensity;
        return sp * scale;
    }

    public ArrayList<String> getSelectedItems() {
        ArrayList<String> lit = new ArrayList<>();
        for (int i = 0; i < mChildViews.size(); i++) {
            if (mChildViews.get(i) instanceof TagView) {
                TagView d = (TagView) mChildViews.get(i);
                if (d.isFlag_on()) {
                    lit.add(d.getText());
                }
            }
        }
        return lit;
    }

    public ArrayList<Integer> getSelectedOrders() {
        ArrayList<Integer> lit = new ArrayList<>();
        for (int i = 0; i < mChildViews.size(); i++) {
            if (mChildViews.get(i) instanceof TagView) {
                TagView d = (TagView) mChildViews.get(i);
                if (d.isFlag_on()) {
                    lit.add(i);
                }
            }
        }
        return lit;
    }
}
