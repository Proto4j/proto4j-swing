package org.proto4j.test.swing.case1; //@date 06.09.2022

import org.proto4j.swing.annotation.*;

import javax.swing.*;
import java.awt.*;

@GUI
public class HelloWorldGUI {

    @SwingWindow
    @Layout(BorderLayout.class)
    @Position(width = 300, height = 300)
    private JFrame mainFrame;

    public HelloWorldGUI() {
    }

    @EntryPoint
    public void show() {
        mainFrame.setVisible(true);
    }
}
