package com.image;
/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.ImageView;

public abstract class ImageViewTouchBase extends ImageView {

    /** log tag. */
    private static final String TAG = "ImageViewTouchBase";

    /**
     * This is the base transformation which is used to show the image
     * initially. The current computation for this shows the image in it's
     * entirety, letterboxing as needed. One could choose to show the image as
     * cropped instead.
     * 
     * This matrix is recomputed when we go from the thumbnail image to the full
     * size image.
     */
    protected Matrix mBaseMatrix = new Matrix();

    /**
     * This is the supplementary transformation which reflects what the user has
     * done in terms of zooming and panning.
     * 
     * This matrix remains the same when we go from the thumbnail image to the
     * full size image.
     */
    protected Matrix mSuppMatrix = new Matrix();

    /**
     * This is the final matrix which is computed as the concatentation of the
     * base matrix and the supplementary matrix.
     */
    private final Matrix mDisplayMatrix = new Matrix();

    /**Temporary buffer used for getting the values out of a matrix.*/
    private final float[] mMatrixValues = new float[9]; // SUPPRESS CHECKSTYLE : magic number

    /** The current bitmap being displayed. */
    protected final RotateBitmap mBitmapDisplayed = new RotateBitmap(null);

    /** width.  */
    int mThisWidth = -1;
    /** height. */
    int mThisHeight = -1;

    /** max zoom . */
    float mMaxZoom;

    /**
     * ImageViewTouchBase will pass a Bitmap to the Recycler if it has finished
     * its use of that Bitmap.
     */
    public interface Recycler {
        /**
         * recyle bitmap.
         * @param b bitmap
         */
        void recycle(Bitmap b);
    }
    /**
     * set Recycler.
     * @param r Recycler
     */
    public void setRecycler(Recycler r) {
        mRecycler = r;
    }
    /** Recycler. */
    private Recycler mRecycler;

    @Override
    protected void onLayout(boolean changed, int left, int top,
                            int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mThisWidth = right - left;
        mThisHeight = bottom - top;
        Runnable r = mOnLayoutRunnable;
        if (r != null) {
            mOnLayoutRunnable = null;
            r.run();
        }
        if (mBitmapDisplayed.getBitmap() != null) {
            getProperBaseMatrix(mBitmapDisplayed, mBaseMatrix);
            setImageMatrix(getImageViewMatrix());
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (getScale() > 1.0f) {
                // If we're zoomed in, pressing Back jumps out to show the
                // entire image, otherwise Back returns the user to the gallery.
                zoomTo(1.0f);
                return true;
            }
        }
        return super.onKeyUp(keyCode, event);
    }
    /** work handler. */
    protected Handler mHandler = new Handler();

    @Override
    public void setImageBitmap(Bitmap bitmap) {
        setImageBitmap(bitmap, 0);
    }

    /**
     * set image bitmap.
     * @param bitmap the visible bitmap.
     * @param rotation rotation.
     */
    private void setImageBitmap(Bitmap bitmap, int rotation) {
        super.setImageBitmap(bitmap);
        Drawable d = getDrawable();
        if (d != null) {
            d.setDither(true);
        }

        Bitmap old = mBitmapDisplayed.getBitmap();
        mBitmapDisplayed.setBitmap(bitmap);
        mBitmapDisplayed.setRotation(rotation);

        if (old != null && old != bitmap && mRecycler != null) {
            mRecycler.recycle(old);
        }
    }

    /**
     * set the bitmap.
     */
    public void clear() {
        setImageBitmapResetBase(null, true);
    }

    /** layout work runnable. */
    private Runnable mOnLayoutRunnable = null;


    /**
     * This function changes bitmap, reset base matrix according to the size
     * of the bitmap, and optionally reset the supplementary matrix.
     * 
     * @param bitmap the bitmap 
     * @param resetSupp  rese supplementary matrix.
     */
    public void setImageBitmapResetBase(final Bitmap bitmap,
            final boolean resetSupp) {
        setImageRotateBitmapResetBase(new RotateBitmap(bitmap), resetSupp);
    }

    /**
     * This function changes bitmap, reset base matrix according to the size
     * of the bitmap, and optionally reset the supplementary matrix.
     * 
     * @param bitmap the bitmap 
     * @param resetSupp  rese supplementary matrix.
     */
    public void setImageRotateBitmapResetBase(final RotateBitmap bitmap,
            final boolean resetSupp) {
        final int viewWidth = getWidth();

        if (viewWidth <= 0)  {
            mOnLayoutRunnable = new Runnable() {
                public void run() {
                    setImageRotateBitmapResetBase(bitmap, resetSupp);
                }
            };
            return;
        }

        if (bitmap.getBitmap() != null) {
            getProperBaseMatrix(bitmap, mBaseMatrix);
            setImageBitmap(bitmap.getBitmap(), bitmap.getRotation());
        } else {
            mBaseMatrix.reset();
            setImageBitmap(null);
        }

        if (resetSupp) {
            mSuppMatrix.reset();
        }
        setImageMatrix(getImageViewMatrix());
        mMaxZoom = maxZoom();
    }

    /**
     * Center as much as possible in one or both axis. Centering is defined as
     * follows: if the image is scaled down below the view's dimensions then
     * center it (literally). If the image is scaled larger than the view and is
     * translated out of view then translate it back into view (i.e. eliminate
     * black bars).
     * 
     * @param horizontal center horizontal
     * @param vertical center vertical
     */
    public void center(boolean horizontal, boolean vertical) {
        if (mBitmapDisplayed.getBitmap() == null) {
            return;
        }

        Matrix m = getImageViewMatrix();

        RectF rect = new RectF(0, 0,
                mBitmapDisplayed.getBitmap().getWidth(),
                mBitmapDisplayed.getBitmap().getHeight());

        m.mapRect(rect);

        float height = rect.height();
        float width  = rect.width();

        float deltaX = 0, deltaY = 0;

        if (vertical) {
            int viewHeight = getHeight();
            if (height < viewHeight) {
                deltaY = (viewHeight - height) / 2 - rect.top;
            } else if (rect.top > 0) {
                deltaY = -rect.top;
            } else if (rect.bottom < viewHeight) {
                deltaY = getHeight() - rect.bottom;
            }
        }

        if (horizontal) {
            int viewWidth = getWidth();
            if (width < viewWidth) {
                deltaX = (viewWidth - width) / 2 - rect.left;
            } else if (rect.left > 0) {
                deltaX = -rect.left;
            } else if (rect.right < viewWidth) {
                deltaX = viewWidth - rect.right;
            }
        }

        postTranslate(deltaX, deltaY);
        setImageMatrix(getImageViewMatrix());
    }
    
    /**
     * constructor.
     * @param context Context
     */
    public ImageViewTouchBase(Context context) {
        super(context);
        init();
    }
    /**
     * constructor.
     * @param context Context
     * @param attrs AttributeSet
     */
    public ImageViewTouchBase(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    
    /**
     * init.
     */
    private void init() {
        setScaleType(ScaleType.MATRIX);
    }
    
    /**
     * get which matrix value.
     * @param matrix Matrix
     * @param whichValue whichValue
     * @return value
     */
    protected float getValue(Matrix matrix, int whichValue) {
        matrix.getValues(mMatrixValues);
        return mMatrixValues[whichValue];
    }

    /**
     * Get the scale factor out of the matrix.
     * @param matrix Matrix
     * @return value
     */
    protected float getScale(Matrix matrix) {
        return getValue(matrix, Matrix.MSCALE_X);
    }

    /**
     * Get the scale factor out of the matrix.
     * @return value
     */
    public float getScale() {
        return getScale(mSuppMatrix);
    }

    /**
     * Setup the base matrix so that the image is centered and scaled properly.
     * @param bitmap RotateBitmap
     * @param matrix Matrix
     */
    private void getProperBaseMatrix(RotateBitmap bitmap, Matrix matrix) {
        float viewWidth = getWidth();
        float viewHeight = getHeight();

        float w = bitmap.getWidth();
        float h = bitmap.getHeight();
        matrix.reset();

        // We limit up-scaling to 3x otherwise the result may look bad if it's
        // a small icon.
        float widthScale = Math.min(viewWidth / w, 3.0f); // SUPPRESS CHECKSTYLE :magic number
        float heightScale = Math.min(viewHeight / h, 3.0f); // SUPPRESS CHECKSTYLE :magic number
        float scale = Math.min(widthScale, heightScale);

        matrix.postConcat(bitmap.getRotateMatrix());
        matrix.postScale(scale, scale);

        matrix.postTranslate(
                (viewWidth  - w * scale) / 2F,
                (viewHeight - h * scale) / 2F);
    }

    
    public Bitmap getDisplayBitmap() {
    	return mBitmapDisplayed.getBitmap();
    }
    /**
     * Combine the base matrix and the supp matrix to make the final matrix.
     * @return getImageViewMatrix
     */
    public Matrix getImageViewMatrix() {
        // The final matrix is computed as the concatentation of the base matrix
        // and the supplementary matrix.
        mDisplayMatrix.set(mBaseMatrix);
        mDisplayMatrix.postConcat(mSuppMatrix);
        return mDisplayMatrix;
    }
    
    /**
     * SCALE_RATE
     */
    static final float SCALE_RATE = 1.25F;

    /**
     * Sets the maximum zoom, which is a scale relative to the base matrix. It
     * is calculated to show the image at 400% zoom regardless of screen or
     * image orientation. If in the future we decode the full 3 megapixel image,
     * rather than the current 1024x768, this should be changed down to 200%.
     * 
     * @return max zoom.
     */
    protected float maxZoom() {
        if (mBitmapDisplayed.getBitmap() == null) {
            return 1F;
        }

        float fw = (float) mBitmapDisplayed.getWidth()  / (float) mThisWidth;
        float fh = (float) mBitmapDisplayed.getHeight() / (float) mThisHeight;
        float max = Math.max(fw, fh) * 4; // SUPPRESS CHECKSTYLE : magic number
        return max;
    }
    
    public boolean isNeedDrag(){
    	return true;
    }
    
    public void PanX(float distanceX, float distanceY){
   	 float dx = 0;
        float dy = 0;
        
        if (distanceX > 0 && !isRightBound() || 
                distanceX < 0 && !isLeftBound()) {
            dx = mesaureDistanceX(distanceX);
        }
        if (distanceY > 0 && !isBottomBound() || 
                distanceY < 0 && !isTopBound()) {
            dy = mesaureDistanceY(distanceY);
        }
        
        if (dx != 0 || dy != 0) {
            panBy(-dx, -dy);
        }
        
   }
    
    private float mesaureDistanceX(float dx) {
        float distanceX = 0;
        
        Matrix m = getImageViewMatrix();
        float transX = getValue(getImageMatrix(), Matrix.MTRANS_X);

        RectF rect = new RectF(0, 0,
                mBitmapDisplayed.getBitmap().getWidth(),
                mBitmapDisplayed.getBitmap().getHeight());
        m.mapRect(rect);
        float bmpWidth  = rect.width();
        int viewWidth = getWidth();
        
        if (dx < 0) {
            if (transX - dx < 0) {
                distanceX = dx;
            } else {
                distanceX = transX;
            }
        } else {
            if (viewWidth - transX + dx < bmpWidth) {
                distanceX = dx;
            } else {
                distanceX = bmpWidth - viewWidth + transX;
            }
        }
        
//        Log.v("image", "mesaureDistanceX" + " bmpWidth:" + bmpWidth + 
//                " viewWidth:" + viewWidth + " transX:" + transX + 
//                " dx:" + dx + " x:" + distanceX);
        
        return distanceX;
    }
    
    private float mesaureDistanceY(float dy) {
        float y = 0;
        
        Matrix m = getImageViewMatrix();
        float transY = getValue(getImageMatrix(), Matrix.MTRANS_Y);

        RectF rect = new RectF(0, 0,
                mBitmapDisplayed.getBitmap().getWidth(),
                mBitmapDisplayed.getBitmap().getHeight());
        m.mapRect(rect);
        float bmpHeight  = rect.height();
        int viewHeight = getHeight();
        
        if (dy < 0) {
            if (transY - dy < 0) {
                y = dy;
            } else {
                y = transY;
            }
        } else {
            if (viewHeight - transY + dy < bmpHeight) {
                y = dy;
            } else {
                y = bmpHeight - viewHeight + transY;
            }
        }
        
//        Log.v("image", "mesaureDistanceY" + " bmpHeight:" + bmpHeight + 
//                " viewHeight:" + viewHeight + " transY:" + transY + 
//                " dy:" + dy + " y:" + y);
        
        return y;
    }
    
    public boolean isLeftBound() {
        boolean isBound = false;
        
        Matrix m = getImageViewMatrix();
        float transX = getValue(getImageMatrix(), Matrix.MTRANS_X);

        RectF rect = new RectF(0, 0,
                mBitmapDisplayed.getBitmap().getWidth(),
                mBitmapDisplayed.getBitmap().getHeight());
        m.mapRect(rect);
        float bmpWidth  = rect.width();
        int viewWidth = getWidth() - getPaddingLeft() - getPaddingRight();
        
        if (bmpWidth <= viewWidth) {
            isBound = true;
        } else if (transX >= 0) {
            isBound = true;
        }
        
//        Log.v("image", "isLeftBound" + " bmpWidth:" + bmpWidth + 
//                " viewWidth:" + viewWidth + " transX:" + transX + 
//                " isBound:" + isBound);
        
        return isBound;
    }
    
    public boolean isRightBound() {
        boolean isBound = false;
        
        Matrix m = getImageViewMatrix();
        float transX = getValue(getImageMatrix(), Matrix.MTRANS_X);

        RectF rect = new RectF(0, 0,
                mBitmapDisplayed.getBitmap().getWidth(),
                mBitmapDisplayed.getBitmap().getHeight());
        m.mapRect(rect);
        float bmpWidth  = rect.width();
        int viewWidth = getWidth();
        
        if (bmpWidth <= viewWidth) {
            isBound = true;
        } else if (transX <= (viewWidth - bmpWidth)) {
            isBound = true;
        }
        
//        Log.v("image", "isRightBound" + " bmpWidth:" + bmpWidth + 
//                " viewWidth:" + viewWidth + " transX:" + transX + 
//                " isBound:" + isBound);
        
        return isBound;
    }
    
    public boolean isTopBound() {
        boolean isBound = false;
        
        Matrix m = getImageViewMatrix();
        float transY = getValue(getImageMatrix(), Matrix.MTRANS_Y);

        RectF rect = new RectF(0, 0,
                mBitmapDisplayed.getBitmap().getWidth(),
                mBitmapDisplayed.getBitmap().getHeight());
        m.mapRect(rect);
        float bmpHeight  = rect.height();
        int viewHeight = getWidth();
        
        if (bmpHeight <= viewHeight) {
            isBound = true;
        } else if (transY >= 0) {
            isBound = true;
        }
        
//        Log.v("image", "isTopBound" + " bmpHeight:" + bmpHeight + 
//                " viewHeight:" + viewHeight + " transY:" + transY + 
//                " isBound:" + isBound);
        
        return isBound;
    }
    
    public boolean isBottomBound() {
        boolean isBound = false;
        
        Matrix m = getImageViewMatrix();
        float transY = getValue(getImageMatrix(), Matrix.MTRANS_Y);

        RectF rect = new RectF(0, 0,
                mBitmapDisplayed.getBitmap().getWidth(),
                mBitmapDisplayed.getBitmap().getHeight());
        m.mapRect(rect);
        float bmpHeight  = rect.height();
        int viewHeight = getHeight();
        
        if (bmpHeight <= viewHeight) {
            isBound = true;
        } else if (transY <= (viewHeight - bmpHeight)) {
            isBound = true;
        }
        
//        Log.v("image", "isBottomBound" + " bmpHeight:" + bmpHeight + 
//                " viewHeight:" + viewHeight + " transY:" + transY + 
//                " isBound:" + isBound);
        
        return isBound;
    }
    
    
    
    /**
     * zoom to scale rate.
     * @param scale  value
     * @param centerX center x
     * @param centerY center y
     */
    public void zoomTo(float scale, float centerX, float centerY) {
        if (scale > mMaxZoom) {
            scale = mMaxZoom;
        }

        float oldScale = getScale();
        float deltaScale = scale / oldScale;

        mSuppMatrix.postScale(deltaScale, deltaScale, centerX, centerY);
        setImageMatrix(getImageViewMatrix());
        center(true, true);
    }

    /**
     * zoom to scale rate.
     * @param scale scale
     * @param centerX centerX
     * @param centerY centerY
     * @param durationMs durationMs
     */
    protected void zoomTo(final float scale, final float centerX,
                          final float centerY, final float durationMs) {
        final float incrementPerMs = (scale - getScale()) / durationMs;
        final float oldScale = getScale();
        final long startTime = System.currentTimeMillis();

        mHandler.post(new Runnable() {
            public void run() {
                long now = System.currentTimeMillis();
                float currentMs = Math.min(durationMs, now - startTime);
                float target = oldScale + (incrementPerMs * currentMs);
                zoomTo(target, centerX, centerY);

                if (currentMs < durationMs) {
                    mHandler.post(this);
                }
            }
        });
    }
    
    /**
     * zoom to scale rate.
     * @param scale scale
     */
    public void zoomTo(float scale) {
        float cx = getWidth() / 2F;
        float cy = getHeight() / 2F;

        zoomTo(scale, cx, cy);
    }

    /**
     * zoom to point.
     * @param scale scale
     * @param pointX pointX
     * @param pointY pointY
     */
    protected void zoomToPoint(float scale, float pointX, float pointY) {
        float cx = getWidth() / 2F;
        float cy = getHeight() / 2F;

        panBy(cx - pointX, cy - pointY);
        zoomTo(scale, cx, cy);
    }

    /**
     * zoom in.
     */
    protected void zoomIn() {
        zoomIn(SCALE_RATE);
    }

    /**
     * zoom out.
     */
    protected void zoomOut() {
        zoomOut(SCALE_RATE);
    }

    /**
     * zoom in.
     * @param rate rate
     */
    protected void zoomIn(float rate) {
        if (getScale() >= mMaxZoom) {
            return;     // Don't let the user zoom into the molecular level.
        }
        if (mBitmapDisplayed.getBitmap() == null) {
            return;
        }

        float cx = getWidth() / 2F;
        float cy = getHeight() / 2F;

        mSuppMatrix.postScale(rate, rate, cx, cy);
        setImageMatrix(getImageViewMatrix());
    }

    /**
     * zoom out.
     * @param rate rate
     */
    protected void zoomOut(float rate) {
        if (mBitmapDisplayed.getBitmap() == null) {
            return;
        }

        float cx = getWidth() / 2F;
        float cy = getHeight() / 2F;

        // Zoom out to at most 1x.
        Matrix tmp = new Matrix(mSuppMatrix);
        tmp.postScale(1F / rate, 1F / rate, cx, cy);

        if (getScale(tmp) < 1F) {
            mSuppMatrix.setScale(1F, 1F, cx, cy);
        } else {
            mSuppMatrix.postScale(1F / rate, 1F / rate, cx, cy);
        }
        setImageMatrix(getImageViewMatrix());
        center(true, true);
    }

    /**
     * Translate delta x and delta y.
     * @param dx delta
     * @param dy delta
     */
    protected void postTranslate(float dx, float dy) {
        mSuppMatrix.postTranslate(dx, dy);
    }
    
    /**
     * panBy delta x and delta y.
     * @param dx delta
     * @param dy delta
     */
    public void panBy(float dx, float dy) {
        postTranslate(dx, dy);
        setImageMatrix(getImageViewMatrix());
    }
}
