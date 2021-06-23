package com.example.simpletest.factories;

import org.jetbrains.annotations.NotNull;

import osp.leobert.android.davinci.StyleRegistry;
import osp.leobert.android.davinci.annotation.DaVinCiStyleFactory;

/**
 * <p><b>Package:</b> com.example.simpletest.factories </p>
 * <p><b>Project:</b> DaVinCi </p>
 * <p><b>Classname:</b> JavaStyleFactory </p>
 * <p><b>Description:</b> TODO </p>
 * Created by leobert on 2021/6/23.
 */
@DaVinCiStyleFactory(styleName = "test_style.java")
public class JavaStyleFactory extends StyleRegistry.Style.Factory {
    @NotNull
    @Override
    public String getStyleName() {
        return "test_style.java";
    }

    @Override
    public void apply(@NotNull StyleRegistry.Style style) {
        //ignore ,just test
    }
}
