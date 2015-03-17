/*
 * Catroid: An on-device visual programming system for Android devices
 * Copyright (C) 2010-2015 The Catrobat Team
 * (<http://developer.catrobat.org/credits>)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * An additional term exception under section 7 of the GNU Affero
 * General Public License, version 3, is available at
 * http://developer.catrobat.org/license_additional_term
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.catrobat.catroid.camera;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Parameters;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;

import com.thoughtworks.xstream.mapper.Mapper;

import org.catrobat.catroid.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class CameraManager implements Camera.PreviewCallback {

	public static final int TEXTURE_NAME = 1;
	private static final String TAG = CameraManager.class.getSimpleName();
	private static CameraManager instance;
	private Camera camera;
	private SurfaceTexture texture;
	private List<JpgPreviewCallback> callbacks = new ArrayList<JpgPreviewCallback>();
	private int previewFormat;
	private int previewWidth;
	private int previewHeight;
	private int cameraID = 0;
	private int orientation = 0;
	private boolean started = false;

	private boolean facingBack = true;
	private boolean useTexture = false;
	private boolean videoOn = false;

	//private byte[] currentFrame;
	private byte[] jpgData;
	private boolean frameExist = false;

	private transient int width = 1280;
	private transient int height = 720;

	public static CameraManager getInstance() {
		if (instance == null) {
			instance = new CameraManager();
		}
		return instance;
	}

	private CameraManager() {
		int currentApi = android.os.Build.VERSION.SDK_INT;
		if (currentApi >= android.os.Build.VERSION_CODES.HONEYCOMB) {
			useTexture = true;
			//createTexture();
		}

		//currentFrame = new byte[height*width/8*12];
	}

	public Camera getCamera() {
		if (camera == null) {
			createCamera();
		}
		return camera;
	}

	public void updateCameraID(Context context) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		String idAsString = preferences.getString(
				context.getResources().getString(R.string.preference_key_select_camera), "0");
		cameraID = Integer.parseInt(idAsString);

		CameraInfo cameraInfo = new CameraInfo();
		Camera.getCameraInfo(cameraID, cameraInfo);
		orientation = cameraInfo.orientation;
		facingBack = cameraInfo.facing == CameraInfo.CAMERA_FACING_BACK;
	}

	public int getCameraID() {
		return cameraID;
	}

	public int getOrientation() {
		return orientation;
	}

	public boolean isFacingBack() {
		return facingBack;
	}

	private boolean createCamera() {
		Log.d("Lausi", "CREATECAM");
		if (camera != null) {
			return false;
		}
		try {
			camera = Camera.open(cameraID);
		} catch (RuntimeException runtimeException) {
			Log.e(TAG, "Creating camera failed!", runtimeException);
			return false;
		}
		camera.setDisplayOrientation(90);
		//camera.setPreviewCallbackWithBuffer(this)
		Log.d("Lausi", "SETCALLBACK");
		if (useTexture && texture != null) {
            try {
                setTexture();
            } catch (IOException iOException) {
                Log.e(TAG, "Setting preview texture failed!", iOException);
                return false;
            }
		}
		//camera.addCallbackBuffer(currentFrame);*/
		return true;
	}

	public boolean startCamera() {

		//camera.setPreviewCallbackWithBuffer(this);
		Log.d("Lausi", "STARTCAM");
		if (camera == null) {
			boolean success = createCamera();
			if (!success) {
				Log.d("Lausi", "STARTCAMPROBLEM");
				return false;
			}
		}
		//initCam();

		Parameters parameters = camera.getParameters();
		previewFormat = parameters.getPreviewFormat();
		previewWidth = parameters.getPreviewSize().width;
		previewHeight = parameters.getPreviewSize().height;
		camera.startPreview();
		Log.d("Lausi", "STARTCAMOK");
		//camera.addCallbackBuffer(currentFrame);
		return true;
	}

	public void initCam() {
		Log.d("Lausi", "INIT:CAM");
		if (camera != null) {
			Log.d("Lausi", "in if!");
			camera.setPreviewCallbackWithBuffer(this);
			Camera.Parameters params = camera.getParameters();
			params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
			List<Camera.Size> bla = params.getSupportedPreviewSizes();
			Log.d("Lausi","height"+bla.get(1).height);
			Log.d("Lausi","width"+bla.get(1).width);
			params.setPreviewSize(width, height);
			params.set("orientation", "portrait");

			if (Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD_MR1) {
				try {
					camera.setParameters(params);
					//texture = new SurfaceTexture(100);
					//camera.setPreviewTexture(texture);
				} catch (Exception e) {
					Log.d("Lausi", "Could not create surface!" + e.getMessage());
				}
			}

		}
		else {
			Log.d("Lausi", "ich glaub es hackt!");
		}
	}

	public void releaseCamera() {
		Log.d("Lausi", "RELEASE");
		if (camera == null) {
			return;
		}
		camera.setPreviewCallback(null);
		camera.stopPreview();
		camera.release();
		camera = null;
		started = false;
	}

	public void addOnJpgPreviewFrameCallback(JpgPreviewCallback callback) {
		Log.d("Lausi", "JpegPreviewCallback");
		if (callbacks.contains(callback)) {
			return;
		}
		callbacks.add(callback);
	}

	public void removeOnJpgPreviewFrameCallback(JpgPreviewCallback callback) {
		callbacks.remove(callback);
	}

	@Override
	public void onPreviewFrame(byte[] data, Camera camera) {
		Log.d("Lausi", "OnPReviewFram");
		//camera.addCallbackBuffer(currentFrame);
		if (callbacks.size() == 0) {
			return;
		}
		jpgData = getDecodeableBytesFromCameraFrame(data);
		for (JpgPreviewCallback callback : callbacks) {
			callback.onFrame(jpgData);
		}
	}

	private byte[] getDecodeableBytesFromCameraFrame(byte[] cameraData) {
		byte[] decodableBytes;
		YuvImage image = new YuvImage(cameraData, previewFormat, previewWidth, previewHeight, null);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		image.compressToJpeg(new Rect(0, 0, previewWidth, previewHeight), 50, out);
		decodableBytes = out.toByteArray();
		return decodableBytes;
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void createTexture() {
		texture = new SurfaceTexture(TEXTURE_NAME);
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setTexture() throws IOException {
		camera.setPreviewTexture(texture);
	}

	public void setLedParams(Parameters led) {
		if (camera != null && led != null) {
			camera.setParameters(led);
		}
	}

	public void setVideoRunning(boolean on)
	{
		if(on) {
			Log.d("Lausi", "VideoON");
			//startCamera();
		}

		videoOn = on;
	}

	public byte[] getCurrentFrame() {
		return jpgData;
	}

	public boolean isVideoOn() {
		return videoOn;
	}

	public boolean frameExist() { return frameExist; }

}
