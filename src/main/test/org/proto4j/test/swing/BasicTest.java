package org.proto4j.test.swing; //@date 06.09.2022

import org.proto4j.swing.Entry;
import org.proto4j.swing.FieldReference;
import org.proto4j.swing.annotation.ActionHandler;
import org.proto4j.test.swing.case1.HelloWorldGUI;
import org.proto4j.test.swing.case2.HelloWorldActionGUI;
import org.proto4j.test.swing.panel.GenPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class BasicTest {

    public static void main(String[] args) throws Exception {
        // CASE 1
//        Entry<HelloWorldGUI> case1 = Entry.of(HelloWorldGUI.class);
//        case1.start();
//        case1.linkAction("mainFrame", ActionListener.class, e -> {});

        // CASE 2
        //Entry<HelloWorldActionGUI> case2 = Entry.of(HelloWorldActionGUI.class);
        //case2.linkAction(new BasicTest());
        //case2.start();

        // CASE 3:
        UIManager.put("GenPanel.text", "Hello World");
        Entry<GenPanel> view = Entry.of(GenPanel.class);
        FieldReference<JButton> btn = view.getDeclaredField("button", JButton.class);
        JButton button = btn.get();
        System.out.println(button.getText());
    }

    @ActionHandler({"button"})
    private void onEvent(ActionEvent event) {
        JOptionPane.showMessageDialog(null, "Hello, World!");
    }
}
