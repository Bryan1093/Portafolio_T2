package org.example.portafolio.ejercicios.ecualizador;

import org.example.portafolio.ejercicios.ecualizador.theme.TemaVisual;
import org.example.portafolio.ejercicios.ecualizador.ui.AppEqualizador;

import javax.swing.SwingUtilities;

public class MainEqualizador {
    public static void main(String[] args) {
        TemaVisual.aplicarTemaOscuro();
        SwingUtilities.invokeLater(() -> {
            AppEqualizador app = new AppEqualizador();
            app.setVisible(true);
        });
    }
}