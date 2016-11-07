package com.monkeybuisness.thimbles.localization;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.I18NBundle;

import java.util.Locale;

public class LocalizationBundle {

    private static I18NBundle i18NBundle = null;

    public static void create(FileHandle baseFileHandle, Locale locale) {
        i18NBundle = I18NBundle.createBundle(baseFileHandle, locale);
    }

    public static I18NBundle bundle() {
        return i18NBundle;
    }
}