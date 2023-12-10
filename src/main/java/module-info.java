import io.github.proto4j.swing.core.SwingHandler;
import io.github.proto4j.swing.laf.LAFProvider;

/**
 * This Java module turns an annotated class into a Swing-Application. It
 * supports extensions for the used LookAndFeel and own implementations of
 * {@code java.awt.Component}s.
 *
 * @author MatrixEditor
 * @author Proto4j
 */
module proto4j.swing {
    requires java.desktop;

    exports io.github.proto4j.swing;
    exports io.github.proto4j.swing.laf;
    exports io.github.proto4j.swing.annotation;

    exports io.github.proto4j.swing.core;
    exports io.github.proto4j.swing.core.desc;
    exports io.github.proto4j.swing.core.desc.layout;
    exports io.github.proto4j.swing.core.desc.margin;
    exports io.github.proto4j.swing.core.handler;

    // The following two statements define the LAFProvider and SwingHandler
    // as an exported service, so new implementations can be added with just
    // following the basics on Java-Services.
    uses LAFProvider;
    uses SwingHandler;
}