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
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;

import org.catrobat.catroid.R;
import org.catrobat.catroid.common.BrickValues;
import org.catrobat.catroid.content.Project;
import org.catrobat.catroid.content.Sprite;
import org.catrobat.catroid.formulaeditor.Formula;
import org.catrobat.catroid.ui.fragment.FormulaEditorFragment;

import java.util.List;

public class ArduinoSendPWMValueBrick extends FormulaBrick {

	private static final long serialVersionUID = 1L;

	public ArduinoSendPWMValueBrick() {
		addAllowedBrickField(BrickField.ARDUINO_ANALOG_PIN_NUMBER);
		addAllowedBrickField(BrickField.ARDUINO_ANALOG_PIN_VALUE);
	}

	public ArduinoSendPWMValueBrick(int pinNumber, int pinValue) {
		initializeBrickFields(new Formula(pinNumber), new Formula(pinValue));
	}

	public ArduinoSendPWMValueBrick(Formula pinNumber, Formula pinValue) {
		initializeBrickFields(pinNumber, pinValue);
	}

	private void initializeBrickFields(Formula pinNumber, Formula pinValue) {
		addAllowedBrickField(BrickField.ARDUINO_ANALOG_PIN_NUMBER);
		addAllowedBrickField(BrickField.ARDUINO_ANALOG_PIN_VALUE);
		setFormulaWithBrickField(BrickField.ARDUINO_ANALOG_PIN_NUMBER, pinNumber);
		setFormulaWithBrickField(BrickField.ARDUINO_ANALOG_PIN_VALUE, pinValue);
	}

	@Override
	public int getRequiredResources() {
		return BLUETOOTH_SENSORS_ARDUINO
				| getFormulaWithBrickField(BrickField.ARDUINO_ANALOG_PIN_NUMBER).getRequiredResources()
				| getFormulaWithBrickField(BrickField.ARDUINO_ANALOG_PIN_VALUE).getRequiredResources();
	}

	/*@Override
	public void onClick(View view) {
		if (checkbox.getVisibility() == View.VISIBLE) {
			return;
		}

		switch (view.getId()) {
			case R.id.brick_arduino_set_analog_pin_edit_text:
				FormulaEditorFragment.showFragment(view, this, BrickField.ARDUINO_ANALOG_PIN_NUMBER);
				break;

			case R.id.brick_arduino_set_analog_value_edit_text:
				FormulaEditorFragment.showFragment(view, this, BrickField.ARDUINO_ANALOG_PIN_VALUE);
				break;
		}
	}*/

	@Override
	public List<SequenceAction> addActionToSequence(Sprite sprite, SequenceAction sequence) {
		sequence.addAction(sprite.getActionFactory().createSendPWMArduinoValueAction(sprite,
				getFormulaWithBrickField(BrickField.ARDUINO_ANALOG_PIN_NUMBER),
				getFormulaWithBrickField(BrickField.ARDUINO_ANALOG_PIN_VALUE)));
		return null;
	}

	/*public void showFormulaEditorToEditFormula(View view) {
		FormulaEditorFragment.showFragment(view, this, BrickField.ARDUINO_ANALOG_PIN_NUMBER);
	}

	@Override
	public void updateReferenceAfterMerge(Project into, Project from) {
	}*/
}
