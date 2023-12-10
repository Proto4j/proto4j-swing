package io.github.proto4j.test.swing.case2; //@date 07.09.2022

import io.github.proto4j.swing.annotation.*;

import javax.swing.*;
import java.awt.*;

@GUI
public class HelloWorldActionGUI {

    @SwingWindow
    @Layout(BorderLayout.class)
    @Position(width = 300, height = 300)
    private JFrame mainFrame;

    @Swing
    @Option(target = "mainFrame", text = "Press Me!")
    @Position(constraints = BorderLayout.CENTER)
    private JButton button;

    public HelloWorldActionGUI() {
    }

    @EntryPoint
    public void show() {
        mainFrame.setVisible(true);
    }
}
