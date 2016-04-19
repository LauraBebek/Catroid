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

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import org.catrobat.catroid.ProjectManager;
import org.catrobat.catroid.R;
import org.catrobat.catroid.content.Sprite;
import org.catrobat.catroid.content.bricks.Brick;

import org.catrobat.catroid.content.bricks.HideTextBrick;
import org.catrobat.catroid.content.bricks.ShowTextBrick;
import org.catrobat.catroid.content.bricks.UserVariableBrick;
import org.catrobat.catroid.formulaeditor.UserVariable;
import org.catrobat.catroid.ui.adapter.DataAdapter;
import org.catrobat.catroid.ui.adapter.UserListAdapterWrapper;
import org.catrobat.catroid.ui.adapter.UserVariableAdapterWrapper;
import org.catrobat.catroid.ui.dialogs.NewDataDialog;

public class HideShowTextBrickViewProvider extends BrickViewProvider {

    public HideShowTextBrickViewProvider(Context context, LayoutInflater inflater) {
        super(context, inflater);
    }

    public View createShowTextBrickView(final ShowTextBrick brick, ViewGroup parent) {

        final View view = inflateBrickView(parent, R.layout.brick_show_text);
        initFormulaEditView(brick, view, Brick.BrickField.X_POSITION, R.id.brick_show_text_edit_text_x);
        initFormulaEditView(brick, view, Brick.BrickField.Y_POSITION, R.id.brick_show_text_edit_text_y);
        Spinner variableSpinner = (Spinner) view.findViewById(R.id.show_text_spinner);

        createSpinnerAdapter(variableSpinner, brick);

        return view;
    }

    public View createHideTextBrickView(final HideTextBrick brick, ViewGroup parent) {

        View view = inflateBrickView(parent, R.layout.brick_hide_text);
        Spinner variableSpinner = (Spinner) view.findViewById(R.id.hide_text_spinner);
        createSpinnerAdapter(variableSpinner, brick);

        return view;
}

    private void setSpinnerSelection(UserVariableBrick brick, Spinner userListSpinner, UserVariable variable) {
        UserVariableAdapterWrapper userVariableAdapterWrapper = (UserVariableAdapterWrapper) userListSpinner.getAdapter();

        updateVariableList(brick, userVariableAdapterWrapper);

        if (brick.getUserVariable() != null) {
            userListSpinner.setSelection(userVariableAdapterWrapper.getPositionOfItem(brick.getUserVariable()), true);
        } else if (variable != null) {
            userListSpinner.setSelection(userVariableAdapterWrapper.getPositionOfItem(variable), true);
            brick.setUserVariable(variable);
        } else {
            userListSpinner.setSelection(userVariableAdapterWrapper.getCount() - 1, true);
            brick.setUserVariable(userVariableAdapterWrapper.getItem(userVariableAdapterWrapper.getCount() - 1));
        }
    }

    private void updateVariableList(UserVariableBrick brick, UserVariableAdapterWrapper
            userVariableAdapterWrapper) {
        if (brick.getUserVariable() != null && (userVariableAdapterWrapper.getPositionOfItem(brick.getUserVariable()) == 0)) {
            brick.setUserVariable(null);
        }
    }

    private void createSpinnerAdapter(Spinner spinner, final UserVariableBrick brick) {

        DataAdapter dataAdapter = ProjectManager.getInstance().getCurrentProject().getDataContainer()
                .createDataAdapter(context, ProjectManager.getInstance().getCurrentSprite());
        UserVariableAdapterWrapper userVariableAdapterWrapper = new UserVariableAdapterWrapper(context,
                dataAdapter);
        userVariableAdapterWrapper.setItemLayout(android.R.layout.simple_spinner_item, android.R.id.text1);

        spinner.setAdapter(userVariableAdapterWrapper);
        spinner.setFocusableInTouchMode(false);
        spinner.setFocusable(false);
        setSpinnerSelection(brick, spinner, null);

        if (isPrototypeLayout()) {
            spinner.setEnabled(false);
            spinner.setClickable(false);
        } else {
            spinner.setEnabled(true);
            spinner.setClickable(true);

            final NewDataDialog.NewVariableDialogListener listener = new NewDataDialog.NewVariableDialogListener() {
                @Override
                public void onFinishNewVariableDialog(Spinner spinnerToUpdate, UserVariable variable) {
                    UserVariableAdapterWrapper userListAdapterWrapper = ((UserVariableAdapterWrapper) spinnerToUpdate.getAdapter());
                    userListAdapterWrapper.notifyDataSetChanged();
                    brick.setUserVariable(variable);
                }
            };

            spinner.setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View view, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_UP
                            && (((Spinner) view).getSelectedItemPosition() == 0
                            && ((Spinner) view).getAdapter().getCount() == 1)) {
                        NewDataDialog dialog = new NewDataDialog((Spinner) view, NewDataDialog.DialogType.USER_VARIABLE);
                        dialog.addVariableDialogListener(listener);
                        dialog.show(((Activity) view.getContext()).getFragmentManager(), NewDataDialog.DIALOG_FRAGMENT_TAG);
                        return true;
                    }

                    return false;
                }
            });
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (position == 0 && ((UserVariableAdapterWrapper) parent.getAdapter()).isTouchInDropDownView()) {
                        NewDataDialog dialog = new NewDataDialog((Spinner) parent, NewDataDialog.DialogType.USER_VARIABLE);
                        dialog.addVariableDialogListener(listener);
                        dialog.setUserVariableIfCancel(position);
                        dialog.show(((Activity) view.getContext()).getFragmentManager(),
                                NewDataDialog.DIALOG_FRAGMENT_TAG);
                    }
                    ((UserVariableAdapterWrapper) parent.getAdapter()).resetIsTouchInDropDownView();
                    brick.setUserVariable((UserVariable) parent.getItemAtPosition(position));
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                }
            });
        }
    }
}