package org.example.portafolio.gui;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.*;
import javax.imageio.ImageIO;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;

import backend.logica.*;
import org.example.portafolio.utils.ImageIOManager;

public class FramePrincipal extends JFrame {

    private BufferedImage imagenOriginal;
    private BufferedImage imagenProcesada;

    private JLabel lblOriginal;
    private JLabel lblProcesada;
    private JScrollPane scrollOriginal;
    private JScrollPane scrollProcesada;

    private JCheckBox chkAjustar;

    private JSlider sldBrillo;
    private JSlider sldTransparencia;
    private JSlider sldSaturacion;
    private JSlider sldRecorteBits;

    private boolean ignoreSliderEvents = false;

    private static final Map<String, String> CLASS_PACKAGE_MAP = Map.ofEntries(
            Map.entry("Blending", "org.example.portafolio.ejercicios.transparencia"),
            Map.entry("convolucion", "org.example.portafolio.ejercicios.convoluciones"),
            Map.entry("convolucionOp", "org.example.portafolio.ejercicios.convoluciones"),
            Map.entry("kernels", "org.example.portafolio.ejercicios.convoluciones"),
            Map.entry("ejemploClase", "org.example.portafolio.ejercicios.colores"),
            Map.entry("Ejercicio_1", "org.example.portafolio.ejercicios.efectos"),
            Map.entry("Ejercicio_2", "org.example.portafolio.ejercicios.efectos"),
            Map.entry("Ejercicio_3", "org.example.portafolio.ejercicios.efectos"),
            Map.entry("Ejercicio_4", "org.example.portafolio.ejercicios.efectos"),
            Map.entry("Ejercicio_5", "org.example.portafolio.ejercicios.efectos"),
            Map.entry("Ejercicio_6", "org.example.portafolio.ejercicios.efectos"),
            Map.entry("EvaluacionSumativa", "org.example.portafolio.ejercicios.efectos"),
            Map.entry("Filtros", "org.example.portafolio.ejercicios.efectos"),
            Map.entry("Gradientes", "org.example.portafolio.ejercicios.generadores"),
            Map.entry("Histograma", "org.example.portafolio.ejercicios.histograma"),
            Map.entry("Imagen", "org.example.portafolio.ejercicios.colores"),
            Map.entry("matrizDeColores", "org.example.portafolio.ejercicios.colores"),
            Map.entry("personalizada", "org.example.portafolio.ejercicios.efectos"),
            Map.entry("transparencia", "org.example.portafolio.ejercicios.transparencia"),
            Map.entry("transparencia2", "org.example.portafolio.ejercicios.transparencia"),
            Map.entry("MainEqualizador", "org.example.portafolio.ejercicios.ecualizador"));

    public FramePrincipal() {
        super("Portafolio de Procesamiento de Imágenes — Taller 2");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1250, 850);
        setLocationRelativeTo(null);

        FlatDarkLaf.setup();

        initUI();
    }

    private void initUI() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(mainPanel);
        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setBorder(new EmptyBorder(0, 0, 10, 0));

        JLabel lblTitle = new JLabel("PORTAFOLIO DE IMÁGENES");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(new Color(138, 180, 248));
        pnlHeader.add(lblTitle, BorderLayout.WEST);

        JPanel pnlTheme = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JLabel lblTheme = new JLabel("Tema: ");
        String[] temas = { "Modo Oscuro", "Modo Claro" };
        JComboBox<String> cmbTheme = new JComboBox<>(temas);
        cmbTheme.addActionListener(e -> {
            try {
                if (cmbTheme.getSelectedIndex() == 0) {
                    UIManager.setLookAndFeel(new FlatDarkLaf());
                } else {
                    UIManager.setLookAndFeel(new FlatLightLaf());
                }
                SwingUtilities.updateComponentTreeUI(this);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        pnlTheme.add(lblTheme);
        pnlTheme.add(cmbTheme);
        pnlHeader.add(pnlTheme, BorderLayout.EAST);

        mainPanel.add(pnlHeader, BorderLayout.NORTH);

        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.putClientProperty("JTabbedPane.showTabSeparators", true);
        tabbedPane.addTab("Filtros Interactivos", crearPanelFiltrosInteractivos());
        tabbedPane.addTab("Convoluciones", new PanelEjecutorTema(this, new String[] {
                "convolucion (Básica)",
                "convolucionOp (ConvolveOp)"
        }));
        tabbedPane.addTab("Ajustes de Color", new PanelEjecutorTema(this, new String[] {
                "matrizDeColores (Filtros de Color)",
                "ejemploClase (Lectura y Escritura)",
                "Imagen (Copia y Pixeles)"
        }));

        tabbedPane.addTab("Deberes y Efectos Retro", new PanelEjecutorTema(this, new String[] {
                "Ejercicio_1 (Vidrio Esmerilado)",
                "Ejercicio_2 (Desvanecimiento Circular)",
                "Ejercicio_3 (Efecto Retro)",
                "Ejercicio_4 (Efecto Retro Anulación)",
                "Ejercicio_5 (Blanco y Negro)",
                "Ejercicio_6 (Grises Retro)",
                "EvaluacionSumativa",
                "personalizada",
                "Filtros"
        }));
        tabbedPane.addTab("Blending y Transparencia", new PanelEjecutorTema(this, new String[] {
                "Blending (Mezcla)",
                "transparencia",
                "transparencia2"
        }));

        tabbedPane.addTab("Análisis de Imagen", new PanelEjecutorTema(this, new String[] {
                "Histograma"
        }));
        tabbedPane.addTab("Ecualizador (Proyecto Extra)", new PanelEjecutorTema(this, new String[] {
                "MainEqualizador (Abrir Interfaz de Ecualización)"
        }));

        tabbedPane.addTab("Generadores (Gradiente/Ruido)", crearPanelGeneradores());

        mainPanel.add(tabbedPane, BorderLayout.CENTER);

        cargarImagenPorDefecto();
    }

    private void cargarImagenPorDefecto() {
        File defaultFile = new File("images/galaxia.png");
        if (!defaultFile.exists()) {
            defaultFile = new File("images/manchester.jpg");
        }
        if (!defaultFile.exists()) {
            defaultFile = new File("images/images.jpg");
        }
        if (defaultFile.exists()) {
            try {
                imagenOriginal = ImageIO.read(defaultFile);
            } catch (IOException e) {
                imagenOriginal = org.example.portafolio.utils.PlaceholderImageGenerator.createDefaultImage(500, 350,
                        "Imagen A (Autogenerada)");
            }
        } else {
            imagenOriginal = org.example.portafolio.utils.PlaceholderImageGenerator.createDefaultImage(500, 350,
                    "Imagen A (Autogenerada)");
        }
        imagenProcesada = copiarImagen(imagenOriginal);
        actualizarVistas();
    }

    private BufferedImage copiarImagen(BufferedImage img) {
        if (img == null)
            return null;
        BufferedImage copia = new BufferedImage(img.getWidth(), img.getHeight(),
                img.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : img.getType());
        Graphics g = copia.getGraphics();
        g.drawImage(img, 0, 0, null);
        g.dispose();
        return copia;
    }

    private JPanel crearPanelFiltrosInteractivos() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        JSplitPane splitImages = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitImages.setResizeWeight(0.5);

        lblOriginal = new JLabel("Carga una imagen para comenzar", SwingConstants.CENTER);
        scrollOriginal = new JScrollPane(lblOriginal);
        scrollOriginal.setBorder(BorderFactory.createTitledBorder("Imagen Original"));

        lblProcesada = new JLabel("Esperando filtros...", SwingConstants.CENTER);
        scrollProcesada = new JScrollPane(lblProcesada);
        scrollProcesada.setBorder(BorderFactory.createTitledBorder("Imagen Procesada"));

        splitImages.setLeftComponent(scrollOriginal);
        splitImages.setRightComponent(scrollProcesada);

        panel.add(splitImages, BorderLayout.CENTER);

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        JButton btnCargar = new JButton("Cargar Imagen...");
        JButton btnRestaurar = new JButton("Restaurar Original");
        JButton btnGuardar = new JButton("Guardar Resultado");
        chkAjustar = new JCheckBox("Ajustar a la pantalla", true);

        btnCargar.addActionListener(e -> accCargarImagen());
        btnRestaurar.addActionListener(e -> {
            if (imagenOriginal != null) {
                imagenProcesada = copiarImagen(imagenOriginal);
                resetSliders();
                actualizarVistas();
            }
        });
        btnGuardar.addActionListener(e -> accGuardarImagen());
        chkAjustar.addActionListener(e -> actualizarVistas());

        toolbar.add(btnCargar);
        toolbar.add(btnRestaurar);
        toolbar.add(btnGuardar);
        toolbar.add(new JSeparator(JSeparator.VERTICAL));
        toolbar.add(chkAjustar);

        panel.add(toolbar, BorderLayout.NORTH);

        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setPreferredSize(new Dimension(320, 0));
        sidebar.setBorder(new EmptyBorder(0, 5, 0, 5));

        JScrollPane scrollSidebar = new JScrollPane(sidebar);
        scrollSidebar.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollSidebar.setBorder(null);
        panel.add(scrollSidebar, BorderLayout.EAST);

        // 1. SECCIÓN FILTROS BÁSICOS
        JPanel pnlBasicos = new JPanel();
        pnlBasicos.setLayout(new BoxLayout(pnlBasicos, BoxLayout.Y_AXIS));
        pnlBasicos.setBorder(BorderFactory.createTitledBorder("Filtros Básicos"));

        // Brillo Slider
        JPanel pnlBrillo = new JPanel(new BorderLayout());
        JLabel lblBrilloName = new JLabel("Brillo: ");
        JLabel lblBrilloVal = new JLabel("0");
        sldBrillo = new JSlider(-255, 255, 0);
        sldBrillo.addChangeListener(e -> {
            if (imagenOriginal == null || ignoreSliderEvents)
                return;
            int offset = sldBrillo.getValue();
            lblBrilloVal.setText(String.valueOf(offset));
            if (!sldBrillo.getValueIsAdjusting()) {
                aplicarFiltroBasico("brillo", offset);
            }
        });
        pnlBrillo.add(lblBrilloName, BorderLayout.WEST);
        pnlBrillo.add(lblBrilloVal, BorderLayout.EAST);
        pnlBrillo.add(sldBrillo, BorderLayout.SOUTH);
        pnlBasicos.add(pnlBrillo);
        pnlBasicos.add(Box.createRigidArea(new Dimension(0, 10)));

        // Transparencia Slider
        JPanel pnlTransp = new JPanel(new BorderLayout());
        JLabel lblTranspName = new JLabel("Transparencia (Alpha): ");
        JLabel lblTranspVal = new JLabel("1.0");
        sldTransparencia = new JSlider(0, 100, 100);
        sldTransparencia.addChangeListener(e -> {
            if (imagenOriginal == null || ignoreSliderEvents)
                return;
            float factor = sldTransparencia.getValue() / 100.0f;
            lblTranspVal.setText(String.format(Locale.US, "%.2f", factor));
            if (!sldTransparencia.getValueIsAdjusting()) {
                aplicarFiltroBasico("transparencia", factor);
            }
        });
        pnlTransp.add(lblTranspName, BorderLayout.WEST);
        pnlTransp.add(lblTranspVal, BorderLayout.EAST);
        pnlTransp.add(sldTransparencia, BorderLayout.SOUTH);
        pnlBasicos.add(pnlTransp);
        pnlBasicos.add(Box.createRigidArea(new Dimension(0, 10)));

        // Saturación Slider
        JPanel pnlSat = new JPanel(new BorderLayout());
        JLabel lblSatName = new JLabel("Saturación: ");
        JLabel lblSatVal = new JLabel("1.0");
        sldSaturacion = new JSlider(0, 300, 100);
        sldSaturacion.addChangeListener(e -> {
            if (imagenOriginal == null || ignoreSliderEvents)
                return;
            float factor = sldSaturacion.getValue() / 100.0f;
            lblSatVal.setText(String.format(Locale.US, "%.2f", factor));
            if (!sldSaturacion.getValueIsAdjusting()) {
                aplicarFiltroBasico("saturacion", factor);
            }
        });
        pnlSat.add(lblSatName, BorderLayout.WEST);
        pnlSat.add(lblSatVal, BorderLayout.EAST);
        pnlSat.add(sldSaturacion, BorderLayout.SOUTH);
        pnlBasicos.add(pnlSat);
        pnlBasicos.add(Box.createRigidArea(new Dimension(0, 10)));

        // Recorte Bits Slider
        JPanel pnlBits = new JPanel(new BorderLayout());
        JLabel lblBitsName = new JLabel("Recorte de Bits (1-8): ");
        JLabel lblBitsVal = new JLabel("8");
        sldRecorteBits = new JSlider(1, 8, 8);
        sldRecorteBits.addChangeListener(e -> {
            if (imagenOriginal == null || ignoreSliderEvents)
                return;
            int bits = sldRecorteBits.getValue();
            lblBitsVal.setText(String.valueOf(bits));
            if (!sldRecorteBits.getValueIsAdjusting()) {
                aplicarFiltroBasico("recorte", bits);
            }
        });
        pnlBits.add(lblBitsName, BorderLayout.WEST);
        pnlBits.add(lblBitsVal, BorderLayout.EAST);
        pnlBits.add(sldRecorteBits, BorderLayout.SOUTH);
        pnlBasicos.add(pnlBits);
        pnlBasicos.add(Box.createRigidArea(new Dimension(0, 10)));

        // Buttons for Basic filters
        JPanel pnlBasicBtns = new JPanel(new GridLayout(2, 2, 5, 5));
        JButton btnGrises = new JButton("Gris");
        JButton btnNegativo = new JButton("Negativo");
        JButton btnOcre = new JButton("Ocre");
        JButton btnBnw = new JButton("Blanco y Negro");

        btnGrises.addActionListener(e -> aplicarFiltroBasico("grises", null));
        btnNegativo.addActionListener(e -> aplicarFiltroBasico("negativo", null));
        btnOcre.addActionListener(e -> aplicarFiltroBasico("ocre", null));
        btnBnw.addActionListener(e -> aplicarFiltroBasico("bnw", null));

        pnlBasicBtns.add(btnGrises);
        pnlBasicBtns.add(btnNegativo);
        pnlBasicBtns.add(btnOcre);
        pnlBasicBtns.add(btnBnw);
        pnlBasicos.add(pnlBasicBtns);

        sidebar.add(pnlBasicos);
        sidebar.add(Box.createRigidArea(new Dimension(0, 15)));

        // 2. SECCIÓN FILTROS ESPACIALES
        JPanel pnlEspaciales = new JPanel();
        pnlEspaciales.setLayout(new BoxLayout(pnlEspaciales, BoxLayout.Y_AXIS));
        pnlEspaciales.setBorder(BorderFactory.createTitledBorder("Filtros Espaciales / Convolución"));

        JPanel pnlEspBtns1 = new JPanel(new GridLayout(3, 2, 5, 5));
        JButton btnSobel = new JButton("Bordes Sobel");
        JButton btnBoxBlur = new JButton("Box Blur (3x3)");
        JButton btnBoxBlurW = new JButton("Box Blur Wiki");
        JButton btnSharpen = new JButton("Sharpen Wiki");
        JButton btnRelieveRojo = new JButton("Relieve Rojo");
        JButton btnIdentity = new JButton("Filtro Identidad");

        btnSobel.addActionListener(e -> aplicarFiltroEspacial("sobel"));
        btnBoxBlur.addActionListener(e -> aplicarFiltroEspacial("boxblur"));
        btnBoxBlurW.addActionListener(e -> aplicarFiltroEspacial("boxblurwiki"));
        btnSharpen.addActionListener(e -> aplicarFiltroEspacial("sharpen"));
        btnRelieveRojo.addActionListener(e -> aplicarFiltroEspacial("relieverojo"));
        btnIdentity.addActionListener(e -> aplicarFiltroEspacial("identity"));

        pnlEspBtns1.add(btnSobel);
        pnlEspBtns1.add(btnBoxBlur);
        pnlEspBtns1.add(btnBoxBlurW);
        pnlEspBtns1.add(btnSharpen);
        pnlEspBtns1.add(btnRelieveRojo);
        pnlEspBtns1.add(btnIdentity);
        pnlEspaciales.add(pnlEspBtns1);
        pnlEspaciales.add(Box.createRigidArea(new Dimension(0, 5)));

        // Edge Submenu
        JPanel pnlEdge = new JPanel(new GridLayout(1, 3, 5, 5));
        JButton btnEdge1 = new JButton("Edge 1");
        JButton btnEdge2 = new JButton("Edge 2");
        JButton btnEdge3 = new JButton("Edge 3");

        btnEdge1.addActionListener(e -> aplicarFiltroEspacial("edge1"));
        btnEdge2.addActionListener(e -> aplicarFiltroEspacial("edge2"));
        btnEdge3.addActionListener(e -> aplicarFiltroEspacial("edge3"));

        pnlEdge.add(btnEdge1);
        pnlEdge.add(btnEdge2);
        pnlEdge.add(btnEdge3);
        pnlEspaciales.add(pnlEdge);

        sidebar.add(pnlEspaciales);
        sidebar.add(Box.createRigidArea(new Dimension(0, 15)));

        // 3. SECCIÓN EFECTOS RETRO
        JPanel pnlRetro = new JPanel();
        pnlRetro.setLayout(new BoxLayout(pnlRetro, BoxLayout.Y_AXIS));
        pnlRetro.setBorder(BorderFactory.createTitledBorder("Efectos Retro & Especiales"));

        JPanel pnlRetroBtns = new JPanel(new GridLayout(3, 2, 5, 5));
        JButton btnVidrio = new JButton("Vidrio Esmerilado");
        JButton btnDesvanecer = new JButton("Desvanecer Circular");
        JButton btnEspejo = new JButton("Efecto Espejo");
        JButton btnVerdeGris = new JButton("Verde y Gris");
        JButton btnDividido = new JButton("Dividir en 3");
        JButton btnGrisesRetro = new JButton("Grises Retro (8)");

        btnVidrio.addActionListener(e -> aplicarFiltroRetro("vidrio", null));
        btnDesvanecer.addActionListener(e -> aplicarFiltroRetro("desvanecer", null));
        btnEspejo.addActionListener(e -> aplicarFiltroRetro("espejo", null));
        btnVerdeGris.addActionListener(e -> aplicarFiltroRetro("verdegris", null));
        btnDividido.addActionListener(e -> aplicarFiltroRetro("dividido", null));
        btnGrisesRetro.addActionListener(e -> aplicarFiltroRetro("grisesretro", 8));

        pnlRetroBtns.add(btnVidrio);
        pnlRetroBtns.add(btnDesvanecer);
        pnlRetroBtns.add(btnEspejo);
        pnlRetroBtns.add(btnVerdeGris);
        pnlRetroBtns.add(btnDividido);
        pnlRetroBtns.add(btnGrisesRetro);
        pnlRetro.add(pnlRetroBtns);
        pnlRetro.add(Box.createRigidArea(new Dimension(0, 10)));

        // Posterización Slider (Retro)
        JPanel pnlPost = new JPanel(new BorderLayout());
        JLabel lblPostName = new JLabel("Posterizar (N colores): ");
        JLabel lblPostVal = new JLabel("16");
        JSlider sldPost = new JSlider(2, 64, 16);
        sldPost.addChangeListener(e -> {
            if (imagenOriginal == null)
                return;
            int n = sldPost.getValue();
            lblPostVal.setText(String.valueOf(n));
            if (!sldPost.getValueIsAdjusting()) {
                aplicarFiltroRetro("retro", n);
            }
        });
        pnlPost.add(lblPostName, BorderLayout.WEST);
        pnlPost.add(lblPostVal, BorderLayout.EAST);
        pnlPost.add(sldPost, BorderLayout.SOUTH);
        pnlRetro.add(pnlPost);

        sidebar.add(pnlRetro);

        return panel;
    }

    private JPanel crearPanelGeneradores() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        JPanel pnlControls = new JPanel(new GridBagLayout());
        pnlControls.setBorder(BorderFactory.createTitledBorder("Configurar Generador"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Generator Type
        gbc.gridx = 0;
        gbc.gridy = 0;
        pnlControls.add(new JLabel("Tipo:"), gbc);

        String[] types = {
                "HORIZONTAL_NORMAL",
                "HORIZONTAL_INVERSO_ROJO_AZUL",
                "VERTICAL_NORMAL",
                "VERTICAL_INVERSO",
                "RADIAL",
                "RUIDO_ALEATORIO"
        };
        JComboBox<String> cmbType = new JComboBox<>(types);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        pnlControls.add(cmbType, gbc);

        // Dimensions
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        pnlControls.add(new JLabel("Ancho (px):"), gbc);

        JTextField txtAncho = new JTextField("512");
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        pnlControls.add(txtAncho, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0;
        pnlControls.add(new JLabel("Alto (px):"), gbc);

        JTextField txtAlto = new JTextField("512");
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weightx = 1.0;
        pnlControls.add(txtAlto, gbc);

        JButton btnGenerar = new JButton("Generar Imagen");
        btnGenerar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        pnlControls.add(btnGenerar, gbc);

        panel.add(pnlControls, BorderLayout.WEST);

        // Display area
        JLabel lblDisplayGen = new JLabel("Haz clic en Generar para visualizar", SwingConstants.CENTER);
        lblDisplayGen.setBorder(BorderFactory.createEtchedBorder());
        JScrollPane scrollGen = new JScrollPane(lblDisplayGen);
        panel.add(scrollGen, BorderLayout.CENTER);

        btnGenerar.addActionListener(e -> {
            try {
                int w = Integer.parseInt(txtAncho.getText().trim());
                int h = Integer.parseInt(txtAlto.getText().trim());
                String selectedType = (String) cmbType.getSelectedItem();

                BufferedImage genImg;
                if ("RUIDO_ALEATORIO".equals(selectedType)) {
                    genImg = Generadores.generarRuido(w, h);
                } else {
                    genImg = Generadores.generarGradiente(w, h, selectedType);
                }

                lblDisplayGen.setIcon(new ImageIcon(genImg));
                lblDisplayGen.setText("");

                // Allow saving the generated image
                JPanel pnlSaveGen = new JPanel(new FlowLayout(FlowLayout.CENTER));
                JButton btnSaveGen = new JButton("Guardar Imagen Generada...");
                btnSaveGen.addActionListener(evt -> {
                    JFileChooser chooser = new JFileChooser();
                    if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                        try {
                            File f = chooser.getSelectedFile();
                            if (!f.getName().contains(".")) {
                                f = new File(f.getAbsolutePath() + ".png");
                            }
                            ImageIO.write(genImg, "png", f);
                            JOptionPane.showMessageDialog(this, "Imagen guardada correctamente.", "Éxito",
                                    JOptionPane.INFORMATION_MESSAGE);
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(this, "Error al guardar imagen: " + ex.getMessage(), "Error",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    }
                });
                // Remove older buttons if any
                BorderLayout layout = (BorderLayout) panel.getLayout();
                Component south = layout.getLayoutComponent(BorderLayout.SOUTH);
                if (south != null)
                    panel.remove(south);

                pnlSaveGen.add(btnSaveGen);
                panel.add(pnlSaveGen, BorderLayout.SOUTH);
                panel.revalidate();
                panel.repaint();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Dimensiones inválidas.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        return panel;
    }

    private void resetSliders() {
        ignoreSliderEvents = true;
        sldBrillo.setValue(0);
        sldTransparencia.setValue(100);
        sldSaturacion.setValue(100);
        sldRecorteBits.setValue(8);
        ignoreSliderEvents = false;
    }

    private void accCargarImagen() {
        JFileChooser chooser = new JFileChooser(new File("."));
        chooser.setDialogTitle("Selecciona una Imagen de Entrada");
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                File selectedFile = chooser.getSelectedFile();
                BufferedImage img = ImageIO.read(selectedFile);
                if (img == null) {
                    JOptionPane.showMessageDialog(this, "El archivo seleccionado no es una imagen válida.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                imagenOriginal = img;
                imagenProcesada = copiarImagen(imagenOriginal);
                resetSliders();
                actualizarVistas();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al cargar la imagen: " + ex.getMessage(), "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void accGuardarImagen() {
        if (imagenProcesada == null) {
            JOptionPane.showMessageDialog(this, "No hay ninguna imagen procesada para guardar.", "Atención",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        JFileChooser chooser = new JFileChooser(new File("."));
        chooser.setDialogTitle("Guardar Imagen Procesada");
        if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                File file = chooser.getSelectedFile();
                String ext = "png";
                if (file.getName().toLowerCase().endsWith(".jpg") || file.getName().toLowerCase().endsWith(".jpeg")) {
                    ext = "jpg";
                } else if (!file.getName().contains(".")) {
                    file = new File(file.getAbsolutePath() + ".png");
                }
                ImageIO.write(imagenProcesada, ext, file);
                JOptionPane.showMessageDialog(this, "Imagen guardada exitosamente en " + file.getName(), "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al guardar la imagen: " + ex.getMessage(), "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void actualizarVistas() {
        if (imagenOriginal == null) {
            lblOriginal.setIcon(null);
            lblOriginal.setText("Ninguna imagen cargada");
            lblProcesada.setIcon(null);
            lblProcesada.setText("");
            return;
        }

        if (chkAjustar.isSelected()) {
            lblOriginal.setIcon(obtenerIconoAjustado(imagenOriginal, scrollOriginal));
            lblOriginal.setText("");
            lblProcesada.setIcon(obtenerIconoAjustado(imagenProcesada, scrollProcesada));
            lblProcesada.setText("");
        } else {
            lblOriginal.setIcon(new ImageIcon(imagenOriginal));
            lblOriginal.setText("");
            lblProcesada.setIcon(new ImageIcon(imagenProcesada));
            lblProcesada.setText("");
        }

        lblOriginal.revalidate();
        lblOriginal.repaint();
        lblProcesada.revalidate();
        lblProcesada.repaint();
    }

    private ImageIcon obtenerIconoAjustado(BufferedImage img, JScrollPane scrollPane) {
        if (img == null)
            return null;
        int scrollW = scrollPane.getWidth() - 25;
        int scrollH = scrollPane.getHeight() - 25;
        if (scrollW <= 0 || scrollH <= 0) {
            scrollW = 350;
            scrollH = 350;
        }
        double ratioImg = (double) img.getWidth() / img.getHeight();
        double ratioScroll = (double) scrollW / scrollH;
        int newW, newH;
        if (ratioImg > ratioScroll) {
            newW = scrollW;
            newH = (int) (scrollW / ratioImg);
        } else {
            newH = scrollH;
            newW = (int) (scrollH * ratioImg);
        }

        newW = Math.max(1, newW);
        newH = Math.max(1, newH);

        Image scaled = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }

    private void aplicarFiltroBasico(String name, Object param) {
        if (imagenOriginal == null)
            return;

        try {
            switch (name) {
                case "brillo":
                    imagenProcesada = FiltrosBasicos.aplicarBrillo(imagenOriginal, (int) param);
                    break;
                case "transparencia":
                    imagenProcesada = FiltrosBasicos.aplicarTransparencia(imagenOriginal, (float) param);
                    break;
                case "saturacion":
                    imagenProcesada = FiltrosBasicos.aplicarSaturacion(imagenOriginal, (float) param);
                    break;
                case "recorte":
                    imagenProcesada = FiltrosBasicos.aplicarRecorteBits(imagenOriginal, (int) param);
                    break;
                case "grises":
                    imagenProcesada = FiltrosBasicos.aplicarEscalaGrises(imagenOriginal);
                    break;
                case "negativo":
                    imagenProcesada = FiltrosBasicos.aplicarNegativo(imagenOriginal);
                    break;
                case "ocre":
                    imagenProcesada = FiltrosBasicos.aplicarTinteOcre(imagenOriginal);
                    break;
                case "bnw":
                    imagenProcesada = FiltrosBasicos.aplicarBlancoYNegro(imagenOriginal);
                    break;
            }
            actualizarVistas();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al aplicar filtro básico: " + ex.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void aplicarFiltroEspacial(String name) {
        if (imagenOriginal == null)
            return;

        try {
            switch (name) {
                case "sobel":
                    imagenProcesada = FiltrosEspaciales.aplicarSobel(imagenOriginal);
                    break;
                case "boxblur":
                    imagenProcesada = FiltrosEspaciales.aplicarBoxBlur(imagenOriginal, 3);
                    break;
                case "boxblurwiki":
                    imagenProcesada = FiltrosEspaciales.aplicarBoxBlurWiki(imagenOriginal);
                    break;
                case "sharpen":
                    imagenProcesada = FiltrosEspaciales.aplicarSharpenWiki(imagenOriginal);
                    break;
                case "relieverojo":
                    imagenProcesada = FiltrosEspaciales.aplicarRelieveRojo(imagenOriginal);
                    break;
                case "identity":
                    imagenProcesada = FiltrosEspaciales.aplicarIdentity(imagenOriginal);
                    break;
                case "edge1":
                    imagenProcesada = FiltrosEspaciales.aplicarEdge1(imagenOriginal);
                    break;
                case "edge2":
                    imagenProcesada = FiltrosEspaciales.aplicarEdge2(imagenOriginal);
                    break;
                case "edge3":
                    imagenProcesada = FiltrosEspaciales.aplicarEdge3(imagenOriginal);
                    break;
            }
            actualizarVistas();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al aplicar filtro espacial: " + ex.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void aplicarFiltroRetro(String name, Object param) {
        if (imagenOriginal == null)
            return;

        try {
            switch (name) {
                case "vidrio":
                    imagenProcesada = EfectosRetro.aplicarVidrioEsmerilado(imagenOriginal);
                    break;
                case "desvanecer":
                    imagenProcesada = EfectosRetro.aplicarDesvanecimientoCircular(imagenOriginal);
                    break;
                case "espejo":
                    imagenProcesada = EfectosRetro.aplicarEspejo(imagenOriginal); // Corrected mirror name matching
                    break;
                case "verdegris":
                    imagenProcesada = EfectosRetro.aplicarVerdeYGris(imagenOriginal);
                    break;
                case "dividido":
                    imagenProcesada = EfectosRetro.aplicarDivididoEn3(imagenOriginal);
                    break;
                case "grisesretro":
                    imagenProcesada = EfectosRetro.aplicarEscalaDeGrisesRetro(imagenOriginal, (int) param);
                    break;
                case "retro":
                    imagenProcesada = EfectosRetro.aplicarEfectoRetro(imagenOriginal, (int) param);
                    break;
            }
            actualizarVistas();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al aplicar efecto retro: " + ex.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public void ejecutarEjercicio(String className, JTextArea consolaDest, JPanel resultadosDest) {
        ejecutarEjercicio(className, consolaDest, resultadosDest, new String[0]);
    }

    public void ejecutarEjercicio(String className, JTextArea consolaDest, JPanel resultadosDest, String[] extraArgs) {
        consolaDest.setText("Iniciando ejecución de " + className + "...\n\n");
        resultadosDest.removeAll();
        resultadosDest.revalidate();
        resultadosDest.repaint();

        SwingWorker<String, Void> worker = new SwingWorker<>() {
            @Override
            protected String doInBackground() throws Exception {
                // Redirect System.out
                PrintStream oldOut = System.out;
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                PrintStream newOut = new PrintStream(baos, true, StandardCharsets.UTF_8);

                System.setOut(newOut);
                try {
                    // Resolve Package Name from Map
                    String resolvedPkg = CLASS_PACKAGE_MAP.get(className);
                    if (resolvedPkg == null) {
                        resolvedPkg = "org.example.portafolio.ejercicios";
                    }

                    Class<?> clazz = Class.forName(resolvedPkg + "." + className);
                    
                    // Check if class has main method
                    boolean hasMain = false;
                    for (Method m : clazz.getDeclaredMethods()) {
                        if ("main".equals(m.getName())) {
                            hasMain = true;
                            break;
                        }
                    }
                    
                    if (!hasMain) {
                        newOut.println("La clase '" + className + "' no es una clase ejecutable directamente.");
                        newOut.println("Esta clase actúa como biblioteca de constantes, utilidades o definición de matrices (kernels).");
                        newOut.println("Es importada y utilizada por otros ejercicios del portafolio (como convolucion).");
                        System.setOut(oldOut);
                        return baos.toString(StandardCharsets.UTF_8);
                    }

                    Method mainMethod;
                    try {
                        mainMethod = clazz.getMethod("main", String[].class);
                        mainMethod.invoke(null, (Object) extraArgs);
                    } catch (NoSuchMethodException e) {
                        try {
                            mainMethod = clazz.getDeclaredMethod("main");
                            mainMethod.setAccessible(true);
                            mainMethod.invoke(null);
                        } catch (NoSuchMethodException ex2) {
                            Method[] methods = clazz.getDeclaredMethods();
                            boolean found = false;
                            for (Method m : methods) {
                                if ("main".equals(m.getName()) && m.getParameterCount() == 1
                                        && m.getParameterTypes()[0].isArray()) {
                                    m.setAccessible(true);
                                    m.invoke(null, (Object) extraArgs);
                                    found = true;
                                    break;
                                }
                            }
                            if (!found)
                                throw ex2;
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace(newOut);
                } finally {
                    System.setOut(oldOut);
                }

                return baos.toString(StandardCharsets.UTF_8);
            }

            @Override
            protected void done() {
                try {
                    String output = get();
                    consolaDest.append(output);
                    consolaDest.append("\nEjecución finalizada.");

                    // Fetch outputs directly from memory (ImageIOManager)
                    java.util.List<ImageIOManager.OutputEntry> outputs = ImageIOManager.getOutputImages();
                    if (!outputs.isEmpty()) {
                        mostrarEjerciciosGeneradosMemoria(outputs, resultadosDest);
                    } else {
                        consolaDest.append("\nNo se detectaron nuevas imágenes creadas en memoria.");
                    }

                } catch (Exception ex) {
                    consolaDest.append("\nError en SwingWorker: " + ex.getMessage());
                }
            }
        };
        worker.execute();
    }

    private void mostrarEjerciciosGeneradosMemoria(java.util.List<ImageIOManager.OutputEntry> outputs,
            JPanel resultadosDest) {
        for (ImageIOManager.OutputEntry entry : outputs) {
            BufferedImage img = entry.image;
            if (img == null)
                continue;

            JPanel card = new JPanel(new BorderLayout(5, 5));
            card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(100, 100, 100)),
                    BorderFactory.createEmptyBorder(5, 5, 5, 5)));

            int maxSide = 140;
            double ratio = (double) img.getWidth() / img.getHeight();
            int w = ratio > 1.0 ? maxSide : (int) (maxSide * ratio);
            int h = ratio > 1.0 ? (int) (maxSide / ratio) : maxSide;
            w = Math.max(1, w);
            h = Math.max(1, h);

            Image scaled = img.getScaledInstance(w, h, Image.SCALE_SMOOTH);
            JLabel lblImg = new JLabel(new ImageIcon(scaled));

            JLabel lblName = new JLabel(entry.name);
            lblName.setFont(new Font("Segoe UI", Font.PLAIN, 10));
            lblName.setHorizontalAlignment(SwingConstants.CENTER);

            card.add(lblImg, BorderLayout.CENTER);
            card.add(lblName, BorderLayout.SOUTH);

            lblImg.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 2) {
                        visualizarImagenDetalle(entry.name, img);
                    }
                }
            });

            resultadosDest.add(card);
        }
        resultadosDest.revalidate();
        resultadosDest.repaint();
    }

    private void visualizarImagenDetalle(String name, BufferedImage img) {
        JFrame detailFrame = new JFrame("Vista Completa: " + name);
        detailFrame.setSize(800, 600);
        detailFrame.setLocationRelativeTo(this);

        JLabel lbl = new JLabel();
        lbl.setHorizontalAlignment(SwingConstants.CENTER);
        JScrollPane scroll = new JScrollPane(lbl);
        detailFrame.add(scroll, BorderLayout.CENTER);

        lbl.setIcon(new ImageIcon(img));
        detailFrame.setVisible(true);
    }
}

class PanelEjecutorTema extends JPanel {
    private JList<String> listEjercicios;
    private JTextArea txtConsola;
    private JPanel pnlResultados;
    private JLabel lblSelectedTitle;
    private JComboBox<String> cmbParam;
    private JButton btnEjecutar;
    private FramePrincipal parentFrame;

    private BufferedImage imgEntrada1;
    private BufferedImage imgEntrada2;

    public PanelEjecutorTema(FramePrincipal parentFrame, String[] items) {
        this.parentFrame = parentFrame;
        setLayout(new BorderLayout(10, 10));

        try {
            File f1 = new File("images/galaxia.png");
            if (f1.exists()) {
                imgEntrada1 = ImageIO.read(f1);
            } else {
                imgEntrada1 = org.example.portafolio.utils.PlaceholderImageGenerator.createDefaultImage(500, 500,
                        "Imagen A (Autogenerada)");
            }

            File f2 = new File("images/manchester.jpg");
            if (!f2.exists())
                f2 = new File("images/clara.jpg");
            if (f2.exists()) {
                imgEntrada2 = ImageIO.read(f2);
            } else {
                imgEntrada2 = org.example.portafolio.utils.PlaceholderImageGenerator.createDefaultImage(500, 500,
                        "Imagen B (Autogenerada)");
            }
        } catch (Exception ex) {
            imgEntrada1 = org.example.portafolio.utils.PlaceholderImageGenerator.createDefaultImage(500, 500,
                    "Imagen A (Autogenerada)");
            imgEntrada2 = org.example.portafolio.utils.PlaceholderImageGenerator.createDefaultImage(500, 500,
                    "Imagen B (Autogenerada)");
        }

        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (String item : items) {
            listModel.addElement(item);
        }

        listEjercicios = new JList<>(listModel);
        listEjercicios.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listEjercicios.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        listEjercicios.setFixedCellHeight(26);
        JScrollPane scrollList = new JScrollPane(listEjercicios);

        JPanel pnlLeft = new JPanel(new BorderLayout(5, 5));
        pnlLeft.setPreferredSize(new Dimension(300, 0));
        pnlLeft.add(scrollList, BorderLayout.CENTER);

        JPanel pnlInputs = new JPanel(new GridLayout(2, 1, 5, 5));
        pnlInputs.setBorder(BorderFactory.createTitledBorder("Imǭgenes de Entrada para Ejercicio"));

        String nameA = new File("images/galaxia.png").exists() ? "galaxia.png" : "Autogenerada A";
        JPanel pnlIn1 = new JPanel(new BorderLayout(5, 2));
        JLabel lblIn1 = new JLabel("Img A: [" + nameA + "]");
        JButton btnLoadIn1 = new JButton("Cargar...");
        btnLoadIn1.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser(new File("."));
            if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                try {
                    imgEntrada1 = ImageIO.read(chooser.getSelectedFile());
                    lblIn1.setText("Img A: [" + chooser.getSelectedFile().getName() + "]");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error al cargar: " + ex.getMessage(), "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        pnlIn1.add(lblIn1, BorderLayout.CENTER);
        pnlIn1.add(btnLoadIn1, BorderLayout.EAST);

        String nameB = new File("images/manchester.jpg").exists() ? "manchester.jpg"
                : (new File("images/clara.jpg").exists() ? "clara.jpg" : "Autogenerada B");
        JPanel pnlIn2 = new JPanel(new BorderLayout(5, 2));
        JLabel lblIn2 = new JLabel("Img B: [" + nameB + "]");
        JButton btnLoadIn2 = new JButton("Cargar...");
        btnLoadIn2.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser(new File("."));
            if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                try {
                    imgEntrada2 = ImageIO.read(chooser.getSelectedFile());
                    lblIn2.setText("Img B: [" + chooser.getSelectedFile().getName() + "]");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error al cargar: " + ex.getMessage(), "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        pnlIn2.add(lblIn2, BorderLayout.CENTER);
        pnlIn2.add(btnLoadIn2, BorderLayout.EAST);

        pnlInputs.add(pnlIn1);
        pnlInputs.add(pnlIn2);
        pnlLeft.add(pnlInputs, BorderLayout.SOUTH);

        add(pnlLeft, BorderLayout.WEST);

        JPanel pnlRight = new JPanel(new BorderLayout(5, 5));

        JPanel pnlSubHeader = new JPanel(new BorderLayout());
        pnlSubHeader.setBorder(new CompoundBorder(
                new MatteBorder(0, 0, 1, 0, new Color(80, 80, 80)),
                new EmptyBorder(5, 10, 5, 10)));

        lblSelectedTitle = new JLabel("Selecciona una clase para ejecutar...");
        lblSelectedTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblSelectedTitle.setForeground(new Color(138, 180, 248));
        pnlSubHeader.add(lblSelectedTitle, BorderLayout.WEST);

        JPanel pnlControls = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        pnlControls.setOpaque(false);

        cmbParam = new JComboBox<>();
        cmbParam.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cmbParam.setVisible(false);
        pnlControls.add(cmbParam);

        btnEjecutar = new JButton("Ejecutar Clase");
        btnEjecutar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnEjecutar.putClientProperty("JButton.buttonType", "roundRect");
        btnEjecutar.putClientProperty("JButton.boldText", true);
        btnEjecutar.setBackground(new Color(52, 168, 83));
        btnEjecutar.setForeground(Color.WHITE);
        btnEjecutar.setEnabled(false);
        pnlControls.add(btnEjecutar);

        pnlSubHeader.add(pnlControls, BorderLayout.EAST);

        pnlRight.add(pnlSubHeader, BorderLayout.NORTH);

        JSplitPane splitOutputs = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitOutputs.setResizeWeight(0.5);

        txtConsola = new JTextArea();
        txtConsola.setEditable(false);
        txtConsola.setFont(new Font("Consolas", Font.PLAIN, 12));
        txtConsola.setBackground(new Color(25, 25, 25));
        txtConsola.setForeground(new Color(220, 220, 220));
        txtConsola.setMargin(new Insets(10, 10, 10, 10));
        JScrollPane scrollConsola = new JScrollPane(txtConsola);
        scrollConsola.setBorder(BorderFactory.createTitledBorder("Salida de Consola (System.out)"));
        splitOutputs.setTopComponent(scrollConsola);

        pnlResultados = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
        JScrollPane scrollResultados = new JScrollPane(pnlResultados);
        scrollResultados.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollResultados.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scrollResultados.setBorder(BorderFactory.createTitledBorder("Imǭgenes de Resultados (Generadas en Memoria)"));
        splitOutputs.setBottomComponent(scrollResultados);

        pnlRight.add(splitOutputs, BorderLayout.CENTER);
        add(pnlRight, BorderLayout.CENTER);

        listEjercicios.addListSelectionListener(e -> {
            String selected = listEjercicios.getSelectedValue();
            if (selected == null) {
                lblSelectedTitle.setText("Selecciona una clase para ejecutar...");
                btnEjecutar.setEnabled(false);
                cmbParam.setVisible(false);
                return;
            }
            lblSelectedTitle.setText("Clase Seleccionada: " + selected);
            btnEjecutar.setEnabled(true);

            String className = selected.split(" ")[0].trim();
            if ("convolucion".equals(className)) {
                cmbParam.removeAllItems();
                cmbParam.addItem("sobel");
                cmbParam.addItem("box blur");
                cmbParam.addItem("identidad");
                cmbParam.addItem("detección de bordes");
                cmbParam.addItem("sharpen");
                cmbParam.addItem("gaussiano");
                cmbParam.setSelectedIndex(0);
                cmbParam.setVisible(true);
            } else if ("convolucionOp".equals(className)) {
                cmbParam.removeAllItems();
                cmbParam.addItem("box blur");
                cmbParam.addItem("identidad");
                cmbParam.addItem("sharpen");
                cmbParam.addItem("detección de bordes");
                cmbParam.addItem("aclarar");
                cmbParam.addItem("oscurecer");
                cmbParam.setSelectedIndex(0);
                cmbParam.setVisible(true);
            } else {
                cmbParam.setVisible(false);
            }
            pnlSubHeader.revalidate();
            pnlSubHeader.repaint();
        });

        btnEjecutar.addActionListener(e -> {
            String selected = listEjercicios.getSelectedValue();
            if (selected != null) {
                String className = selected.split(" ")[0].trim();

                ImageIOManager.setInputImages(imgEntrada1, imgEntrada2);
                ImageIOManager.clearOutputs();

                if ("MainEqualizador".equals(className)) {
                    ImageIOManager.setInterceptEnabled(false);
                } else {
                    ImageIOManager.setInterceptEnabled(true);
                }

                String[] extraArgs = new String[0];
                if (cmbParam.isVisible() && cmbParam.getSelectedItem() != null) {
                    extraArgs = new String[] { (String) cmbParam.getSelectedItem() };
                }

                parentFrame.ejecutarEjercicio(className, txtConsola, pnlResultados, extraArgs);
            }
        });
    }
}


