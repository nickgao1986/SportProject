package com.image;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.Images.Media;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import com.nick.apps.pregnancy11.mypedometer.fragment.base.BaseActivity;
import com.imooc.sport.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class PedoCropImageActivity extends BaseActivity {

	private static final String TAG = "PedoCropImageActivity";

	static final int UNCONSTRAINED = -1;
	/**
	 * These options specifiy the output image size and whether we should scale
	 * the output to fit it (or just crop it).
	 */
	private int mOutputX, mOutputY;
	private Bitmap mBitmap;
	ClipLayout mClipLayout;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		setContentView(R.layout.pedo_rc_cropimage);

		ImageView mImageView = (ImageView) findViewById(R.id.image);

		Intent intent = getIntent();
		Bundle extras = intent.getExtras();

		if (extras != null) {
			mBitmap = (Bitmap) extras.getParcelable("data");
			mOutputX = extras.getInt("outputX");
			mOutputY = extras.getInt("outputY");
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

			if (targetFile != null && !targetFile.equals("")) {
				mBitmap = makeBitmap(targetFile, mOutputX * mOutputY * 4);

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
		mClipLayout = (ClipLayout) findViewById(R.id.clip_layout);
		Window window = getWindow();
		mClipLayout.setSourceImage(mBitmap, window);
	}

	private void onSaveClicked() {
		Bitmap bitmap = mClipLayout.getBitmap();
		saveImage(bitmap);
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

	public static int computeSampleSize(BitmapFactory.Options options,
                                        int minSideLength, int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength,
				maxNumOfPixels);

		int roundedSize;
		if (initialSize <= 8) {
			roundedSize = 1;
			while (roundedSize < initialSize) {
				roundedSize <<= 1;
			}
		} else {
			roundedSize = (initialSize + 7) / 8 * 8;
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
				.min(Math.floor(w / minSideLength),
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
				ex.printStackTrace();
			}
		}
		return b;
	}

	@Override
	public Object getTitleString() {
		return null;
	}

	@Override
	public int getContentView() {
		return R.layout.pedo_rc_cropimage;
	}

	@Override
	public void onLeftButtonClick() {

	}

	@Override
	public void onRightButtonClick() {

	}
}
