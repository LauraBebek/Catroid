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

import android.view.View;

import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;

import org.catrobat.catroid.content.Sprite;
import org.catrobat.catroid.formulaeditor.Formula;
import org.catrobat.catroid.ui.fragment.FormulaEditorFragment;

import java.util.List;

public class RaspiSendDigitalValueBrick extends FormulaBrick {

	private static final long serialVersionUID = 1L;

	public RaspiSendDigitalValueBrick() {
		addAllowedBrickField(BrickField.RASPI_DIGITAL_PIN_NUMBER);
		addAllowedBrickField(BrickField.RASPI_DIGITAL_PIN_VALUE);
	}

	public RaspiSendDigitalValueBrick(int pinNumber, int pinValue) {
		initializeBrickFields(new Formula(pinNumber), new Formula(pinValue));
	}

	public RaspiSendDigitalValueBrick(Formula pinNumber, Formula pinValue) {
		initializeBrickFields(pinNumber, pinValue);
	}

	public RaspiSendDigitalValueBrick(int pinNumber, String pinValue) {
		initializeBrickFields(new Formula(pinNumber), new Formula(pinValue));
	}

	private void initializeBrickFields(Formula pinNumber, Formula pinValue) {
		addAllowedBrickField(BrickField.RASPI_DIGITAL_PIN_NUMBER);
		addAllowedBrickField(BrickField.RASPI_DIGITAL_PIN_VALUE);
		setFormulaWithBrickField(BrickField.RASPI_DIGITAL_PIN_NUMBER, pinNumber);
		setFormulaWithBrickField(BrickField.RASPI_DIGITAL_PIN_VALUE, pinValue);
	}

	@Override
	public int getRequiredResources() {
		return SOCKET_RASPI
				| getFormulaWithBrickField(BrickField.RASPI_DIGITAL_PIN_NUMBER).getRequiredResources()
				| getFormulaWithBrickField(BrickField.RASPI_DIGITAL_PIN_VALUE).getRequiredResources();
	}

	@Override
	public List<SequenceAction> addActionToSequence(Sprite sprite, SequenceAction sequence) {
		sequence.addAction(sprite.getActionFactory().createSendDigitalRaspiValueAction(sprite,
				getFormulaWithBrickField(BrickField.RASPI_DIGITAL_PIN_NUMBER),
				getFormulaWithBrickField(BrickField.RASPI_DIGITAL_PIN_VALUE)));
		return null;
	}

	public void showFormulaEditorToEditFormula(View view) {
		FormulaEditorFragment.showFragment(view, this, BrickField.RASPI_DIGITAL_PIN_NUMBER);
	}
}
