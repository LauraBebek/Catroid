/*
 * Catroid: An on-device visual programming system for Android devices
 * Copyright (C) 2010-2015 The Catrobat Team
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

package org.catrobat.catroid.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ListView;

import org.catrobat.catroid.R;

public final class UiUtils {

    private UiUtils() {
    }

    /**
     * Start Blink animation. Animation is started also when view has no window focus.
     *
     * @param view view to animate
     */
    public static void startBlinkAnimation(final Context context, final View view) {
        startBlinkAnimation(context, view, false);
    }

    /**
     * Start Blink animation.
     *
     * @param context           context
     * @param view              view to animate
     * @param ignoreWindowFocus when true - Animation is started also when view has no window focus.
     */
    public static void startBlinkAnimation(Context context, View view, boolean ignoreWindowFocus) {
        if (context == null || view == null || (!view.hasWindowFocus() && !ignoreWindowFocus)) {
            return;
        }
        startAnimation(view, AnimationUtils.loadAnimation(context, R.anim.blink));
    }

    /**
     * Start animation. Set View Transient state during animation to false.
     * Animation is started also when view has no window focus.
     *
     * @param view      view to animate
     * @param animation animation.
     */
    public static void startAnimation(final View view, Animation animation) {
        if (animation == null || view == null) {
            return;
        }

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                ViewCompat.setHasTransientState(view, true);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                ViewCompat.setHasTransientState(view, false);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(animation);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static int getCheckedItemCount(ListView listView) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            return listView.getCheckedItemCount();
        }

        int count = 0;
        SparseBooleanArray checkedItemPositions = listView.getCheckedItemPositions();
        for (int i = 0, ei = checkedItemPositions.size(); i < ei; i++) {
            if (checkedItemPositions.valueAt(i)) {
                count++;
            }
        }
        return count;
    }
}