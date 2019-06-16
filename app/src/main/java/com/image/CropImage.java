package com.image;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.*;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;
import com.imooc.sport.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.CountDownLatch;

public class CropImage extends Activity {
	private static final String TAG = "CropImage";

	private Bitmap.CompressFormat mOutputFormat = Bitmap.CompressFormat.JPEG; // only
																				// used
																				// with
																				// mSaveUri
	private Uri mSaveUri = null;
	private int mAspectX;
	private int mAspectY;
	private boolean mCircleCrop = true;
	private final Handler mHandler = new Handler();

	/**
	 * These options specifiy the output image size and whether we should scale
	 * the output to fit it (or just crop it).
	 */
	private int mOutputX, mOutputY;
	private boolean mScale;
	private boolean mScaleUp = true;
	boolean mSaving; //
	private CropImageView mImageView;
	private ContentResolver mContentResolver;
	private Bitmap mBitmap;
	HighlightView mCrop;
	public static int mScreenWidth = 0;
	public static int mScreenHeight = 0;

	/**
	 * UNCONSTRAINED.
	 */
	static final int UNCONSTRAINED = -1;
	public static final int CROP_PHOTO_FINISHED = 5;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		mContentResolver = getContentResolver();

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.pedo_cropimage);

		mImageView = (CropImageView) findViewById(R.id.image);
		mImageView.setDrawingCacheEnabled(true);

		Intent intent = getIntent();
		Bundle extras = intent.getExtras();

		if (extras != null) {
			if (extras.getString("circleCrop") != null) {
				mCircleCrop = true;
				mAspectX = 1;
				mAspectY = 1;
			}
			mSaveUri = (Uri) extras.getParcelable(MediaStore.EXTRA_OUTPUT);
			if (mSaveUri != null) {
				String outputFormatString = extras.getString("outputFormat");
				if (outputFormatString != null) {
					mOutputFormat = Bitmap.CompressFormat
							.valueOf(outputFormatString);
				}
			}
			mBitmap = (Bitmap) extras.getParcelable("data");
			mAspectX = extras.getInt("aspectX");
			mAspectY = extras.getInt("aspectY");
			mOutputX = extras.getInt("outputX");
			mOutputY = extras.getInt("outputY");
			mScale = extras.getBoolean("scale", true);
			mScaleUp = extras.getBoolean("scaleUpIfNeeded", true);
		}

		if (mBitmap == null) {
			Uri target = intent.getData();
			String targetFile = "";
			int orientation = 0;
			if (target.getScheme().equals("content")) {
				String[] proj = { Media.DATA,
						Media.ORIENTATION };
				Cursor cursor = managedQuery(target, proj, null, null, null);
				if (cursor != null) {
					int columnIndex = cursor
							.getColumnIndexOrThrow(Media.DATA);
					cursor.moveToFirst();
					targetFile = cursor.getString(columnIndex);
					orientation = cursor.getInt(cursor
							.getColumnIndexOrThrow(Media.ORIENTATION));
					cursor.close();
				}
			} else if (target.getScheme().equals("file")) {
				targetFile = target.getPath();
				String[] projection = new String[] { Media.ORIENTATION };
				String selection = Media.DATA + "=?";
				String[] selectionArgs = new String[] { target.getPath() };
				Cursor c = Media.query(getContentResolver(),
						Media.EXTERNAL_CONTENT_URI, projection, selection,
						selectionArgs, null);
				if (c == null || c.getCount() == 0) {
					c = Media.query(getContentResolver(),
							Media.INTERNAL_CONTENT_URI, projection, selection,
							selectionArgs, null);
				}
				if (c != null && c.getCount() != 0) {
					c.moveToFirst();
					orientation = c.getInt(c
							.getColumnIndexOrThrow(Media.ORIENTATION));
					c.close();
				}
			}
			// mBitmap = BitmapFactory.decodeFile(target.getPath());

			if (targetFile != null && !targetFile.equals("")) {
				mBitmap = makeBitmap(targetFile, mOutputX * mOutputY * 4); // SUPPRESS
																			// CHECKSTYLE
			}
			if (orientation != 0) {
				mBitmap = rotate(mBitmap, orientation);
			}
		}

		if (mBitmap == null) {
			finish();
			return;
		}

		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

		findViewById(R.id.discard).setOnClickListener(
				new View.OnClickListener() {
					public void onClick(View v) {
						setResult(RESULT_CANCELED);
						finish();
					}
				});

		findViewById(R.id.save).setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				onSaveClicked();
			}
		});

		startDetection();
	}

	public static Bitmap makeBitmap(String filePath, int maxNumOfPixels) {
		try {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(filePath, options);

			if (options.mCancel || options.outWidth == -1
					|| options.outHeight == -1) {
				return null;
			}
			options.inSampleSize = computeSampleSize(options, UNCONSTRAINED,
					maxNumOfPixels);
			options.inJustDecodeBounds = false;

			options.inDither = false;
			options.inPreferredConfig = Bitmap.Config.ARGB_8888;
			return BitmapFactory.decodeFile(filePath, options);
		} catch (OutOfMemoryError ex) {
			Log.e(TAG, "Got oom exception ", ex);
			return null;
		}
	}

	public static int computeSampleSize(BitmapFactory.Options options,
                                        int minSideLength, int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength,
				maxNumOfPixels);

		int roundedSize;
		if (initialSize <= 8) { // SUPPRESS CHECKSTYLE
			roundedSize = 1;
			while (roundedSize < initialSize) {
				roundedSize <<= 1;
			}
		} else {
			roundedSize = (initialSize + 7) / 8 * 8; // SUPPRESS CHECKSTYLE
		}

		return roundedSize;
	}

	private static int computeInitialSampleSize(BitmapFactory.Options options,
                                                int minSideLength, int maxNumOfPixels) {
		double w = options.outWidth;
		double h = options.outHeight;

		int lowerBound = (maxNumOfPixels == UNCONSTRAINED) ? 1 : (int) Math
				.ceil(Math.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == UNCONSTRAINED) ? 128 : (int) Math
				.min(Math.floor(w / minSideLength), // SUPPRESS CHECKSTYLE
						Math.floor(h / minSideLength));

		if (upperBound < lowerBound) {
			// return the larger one when there is no overlapping zone.
			return lowerBound;
		}

		if ((maxNumOfPixels == UNCONSTRAINED)
				&& (minSideLength == UNCONSTRAINED)) {
			return 1;
		} else if (minSideLength == UNCONSTRAINED) {
			return lowerBound;
		} else {
			return upperBound;
		}
	}

	private void startDetection() {
		if (isFinishing()) {
			return;
		}

		mImageView.setImageBitmapResetBase(mBitmap, true);

		Runnable r = new Runnable() {
			public void run() {
				final CountDownLatch latch = new CountDownLatch(1);
				final Bitmap b = mBitmap;
				mHandler.post(new Runnable() {
					public void run() {
						if (b != mBitmap && b != null) {
							mImageView.setImageBitmapResetBase(b, true);
							mBitmap.recycle();
							mBitmap = b;
						}
						if (mImageView.getScale() == 1F) {
							mImageView.center(true, true);
						}
						latch.countDown();
					}
				});
				try {
					latch.await();
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				}
				mRunDetection.run();
			}
		};

		Thread t = new Thread(r);
		t.start();
	}

	/**
	 * onSaveClicked.
	 */
	private void onSaveClicked() {
		if (mCrop == null) {
			return;
		}

		if (mSaving) {
			return;
		}

		mSaving = true;

		Bitmap croppedImage;

		Rect r = mCrop.getCropRect();
		int logo_size = this.getResources().getDimensionPixelSize(
				R.dimen.logo_size);
		int width = r.width();
		int height = r.height();

		// If we are circle cropping, we want alpha channel, which is the
		// third param here.

		System.out.println(width + " " + height);
		int widthAndHeight = CropImage.this.getResources()
				.getDimensionPixelSize(R.dimen.logo_size);
		System.out.println("====mOutputX=" + mOutputX + "mOutputY=" + mOutputY);
		croppedImage = Bitmap.createBitmap(widthAndHeight, widthAndHeight,
				mCircleCrop ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);

		Canvas canvas = new Canvas(croppedImage);
		Rect dstRect = new Rect(0, 0, widthAndHeight, widthAndHeight);

		System.out.println("=====save left=" + r.left + "top=" + r.top);
		canvas.drawBitmap(mBitmap, r, dstRect, null);
		
		// mImageView.setImageBitmapResetBase(croppedImage, true);
		// mImageView.center(true, true);
		mImageView.mHighlightView = null;
		saveImage(croppedImage);

		setResult(CROP_PHOTO_FINISHED);
		finish();
	}

	private void saveImage(Bitmap bitmap) {
		try {
			File photo = new File("/sdcard/rc.jpg");
			OutputStream stream = new FileOutputStream(photo);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
			stream.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * transform bitmap.
	 * 
	 * @param scaler
	 *            Matrix
	 * @param source
	 *            source bitmap
	 * @param targetWidth
	 *            targetWidth
	 * @param targetHeight
	 *            targetHeight
	 * @param scaleUp
	 *            scaleUp
	 * @param recycle
	 *            recycle after transform?
	 * @return transform bitmap.
	 */
	public static Bitmap transform(Matrix scaler, Bitmap source,
                                   int targetWidth, int targetHeight, boolean scaleUp, boolean recycle) {
		int deltaX = source.getWidth() - targetWidth;
		int deltaY = source.getHeight() - targetHeight;
		if (!scaleUp && (deltaX < 0 || deltaY < 0)) {
			/*
			 * In this case the bitmap is smaller, at least in one dimension,
			 * than the target. Transform it by placing as much of the image as
			 * possible into the target and leaving the top/bottom or left/right
			 * (or both) black.
			 */
			Bitmap b2 = Bitmap.createBitmap(targetWidth, targetHeight,
					Bitmap.Config.ARGB_8888);
			Canvas c = new Canvas(b2);

			int deltaXHalf = Math.max(0, deltaX / 2);
			int deltaYHalf = Math.max(0, deltaY / 2);
			Rect src = new Rect(deltaXHalf, deltaYHalf, deltaXHalf
					+ Math.min(targetWidth, source.getWidth()), deltaYHalf
					+ Math.min(targetHeight, source.getHeight()));
			int dstX = (targetWidth - src.width()) / 2;
			int dstY = (targetHeight - src.height()) / 2;
			Rect dst = new Rect(dstX, dstY, targetWidth - dstX, targetHeight
					- dstY);
			c.drawBitmap(source, src, dst, null);
			if (recycle) {
				source.recycle();
			}
			return b2;
		}
		float bitmapWidthF = source.getWidth();
		float bitmapHeightF = source.getHeight();

		float bitmapAspect = bitmapWidthF / bitmapHeightF;
		float viewAspect = (float) targetWidth / targetHeight;

		if (bitmapAspect > viewAspect) {
			float scale = targetHeight / bitmapHeightF;
			if (scale < .9F || scale > 1F) { // SUPPRESS CHECKSTYLE
				scaler.setScale(scale, scale);
			} else {
				scaler = null;
			}
		} else {
			float scale = targetWidth / bitmapWidthF;
			if (scale < .9F || scale > 1F) { // SUPPRESS CHECKSTYLE
				scaler.setScale(scale, scale);
			} else {
				scaler = null;
			}
		}

		Bitmap b1;
		if (scaler != null) {
			// this is used for minithumb and crop, so we want to filter here.
			b1 = Bitmap.createBitmap(source, 0, 0, source.getWidth(),
					source.getHeight(), scaler, true);
		} else {
			b1 = source;
		}

		if (recycle && b1 != source) {
			source.recycle();
		}

		int dx1 = Math.max(0, b1.getWidth() - targetWidth);
		int dy1 = Math.max(0, b1.getHeight() - targetHeight);

		Bitmap b2 = Bitmap.createBitmap(b1, dx1 / 2, dy1 / 2, targetWidth,
				targetHeight);

		if (b2 != b1) {
			if (recycle || b1 != source) {
				b1.recycle();
			}
		}

		return b2;
	}

	/**
	 * startBackgroundJob.
	 * 
	 * @param activity
	 *            Activity
	 * @param title
	 *            title
	 * @param message
	 *            message
	 * @param job
	 *            job
	 * @param handler
	 *            Handler
	 */
	public static void startBackgroundJob(Activity activity, String title,
                                          String message, Runnable job, Handler handler) {

		Toast t = Toast.makeText(activity, message, Toast.LENGTH_SHORT);
		t.show();

		new Thread(job).start();
	}

	/**
	 * save crop bitmap to output.
	 * 
	 * @param croppedImage
	 *            croppedImage
	 */
	private void saveOutput(Bitmap croppedImage) {
		if (mSaveUri != null) {
			OutputStream outputStream = null;
			try {
				outputStream = mContentResolver.openOutputStream(mSaveUri);
				if (outputStream != null) {
					croppedImage.compress(mOutputFormat, 75, outputStream); // SUPPRESS
																			// CHECKSTYLE
				}
			} catch (IOException ex) {
				Log.e(TAG, "Cannot open file: " + mSaveUri, ex);
			} finally {
				try {
					if (outputStream != null) {
						outputStream.close();
					}
				} catch (Throwable t) {
					// do nothing
					Log.e(TAG, "Cannot close file: " + mSaveUri, t);
				}
			}
			Bundle extras = new Bundle();
			setResult(RESULT_OK,
					new Intent(mSaveUri.toString()).putExtras(extras));
		}
		final Bitmap b = croppedImage;
		mHandler.post(new Runnable() {
			public void run() {
				// mImageView.clear();
				b.recycle();
			}
		});
		finish();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mImageView != null) {
			mImageView.clear();
		}

		// SEARHBOX-377 [����]������ҳ����ͼƬʱ����ѡ����Ƶ�ļ���ѡ���������
		// mBitmap.recycle();
		if (mBitmap != null) {
			mBitmap.recycle();
		}
	}

	/**
	 * mRunFaceDetection.
	 */
	Runnable mRunDetection = new Runnable() {
		@SuppressWarnings("hiding")
		float mScale = 1F;
		Matrix mImageMatrix;
		int mNumFaces;

		private void makeDefault() {
			HighlightView hv = new HighlightView(mImageView);

			int width = mBitmap.getWidth();
			int height = mBitmap.getHeight();

			Rect imageRect = new Rect(0, 0, width, height);

			// make the default size about 4/5 of the width or height
			int cropWidth = Math.min(width, height) * 4 / 5;

			int cropHeight = cropWidth;

			if (mAspectX != 0 && mAspectY != 0) {
				if (mAspectX > mAspectY) {
					cropHeight = cropWidth * mAspectY / mAspectX;
				} else {
					cropWidth = cropHeight * mAspectX / mAspectY;
				}
			}

			int x = (width - cropWidth) / 2;
			int y = (height - cropHeight) / 2;

			RectF cropRect = new RectF(x, y, x + cropWidth, y + cropHeight);
			hv.setup(mImageMatrix, imageRect, cropRect, mCircleCrop,
					mAspectX != 0 && mAspectY != 0);
			Log.d(TAG, "==========mImageView.mHighlightView="
					+ mImageView.mHighlightView);
			mImageView.mHighlightView = hv;
		}

		private Bitmap prepareBitmap() {
			if (mBitmap == null) {
				return null;
			}

			// 256 pixels wide is enough.
			if (mBitmap.getWidth() > 256) {
				mScale = 256.0F / mBitmap.getWidth();
			}
			Matrix matrix = new Matrix();
			matrix.setScale(mScale, mScale);
			Bitmap faceBitmap = Bitmap.createBitmap(mBitmap, 0, 0,
					mBitmap.getWidth(), mBitmap.getHeight(), matrix, true);
			return faceBitmap;
		}

		public void run() {
			mImageMatrix = mImageView.getImageMatrix();

			mScale = 1.0F / mScale;

			mHandler.post(new Runnable() {
				public void run() {
					makeDefault();
					mImageView.invalidate();
					mCrop = mImageView.mHighlightView;
					mCrop.setFocus(true);

				}
			});
		}
	};

	public static Bitmap rotate(Bitmap b, int degrees) {
		if (b == null) {
			return null;
		}
		if (degrees != 0 && b != null) {
			Matrix m = new Matrix();
			m.setRotate(degrees, (float) b.getWidth() / 2,
					(float) b.getHeight() / 2);
			try {
				Bitmap b2 = Bitmap.createBitmap(b, 0, 0, b.getWidth(),
						b.getHeight(), m, true);
				if (b != b2) {
					b.recycle();
					b = b2;
				}
			} catch (OutOfMemoryError ex) {
				// We have no memory to rotate. Return the original bitmap.
				ex.printStackTrace();
			}
		}
		return b;
	}

	private CropImageView getCropImageView() {
		return mImageView;
	}

	private static final int NONE = 0;
	private static final int DRAG = 1;
	private static final int ZOOM = 2;

	private int mode = NONE;
	private float oldDist;
	private Matrix matrix = new Matrix();
	private Matrix savedMatrix = new Matrix();
	private PointF start = new PointF();
	private PointF mid = new PointF();
	private float ori_ratio = 1F;
	private long mNextChangePositionTime;

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		int action = event.getAction() & MotionEvent.ACTION_MASK, p_count = event
				.getPointerCount();
		System.out.println("in onTouchEvent");
		if (p_count == 1) {

		}
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
			start.set(event.getX(), event.getY());
			mode = DRAG;
			break;
		case MotionEvent.ACTION_UP:

		case MotionEvent.ACTION_POINTER_UP:
			mode = NONE;
			break;
		case MotionEvent.ACTION_POINTER_DOWN:
			ori_ratio = mImageView.getScale();
			oldDist = spacing(event);
			if (oldDist > 10f) {
				mode = ZOOM;
			}
			break;
		case MotionEvent.ACTION_MOVE:
			// ��һ����ָ�϶�ͼƬ
			if (mode == DRAG) {
				if (mImageView.getScale() > 1.0F) {
					// if(imgView.isBottomBound() || imgView.isTopBound()){
					// return true;
					// }
					if (event.getEventTime() >= mNextChangePositionTime) {
						mNextChangePositionTime = event.getEventTime() + 500;
					} else {
						if (mImageView.isNeedDrag()) {
							// imgView.panBy(event.getX() - start.x,
							// event.getY()
							// - start.y);
							mImageView.panBy(start.x - event.getX(), start.y
									- event.getY());
							mImageView.mHighlightView.handleMotion(event.getX()
									- start.x, event.getY() - start.y);
							start.set(event.getX(), event.getY());
							// imgView.center(true, false);

						} else {
							mImageView.panBy(start.x - event.getX(), 0);
							start.set(event.getX(), start.y);
						}
					}
				}

				return true;
			} else if (mode == ZOOM) {
				float newDist = spacing(event);
				if (newDist > 10f) {
					float scale = (newDist / oldDist) * ori_ratio;
					if (p_count == 2) {
						if (scale >= 1)
							mImageView.zoomTo(scale);
						return true;
					}
				}
			}
			break;
		}

		return super.onTouchEvent(event);
	}

	private float spacing(MotionEvent event) {
		float x = event.getX(0) - event.getX(1);
		float y = event.getY(0) - event.getY(1);
		double f = Math.sqrt(x * x + y * y);
		return (float) f;
	}

	private void midPoint(PointF point, MotionEvent event) {
		float x = event.getX(0) + event.getX(1);
		float y = event.getY(0) + event.getY(1);
		point.set(x / 2, y / 2);
	}
}

/**
 * CropImageView, used to show the crop image.
 */
class CropImageView extends ImageViewTouchBase {

	HighlightView mHighlightView;
	HighlightView mMotionHighlightView = null;
	float mLastX, mLastY;
	int mMotionEdge;

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		if (mBitmapDisplayed.getBitmap() != null) {
			if (mHighlightView != null) {
				mHighlightView.mMatrix.set(getImageMatrix());
				mHighlightView.invalidate();
				if (mHighlightView.mIsFocused) {
					centerBasedOnHighlightView(mHighlightView);
				}
			}

		}

	}

	@Override
	public void zoomTo(float scale, float centerX, float centerY) {
		super.zoomTo(scale, centerX, centerY);
		if (mHighlightView != null) {
			mHighlightView.mMatrix.set(getImageMatrix());
			mHighlightView.invalidate();
		}
	}

	/**
	 * constructor.
	 * 
	 * @param context
	 *            Context
	 * @param attrs
	 *            AttributeSet
	 */
	public CropImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void zoomIn() {
		super.zoomIn();
	}

	@Override
	protected void zoomOut() {
		super.zoomOut();
	}

	@Override
	protected void postTranslate(float deltaX, float deltaY) {
		super.postTranslate(deltaX, deltaY);
		if (mHighlightView != null) {
			mHighlightView.mMatrix.postTranslate(-deltaX, -deltaY);
			mHighlightView.invalidate();
		}
	}

	private void ensureVisible(HighlightView hv) {
		Rect r = hv.mDrawRect;

		int panDeltaX1 = Math.max(0, getLeft() - r.left);
		int panDeltaX2 = Math.min(0, getRight() - r.right);

		int panDeltaY1 = Math.max(0, getTop() - r.top);
		int panDeltaY2 = Math.min(0, getBottom() - r.bottom);

		int panDeltaX = panDeltaX1 != 0 ? panDeltaX1 : panDeltaX2;
		int panDeltaY = panDeltaY1 != 0 ? panDeltaY1 : panDeltaY2;

		if (panDeltaX != 0 || panDeltaY != 0) {
			panBy(panDeltaX, panDeltaY);
		}
	}

	private void centerBasedOnHighlightView(HighlightView hv) {
		Rect drawRect = hv.mDrawRect;

		float width = drawRect.width();
		float height = drawRect.height();

		float thisWidth = getWidth();
		float thisHeight = getHeight();

		float z1 = thisWidth / width * .6F;
		float z2 = thisHeight / height * .6F;

		float zoom = Math.min(z1, z2);
		zoom = zoom * this.getScale();
		zoom = Math.max(1F, zoom);

		if ((Math.abs(zoom - getScale()) / zoom) > .1) {
			float[] coordinates = new float[] { hv.mCropRect.centerX(),
					hv.mCropRect.centerY() };
			getImageMatrix().mapPoints(coordinates);
			zoomTo(zoom, coordinates[0], coordinates[1], 300F);
		}

		ensureVisible(hv);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (mHighlightView != null) {
			mHighlightView.draw(canvas);
		}
	}

}
