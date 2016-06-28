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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import org.catrobat.catroid.ProjectManager;
import org.catrobat.catroid.R;
import org.catrobat.catroid.content.bricks.Brick;
import org.catrobat.catroid.content.bricks.UserListBrick;
import org.catrobat.catroid.formulaeditor.UserList;
import org.catrobat.catroid.ui.adapter.DataAdapter;
import org.catrobat.catroid.ui.adapter.UserListAdapterWrapper;
import org.catrobat.catroid.ui.dialogs.NewDataDialog;

public class ManipulateItemOfUserListBrickViewProvider extends BrickViewProvider {

    public enum ManipulationMode {
        ADD, DELETE, REPLACE, INSERT
    }
    public ManipulateItemOfUserListBrickViewProvider(Context context, LayoutInflater inflater) {
        super(context, inflater);
    }

    public View createManipulateItemToUserListBrickView(final UserListBrick brick, ViewGroup parent, ManipulationMode mode) {

        final View view;
        Spinner spinner;

        switch (mode) {
            case ADD:
                view = inflateBrickView(parent, R.layout.brick_add_item_to_userlist);
                initFormulaEditView(brick, view, Brick.BrickField.LIST_ADD_ITEM, R.id.brick_add_item_to_userlist_edit_text);
                spinner = (Spinner) view.findViewById(R.id.add_item_to_userlist_spinner);
                break;
            case DELETE:
                view = inflateBrickView(parent, R.layout.brick_delete_item_of_userlist);
                initFormulaEditView(brick, view, Brick.BrickField.LIST_DELETE_ITEM, R.id.brick_delete_item_of_userlist_edit_text);
                spinner = (Spinner) view.findViewById(R.id.delete_item_of_userlist_spinner);
                break;
            case INSERT:
                view = inflateBrickView(parent, R.layout.brick_insert_item_into_userlist);
                initFormulaEditView(brick, view, Brick.BrickField.INSERT_ITEM_INTO_USERLIST_VALUE, R.id.brick_insert_item_into_userlist_value_edit_text);
                initFormulaEditView(brick, view, Brick.BrickField.INSERT_ITEM_INTO_USERLIST_INDEX, R.id.brick_insert_item_into_userlist_at_index_edit_text);
                spinner = (Spinner) view.findViewById(R.id.insert_item_into_userlist_spinner);
                break;
            case REPLACE:
                view = inflateBrickView(parent, R.layout.brick_replace_item_in_userlist);
                initFormulaEditView(brick, view, Brick.BrickField.REPLACE_ITEM_IN_USERLIST_VALUE, R.id.brick_replace_item_in_userlist_value_edit_text);
                initFormulaEditView(brick, view, Brick.BrickField.REPLACE_ITEM_IN_USERLIST_INDEX, R.id.brick_replace_item_in_userlist_at_index_edit_text);
                spinner = (Spinner) view.findViewById(R.id.replace_item_in_userlist_spinner);
                break;
            default:
                Log.e("PROVIDER", "SHOULD NOT reach this!");
                return null;
        }

        DataAdapter dataAdapter = ProjectManager.getInstance().getCurrentProject().getDataContainer()
                .createDataAdapter(context, ProjectManager.getInstance().getCurrentSprite());
        UserListAdapterWrapper userListAdapterWrapper = new UserListAdapterWrapper(context, dataAdapter);
        userListAdapterWrapper.setItemLayout(android.R.layout.simple_spinner_item, android.R.id.text1);

        spinner.setAdapter(userListAdapterWrapper);
        spinner.setFocusableInTouchMode(false);
        spinner.setFocusable(false);
        setSpinnerSelection(brick, spinner, null);

        if (isPrototypeLayout()) {
            spinner.setEnabled(false);
            spinner.setClickable(false);
        } else {
            spinner.setEnabled(true);
            spinner.setClickable(true);

            final NewDataDialog.NewUserListDialogListener listener = new NewDataDialog.NewUserListDialogListener() {
                @Override
                public void onFinishNewUserListDialog(Spinner spinnerToUpdate, UserList newUserList) {
                    UserListAdapterWrapper userListAdapterWrapper = ((UserListAdapterWrapper) spinnerToUpdate.getAdapter());
                    userListAdapterWrapper.notifyDataSetChanged();
                    setSpinnerSelection(brick, spinnerToUpdate, newUserList);
                }
            };

            spinner.setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View view, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_UP
                            && (((Spinner) view).getSelectedItemPosition() == 0 && ((Spinner) view).getAdapter().getCount() == 1)) {
                        NewDataDialog dialog = new NewDataDialog((Spinner) view, NewDataDialog.DialogType.USER_LIST);

                        dialog.addUserListDialogListener(listener);
                        dialog.show(((Activity) view.getContext()).getFragmentManager(),
                                NewDataDialog.DIALOG_FRAGMENT_TAG);
                        return true;
                    }

                    return false;
                }
            });
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (position == 0 && ((UserListAdapterWrapper) parent.getAdapter()).isTouchInDropDownView()) {
                        NewDataDialog dialog = new NewDataDialog((Spinner) parent, NewDataDialog.DialogType.USER_LIST);
                        dialog.addUserListDialogListener(listener);
                        dialog.show(((Activity) view.getContext()).getFragmentManager(),
                                NewDataDialog.DIALOG_FRAGMENT_TAG);
                    }
                    ((UserListAdapterWrapper) parent.getAdapter()).resetIsTouchInDropDownView();
                    brick.setUserList((UserList) parent.getItemAtPosition(position));
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                    brick.setUserList(null);
                }
            });
        }

        return view;
    }

    private void setSpinnerSelection(UserListBrick brick, Spinner userListSpinner, UserList newUserList) {
        UserListAdapterWrapper userListAdapterWrapper = (UserListAdapterWrapper) userListSpinner.getAdapter();

        updateUserListIfDeleted(brick, userListAdapterWrapper);

        if (brick.getUserList() != null) {
            userListSpinner.setSelection(userListAdapterWrapper.getPositionOfItem(brick.getUserList()), true);
        } else if (newUserList != null) {
            userListSpinner.setSelection(userListAdapterWrapper.getPositionOfItem(newUserList), true);
            brick.setUserList(newUserList);
        } else {
            userListSpinner.setSelection(userListAdapterWrapper.getCount() - 1, true);
            brick.setUserList(userListAdapterWrapper.getItem(userListAdapterWrapper.getCount() - 1));
        }
    }

    private void updateUserListIfDeleted(UserListBrick brick, UserListAdapterWrapper userListAdapterWrapper) {
        if (brick.getUserList() != null && (userListAdapterWrapper.getPositionOfItem(brick.getUserList()) == 0)) {
            brick.setUserList(null);
        }
    }
}