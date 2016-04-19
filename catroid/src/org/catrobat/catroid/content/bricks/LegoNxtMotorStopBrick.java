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
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;

import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;

import org.catrobat.catroid.R;
import org.catrobat.catroid.content.Sprite;

import java.util.List;

public class LegoNxtMotorStopBrick extends BrickBaseType implements SpinnerBrick {
	private static final long serialVersionUID = 1L;
	private transient Motor motorEnum;
	private String motor;

	private int spinnerSelectionID;
	private final int layoutId = R.layout.brick_nxt_motor_stop;
	private final int spinnerId = R.id.stop_motor_spinner;

	public static enum Motor {
		MOTOR_A, MOTOR_B, MOTOR_C, MOTOR_B_C, ALL_MOTORS
	}

	public LegoNxtMotorStopBrick(Motor motor) {
		this.motorEnum = motor;
		this.motor = motorEnum.name();
	}

	protected Object readResolve() {
		if (motor != null) {
			motorEnum = Motor.valueOf(motor);
		}
		return this;
	}

	public void setMotorValue(Motor motorEnum) {
		this.motorEnum = motorEnum;
		motor = motorEnum.name();
	}

	public Motor getMotorEnum() {
		return motorEnum;
	}

	@Override
	public int getRequiredResources() {
		return BLUETOOTH_LEGO_NXT;
	}

	@Override
	public Brick copyBrickForSprite(Sprite sprite) {
		LegoNxtMotorStopBrick copyBrick = (LegoNxtMotorStopBrick) clone();
		return copyBrick;
	}

	@Override
	public Brick clone() {
		return new LegoNxtMotorStopBrick(motorEnum);
	}

	/*@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		motorEnum = Motor.values()[position];
		motor = motorEnum.name();
		adapterView = parent;
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
	}*/

	//_----------------------------
	public ArrayAdapter<CharSequence> createArrayAdapter(Context context) {
		ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(context, R.array.nxt_motor_chooser,
				android.R.layout.simple_spinner_item);
		spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		return spinnerAdapter;
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

	@Override
	public List<SequenceAction> addActionToSequence(Sprite sprite, SequenceAction sequence) {
		sequence.addAction(sprite.getActionFactory().createLegoNxtMotorStopAction(motorEnum));
		return null;
	}
}
