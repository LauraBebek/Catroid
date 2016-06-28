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
import android.content.res.ColorStateList;
import android.database.DataSetObserver;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;

import org.catrobat.catroid.ProjectManager;
import org.catrobat.catroid.R;
import org.catrobat.catroid.common.SoundInfo;
import org.catrobat.catroid.content.Sprite;
import org.catrobat.catroid.ui.ScriptActivity;
import org.catrobat.catroid.ui.controller.SoundController;
import org.catrobat.catroid.ui.fragment.SoundFragment;
import org.catrobat.catroid.ui.fragment.SoundFragment.OnSoundInfoListChangedAfterNewListener;

import java.util.List;

public class PlaySoundBrick extends BrickBaseType { //implements OnItemSelectedListener,
	//	OnSoundInfoListChangedAfterNewListener {
	private static final long serialVersionUID = 1L;

	private SoundInfo sound;
	private transient SoundInfo oldSelectedSound;

	public PlaySoundBrick() {
	}

	@Override
	public int getRequiredResources() {
		return NO_RESOURCES;
	}

	@Override
	public Brick copyBrickForSprite(Sprite sprite) {
		PlaySoundBrick copyBrick = (PlaySoundBrick) clone();

		if (sound != null && sound.isBackpackSoundInfo) {
			copyBrick.sound = sound.clone();
			copyBrick.sound.isBackpackSoundInfo = false;
			return copyBrick;
		}

		for (SoundInfo soundInfo : sprite.getSoundList()) {
			if (sound != null && soundInfo != null && soundInfo.getAbsolutePath().equals(sound.getAbsolutePath())) {
				copyBrick.sound = soundInfo.clone();
				copyBrick.sound.isBackpackSoundInfo = true;
				break;
			}
		}
		return copyBrick;
	}

	@Override
	public Brick clone() {
		return new PlaySoundBrick();
	}

	public void setSoundInfo(SoundInfo soundInfo) {
		this.sound = soundInfo;
	}

	/*public void onItemSelected(AdapterView<?> parent, View arg1, int position, long arg3) {
		if (position == 0) {
			sound = null;
		} else {
			sound = (SoundInfo) parent.getItemAtPosition(position);
			oldSelectedSound = sound;
		}
		adapterView = parent;
	}

	public void onNothingSelected(AdapterView<?> arg0) {
	}*/

	@Override
	public List<SequenceAction> addActionToSequence(Sprite sprite, SequenceAction sequence) {
		sequence.addAction(sprite.getActionFactory().createPlaySoundAction(sprite, sound));
		return null;
	}

	public SoundInfo getSound() {
		return sound;
	}

	public SoundInfo getOldSoundInfo() {
		return oldSelectedSound;
	}

	public void setOldSoundInfo(SoundInfo currentSound) {
		this.oldSelectedSound = currentSound;
	}
	/*private void setOnSoundInfoListChangedAfterNewListener(Context context) {
		ScriptActivity scriptActivity = (ScriptActivity) context;
		SoundFragment soundFragment = (SoundFragment) scriptActivity.getFragment(ScriptActivity.FRAGMENT_SOUNDS);
		if (soundFragment != null) {
			soundFragment.setOnSoundInfoListChangedAfterNewListener(this);
		}
	}

	private class SpinnerAdapterWrapper implements SpinnerAdapter {

		protected Context context;
		protected ArrayAdapter<SoundInfo> spinnerAdapter;

		private boolean isTouchInDropDownView;

		public SpinnerAdapterWrapper(Context context, ArrayAdapter<SoundInfo> spinnerAdapter) {
			this.context = context;
			this.spinnerAdapter = spinnerAdapter;

			this.isTouchInDropDownView = false;
		}

		@Override
		public void registerDataSetObserver(DataSetObserver paramDataSetObserver) {
			spinnerAdapter.registerDataSetObserver(paramDataSetObserver);
		}

		@Override
		public void unregisterDataSetObserver(DataSetObserver paramDataSetObserver) {
			spinnerAdapter.unregisterDataSetObserver(paramDataSetObserver);
		}

		@Override
		public int getCount() {
			return spinnerAdapter.getCount();
		}

		@Override
		public Object getItem(int paramInt) {
			return spinnerAdapter.getItem(paramInt);
		}

		@Override
		public long getItemId(int paramInt) {
			SoundInfo currentSound = spinnerAdapter.getItem(paramInt);
			if (!currentSound.getTitle().equals(context.getString(R.string.new_broadcast_message))) {
				oldSelectedSound = currentSound;
			}
			return spinnerAdapter.getItemId(paramInt);
		}

		@Override
		public boolean hasStableIds() {
			return spinnerAdapter.hasStableIds();
		}

		@Override
		public View getView(int paramInt, View paramView, ViewGroup paramViewGroup) {
			if (isTouchInDropDownView) {
				isTouchInDropDownView = false;
				if (paramInt == 0) {
					switchToSoundFragmentFromScriptFragment();
				}
			}
			return spinnerAdapter.getView(paramInt, paramView, paramViewGroup);
		}

		@Override
		public int getItemViewType(int paramInt) {
			return spinnerAdapter.getItemViewType(paramInt);
		}

		@Override
		public int getViewTypeCount() {
			return spinnerAdapter.getViewTypeCount();
		}

		@Override
		public boolean isEmpty() {
			return spinnerAdapter.isEmpty();
		}

		@Override
		public View getDropDownView(int paramInt, View paramView, ViewGroup paramViewGroup) {
			View dropDownView = spinnerAdapter.getDropDownView(paramInt, paramView, paramViewGroup);

			dropDownView.setOnTouchListener(new OnTouchListener() {
				@Override
				public boolean onTouch(View paramView, MotionEvent paramMotionEvent) {
					isTouchInDropDownView = true;
					return false;
				}
			});

			return dropDownView;
		}

		private void switchToSoundFragmentFromScriptFragment() {
			ScriptActivity scriptActivity = ((ScriptActivity) context);
			scriptActivity.switchToFragmentFromScriptFragment(ScriptActivity.FRAGMENT_SOUNDS);

			setOnSoundInfoListChangedAfterNewListener(context);
		}
	}

	@Override
	public void onSoundInfoListChangedAfterNew(SoundInfo soundInfo) {
		sound = soundInfo;
		oldSelectedSound = soundInfo;
	}*/
	}

	@Override
	public void storeDataForBackPack(Sprite sprite) {
		if (sound == null) {
			return;
		}
		sound = SoundController.getInstance().backPackHiddenSound(sound);
		if (sprite != null && !sprite.getSoundList().contains(sound)) {
			sprite.getSoundList().add(sound);
		}
	}
}
