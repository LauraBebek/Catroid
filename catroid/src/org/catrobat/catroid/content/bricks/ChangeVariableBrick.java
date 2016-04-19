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
import org.catrobat.catroid.formulaeditor.UserVariable;

import java.util.List;

public class ChangeVariableBrick extends UserVariableBrick {

	private static final long serialVersionUID = 1L;

	public ChangeVariableBrick() {
		addAllowedBrickField(BrickField.VARIABLE_CHANGE);
	}

	public ChangeVariableBrick(Formula variableFormula) {
		initializeBrickFields(variableFormula);
	}

	public ChangeVariableBrick(Formula variableFormula, UserVariable userVariable) {
		initializeBrickFields(variableFormula);
		this.userVariable = userVariable;
	}

	public ChangeVariableBrick(double value) {
		initializeBrickFields(new Formula(value));
	}

	public ChangeVariableBrick(Formula variableFormula, UserVariable userVariable, boolean inUserBrick) {
		initializeBrickFields(variableFormula);
		this.userVariable = userVariable;
		this.inUserBrick = inUserBrick;
	}

	private void initializeBrickFields(Formula variableFormula) {
		addAllowedBrickField(BrickField.VARIABLE_CHANGE);
		setFormulaWithBrickField(BrickField.VARIABLE_CHANGE, variableFormula);
	}

	@Override
	public int getRequiredResources() {
		return getFormulaWithBrickField(BrickField.VARIABLE_CHANGE).getRequiredResources();
	}

	@Override
	public ChangeVariableBrick copyBrickForSprite(Sprite sprite) {
		Project currentProject = ProjectManager.getInstance().getCurrentProject();
		ChangeVariableBrick copyBrick = clone();
		if (this.userVariable != null) {
			copyBrick.userVariable = currentProject.getDataContainer().getUserVariable(userVariable.getName(), sprite);
		}
		return copyBrick;
	}

	@Override
	public ChangeVariableBrick clone() {
		ChangeVariableBrick clonedBrick = new ChangeVariableBrick(getFormulaWithBrickField(
				BrickField.VARIABLE_CHANGE).clone(), userVariable, inUserBrick);
		clonedBrick.setBackPackedData(new UserVariableBrick.BackPackedData(backPackedData));
		return clonedBrick;
	}

	@Override
	public List<SequenceAction> addActionToSequence(Sprite sprite, SequenceAction sequence) {
		sequence.addAction(sprite.getActionFactory().createChangeVariableAction(sprite, getFormulaWithBrickField(BrickField.VARIABLE_CHANGE), userVariable));
		return null;
	}

	/*public void showFormulaEditorToEditFormula(View view) {
		FormulaEditorFragment.showFragment(view, this, BrickField.VARIABLE_CHANGE);
	}

	@Override
	public void updateReferenceAfterMerge(Project into, Project from) {
		super.updateUserVariableReference(into, from);
	}*/
}
