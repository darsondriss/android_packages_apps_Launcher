/*
 * Copyright (C) 2008 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.launcher;

import android.widget.AutoCompleteTextView;
import android.content.Context;
import android.util.AttributeSet;
import android.graphics.Rect;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.app.Activity;

/**
 * This class is not for the faint of heart. Home works in the pan & scan
 * soft input mode. However, this mode gets rid of the soft keyboard on rotation,
 * which is a probelm when the Search widget has focus. This special class
 * changes Home's soft input method temporarily as long as the Search widget holds
 * the focus. This way, the soft keyboard remains after rotation.
 */
public class SearchAutoCompleteTextView extends AutoCompleteTextView {
    public SearchAutoCompleteTextView(Context context) {
        super(context);
    }

    public SearchAutoCompleteTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SearchAutoCompleteTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);

        final WindowManager.LayoutParams lp = ((Activity) getContext()).getWindow().getAttributes();
        if (gainFocus) {
            lp.softInputMode =
                    (lp.softInputMode & ~WindowManager.LayoutParams.SOFT_INPUT_MASK_STATE) |
                            WindowManager.LayoutParams.SOFT_INPUT_STATE_UNCHANGED;
        } else {
            //noinspection PointlessBitwiseExpression
            lp.softInputMode =
                    (lp.softInputMode & ~WindowManager.LayoutParams.SOFT_INPUT_MASK_STATE) |
                            WindowManager.LayoutParams.SOFT_INPUT_STATE_UNSPECIFIED;

            // Hide the soft keyboard when the search widget loses the focus
            InputMethodManager.peekInstance().hideSoftInputFromWindow(getWindowToken(), 0);
        }

        final WindowManager manager = (WindowManager)
                getContext().getSystemService(Context.WINDOW_SERVICE);
        manager.updateViewLayout(getRootView(), lp);
    }
}