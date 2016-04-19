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
import org.catrobat.catroid.formulaeditor.Formula;

import java.util.List;

public class PhiroMotorMoveBackwardBrick extends FormulaBrick implements SpinnerBrick {
	private static final long serialVersionUID = 1L;

	private String motor;
	private transient Motor motorEnum;

	private int spinnerSelectionID;
	private final int layoutId = R.layout.brick_phiro_motor_backward;
	private final int spinnerId = R.id.brick_phiro_motor_backward_action_spinner;

	public enum Motor {
		MOTOR_LEFT, MOTOR_RIGHT, MOTOR_BOTH
	}

	public PhiroMotorMoveBackwardBrick() {
		addAllowedBrickField(BrickField.PHIRO_SPEED);
	}

	public PhiroMotorMoveBackwardBrick(Motor motor, int speedValue) {
		this.motorEnum = motor;
		this.motor = motorEnum.name();
		initializeBrickFields(new Formula(speedValue));
	}

	public PhiroMotorMoveBackwardBrick(Motor motor, Formula speedFormula) {
		this.motorEnum = motor;
		this.motor = motorEnum.name();

		initializeBrickFields(speedFormula);
	}

	private void initializeBrickFields(Formula speed) {
		addAllowedBrickField(BrickField.PHIRO_SPEED);
		setFormulaWithBrickField(BrickField.PHIRO_SPEED, speed);
	}

	protected Object readResolve() {
		if (motor != null) {
			motorEnum = Motor.valueOf(motor);
		}
		return this;
	}

	@Override
	public int getRequiredResources() {
		return BLUETOOTH_PHIRO | getFormulaWithBrickField(BrickField.PHIRO_SPEED).getRequiredResources();
	}

	@Override
	public Brick clone() {
		return new PhiroMotorMoveBackwardBrick(motorEnum,
				getFormulaWithBrickField(BrickField.PHIRO_SPEED).clone());
	}

	/*private boolean isSpeedOnlyANumber() {
		return getFormulaWithBrickField(BrickField.PHIRO_SPEED).getRoot().getElementType()
				== FormulaElement.ElementType.NUMBER;
	}*/

	@Override
	public List<SequenceAction> addActionToSequence(Sprite sprite, SequenceAction sequence) {
		sequence.addAction(sprite.getActionFactory().createPhiroMotorMoveBackwardActionAction(sprite, motorEnum, getFormulaWithBrickField(BrickField.PHIRO_SPEED)));
		return null;
	}

	public ArrayAdapter<CharSequence> createArrayAdapter(Context context) {
		ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(context,
				R.array.brick_phiro_select_motor_spinner, android.R.layout.simple_spinner_item);
		spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		return spinnerAdapter;
	}

	public void setSelectedSpinnerItem(int position) {
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
