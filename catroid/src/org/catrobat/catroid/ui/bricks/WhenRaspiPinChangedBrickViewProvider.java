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
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import org.catrobat.catroid.R;
import org.catrobat.catroid.common.BrickValues;
import org.catrobat.catroid.content.bricks.WhenRaspiPinChangedBrick;
import org.catrobat.catroid.devices.raspberrypi.RaspberryPiService;
import org.catrobat.catroid.ui.SettingsActivity;

import java.util.ArrayList;

public class WhenRaspiPinChangedBrickViewProvider extends BrickViewProvider {

	public WhenRaspiPinChangedBrickViewProvider(Context context, LayoutInflater inflater) {
		super(context, inflater);
	}

	public View createWhenRaspiPinChangedBrickView(final WhenRaspiPinChangedBrick brick, ViewGroup parent) {
		final View view = inflateBrickView(parent, R.layout.brick_raspi_pin_changed);

		setupValueSpinner(context, view, brick);
		setupPinSpinner(context, view, brick);

		return view;
	}

	private void setupPinSpinner(Context context, View view, final WhenRaspiPinChangedBrick brick) {
		final Spinner pinSpinner = (Spinner) view.findViewById(R.id.brick_raspi_when_pinspinner);
		pinSpinner.setFocusableInTouchMode(false);
		pinSpinner.setFocusable(false);
		pinSpinner.setClickable(true);
		pinSpinner.setEnabled(true);

		String revision = SettingsActivity.getRaspiRevision(context);
		ArrayList<Integer> availableGPIOs = RaspberryPiService.getInstance().getGpioList(revision);
		ArrayAdapter<String> messageAdapter2 = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item);
		messageAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		for (Integer gpio : availableGPIOs) {
			messageAdapter2.add(gpio.toString());
		}
		pinSpinner.setAdapter(messageAdapter2);

		if (isPrototypeLayout()) {
			pinSpinner.setEnabled(false);
			pinSpinner.setClickable(false);
		} else {
			pinSpinner.setEnabled(true);
			pinSpinner.setClickable(true);
			pinSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
					String selectedMessage = pinSpinner.getSelectedItem().toString();
					brick.setPinSelected(selectedMessage);
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
				}
			});
		}
		pinSpinner.setSelection(messageAdapter2.getPosition(brick.getPinSelected()), true);
	}

	private void setupValueSpinner(final Context context, View view, final WhenRaspiPinChangedBrick brick) {

		final Spinner valueSpinner = (Spinner) view.findViewById(R.id.brick_raspi_when_valuespinner);
		valueSpinner.setFocusableInTouchMode(false);
		valueSpinner.setFocusable(false);

		ArrayAdapter<String> valueAdapter = getValueSpinnerArrayAdapter(context);
		valueSpinner.setAdapter(valueAdapter);
		if (isPrototypeLayout()) {
			valueSpinner.setEnabled(false);
			valueSpinner.setClickable(false);
		} else {
			valueSpinner.setEnabled(true);
			valueSpinner.setClickable(true);
			valueSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
					String selectedMessage = getProtocolStringFromLanguageSpecificSpinnerSelection(valueSpinner.getSelectedItem().toString(), context);
					brick.setEventSelected(selectedMessage);
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {
				}
			});
		}
		valueSpinner.setSelection(valueAdapter.getPosition(getProtocolStringFromLanguageSpecificSpinnerSelection
				(brick.getEventSelected(), context)), true);
	}

	private ArrayAdapter<String> getValueSpinnerArrayAdapter(Context context) {
		ArrayAdapter<String> messageAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item);
		messageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		messageAdapter.add(context.getString(R.string.brick_raspi_pressed_text));
		messageAdapter.add(context.getString(R.string.brick_raspi_released_text));

		return messageAdapter;
	}

	private String getProtocolStringFromLanguageSpecificSpinnerSelection(String spinnerSelection, Context context) {
		if (spinnerSelection.equals(context.getString(R.string.brick_raspi_pressed_text))) {
			return BrickValues.RASPI_PRESSED_EVENT;
		} else {
			return BrickValues.RASPI_RELEASED_EVENT;
		}
	}
}
