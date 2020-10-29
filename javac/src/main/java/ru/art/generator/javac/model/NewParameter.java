package ru.art.generator.javac.model;

import com.sun.tools.javac.tree.JCTree.*;
import com.sun.tools.javac.util.*;
import lombok.*;
import static com.sun.tools.javac.code.Flags.*;
import static ru.art.generator.javac.context.GenerationContext.*;

@Getter
@EqualsAndHashCode
@RequiredArgsConstructor
public class NewParameter {
    private final String name;
    private final TypeModel type;

    public static NewParameter newParameter(TypeModel type, String name) {
        return new NewParameter(name, type);
    }

    public JCVariableDecl generate() {
        JCModifiers modifiers = maker().Modifiers(PARAMETER);
        Name name = elements().getName(this.name);
        JCExpression type = this.type.isArray()
                ? maker().TypeArray(maker().Ident(elements().getName(this.type.getName())))
                : maker().Ident(elements().getName(this.type.getName()));
        return maker().VarDef(modifiers, name, type, null);
    }
}