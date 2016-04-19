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

import org.catrobat.catroid.R;
import org.catrobat.catroid.content.Sprite;
import org.catrobat.catroid.formulaeditor.Formula;

import java.util.List;

public class PhiroPlayToneBrick extends FormulaBrick implements SpinnerBrick {

	private static final long serialVersionUID = 1L;

	private String tone;
	private transient Tone toneEnum;

	private int spinnerSelectionID;
	private final int layoutId = R.layout.brick_phiro_play_tone;
	private final int spinnerId = R.id.brick_phiro_select_tone_spinner;

	public static enum Tone {
		DO, RE, MI, FA, SO, LA, TI
	}

	public PhiroPlayToneBrick() {
		addAllowedBrickField(BrickField.PHIRO_DURATION_IN_SECONDS);
	}

	public PhiroPlayToneBrick(Tone tone, int durationValue) {
		this.toneEnum = tone;
		this.tone = toneEnum.name();
		initializeBrickFields(new Formula(durationValue));
	}

	public PhiroPlayToneBrick(Tone tone, Formula durationFormula) {
		this.toneEnum = tone;
		this.tone = toneEnum.name();
		initializeBrickFields(durationFormula);
	}

	protected Object readResolve() {
		if (tone != null) {
			toneEnum = Tone.valueOf(tone);
		}
		return this;
	}

	private void initializeBrickFields(Formula duration) {
		addAllowedBrickField(BrickField.PHIRO_DURATION_IN_SECONDS);
		setFormulaWithBrickField(BrickField.PHIRO_DURATION_IN_SECONDS, duration);
	}

	@Override
	public int getRequiredResources() {
		return BLUETOOTH_PHIRO | getFormulaWithBrickField(BrickField.PHIRO_DURATION_IN_SECONDS).getRequiredResources();
	}

	@Override
	public Brick clone() {
		return new PhiroPlayToneBrick(toneEnum,
				getFormulaWithBrickField(BrickField.PHIRO_DURATION_IN_SECONDS).clone());
	}

	@Override
	public List<SequenceAction> addActionToSequence(Sprite sprite, SequenceAction sequence) {
		sequence.addAction(sprite.getActionFactory().createPhiroPlayToneActionAction(sprite, toneEnum,
				getFormulaWithBrickField(BrickField.PHIRO_DURATION_IN_SECONDS)));
		sequence.addAction(sprite.getActionFactory().createDelayAction(sprite, getFormulaWithBrickField(BrickField
				.PHIRO_DURATION_IN_SECONDS)));
		return null;
	}

	/*@Override
	public void showFormulaEditorToEditFormula(View view) {
		FormulaEditorFragment.showFragment(view, this, BrickField.PHIRO_DURATION_IN_SECONDS);
	}

	@Override
	public void updateReferenceAfterMerge(Project into, Project from) {
	}*/

	public ArrayAdapter<CharSequence> createArrayAdapter(Context context) {
		ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(context,
				R.array.brick_phiro_select_tone_spinner, android.R.layout.simple_spinner_item);
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
