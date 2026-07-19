package org.example.portafolio.ejercicios.ecualizador.ui;

import org.example.portafolio.ejercicios.ecualizador.processor.ProcesadorImagen;
import org.example.portafolio.ejercicios.ecualizador.processor.ModosAvanzados;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class AppEqualizador extends JFrame {

    private BufferedImage originalImage;
    private BufferedImage processedImage;

    private JLabel lblOriginalImage;
    private JLabel lblProcessedImage;
    private PanelHistograma originalHistogramPanel;
    private PanelHistograma processedHistogramPanel;

    private JSlider sliderEqualization;
    private JLabel lblEqVal;
    private JLabel lblStatus;

    private JComboBox<String> comboMode;

    public AppEqualizador() {
        super("Ecualizador de Histogramas");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        initUI();
    }

    private void initUI() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(12, 12, 12, 12));
        setContentPane(mainPanel);

        // Boton para cargar la imagen
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 5));
        JButton btnLoad = new JButton("Cargar Imagen");
        btnLoad.addActionListener(e -> selectImageViaDialog());
        topPanel.add(btnLoad);

        // MODO SELECTOR (HSB / RGB / Grises)

        JPanel modePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        JLabel lblMode = new JLabel("Modo:");
        comboMode = new JComboBox<>(new String[] { "HSB (Brillo)", "RGB (Color)", "Escala de Grises" });
        comboMode.addActionListener(e2 -> {
            if (originalImage != null) {
                applyEqualizationAdjustments(sliderEqualization.getValue() / 100.0f);
            }
        });
        modePanel.add(lblMode);
        modePanel.add(comboMode);
        topPanel.add(modePanel);

        mainPanel.add(topPanel, BorderLayout.NORTH);

        // imagen original / imagen procesada
        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 12, 0));

        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBorder(BorderFactory.createLineBorder(new Color(60, 63, 65), 1, true));
        JLabel titleLeft = new JLabel("IMAGEN ORIGINAL", SwingConstants.CENTER);
        leftPanel.add(titleLeft, BorderLayout.NORTH);
        lblOriginalImage = new JLabel("Selecciona una imagen", SwingConstants.CENTER);
        lblOriginalImage.setPreferredSize(new Dimension(400, 300));
        leftPanel.add(lblOriginalImage, BorderLayout.CENTER);
        originalHistogramPanel = new PanelHistograma();
        originalHistogramPanel.setPreferredSize(new Dimension(400, 180));
        leftPanel.add(originalHistogramPanel, BorderLayout.SOUTH);
        centerPanel.add(leftPanel);

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBorder(BorderFactory.createLineBorder(new Color(60, 63, 65), 1, true));
        JLabel titleRight = new JLabel("IMAGEN PROCESADA", SwingConstants.CENTER);
        rightPanel.add(titleRight, BorderLayout.NORTH);
        lblProcessedImage = new JLabel("Imagen Procesada", SwingConstants.CENTER);
        lblProcessedImage.setPreferredSize(new Dimension(400, 300));
        rightPanel.add(lblProcessedImage, BorderLayout.CENTER);
        processedHistogramPanel = new PanelHistograma();
        processedHistogramPanel.setPreferredSize(new Dimension(400, 180));
        rightPanel.add(processedHistogramPanel, BorderLayout.SOUTH);
        centerPanel.add(rightPanel);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Slider de ecualizacion + estado
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
        bottomPanel.setBorder(new EmptyBorder(10, 5, 5, 5));

        JPanel sliderPanel = new JPanel(new BorderLayout(15, 0));
        JLabel lblEqTitle = new JLabel("Ecualizacion: ");
        sliderEqualization = new JSlider(0, 100, 0);
        sliderEqualization.setPaintTicks(true);
        sliderEqualization.setMajorTickSpacing(10);
        sliderEqualization.setMinorTickSpacing(2);
        sliderEqualization.addChangeListener(e -> {
            int val = sliderEqualization.getValue();
            lblEqVal.setText(val + "%");
            applyEqualizationAdjustments(val / 100.0f);
        });
        lblEqVal = new JLabel("0%");
        lblEqVal.setPreferredSize(new Dimension(45, 20));

        sliderPanel.add(lblEqTitle, BorderLayout.WEST);
        sliderPanel.add(sliderEqualization, BorderLayout.CENTER);
        sliderPanel.add(lblEqVal, BorderLayout.EAST);

        bottomPanel.add(sliderPanel);
        bottomPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        lblStatus = new JLabel("Listo. Carga una imagen para comenzar.");
        bottomPanel.add(lblStatus);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
    }

    private void selectImageViaDialog() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Seleccionar Imagen");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                "Imagenes (JPG, PNG, BMP)", "jpg", "jpeg", "png", "bmp"));

        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            loadImageFromFile(fileChooser.getSelectedFile());
        }
    }

    private void loadImageFromFile(File file) {
        try {
            originalImage = org.example.portafolio.utils.ImageIOManager.read(file);
            if (originalImage == null) {
                JOptionPane.showMessageDialog(this,
                        "El archivo no es una imagen valida.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            sliderEqualization.setValue(0);
            lblEqVal.setText("0%");
            processedImage = originalImage;

            renderImage(originalImage, lblOriginalImage);
            renderImage(processedImage, lblProcessedImage);

            int[][] originalHist = ProcesadorImagen.calculateHistogram(originalImage);
            originalHistogramPanel.setHistogram(originalHist, false);
            processedHistogramPanel.setHistogram(originalHist, false);

            lblStatus.setText("Cargada: " + file.getName() + " (" + originalImage.getWidth()
                    + "x" + originalImage.getHeight() + " px)");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error de lectura: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void renderImage(BufferedImage img, JLabel label) {
        if (img == null)
            return;
        label.setText("");

        int labelWidth = label.getWidth() > 0 ? label.getWidth() : 400;
        int labelHeight = label.getHeight() > 0 ? label.getHeight() : 300;

        double imgRatio = (double) img.getWidth() / img.getHeight();
        double labelRatio = (double) labelWidth / labelHeight;

        int finalWidth, finalHeight;
        if (imgRatio > labelRatio) {
            finalWidth = labelWidth;
            finalHeight = (int) (labelWidth / imgRatio);
        } else {
            finalHeight = labelHeight;
            finalWidth = (int) (labelHeight * imgRatio);
        }
        finalWidth = Math.max(1, finalWidth);
        finalHeight = Math.max(1, finalHeight);

        Image scaled = img.getScaledInstance(finalWidth, finalHeight, Image.SCALE_SMOOTH);
        label.setIcon(new ImageIcon(scaled));
        label.revalidate();
        label.repaint();
    }

    private void applyEqualizationAdjustments(float alpha) {
        if (originalImage == null)
            return;

        // RGB/brillo:
        processedImage = ProcesadorImagen.equalize(originalImage, alpha);

        String mode = (String) comboMode.getSelectedItem();
        if ("HSB (Brillo)".equals(mode)) {
            processedImage = ModosAvanzados.equalizeHSB(originalImage, alpha);
        } else if ("RGB (Color)".equals(mode)) {
            processedImage = ProcesadorImagen.equalize(originalImage, alpha);
        } else {
            processedImage = ModosAvanzados.equalizeGray(originalImage, alpha);
        }

        renderImage(processedImage, lblProcessedImage);

        int[][] hist = ProcesadorImagen.calculateHistogram(processedImage);
        processedHistogramPanel.setHistogram(hist, false);

        lblStatus.setText(String.format("Ecualizacion aplicada: %.0f%%", alpha * 100));
    }
}