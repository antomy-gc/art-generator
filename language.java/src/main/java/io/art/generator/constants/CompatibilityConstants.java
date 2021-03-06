package io.art.generator.constants;

import com.sun.tools.javac.tree.*;
import com.sun.tools.javac.util.*;
import lombok.experimental.*;
import static io.art.core.constants.CompilerSuppressingWarnings.*;
import static io.art.core.wrapper.ExceptionWrapper.*;
import static javax.lang.model.SourceVersion.*;
import java.lang.reflect.*;

@UtilityClass
@SuppressWarnings(ALL)
public class CompatibilityConstants {
    public static Method TOP_LEVEL_METHOD;
    public static Method PACKAGE_DECL_METHOD;

    static {
        if (latest() == RELEASE_8) {
            TOP_LEVEL_METHOD = wrapException(() -> TreeMaker.class.getMethod("TopLevel", List.class, JCTree.JCExpression.class, List.class));
        }
        if (latest().compareTo(RELEASE_8) > 0) {
            PACKAGE_DECL_METHOD = wrapException(() -> TreeMaker.class.getMethod("PackageDecl", List.class, JCTree.JCExpression.class));
            TOP_LEVEL_METHOD = wrapException(() -> TreeMaker.class.getMethod("TopLevel", List.class));
        }
    }
}
