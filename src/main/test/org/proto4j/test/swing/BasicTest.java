package org.proto4j.test.swing; //@date 06.09.2022

import org.proto4j.swing.Entry;
import org.proto4j.swing.annotation.ActionHandler;
import org.proto4j.test.swing.case2.HelloWorldActionGUI;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class BasicTest {

    public static void main(String[] args) throws Exception {
        // CASE 1
        //Entry<HelloWorldGUI> case1 = Entry.of(HelloWorldGUI.class);
        //case1.start();

        // CASE 2
        Entry<HelloWorldActionGUI> case2 = Entry.of(HelloWorldActionGUI.class);
        case2.linkAction(new BasicTest());
        case2.start();
    }

    @ActionHandler({"button"})
    private void onEvent(ActionEvent event) {
        JOptionPane.showMessageDialog(null, "Hello, World!");
    }
}
