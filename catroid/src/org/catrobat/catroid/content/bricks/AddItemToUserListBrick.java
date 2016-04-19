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

import org.catrobat.catroid.content.Project;
import org.catrobat.catroid.content.Sprite;
import org.catrobat.catroid.formulaeditor.Formula;
import org.catrobat.catroid.formulaeditor.UserList;
import org.catrobat.catroid.ui.fragment.FormulaEditorFragment;

import java.util.List;

public class AddItemToUserListBrick extends UserListBrick {

	private static final long serialVersionUID = 1L;

	public AddItemToUserListBrick() {
		addAllowedBrickField(BrickField.LIST_ADD_ITEM);
	}

	public AddItemToUserListBrick(Formula userListFormula, UserList userList) {
		initializeBrickFields(userListFormula);
		this.userList = userList;
	}

	public AddItemToUserListBrick(double value) {
		initializeBrickFields(new Formula(value));
	}

	@Override
	public List<SequenceAction> addActionToSequence(Sprite sprite, SequenceAction sequence) {
		sequence.addAction(sprite.getActionFactory().createAddItemToUserListAction(sprite,
				getFormulaWithBrickField(BrickField.LIST_ADD_ITEM), userList));
		return null;
	}

	@Override
	public Brick clone() {
		AddItemToUserListBrick clonedBrick = new AddItemToUserListBrick(getFormulaWithBrickField(BrickField.LIST_ADD_ITEM).clone(), userList);
		clonedBrick.setBackPackedData(new UserListBrick.BackPackedData(backPackedData));
		return clonedBrick;
	}

	private void initializeBrickFields(Formula listAddItemFormula) {
		addAllowedBrickField(BrickField.LIST_ADD_ITEM);
		setFormulaWithBrickField(BrickField.LIST_ADD_ITEM, listAddItemFormula);
	}

	/*@Override
	public void showFormulaEditorToEditFormula(View view) {
		FormulaEditorFragment.showFragment(view, this, getFormula());
	}

	@Override
	public void updateReferenceAfterMerge(Project into, Project from) {
		super.updateUserListReference(into, from);
	}*/
}
