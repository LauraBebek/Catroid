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

import org.catrobat.catroid.ProjectManager;
import org.catrobat.catroid.R;
import org.catrobat.catroid.common.LookData;
import org.catrobat.catroid.content.Sprite;

import java.util.List;

public class SetLookBrick extends BrickBaseType {
	private static final long serialVersionUID = 1L;
	private LookData look;
	private transient LookData oldSelectedLook;

	public SetLookBrick() {
	}

	public void setLook(LookData lookData) {
		this.look = lookData;
	}

	public LookData getLook() {
		return this.look;
	}

	@Override
	public Brick copyBrickForSprite(Sprite sprite) {
		SetLookBrick copyBrick = (SetLookBrick) clone();

		if (look != null && look.isBackpackLookData) {
			copyBrick.look = look.clone();
			return copyBrick;
		}

		for (LookData data : sprite.getLookDataList()) {
			if (look != null && data != null && data.getAbsolutePath().equals(look.getAbsolutePath())) {
				copyBrick.look = data.clone();
				break;
			}
		}
		copyBrick.look.isBackpackLookData = false;
		return copyBrick;
	}

	public String getImagePath() {
		return look.getAbsolutePath();
	}

	@Override
	public Brick clone() {
		SetLookBrick clonedBrick = new SetLookBrick();
		clonedBrick.setLook(look);
		return clonedBrick;
	}

	@Override
	public List<SequenceAction> addActionToSequence(Sprite sprite, SequenceAction sequence) {
		sequence.addAction(sprite.getActionFactory().createSetLookAction(sprite, look));
		return null;
	}

	public void setOldSelectedLook(LookData oldSelectedLook) {
		this.oldSelectedLook = oldSelectedLook;
	}

	public LookData getOldSelectedLook() {
		return oldSelectedLook;
	}
}
