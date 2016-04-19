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
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;

import org.catrobat.catroid.ProjectManager;
import org.catrobat.catroid.R;
import org.catrobat.catroid.common.ScreenValues;
import org.catrobat.catroid.content.Script;
import org.catrobat.catroid.content.Sprite;
import org.catrobat.catroid.content.StartScript;
import org.catrobat.catroid.formulaeditor.DataContainer;
import org.catrobat.catroid.formulaeditor.Formula;
import org.catrobat.catroid.ui.dragndrop.DragAndDropListView;
import org.catrobat.catroid.ui.fragment.UserBrickElementEditorFragment;
import org.catrobat.catroid.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class UserScriptDefinitionBrick extends ScriptBrick { //} implements OnClickListener {

	private static final long serialVersionUID = 1L;
	private static final String LINE_BREAK = "linebreak";

	private StartScript script;
	private UserScriptDefinitionBrickElements userScriptDefinitionBrickElements;

	private transient UserBrick brick; //TODO: remove this when bitmap is loaded differently (double reference)

	public UserScriptDefinitionBrick(UserBrick brick) {
		this.script = new StartScript(true);
		this.brick = brick;
		this.userScriptDefinitionBrickElements = new UserScriptDefinitionBrickElements();
	}

	public int getUserBrickId() {
		return brick.getUserBrickId();
	}

	public void setUserBrick(UserBrick brick) {
		this.brick = brick;
	}

	public UserBrick getBrick() {
		return  brick;
	}

	public int getRequiredResources() {
		int resources = Brick.NO_RESOURCES;

		for (Brick brick : script.getBrickList()) {
			if (brick instanceof UserBrick && ((UserBrick) brick).getDefinitionBrick() == this) {
				continue;
			}
			resources |= brick.getRequiredResources();
		}
		return resources;
	}

	public void appendBrickToScript(Brick brick) {
		this.getScriptSafe().addBrick(brick);
	}

	@Override
	public Brick copyBrickForSprite(Sprite sprite) {
		UserScriptDefinitionBrick clonedBrick = new UserScriptDefinitionBrick();
		clonedBrick.userScriptDefinitionBrickElements = cloneDefinitionBrickElements();
		for (Brick brick : this.script.getBrickList()) {
			clonedBrick.script.addBrick(brick.copyBrickForSprite(sprite));
		}

		return clonedBrick;
	}

	public List<UserScriptDefinitionBrickElement> cloneDefinitionBrickElements() {
		List<UserScriptDefinitionBrickElement> cloneList = new ArrayList<>();
		for (UserScriptDefinitionBrickElement originalUserBrickElement : userScriptDefinitionBrickElements) {
			UserScriptDefinitionBrickElement clonedUserBrickElement = new UserScriptDefinitionBrickElement();
			clonedUserBrickElement.setText(originalUserBrickElement.getText());
			clonedUserBrickElement.setElementType(originalUserBrickElement.getElementType());
			clonedUserBrickElement.setNewLineHint(originalUserBrickElement.isNewLineHint());
			cloneList.add(clonedUserBrickElement);
		}
		return cloneList;
	}

	@Override
	public List<SequenceAction> addActionToSequence(Sprite sprite, SequenceAction sequence) {
		return null;
	}

	@Override
	public Brick clone() {
		return new UserScriptDefinitionBrick(brick);
	}

	@Override
	public Script getScriptSafe() {
		if (getUserScript() == null) {
			script.addBrick(this);
		}

		return getUserScript();
	}

	public Script getUserScript() {
		return script;
	}

	public void setUserScript(StartScript script) {
		this.script = script;
	}

	public int addUIText(String text) {
		UserScriptDefinitionBrickElement element = new UserScriptDefinitionBrickElement();
		element.setIsText();
		element.setText(text);
		int toReturn = userScriptDefinitionBrickElements.size();
		userScriptDefinitionBrickElements.add(element);
		return toReturn;
	}

	public void addUILineBreak() {
		UserScriptDefinitionBrickElement element = new UserScriptDefinitionBrickElement();
		element.setIsLineBreak();
		element.setText(LINE_BREAK);
		userScriptDefinitionBrickElements.add(element);
	}

	public int addUILocalizedVariable(String name) {
		UserScriptDefinitionBrickElement element = new UserScriptDefinitionBrickElement();
		element.setIsVariable();
		element.setText(name);

		int toReturn = userScriptDefinitionBrickElements.size();
		userScriptDefinitionBrickElements.add(element);
		return toReturn;
	}

	public void renameUIElement(UserScriptDefinitionBrickElement element, String oldName, String newName, Context context) {
		if (element.getText().equals(oldName)) {
			element.setText(newName);
			if (element.isVariable()) {
				Project currentProject = ProjectManager.getInstance().getCurrentProject();
				Sprite currentSprite = ProjectManager.getInstance().getCurrentSprite();
				DataContainer dataContainer = currentProject.getDataContainer();
				if (dataContainer != null) {
					List<UserBrick> matchingBricks = currentSprite.getUserBricksByDefinitionBrick(this, true, true);
					for (UserBrick userBrick : matchingBricks) {
						UserVariable userVariable = dataContainer.getUserVariable(oldName, userBrick, currentSprite);
						if (userVariable != null) {
							userVariable.setName(newName);
						}
					}
				}
			}
		}

		renameVariablesInFormulasAndBricks(oldName, newName, context);
	}

	public void removeDataAt(int id, Context context) {
		removeVariablesInFormulas(getUserScriptDefinitionBrickElements().get(id).getText(), context);
		userScriptDefinitionBrickElements.remove(id);
	}

	/**
	 * Removes element at <b>from</b> and adds it after element at <b>to</b>
	 */
	public void reorderUIData(int from, int to) {

		if (to == -1) {
			UserScriptDefinitionBrickElement element = getUserScriptDefinitionBrickElements().remove(from);
			userScriptDefinitionBrickElements.add(0, element);
		} else if (from <= to) {
			UserScriptDefinitionBrickElement element = getUserScriptDefinitionBrickElements().remove(from);
			userScriptDefinitionBrickElements.add(to, element);
		} else {
			// from > to
			UserScriptDefinitionBrickElement element = getUserScriptDefinitionBrickElements().remove(from);
			userScriptDefinitionBrickElements.add(to + 1, element);
		}
	}

	public CharSequence getName() {
		CharSequence name = "";
		for (UserScriptDefinitionBrickElement element : getUserScriptDefinitionBrickElements()) {
			if (!element.isVariable()) {
				name = element.getText();
				break;
			}
		}
		return name;
	}

	public List<UserScriptDefinitionBrickElement> getUserScriptDefinitionBrickElements() {
		return userScriptDefinitionBrickElements;
	}

	public void renameVariablesInFormulasAndBricks(String oldName, String newName, Context context) {
		List<Brick> brickList = script.getBrickList();
		for (Brick brick : brickList) {
			if (brick instanceof UserBrick) {
				List<Formula> formulaList = ((UserBrick) brick).getFormulas();
				for (Formula formula : formulaList) {
					formula.updateVariableReferences(oldName, newName, context);
				}
			}
			if (brick instanceof FormulaBrick) {
				List<Formula> formulas = ((FormulaBrick) brick).getFormulas();
				for (Formula formula : formulas) {
					formula.updateVariableReferences(oldName, newName, context);
				}
			}
			if (brick instanceof ShowTextBrick) {
				ShowTextBrick showTextBrick = (ShowTextBrick) brick;
				if (showTextBrick.getUserVariable().getName().equals(oldName)) {
					((ShowTextBrick) brick).setUserVariableName(newName);
				}
			}
			if (brick instanceof HideTextBrick) {
				HideTextBrick showTextBrick = (HideTextBrick) brick;
				if (showTextBrick.getUserVariable().getName().equals(oldName)) {
					((HideTextBrick) brick).setUserVariableName(newName);
				}
			}
		}
	}

	public void removeVariablesInFormulas(String name, Context context) {
		if (ProjectManager.getInstance().getCurrentScript() == null) {
			return;
		}
		List<Brick> brickList = ProjectManager.getInstance().getCurrentScript().getBrickList();
		for (Brick brick : brickList) {
			if (brick instanceof UserBrick) {
				List<Formula> formulaList = ((UserBrick) brick).getFormulas();
				for (Formula formula : formulaList) {
					formula.removeVariableReferences(name, context);
				}
			}
			if (brick instanceof FormulaBrick) {
				List<Formula> formulas = ((FormulaBrick) brick).getFormulas();
				for (Formula formula : formulas) {
					formula.removeVariableReferences(name, context);
				}
			}
		}
	}
}
