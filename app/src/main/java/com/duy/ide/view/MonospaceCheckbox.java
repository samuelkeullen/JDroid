/*
 *  Copyright (c) 2017 Tran Le Duy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.duy.ide.view;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatCheckBox;
import android.util.AttributeSet;

/**
 * Created by Duy on 10-Jun-17.
 */

public class MonospaceCheckbox extends AppCompatCheckBox {

    public MonospaceCheckbox(Context context) {
        super(context);
        setTypeface(Typeface.MONOSPACE);
    }

    public MonospaceCheckbox(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTypeface(Typeface.MONOSPACE);

    }

    public MonospaceCheckbox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setTypeface(Typeface.MONOSPACE);
    }
}
