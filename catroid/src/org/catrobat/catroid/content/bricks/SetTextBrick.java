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

import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;

import org.catrobat.catroid.content.Sprite;
import org.catrobat.catroid.formulaeditor.Formula;

import java.util.List;

public class SetTextBrick extends FormulaBrick { //implements View.OnClickListener {
	private static final long serialVersionUID = 1L;

	public SetTextBrick() {
		addAllowedBrickField(BrickField.X_DESTINATION);
		addAllowedBrickField(BrickField.Y_DESTINATION);
		addAllowedBrickField(BrickField.STRING);
	}

	public SetTextBrick(int xDestinationValue, int yDestinationValue, String text) {
		initializeBrickFields(new Formula(xDestinationValue), new Formula(yDestinationValue), new Formula(text));
	}

	public SetTextBrick(Formula xDestination, Formula yDestination, Formula text) {
		initializeBrickFields(xDestination, yDestination, text);
	}

	private void initializeBrickFields(Formula xDestination, Formula yDestination, Formula text) {
		addAllowedBrickField(BrickField.X_DESTINATION);
		addAllowedBrickField(BrickField.Y_DESTINATION);
		addAllowedBrickField(BrickField.STRING);

		setFormulaWithBrickField(BrickField.X_DESTINATION, xDestination);
		setFormulaWithBrickField(BrickField.Y_DESTINATION, yDestination);
		setFormulaWithBrickField(BrickField.STRING, text);
	}

	public void setXDestination(Formula xDestination) {
		setFormulaWithBrickField(BrickField.X_DESTINATION, xDestination);
	}

	public void setYDestination(Formula yDestination) {
		setFormulaWithBrickField(BrickField.Y_DESTINATION, yDestination);
	}

	public void setText(Formula text) {
		setFormulaWithBrickField(BrickField.STRING, text);
	}

	@Override
	public int getRequiredResources() {
		return getFormulaWithBrickField(BrickField.X_DESTINATION).getRequiredResources()
				| getFormulaWithBrickField(BrickField.Y_DESTINATION).getRequiredResources()
				| getFormulaWithBrickField(BrickField.STRING).getRequiredResources();
	}

	/*@Override
	public void showFormulaEditorToEditFormula(View view) {
		FormulaEditorFragment.showFragment(view, this, BrickField.X_DESTINATION);
	}

	@Override
	public void updateReferenceAfterMerge(Project into, Project from) {
	}*/

	@Override
	public List<SequenceAction> addActionToSequence(Sprite sprite, SequenceAction sequence) {
		sequence.addAction(sprite.getActionFactory().createSetTextAction(sprite, getFormulaWithBrickField(BrickField.X_DESTINATION),
				getFormulaWithBrickField(BrickField.Y_DESTINATION),
				getFormulaWithBrickField(BrickField.STRING)));
		return null;
	}
}
