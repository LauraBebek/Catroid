/**
 *  Catroid: An on-device visual programming system for Android devices
 *  Copyright (C) 2010-2014 The Catrobat Team
 *  (<http://developer.catrobat.org/credits>)
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as
 *  published by the Free Software Foundation, either version 3 of the
 *  License, or (at your option) any later version.
 *
 *  An additional term exception under section 7 of the GNU Affero
 *  General Public License, version 3, is available at
 *  http://developer.catrobat.org/license_additional_term
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU Affero General Public License for more details.
 *
 *  You should have received a copy of the GNU Affero General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.catrobat.catroid.camera;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import org.catrobat.catroid.camera.CameraManager;

import java.io.IOException;

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {

	private SurfaceHolder mHolder;

	public CameraPreview( Context context ) {
		super( context );
		mHolder = getHolder();
		mHolder.addCallback(this);
		// deprecated setting, but required on Android versions prior to 3.0
		mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}

	public void surfaceCreated( SurfaceHolder holder ) {
		// Once the surface is created, simply open a handle to the camera hardware.
		Log.d("Lausi", "SURFACE CREATED");
		try {
			CameraManager.getInstance().initCam();
			CameraManager.getInstance().getCamera().setPreviewDisplay(holder);
			CameraManager.getInstance().startCamera();
		} catch (IOException e) {
			Log.d("Lausi", "Error setting camera preview: " + e.getMessage());
		}
	}

	public void surfaceChanged( SurfaceHolder holder, int format, int width, int height ) {

		Log.d("Lausi", "SURFACE CHANGED");
		if (mHolder.getSurface() == null){
			Log.d("Lausi", "SURFACE DOES NOT EXIST");
			// preview surface does not exist
			return;
		}

		// stop preview before making changes
		try {
			CameraManager.getInstance().getCamera().stopPreview();
		} catch (Exception e){
			// ignore: tried to stop a non-existent preview
		}

		// set preview size and make any resize, rotate or
		// reformatting changes here

		// start preview with new settings
		try {
			CameraManager.getInstance().getCamera().setPreviewDisplay(mHolder);
			CameraManager.getInstance().startCamera();

		} catch (Exception e){
			Log.d("Lausi", "Error starting camera preview: " + e.getMessage());
		}
	}

	public void surfaceDestroyed( SurfaceHolder holder ) {
		Log.d("Lausi", "SURFACE DESTROYED");
		// Once the surface gets destroyed, we stop the preview mode and release
		// the whole camera since we no longer need it.
		CameraManager.getInstance().releaseCamera();
	}


}