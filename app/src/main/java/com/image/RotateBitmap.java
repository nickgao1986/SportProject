package com.image;

import android.graphics.Bitmap;
import android.graphics.Matrix;

/**
 * RotateBitmap.
 */
public class RotateBitmap {
    /** log tag. */
    public static final String TAG = "RotateBitmap";
    /** the real bigmap. */
    private Bitmap mBitmap;
    /** roation degree. */
    private int mRotation;

    /**
     * constructor.
     * @param bitmap the real bitmap.
     */
    public RotateBitmap(Bitmap bitmap) {
        mBitmap = bitmap;
        mRotation = 0;
    }

    /**
     * constructor.
     * @param bitmap the bitmap
     * @param rotation init rotation
     */
    public RotateBitmap(Bitmap bitmap, int rotation) {
        mBitmap = bitmap;
        mRotation = rotation % 360; // SUPPRESS CHECKSTYLE : magic number
    }
    /**
     * setRotation.
     * @param rotation rotation
     */
    public void setRotation(int rotation) {
        mRotation = rotation;
    }

    /**
     * getRotation.
     * @return mRotation
     */
    public int getRotation() {
        return mRotation;
    }

    /**
     * get the delegate bitmap.
     * @return Bitmap
     */
    public Bitmap getBitmap() {
        return mBitmap;
    }

    /**
     * set bitmap.
     * @param bitmap bitmap
     */
    public void setBitmap(Bitmap bitmap) {
        mBitmap = bitmap;
    }

    /**
     * get rotate matrix.
     * @return Matrix
     */
    public Matrix getRotateMatrix() {
        // By default this is an identity matrix.
        Matrix matrix = new Matrix();
        if (mRotation != 0) {
            // We want to do the rotation at origin, but since the bounding
            // rectangle will be changed after rotation, so the delta values
            // are based on old & new width/height respectively.
            int cx = mBitmap.getWidth() / 2;
            int cy = mBitmap.getHeight() / 2;
            matrix.preTranslate(-cx, -cy);
            matrix.postRotate(mRotation);
            matrix.postTranslate(getWidth() / 2f, getHeight() / 2f);
        }
        return matrix;
    }

    /**
     * isOrientationChanged.
     * @return isOrientationChanged
     */
    public boolean isOrientationChanged() {
        return (mRotation / 90) % 2 != 0; // SUPPRESS CHECKSTYLE : magic number
    }

    /**
     * get height.
     * @return the height after rotated.
     */
    public int getHeight() {
        if (isOrientationChanged()) {
            return mBitmap.getWidth();
        } else {
            return mBitmap.getHeight();
        }
    }

    /**
     * get width.
     * @return the width after rotated.
     */
    public int getWidth() {
        if (isOrientationChanged()) {
            return mBitmap.getHeight();
        } else {
            return mBitmap.getWidth();
        }
    }

    /**
     * recycle the bitmap.
     */
    public void recycle() {
        if (mBitmap != null) {
            mBitmap.recycle();
            mBitmap = null;
        }
    }
}