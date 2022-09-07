/**
 * <h2>Proto4j-Swing</h2>
 * The {@code Proto4j-Swing} module turns an annotated GUI class into a
 * Java-Swing application. This module covers most of the swing classes
 * and is written to be extensible.
 * <p>
 * To load an {@link org.proto4j.swing.Entry} with the GUI instance, just
 * type:
 * <pre> {@code
 * Entry<MyGUI> entry = Entry.of(MyGUI.class);
 * }</pre>
 * If the provided GUI class takes arguments at initialization, they can be
 * provided by just adding them to the end of this method.
 *
 * @author MatrixEditor
 * @author Proto4j
 **/
package org.proto4j.swing;