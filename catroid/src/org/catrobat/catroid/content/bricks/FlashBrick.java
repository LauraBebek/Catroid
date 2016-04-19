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
import android.widget.ArrayAdapter;

import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;

import org.catrobat.catroid.R;
import org.catrobat.catroid.content.Sprite;

import java.util.List;

public class FlashBrick extends BrickBaseType implements SpinnerBrick {

	private static final int FLASH_OFF = 0;
	private static final int FLASH_ON = 1;
	private String[] spinnerValues;
	private int spinnerSelectionID;

	private final int layoutId = R.layout.brick_flash;
	private final int spinnerId = R.id.brick_flash_spinner;

	public FlashBrick() {
		spinnerValues = new String[2];
		spinnerSelectionID = FLASH_ON;
	}

	public FlashBrick(int onOrOff) {
		spinnerValues = new String[2];
		spinnerSelectionID = onOrOff;
	}

	public ArrayAdapter<CharSequence> createArrayAdapter(Context context) {
		spinnerValues[FLASH_OFF] = context.getString(R.string.brick_flash_off);
		spinnerValues[FLASH_ON] = context.getString(R.string.brick_flash_on);

		ArrayAdapter<CharSequence> spinnerAdapter = new ArrayAdapter<CharSequence>(context, android.R.layout.simple_spinner_item, spinnerValues);
		spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		return spinnerAdapter;
	}

	@Override
	public int getRequiredResources() {
		return CAMERA_FLASH;
	}

	@Override
	public List<SequenceAction> addActionToSequence(Sprite sprite, SequenceAction sequence) {
		if (spinnerSelectionID == FLASH_ON) {
			sequence.addAction(sprite.getActionFactory().createTurnFlashOnAction());
			return null;
		}
		sequence.addAction(sprite.getActionFactory().createTurnFlashOffAction());
		return null;
	}

	public void  setSelectedSpinnerItem(int position) {
		spinnerSelectionID = position;
	}

	public int getSelectedSpinnerItem() {
		return spinnerSelectionID;
	}

	public int getLayoutId() {
		return layoutId;
	}

	public int getSpinnerId() {
		return spinnerId;
	}
}
