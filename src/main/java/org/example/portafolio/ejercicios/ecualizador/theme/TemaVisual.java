package org.example.portafolio.ejercicios.ecualizador.theme;

import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.UIManager;
import java.awt.Color;
import java.awt.Dimension;

public class TemaVisual {

    public static void aplicarTemaOscuro() {
        UIManager.put("Button.arc", 999);
        UIManager.put("Component.arc", 12);
        UIManager.put("TextComponent.arc", 12);

        UIManager.put("Component.focusColor", new Color(0, 229, 255));
        UIManager.put("Component.innerFocusWidth", 2);

        UIManager.put("Slider.trackWidth", 6);
        UIManager.put("Slider.thumbSize", new Dimension(18, 18));

        FlatDarkLaf.setup();
    }
}