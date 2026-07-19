package org.example;

import com.formdev.flatlaf.FlatDarkLaf;
import org.example.portafolio.gui.FramePrincipal;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        FlatDarkLaf.setup();

        SwingUtilities.invokeLater(() -> {
            FramePrincipal frame = new FramePrincipal();
            frame.setVisible(true);
        });
    }
}
