package com.image;


import android.graphics.*;
import android.view.View;

/**
 * This class is used by CropImage to display a highlighted cropping rectangle
 * overlayed with the image. There are two coordinate spaces in use. One is
 * image, another is screen. computeLayout() uses mMatrix to map from image
 *  space to screen space.
 */
public class HighlightView {

    /** log tag. */
    @SuppressWarnings("unused")
    private static final String TAG = "HighlightView";
    
    /** The View displaying the image. */
    private View mContext;

    
    /** be focused ? */
    public boolean mIsFocused;
    /** hidden ? */
    boolean mHidden;
    public static final int PHOTO_CROP_SIZE = 300;
    /**
     * constructor.
     * @param ctx application context.
     */
    public HighlightView(View ctx) {
        mContext = ctx;
    }


    /**
     * whether is focused.
     * @return focused return true
     */
    public boolean hasFocus() {
        return mIsFocused;
    }

    /**
     * set focused or not.
     * @param f focus true, or false.
     */
    public void setFocus(boolean f) {
        mIsFocused = f;
    }

    /**
     * set be hidden or not.
     * @param hidden hidden true
     */
    public void setHidden(boolean hidden) {
        mHidden = hidden;
    }

    /**
     * draw the view .
     * @param canvas Canvas
     */
    public void draw(Canvas canvas) {
        if (mHidden) {
            return;
        }
        canvas.save();
        Path path = new Path();
        if (!hasFocus()) {
            mOutlinePaint.setColor(0xFF000000); // SUPPRESS CHECKSTYLE : magic number
            canvas.drawRect(mDrawRect, mOutlinePaint);
        } else {
            Rect viewDrawingRect = new Rect();
            mContext.getDrawingRect(viewDrawingRect);
            System.out.println("=====mCropRect left="+mCropRect.left+"top="+mCropRect.top+"mDrawRect left="+mDrawRect.left+"top="+mDrawRect.top);
            
            path.addCircle(CropImage.mScreenWidth/2,
            		CropImage.mScreenHeight/2,
                           PHOTO_CROP_SIZE,
                           Path.Direction.CW);
            mOutlinePaint.setColor(0xFFEF04D6);
            canvas.clipPath(path, Region.Op.DIFFERENCE);
            canvas.drawRect(viewDrawingRect,
                    hasFocus() ? mFocusPaint : mNoFocusPaint);

            canvas.restore();
            canvas.drawPath(path, mOutlinePaint);

        }
    }

    /**
     * @param mode ModifyMode
     */
    public void setMode(ModifyMode mode) {
        if (mode != mMode) {
            mMode = mode;
            mContext.invalidate();
        }
    }





    /**
     * Returns the cropping rectangle in image space.
     * @return Crop rect
     */
    public Rect getCropRect() {
        return new Rect((int) mCropRect.left, (int) mCropRect.top,
                        (int) mCropRect.right, (int) mCropRect.bottom);
    }
    
    public Rect getCalculateRect() {
        return mDrawRect;
    }

    /**
     * Maps the cropping rectangle from image space to screen space.
     * @return Rect
     */
    private Rect computeLayout() {
        RectF r = new RectF(mCropRect.left, mCropRect.top,
                            mCropRect.right, mCropRect.bottom);
        mMatrix.mapRect(r);
        return new Rect(Math.round(r.left), Math.round(r.top),
                        Math.round(r.right), Math.round(r.bottom));
    }

    /**
     * invalidate.
     */
    public void invalidate() {
        mDrawRect = computeLayout();
    }
    
    
    void moveBy(float dx, float dy) {
        Rect invalRect = new Rect(mDrawRect);

        mCropRect.offset(dx, dy);

        // Put the cropping rectangle inside image rectangle.
        mCropRect.offset(
                Math.max(0, mImageRect.left - mCropRect.left),
                Math.max(0, mImageRect.top  - mCropRect.top));

        mCropRect.offset(
                Math.min(0, mImageRect.right  - mCropRect.right),
                Math.min(0, mImageRect.bottom - mCropRect.bottom));

        mDrawRect = computeLayout();
        invalRect.union(mDrawRect);
        invalRect.inset(-10, -10); // SUPPRESS CHECKSTYLE : magic number
        mContext.invalidate(invalRect);
    }
    
    
    public void handleMotion(float dx, float dy) {
    	  Rect r = computeLayout();
    	  moveBy(dx * (mCropRect.width() / r.width()),
                  dy * (mCropRect.height() / r.height()));
    }
    /**
     * set up.
     * @param m Matrix
     * @param imageRect Rect
     * @param cropRect RectF
     * @param circle boolean
     * @param maintainAspectRatio boolean
     */
    public void setup(Matrix m, Rect imageRect, RectF cropRect, boolean circle,
                      boolean maintainAspectRatio) {

        mMatrix = new Matrix(m);

        mCropRect = cropRect;
        mImageRect = new RectF(imageRect);

        mDrawRect = computeLayout();

        mFocusPaint.setARGB(125, 50, 50, 50); // SUPPRESS CHECKSTYLE
        mNoFocusPaint.setARGB(125, 50, 50, 50); // SUPPRESS CHECKSTYLE
        mOutlinePaint.setStrokeWidth(3F); // SUPPRESS CHECKSTYLE
        mOutlinePaint.setStyle(Paint.Style.STROKE);
        mOutlinePaint.setAntiAlias(true);

        mMode = ModifyMode.None;
    }
    /**
     * Modify mode ,moing, growing, or none.
     */
    public enum ModifyMode { None, Move, Grow }

    /** current modify mode. */
    private ModifyMode mMode = ModifyMode.None;

    /** in screen space. */
    public Rect mDrawRect;
    /** in image space. */
    private RectF mImageRect;
    /** in image space. */
    public RectF mCropRect;
    /** Matrix util. */
    public Matrix mMatrix;

    /** Focus Paint. */
    private final Paint mFocusPaint = new Paint();
    /** No Focus Paint. */
    private final Paint mNoFocusPaint = new Paint();
    /** Outline Paint. */
    private final Paint mOutlinePaint = new Paint();
}
