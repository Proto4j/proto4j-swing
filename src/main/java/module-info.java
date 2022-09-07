/**
 * This Java module turns an annotated class into a Swing-Application. It
 * supports extensions for the used LookAndFeel and own implementations of
 * {@link java.awt.Component}s.
 *
 * @author MatrixEditor
 * @author Proto4j
 */
module proto4j.swing {
    requires java.desktop;

    exports org.proto4j.swing;
    exports org.proto4j.swing.laf;
    exports org.proto4j.swing.annotation;

    exports org.proto4j.swing.core;
    exports org.proto4j.swing.core.desc;
    exports org.proto4j.swing.core.desc.layout;
    exports org.proto4j.swing.core.desc.margin;
    exports org.proto4j.swing.core.handler;

    // The following two statements define the LAFProvider and SwingHandler
    // as an exported service, so new implementations can be added with just
    // following the basics on Java-Services.
    uses org.proto4j.swing.laf.LAFProvider;
    uses org.proto4j.swing.core.SwingHandler;
}