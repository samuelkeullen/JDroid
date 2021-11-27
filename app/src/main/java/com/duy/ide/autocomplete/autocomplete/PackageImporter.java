package com.duy.ide.autocomplete.autocomplete;

import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.EditText;

import com.duy.ide.autocomplete.util.EditorUtil;
import com.duy.ide.autocomplete.util.JavaUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.duy.ide.autocomplete.autocomplete.PatternFactory.firstMatch;
import static com.duy.ide.autocomplete.autocomplete.PatternFactory.lastMatch;


/**
 * Created by Duy on 21-Jul-17.
 */

public class PackageImporter {
    private static final String TAG = "ImportUtil";

    /**
     * Add import statement if import does not already exist.
     */
    public static void importClass(EditText editor, String className) {
        String packageName = JavaUtil.getPackageName(className);
        if (getImportedClassName(editor, className) == null
                && !packageName.equals("java.lang")
                && !packageName.equals(EditorUtil.getCurrentPackage(editor))) {
            organizeImports(editor, "import " + className + ";");
        }
    }

    public static String getImportedClassName(EditText editor, @Nullable String className) {
        return getImportedClassName(editor.getText(), className);
    }

    public static String getImportedClassName(CharSequence src, @Nullable String className) {
        if (className == null) return null;

        Pattern pattern = PatternFactory.makeImport(className);
        Matcher matcher = pattern.matcher(src);
        if (matcher.find()) {
            return matcher.group(2);
        }
        return PatternFactory.match(src, pattern);
    }


    public static void organizeImports(EditText editor, String importStr) {
        Log.d(TAG, "organizeImports() called with: editor = [" + editor + "], importStr = [" + importStr + "]");

        ArrayList<String> imports = getImports(editor);
        Log.d(TAG, "organizeImports imports = " + imports);

        imports.add(importStr);
        Collections.sort(imports, new Comparator<String>() {
            @Override
            public int compare(String s, String t1) {
                return s.compareTo(t1);
            }
        });
        StringBuilder imp = new StringBuilder();
        String lastPkg = "";
        for (int i = 0; i < imports.size(); i++) {
            String current = imports.get(i);
            String currentPkg = "";
            if (current.contains(".") && !lastPkg.isEmpty()) {
                currentPkg = current.substring(0, current.indexOf(".")).replaceAll("\\s+", "");
                if (!currentPkg.equals(lastPkg)) {
                    imp.append("\n");
                }
            }
            if (i == imports.size() - 1) {
                imp.append(current);
            } else {
                imp.append(current).append("\n");
            }
            lastPkg = currentPkg;
        }
        int first = firstMatch(editor, PatternFactory.IMPORT);
        int last = PatternFactory.lastMatch(editor, PatternFactory.IMPORT);
        if (first >= 0 && last > first) {
            editor.getText().replace(first, last, ""); //clear import
            editor.getText().insert(first, imp); //insert new
        } else {
            int i = lastMatch(editor, PatternFactory.PACKAGE);
            if (i < 0) {
                editor.getText().insert(0, imp); //insert new
            } else {
                editor.getText().insert(i, "\n\n" + imp + "\n");
            }

        }
    }

    public static ArrayList<String> getImports(EditText editor) {
        return PatternFactory.allMatch(editor.getText(), PatternFactory.IMPORT);
    }

}
