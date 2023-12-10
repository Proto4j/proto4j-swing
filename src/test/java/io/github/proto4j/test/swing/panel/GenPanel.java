package io.github.proto4j.test.swing.panel; //@date 02.11.2022

import io.github.proto4j.swing.annotation.*;

import javax.swing.*;
import java.awt.*;

@GUI
@Swing
@Layout(BorderLayout.class)
@Bounds(preferred = {130, 230})
public class GenPanel extends JPanel {

    @Swing
    @Option(text = "@UIManager#GenPanel.text", foreground = "@Button.foreground")
    private JButton button;

    @Swing
    private final Component box = Box.createVerticalBox();

    public JButton getButton() {
        return button;
    }
}
