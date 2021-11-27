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

package com.duy.ide.editor.code.highlight;

import android.support.annotation.NonNull;
import android.text.Editable;

import com.duy.ide.themefont.themes.database.CodeTheme;

/**
 * Created by Duy on 18-Jun-17.
 */

public interface Highlighter {
    void highlight(@NonNull Editable allText,
                   @NonNull CharSequence textToHighlight, int start);

    void setCodeTheme(CodeTheme codeTheme);

    void setErrorRange(long startPosition, long endPosition);
}
