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
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;

import org.catrobat.catroid.ProjectManager;
import org.catrobat.catroid.R;
import org.catrobat.catroid.common.MessageContainer;
import org.catrobat.catroid.content.BroadcastMessage;
import org.catrobat.catroid.content.CollisionScript;
import org.catrobat.catroid.content.Project;
import org.catrobat.catroid.content.Script;
import org.catrobat.catroid.content.Sprite;
import org.catrobat.catroid.content.bricks.Brick;
import org.catrobat.catroid.content.bricks.ScriptBrick;
import org.catrobat.catroid.content.bricks.SpinnerBrick;
import org.catrobat.catroid.physics.PhysicsCollision;

import java.util.List;

public class CollisionReceiverBrick extends ScriptBrick implements BroadcastMessage, Cloneable {
	private static final long serialVersionUID = 1L;
	public static final String ANYTHING_ESCAPE_CHAR = "\0";

	private CollisionScript collisionScript;
	private transient String selectedMessage;
	ArrayAdapter<String> messageAdapter;

	public CollisionReceiverBrick(String spriteName) {
		this.selectedMessage = spriteName;
	}

	public CollisionReceiverBrick(CollisionScript collisionScript) {
		this.collisionScript = collisionScript;
		this.selectedMessage = "";
	}

	@Override
	public Brick copyBrickForSprite(Sprite sprite) {
		CollisionReceiverBrick copyBrick = (CollisionReceiverBrick) clone();
		copyBrick.collisionScript = collisionScript;
		return copyBrick;
	}

	@Override
	public Brick clone() {
		return new CollisionReceiverBrick(new CollisionScript(getBroadcastMessage()));
	}

	@Override
	public int getRequiredResources() {
		return PHYSICS;
	}

	@Override
	public String getBroadcastMessage() {
		if (collisionScript == null) {
			return selectedMessage;
		}
		return collisionScript.getBroadcastMessage();
	}

	@Override
	public Script getScriptSafe() {
		return collisionScript;
	}

	@Override
	public List<SequenceAction> addActionToSequence(Sprite sprite, SequenceAction sequence) {
		return null;
	}

	public void setSelectedMessage(String msg) {
		this.selectedMessage = msg;
	}

	public void setCollisionScript(CollisionScript script) {
		this.collisionScript = script;
	}

	public void initCollisionScript() {
		if (collisionScript == null) {
			collisionScript = new CollisionScript(selectedMessage);
			MessageContainer.addMessage(getBroadcastMessage());
		}
	}

	public CollisionScript getCollisionScript() {
		return this.collisionScript;
	}

	public String getSelectedMessage() {
		return this.selectedMessage;
	}

	public void setSpinnerSelection(Spinner spinner) {
		String broadcastMessage = getBroadcastMessage();
		if (broadcastMessage == null || broadcastMessage.equals("")) {
			spinner.setSelection(0);
		} else if (collisionScript != null && collisionScript.getBroadcastMessage().equals(broadcastMessage)) {
			CollisionScript.CollisionObjectIdentifier identifier = collisionScript.splitBroadcastMessage();
			int position = getPositionOfMessageInAdapter(spinner.getContext(), identifier.getCollisionObjectTwoIdentifier());
			spinner.setSelection(position);
		} else {
			int position = getPositionOfMessageInAdapter(spinner.getContext(), broadcastMessage);
			spinner.setSelection(position);
		}
	}

	public int getPositionOfMessageInAdapter(Context context, String message) {
		getCollisionObjectAdapter(context);
		int position = messageAdapter.getPosition(message);
		if (position == -1) {
			return 0;
		} else {
			return position;
		}
	}

	public ArrayAdapter<String> getCollisionObjectAdapter(Context context) {
		Project project = ProjectManager.getInstance().getCurrentProject();
		String spriteName = ProjectManager.getInstance().getCurrentSprite().getName();
		messageAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item);
		messageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		messageAdapter.add(getDisplayedAnythingString(context));
		int resources = Brick.NO_RESOURCES;
		for (Sprite sprite : project.getSpriteList()) {
			if (!spriteName.equals(sprite.getName())) {
				resources |= sprite.getRequiredResources();
				if ((resources & Brick.PHYSICS) > 0 && messageAdapter.getPosition(sprite.getName()) < 0) {
					messageAdapter.add(sprite.getName());
					resources &= ~Brick.PHYSICS;
				}
			}
		}
		return messageAdapter;
	}

	private String getDisplayedAnythingString(Context context) {
		return ANYTHING_ESCAPE_CHAR + context.getString(R.string.collision_with_anything) + ANYTHING_ESCAPE_CHAR;
	}
}
