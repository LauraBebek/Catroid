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

import org.catrobat.catroid.ProjectManager;
import org.catrobat.catroid.content.Project;
import org.catrobat.catroid.content.Sprite;
import org.catrobat.catroid.formulaeditor.Formula;
import org.catrobat.catroid.formulaeditor.FormulaElement;
import org.catrobat.catroid.formulaeditor.Sensors;
import org.catrobat.catroid.formulaeditor.UserVariable;

import java.util.List;

public class SetVariableBrick extends UserVariableBrick {

	private static final long serialVersionUID = 1L;

	private transient String defaultPrototypeToken = null;

	public SetVariableBrick() {
		addAllowedBrickField(BrickField.VARIABLE);
	}

	public SetVariableBrick(Formula variableFormula, UserVariable userVariable) {
		this.userVariable = userVariable;
		initializeBrickFields(variableFormula);
	}

	public SetVariableBrick(Sensors defaultValue) {
		this.userVariable = null;
		Formula variableFormula = new Formula(new FormulaElement(FormulaElement.ElementType.SENSOR, defaultValue.name(), null));
		initializeBrickFields(variableFormula);
		defaultPrototypeToken = defaultValue.name();
	}

	public SetVariableBrick(double value) {
		this.userVariable = null;
		initializeBrickFields(new Formula(value));
	}

	private void initializeBrickFields(Formula variableFormula) {
		addAllowedBrickField(BrickField.VARIABLE);
		setFormulaWithBrickField(BrickField.VARIABLE, variableFormula);
	}

	public void setUserVariable(UserVariable userVariable) {
		this.userVariable = userVariable;
	}

	public UserVariable getUserVariable() {
		return userVariable;
	}

	@Override
	public int getRequiredResources() {
		return getFormulaWithBrickField(BrickField.VARIABLE).getRequiredResources();
	}

	@Override
	public List<SequenceAction> addActionToSequence(Sprite sprite, SequenceAction sequence) {
		sequence.addAction(sprite.getActionFactory().createSetVariableAction(sprite,
				getFormulaWithBrickField(BrickField.VARIABLE), userVariable));
		return null;
	}

	@Override
	public SetVariableBrick copyBrickForSprite(Sprite sprite) {
		SetVariableBrick copyBrick = clone();
		if (userVariable != null) {
			copyBrick.userVariable = userVariable;
		}

		return copyBrick;
	}

	@Override
	public SetVariableBrick clone() {
		SetVariableBrick clonedBrick = new SetVariableBrick(getFormulaWithBrickField(BrickField.VARIABLE)
				.clone(), userVariable);
		clonedBrick.setBackPackedData(backPackedData);
		return clonedBrick;
	}
}
