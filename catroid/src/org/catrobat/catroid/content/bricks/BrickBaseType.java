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
import android.util.Log;
import android.view.View;
import android.widget.BaseAdapter;

import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;

import org.catrobat.catroid.content.Project;
import org.catrobat.catroid.content.Sprite;

import java.util.List;

public abstract class BrickBaseType implements Brick {
	private static final long serialVersionUID = 1L;
	private static final String TAG = BrickBaseType.class.getSimpleName();

	@Override
	public boolean isEqualBrick(Brick brick, Project mergeResult, Project current) {
		if (this.getClass().equals(brick.getClass())) {
			return true;
		}
		return false;
	}

	@Override
	public Brick clone() throws CloneNotSupportedException {
		return (Brick) super.clone();
	}

	@Override
	public Brick copyBrickForSprite(Sprite sprite) {
		BrickBaseType copyBrick = null;
		try {
			copyBrick = (BrickBaseType) clone();
		} catch (CloneNotSupportedException exception) {
			Log.e(TAG, Log.getStackTraceString(exception));
		}
		return copyBrick;
	}

	@Override
	public int getRequiredResources() {
		return NO_RESOURCES;
	}

	public View getView(Context context, int brickId, BaseAdapter adapter) {
		return null;
	}

	@Override
	public List<SequenceAction> addActionToSequence(Sprite sprite, SequenceAction sequence) {
		return null;
	}

	@Override
	public void storeDataForBackPack(Sprite sprite) { }
}
