package com.duy.ide.editor.code.completion;

import com.duy.ide.autocomplete.Template;

import junit.framework.TestCase;

import java.lang.reflect.Modifier;

/**
 * Created by Duy on 17-Jul-17.
 */
public class TemplateTest extends TestCase{

    public void testCreateClass() {
        System.out.println(Template.createJava("com.duy.exmaple", "Main", 0, Modifier.PUBLIC, 0, true));
    }
}