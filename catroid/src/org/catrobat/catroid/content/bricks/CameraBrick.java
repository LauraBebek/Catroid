/*
 * Catroid: An on-device visual programming system for Android devices
 * Copyright (C) 2010-2016 The Catrobat Team
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

package org.catrobat.catroid.content.bricks;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;

import org.catrobat.catroid.R;
import org.catrobat.catroid.camera.CameraManager;
import org.catrobat.catroid.content.Sprite;

import java.util.List;

public class CameraBrick extends BrickBaseType implements SpinnerBrick {

	private static final int OFF = 0;
	private static final int ON = 1;
	private final int layoutId = R.layout.brick_video;
	private final int spinnerId = R.id.brick_video_spinner;

	private String[] spinnerValues;
	private int spinnerSelectionID;

	public CameraBrick() {
		spinnerValues = new String[2];
		spinnerSelectionID = ON;
	}

	public CameraBrick(int onOrOff) {
		spinnerValues = new String[2];
		spinnerSelectionID = onOrOff;
	}

	public ArrayAdapter<CharSequence> createArrayAdapter(Context context) {
		spinnerValues[OFF] = context.getString(R.string.video_brick_camera_off);
		spinnerValues[ON] = context.getString(R.string.video_brick_camera_on);

		ArrayAdapter<CharSequence> spinnerAdapter = new ArrayAdapter<CharSequence>(context, android.R.layout.simple_spinner_item, spinnerValues);
		spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		return spinnerAdapter;
	}

	public void  setSelectedSpinnerItem(int position) {
		spinnerSelectionID = position;
	}

	public int getSelectedSpinnerItem() {
		return spinnerSelectionID;
	}

	@Override
	public int getRequiredResources() {
		return Brick.VIDEO;
	}

	public int getLayoutId() {
		return layoutId;
	}

	public int getSpinnerId() {
		return spinnerId;
	}

	@Override
	public List<SequenceAction> addActionToSequence(Sprite sprite, SequenceAction sequence) {
		sequence.addAction(sprite.getActionFactory().createUpdateCameraPreviewAction(getCameraStateFromSpinner()));
		return null;
	}

	private CameraManager.CameraState getCameraStateFromSpinner() {
		if (spinnerSelectionID == OFF) {
			return CameraManager.CameraState.stopped;
		}

		return CameraManager.CameraState.prepare;
	}
}
