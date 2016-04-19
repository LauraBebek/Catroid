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

package org.catrobat.catroid.ui.bricks;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import org.catrobat.catroid.ProjectManager;
import org.catrobat.catroid.R;
import org.catrobat.catroid.physics.PhysicsCollision;
import org.catrobat.catroid.physics.content.bricks.CollisionReceiverBrick;

public class CollisionReceiverBrickViewProvider extends BrickViewProvider {

	//TODO:LAUSI setmessage richtig
	private transient CollisionReceiverBrick brick;

	public CollisionReceiverBrickViewProvider(Context context, LayoutInflater inflater) {
		super(context, inflater);
	}

	public View createCollisionReceiverBrickView(final CollisionReceiverBrick brick, ViewGroup parent) {
		this.brick = brick;
		final View view = inflateBrickView(parent, R.layout.brick_physics_collision_receive);

		brick.initCollisionScript();

		final Spinner broadcastSpinner = (Spinner) view.findViewById(R.id.brick_collision_receive_spinner);
		broadcastSpinner.setFocusableInTouchMode(false);

		broadcastSpinner.setAdapter(brick.getCollisionObjectAdapter(context));

		if (isPrototypeLayout()) {
			broadcastSpinner.setEnabled(false);
			broadcastSpinner.setClickable(false);
		} else {
			broadcastSpinner.setEnabled(true);
			broadcastSpinner.setClickable(true);
			broadcastSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
					String collisionObjectOneIdentifier = ProjectManager.getInstance().getCurrentSprite().getName();
					String collisionObjectTwoIdentifier = broadcastSpinner.getSelectedItem().toString();
					if (collisionObjectTwoIdentifier.equals(brick.getDisplayedAnythingString(context))) {
						collisionObjectTwoIdentifier = PhysicsCollision.COLLISION_WITH_ANYTHING_IDENTIFIER;
					}
					brick.setSelectedMessage(brick.getCollisionScript().setAndReturnBroadcastMessage(collisionObjectOneIdentifier,
							collisionObjectTwoIdentifier));
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
				}
			});
		}

		brick.setSpinnerSelection(broadcastSpinner);
		return view;
	}
}
