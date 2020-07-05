package ru.art.generator.javac.model;

import com.sun.tools.javac.code.*;
import lombok.*;
import static com.sun.tools.javac.tree.JCTree.*;
import java.util.*;

@Getter
@Builder
public class ExistedClass {
    private final String name;
    private final JCClassDecl declaration;
    private final JCCompilationUnit packageUnit;

    @Singular("method")
    private final Map<String, ExistedMethod> methods;

    @Singular("field")
    private final Map<String, ExistedField> fields;

    public String getPackageName() {
        return packageUnit.getPackageName().toString();
    }

    public Type getType() {
        return declaration.sym.type;
    }
}
