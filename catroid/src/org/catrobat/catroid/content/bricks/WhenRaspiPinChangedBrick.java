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

import org.catrobat.catroid.common.BrickValues;
import org.catrobat.catroid.content.RaspiInterruptScript;
import org.catrobat.catroid.content.Sprite;

import java.util.List;

public class WhenRaspiPinChangedBrick extends ScriptBrick {
	private static final long serialVersionUID = 1L;

	private RaspiInterruptScript script;

	private String pinString = Integer.toString(BrickValues.RASPI_DIGITAL_INITIAL_PIN_NUMBER);
	private String eventString = BrickValues.RASPI_PRESSED_EVENT;

	public WhenRaspiPinChangedBrick(RaspiInterruptScript script) {
		this.script = script;
		if (script != null) {
			pinString = script.getPin();
			eventString = script.getEventValue();
		}
	}

	@Override
	public Brick copyBrickForSprite(Sprite sprite) {
		WhenRaspiPinChangedBrick copyBrick = (WhenRaspiPinChangedBrick) clone();
		copyBrick.script = script;
		return copyBrick;
	}

	public void setPinSelected(String pin) {
		pinString = pin;
		getScriptSafe().setPin(pinString);
	}

	public String getPinSelected() {
		return pinString;
	}

	public void setEventSelected(String event) {
		eventString = event;
		getScriptSafe().setEventValue(eventString);
	}

	public String getEventSelected() {
		return eventString;
	}

	@Override
	public Brick clone() {
		return new WhenRaspiPinChangedBrick(script);
	}

	@Override
	public RaspiInterruptScript getScriptSafe() {
		if (script == null) {
			script = new RaspiInterruptScript(getPinString(), getEventString());
		}

		return script;
	}

	public String getPinString() {
		if (script == null) {
			return pinString;
		}
		return script.getPin();
	}

	public String getEventString() {
		if (script == null) {
			return eventString;
		}
		return script.getEventValue();
	}

	@Override
	public List<SequenceAction> addActionToSequence(Sprite sprite, SequenceAction sequence) {
		return null;
	}
}
