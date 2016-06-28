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
import org.catrobat.catroid.formulaeditor.UserList;

import java.util.List;

public class InsertItemIntoUserListBrick extends UserListBrick {

	private static final long serialVersionUID = 1L;

	public InsertItemIntoUserListBrick(Formula userListFormulaValueToInsert, Formula userListFormulaIndexToInsert, UserList userList) {
		initializeBrickFields(userListFormulaValueToInsert, userListFormulaIndexToInsert);
		this.userList = userList;
	}

	public InsertItemIntoUserListBrick(double value, Integer indexToInsert) {
		initializeBrickFields(new Formula(value), new Formula(indexToInsert));
	}

	private void initializeBrickFields(Formula userListFormulaValueToInsert, Formula userListFormulaIndexToInsert) {
		addAllowedBrickField(BrickField.INSERT_ITEM_INTO_USERLIST_VALUE);
		addAllowedBrickField(BrickField.INSERT_ITEM_INTO_USERLIST_INDEX);
		setFormulaWithBrickField(BrickField.INSERT_ITEM_INTO_USERLIST_VALUE, userListFormulaValueToInsert);
		setFormulaWithBrickField(BrickField.INSERT_ITEM_INTO_USERLIST_INDEX, userListFormulaIndexToInsert);
	}

	@Override
	public List<SequenceAction> addActionToSequence(Sprite sprite, SequenceAction sequence) {
		sequence.addAction(sprite.getActionFactory().createInsertItemIntoUserListAction(sprite,
				getFormulaWithBrickField(BrickField.INSERT_ITEM_INTO_USERLIST_INDEX),
				getFormulaWithBrickField(BrickField.INSERT_ITEM_INTO_USERLIST_VALUE), userList));

		return null;
	}

	@Override
	public Brick clone() {
		InsertItemIntoUserListBrick clonedBrick = new InsertItemIntoUserListBrick(getFormulaWithBrickField(BrickField.INSERT_ITEM_INTO_USERLIST_VALUE).clone(), getFormulaWithBrickField(BrickField.INSERT_ITEM_INTO_USERLIST_INDEX).clone(), userList);
		clonedBrick.setBackPackedData(new UserListBrick.BackPackedData(backPackedData));
		return clonedBrick;
	}

	/*@Override
	public void onClick(View view) {
		if (checkbox.getVisibility() == View.VISIBLE) {
			return;
		}
		switch (view.getId()) {
			case R.id.brick_insert_item_into_userlist_at_index_edit_text:
				FormulaEditorFragment.showFragment(view, this, BrickField.INSERT_ITEM_INTO_USERLIST_INDEX);
				break;
			case R.id.brick_insert_item_into_userlist_value_edit_text:
				FormulaEditorFragment.showFragment(view, this, BrickField.INSERT_ITEM_INTO_USERLIST_VALUE);
				break;
		}
	}

	@Override
	public void showFormulaEditorToEditFormula(View view) {
		FormulaEditorFragment.showFragment(view, this, BrickField.INSERT_ITEM_INTO_USERLIST_INDEX);
	}

	@Override
	public void updateReferenceAfterMerge(Project into, Project from) {
		super.updateUserListReference(into, from);
	}*/

	@Override
	public Brick copyBrickForSprite(Sprite sprite) {
		Project currentProject = ProjectManager.getInstance().getCurrentProject();
		if (currentProject == null) {
			throw new RuntimeException("The current project must be set before cloning it");
		}

		InsertItemIntoUserListBrick copyBrick = (InsertItemIntoUserListBrick) clone();
		copyBrick.userList = currentProject.getDataContainer().getUserList(userList.getName(), sprite);
		return copyBrick;
	}
}
