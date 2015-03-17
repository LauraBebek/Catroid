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
package org.catrobat.catroid.stage;

import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.backends.android.AndroidAudio;
import com.badlogic.gdx.backends.android.AndroidFiles;
import com.badlogic.gdx.backends.android.AndroidGraphics;
import com.badlogic.gdx.backends.android.AndroidInputFactory;
import com.badlogic.gdx.backends.android.AndroidNet;
import com.badlogic.gdx.backends.android.surfaceview.FillResolutionStrategy;

import org.catrobat.catroid.ProjectManager;
import org.catrobat.catroid.R;
import org.catrobat.catroid.camera.CameraPreview;
import org.catrobat.catroid.common.ScreenValues;
import org.catrobat.catroid.drone.DroneInitializer;
import org.catrobat.catroid.facedetection.FaceDetectionHandler;
import org.catrobat.catroid.formulaeditor.SensorHandler;
import org.catrobat.catroid.io.StageAudioFocus;
import org.catrobat.catroid.ui.dialogs.StageDialog;
import org.catrobat.catroid.utils.LedUtil;
import org.catrobat.catroid.utils.ToastUtil;
import org.catrobat.catroid.utils.VibratorUtil;

public class StageActivity extends AndroidApplication {
	public static final String TAG = StageActivity.class.getSimpleName();
	public static StageListener stageListener;
	private boolean resizePossible;
	private StageDialog stageDialog;

	private DroneConnection droneConnection = null;

	public static final int STAGE_ACTIVITY_FINISH = 7777;

	private StageAudioFocus stageAudioFocus;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		if (getIntent().getBooleanExtra(DroneInitializer.INIT_DRONE_STRING_EXTRA, false)) {
			droneConnection = new DroneConnection(this);
		}
		stageListener = new StageListener();
		stageDialog = new StageDialog(this, stageListener, R.style.stage_dialog);
		stageDialog = new StageDialog(this, stageListener, R.style.stage_dialog);
		calculateScreenSizes();

		boolean videoNeeded = true;
		initialize(stageListener, true, videoNeeded);

		if (droneConnection != null) {
			try {
				droneConnection.initialise();
			} catch (RuntimeException runtimeException) {
				Log.e(TAG, "Failure during drone service startup", runtimeException);
				ToastUtil.showError(this, R.string.error_no_drone_connected);
				this.finish();
			}
		}

		stageAudioFocus = new StageAudioFocus(this);

		//GLSurfaceView x = (GLSurfaceView)this.initializeForView(stageListener, true);
	}

	@Override
	public void onBackPressed() {
		pause();
		stageDialog.show();
	}

	public void manageLoadAndFinish() {
		stageListener.pause();
		stageListener.finish();

		PreStageActivity.shutdownResources();
	}

	@Override
	public void onPause() {
		SensorHandler.stopSensorListeners();
		stageListener.activityPause();
		stageAudioFocus.releaseAudioFocus();
		LedUtil.pauseLed();
		VibratorUtil.pauseVibrator();
		super.onPause();

		if (droneConnection != null) {
			droneConnection.pause();
		}
	}

	@Override
	public void onResume() {
		SensorHandler.startSensorListener(this);
		stageListener.activityResume();
		stageAudioFocus.requestAudioFocus();
		LedUtil.resumeLed();
		VibratorUtil.resumeVibrator();
		super.onResume();

		if (droneConnection != null) {
			droneConnection.start();
		}
	}

	public void pause() {
		SensorHandler.stopSensorListeners();
		stageListener.menuPause();
		LedUtil.pauseLed();
		VibratorUtil.pauseVibrator();
		FaceDetectionHandler.pauseFaceDetection();
	}

	public void resume() {
		stageListener.menuResume();
		LedUtil.resumeLed();
		VibratorUtil.resumeVibrator();
		SensorHandler.startSensorListener(this);
		FaceDetectionHandler.startFaceDetection(this);
	}

	public boolean getResizePossible() {
		return resizePossible;
	}

	private void calculateScreenSizes() {
		ifLandscapeSwitchWidthAndHeight();
		int virtualScreenWidth = ProjectManager.getInstance().getCurrentProject().getXmlHeader().virtualScreenWidth;
		int virtualScreenHeight = ProjectManager.getInstance().getCurrentProject().getXmlHeader().virtualScreenHeight;
		float aspectRatio = (float) virtualScreenWidth / (float) virtualScreenHeight;
		float screenAspectRatio = ScreenValues.getAspectRatio();

		if ((virtualScreenWidth == ScreenValues.SCREEN_WIDTH && virtualScreenHeight == ScreenValues.SCREEN_HEIGHT)
				|| Float.compare(screenAspectRatio, aspectRatio) == 0) {
			resizePossible = false;
			stageListener.maximizeViewPortWidth = ScreenValues.SCREEN_WIDTH;
			stageListener.maximizeViewPortHeight = ScreenValues.SCREEN_HEIGHT;
			return;
		}

		resizePossible = true;

		float scale = 1f;
		float ratioHeight = (float) ScreenValues.SCREEN_HEIGHT / (float) virtualScreenHeight;
		float ratioWidth = (float) ScreenValues.SCREEN_WIDTH / (float) virtualScreenWidth;

		if (aspectRatio < screenAspectRatio) {
			scale = ratioHeight / ratioWidth;
			stageListener.maximizeViewPortWidth = (int) (ScreenValues.SCREEN_WIDTH * scale);
			stageListener.maximizeViewPortX = (int) ((ScreenValues.SCREEN_WIDTH - stageListener.maximizeViewPortWidth) / 2f);
			stageListener.maximizeViewPortHeight = ScreenValues.SCREEN_HEIGHT;

		} else if (aspectRatio > screenAspectRatio) {
			scale = ratioWidth / ratioHeight;
			stageListener.maximizeViewPortHeight = (int) (ScreenValues.SCREEN_HEIGHT * scale);
			stageListener.maximizeViewPortY = (int) ((ScreenValues.SCREEN_HEIGHT - stageListener.maximizeViewPortHeight) / 2f);
			stageListener.maximizeViewPortWidth = ScreenValues.SCREEN_WIDTH;
		}
	}

	private void ifLandscapeSwitchWidthAndHeight() {
		if (ScreenValues.SCREEN_WIDTH > ScreenValues.SCREEN_HEIGHT) {
			int tmp = ScreenValues.SCREEN_HEIGHT;
			ScreenValues.SCREEN_HEIGHT = ScreenValues.SCREEN_WIDTH;
			ScreenValues.SCREEN_WIDTH = tmp;
		}
	}

	@Override
	protected void onDestroy() {
		if (droneConnection != null) {
			droneConnection.destroy();
		}
		Log.d(TAG, "Destroy");
		LedUtil.destroy();
		VibratorUtil.destroy();
		super.onDestroy();
	}

	@Override
	public ApplicationListener getApplicationListener() {
		return stageListener;
	}

	@Override
	public void log(String tag, String message, Throwable exception) {
		Log.d(tag, message, exception);
	}

	@Override
	public int getLogLevel() {
		return 0;
	}


	public void initialize (ApplicationListener listener, boolean useGL2, boolean VideoNeeded) {

		if(!VideoNeeded)
			initialize(listener, useGL2);
		else {
			try {
				requestWindowFeature(Window.FEATURE_NO_TITLE);
			} catch (Exception ex) {
				log("AndroidApplication", "Content already displayed, cannot request FEATURE_NO_TITLE", ex);
			}
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
			getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

			RelativeLayout layout = new RelativeLayout(this);
			AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
			//cfg.r = cfg.g = cfg.b = cfg.a = 8;
			cfg.useGL20 = true;
			cfg.hideStatusBar = true;
			cfg.useWakelock = true;

			GLSurfaceView gameView = (GLSurfaceView)initializeForView(listener, true);
			if(gameView != null)
				Log.d("Lausi","YES YES YES");
			//gameView.setEGLConfigChooser( 8, 8, 8, 8, 16, 0 );
			gameView.getHolder().setFormat( PixelFormat.TRANSLUCENT );
			//gameView.setZOrderOnTop(true);
			gameView.setZOrderMediaOverlay(true);

			//setContentView(gameView);

			//layout.addView(gameView);
			CameraPreview cameraView = new CameraPreview( this );
			//cameraView.setZOrderOnTop(false);
			// ...and add it, wrapping the full screen size.
			//addContentView( cameraView, new RelativeLayout.LayoutParams( RelativeLayout.LayoutParams.WRAP_CONTENT,
																	     //RelativeLayout.LayoutParams.WRAP_CONTENT ));

			layout.addView(cameraView);
			layout.addView(gameView);

			setContentView(layout);

		}
		//initalizeForView.... LinearLayout.... add SurfaceView and graphic.view.... ZOrder

	}

	protected LinearLayout.LayoutParams createLayoutParamsForVideo() {
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT);
		layoutParams.gravity = Gravity.CENTER;
		return layoutParams;
	}

}
