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
package org.catrobat.catroid.physics.content.bricks;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;

import org.catrobat.catroid.R;
import org.catrobat.catroid.content.Sprite;
import org.catrobat.catroid.content.bricks.Brick;
import org.catrobat.catroid.content.bricks.BrickBaseType;
import org.catrobat.catroid.content.bricks.SpinnerBrick;
import org.catrobat.catroid.physics.PhysicsObject;

import java.util.List;

public class SetPhysicsObjectTypeBrick extends BrickBaseType implements Cloneable, SpinnerBrick {
	private static final long serialVersionUID = 1L;

	private PhysicsObject.Type type = PhysicsObject.Type.NONE;

	private int spinnerSelectionID;
	private final int layoutId = R.layout.brick_phiro_if_sensor;
	private final int spinnerId = R.id.brick_phiro_sensor_action_spinner;

	public SetPhysicsObjectTypeBrick() {
	}

	public SetPhysicsObjectTypeBrick(PhysicsObject.Type type) {
		this.type = type;
	}

	@Override
	public int getRequiredResources() {
		return PHYSICS;
	}

	@Override
	public Brick clone() {
		return new SetPhysicsObjectTypeBrick(type);
	}

	@Override
	public List<SequenceAction> addActionToSequence(Sprite sprite, SequenceAction sequence) {
		sequence.addAction(sprite.getActionFactory().createSetPhysicsObjectTypeAction(sprite, type));
		return null;
	}

	@Override
	public Brick copyBrickForSprite(Sprite sprite) {
		SetPhysicsObjectTypeBrick copyBrick = (SetPhysicsObjectTypeBrick) clone();
		return copyBrick;
	}

	public ArrayAdapter<CharSequence> createArrayAdapter(Context context) {
		ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(context,
				R.array.physics_object_types, android.R.layout.simple_spinner_item);
		spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		return spinnerAdapter;
	}

	public void setSelectedSpinnerItem(int position) {
		spinnerSelectionID = position;
	}

	public int getSelectedSpinnerItem() {
		return spinnerSelectionID;
	}

	public int getLayoutId() {
		return layoutId;
	}

	public int getSpinnerId() {
		return spinnerId;
	}
}
