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

public class ChangeTransparencyByNBrick extends FormulaBrick {

	private static final long serialVersionUID = 1L;

	public ChangeTransparencyByNBrick() {
		addAllowedBrickField(BrickField.TRANSPARENCY_CHANGE);
	}

	public ChangeTransparencyByNBrick(double changeTransparencyValue) {
		initializeBrickFields(new Formula(changeTransparencyValue));
	}

	public ChangeTransparencyByNBrick(Formula changeTransparency) {
		initializeBrickFields(changeTransparency);
	}

	private void initializeBrickFields(Formula changeTransparency) {
		addAllowedBrickField(BrickField.TRANSPARENCY_CHANGE);
		setFormulaWithBrickField(BrickField.TRANSPARENCY_CHANGE, changeTransparency);
	}

	@Override
	public int getRequiredResources() {
		return getFormulaWithBrickField(BrickField.TRANSPARENCY_CHANGE).getRequiredResources();
	}

	@Override
	public List<SequenceAction> addActionToSequence(Sprite sprite, SequenceAction sequence) {
		sequence.addAction(sprite.getActionFactory().createChangeTransparencyByNAction(sprite,
				getFormulaWithBrickField(BrickField.TRANSPARENCY_CHANGE)));
		return null;
	}

	/*@Override
	public void showFormulaEditorToEditFormula(View view) {
		FormulaEditorFragment.showFragment(view, this, BrickField.TRANSPARENCY_CHANGE);
	}

	@Override
	public void updateReferenceAfterMerge(Project into, Project from) {
	}*/
}
