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
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;

import org.catrobat.catroid.ProjectManager;
import org.catrobat.catroid.R;
import org.catrobat.catroid.common.NfcTagData;
import org.catrobat.catroid.content.Script;
import org.catrobat.catroid.content.Sprite;
import org.catrobat.catroid.content.WhenNfcScript;
import org.catrobat.catroid.ui.ScriptActivity;
import org.catrobat.catroid.ui.fragment.NfcTagFragment;

import java.util.List;

public class WhenNfcBrick extends ScriptBrick implements NfcTagFragment.OnNfcTagDataListChangedAfterNewListener, SpinnerBrick {
	protected WhenNfcScript whenNfcScript;
	private transient NfcTagData nfcTag;
	private transient NfcTagData oldSelectedNfcTag;
	private static final long serialVersionUID = 1L;

	private int spinnerSelectionID;
	private final int layoutId = R.layout.brick_when_nfc;
	private final int spinnerId = R.id.brick_when_nfc_spinner;

	public WhenNfcBrick() {
		this.oldSelectedNfcTag = null;
		this.nfcTag = null;
		this.whenNfcScript = new WhenNfcScript();
		this.whenNfcScript.setMatchAll(true);
	}

	public WhenNfcBrick(String tagName, String tagUid) {
		this.oldSelectedNfcTag = null;
		this.nfcTag = new NfcTagData();
		this.nfcTag.setNfcTagName(tagName);
		this.nfcTag.setNfcTagUid(tagUid);
		this.whenNfcScript = new WhenNfcScript(nfcTag);
		this.whenNfcScript.setMatchAll(false);
	}

	public WhenNfcBrick(WhenNfcScript script) {
		this.oldSelectedNfcTag = null;
		this.nfcTag = script.getNfcTag();
		this.whenNfcScript = script;
	}

	@Override
	public Brick copyBrickForSprite(Sprite sprite) {
		WhenNfcBrick copyBrick = (WhenNfcBrick) clone();

		for (NfcTagData data : sprite.getNfcTagList()) {
			if (data.getNfcTagUid().equals(nfcTag.getNfcTagUid())) {
				copyBrick.nfcTag = data;
				break;
			}
		}
		copyBrick.whenNfcScript = whenNfcScript;
		return copyBrick;
	}

	@Override
	public Script getScriptSafe() {
		if (whenNfcScript == null) {
			setWhenNfcScript(new WhenNfcScript(nfcTag));
		}
		return whenNfcScript;
	}

	@Override
	public Brick clone() {
		return new WhenNfcBrick(new WhenNfcScript(nfcTag));
	}

	@Override
	public int getRequiredResources() {
		return NFC_ADAPTER;
	}

	@Override
	public void onNfcTagDataListChangedAfterNew(NfcTagData nfcTagData) {
		oldSelectedNfcTag = nfcTagData;
		setNfcTag(nfcTagData);
	}

	@Override
	public List<SequenceAction> addActionToSequence(Sprite sprite, SequenceAction sequence) {
		return null;
	}

	public NfcTagData getNfcTag() {
		return nfcTag;
	}

	public void setNfcTag(NfcTagData nfcTagData) {
		this.nfcTag = nfcTagData;
	}

	public WhenNfcScript getWhenNfcScript() {
		return whenNfcScript;
	}

	public void setWhenNfcScript(WhenNfcScript whenNfcScript) {
		this.whenNfcScript = whenNfcScript;
	}

	@Override
	public ArrayAdapter<CharSequence> createArrayAdapter(Context context) {

		ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(context, R.array.nxt_motor_chooser,
				android.R.layout.simple_spinner_item);
		spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		return spinnerAdapter;
	}

	public void  setSelectedSpinnerItem(int position) {
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
