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

package org.catrobat.catroid.ui;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import org.catrobat.catroid.ui.adapter.BrickAdapter;

public class BrickView extends CheckableLinearLayout {

    public static final String TAG = BrickView.class.getSimpleName();

    private transient View checkbox;
    private transient ViewGroup brickLayout;
    private int mode = Mode.DEFAULT;

    public BrickView(Context context) {
        this(context, null);
    }

    public BrickView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        this.checkbox = getChildAt(0);
        applyModeChanged(this.brickLayout = (ViewGroup) getChildAt(1), mode, mode);
    }

    public void addMode(int mask) {
        int oldMode = this.mode;
        this.mode |= mask;
        onModeChanged(oldMode, this.mode);
    }

    public void removeMode(int mask) {
        int oldMode = this.mode;
        this.mode &= ~mask;
        onModeChanged(oldMode, this.mode);
    }

    public int getMode() {
        return this.mode;
    }

    public boolean hasMode(int mask) {
        return (this.mode & mask) == mask;
    }


    private void onModeChanged(int oldMode, int newMode) {
        if (oldMode == newMode) {
            return;
        }

        boolean isSelectable = hasMode(Mode.SELECTION);
        this.brickLayout.setDuplicateParentStateEnabled(isSelectable);
        setCheckboxVisibility(isSelectable ? VISIBLE : GONE);

        applyModeChanged(this.brickLayout, oldMode, newMode);
    }

    private void setCheckboxVisibility(int visibility) {
        if (this.checkbox.getVisibility() != visibility) {
            this.checkbox.setVisibility(visibility);
        }
    }

    private void applyModeChanged(ViewGroup viewGroup, int oldMode, int newMode) {
        if (viewGroup != null) {
            ViewParent brickViewParent = getParent();
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                View child = viewGroup.getChildAt(i);
                if (child instanceof ViewGroup && !(child instanceof Spinner)) {
                    applyModeChanged((ViewGroup) child, oldMode, newMode);
                } else {
                    child.setClickable(newMode == Mode.DEFAULT);
                    if (child instanceof Spinner) {
                        //FIXME: provide extra style for spinner in prototype View
                        child.setEnabled(newMode == Mode.DEFAULT);

                        if (brickViewParent == null || ListView.class.isAssignableFrom(brickViewParent.getClass())) {
                            //Change Spinner Clickable Property according to mode in ListView or then parent is unknown,
                            // to avoid showing ListItem context menu when it is clicked on spinner.
                            child.setClickable(newMode == Mode.DEFAULT);
                        }
                    }
                }
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void applyAlpha(int newAlpha) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            float newOpacity = newAlpha / (float) BrickAdapter.ALPHA_FULL;
            if (newOpacity != this.brickLayout.getAlpha()) {
                this.brickLayout.setAlpha(newOpacity);
            }
        } else {
            //pre HONEYCOMB
            applyAlphaChanged(this.brickLayout, newAlpha);
        }
    }

    private void applyAlphaChanged(ViewGroup viewGroup, int newAlpha) {
        Drawable background = viewGroup.getBackground();
        if (background != null) {
            background.setAlpha(newAlpha);
        }

        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View child = viewGroup.getChildAt(i);
            if (child instanceof TextView) {
                TextView textView = (TextView) child;
                textView.setTextColor(textView.getTextColors().withAlpha(newAlpha));
            } else if (child instanceof Spinner) {
                Spinner spinner = (Spinner) child;
                spinner.getBackground().setAlpha(newAlpha);
                View selectedView = spinner.getSelectedView();
                if (selectedView instanceof ViewGroup) {
                    View expectTextView = ((ViewGroup) selectedView).getChildAt(0);
                    if (expectTextView instanceof TextView) {
                        TextView textView = ((TextView) expectTextView);
                        textView.setTextColor(textView.getTextColors().withAlpha(newAlpha));
                    }
                }
            } else if (child instanceof ViewGroup) {
                applyAlphaChanged((ViewGroup) child, newAlpha);
            }
        }
    }

    public final class Mode {
        public static final int DEFAULT = 0;

        public static final int SELECTION = 2;

        private Mode() {
        }
    }
}