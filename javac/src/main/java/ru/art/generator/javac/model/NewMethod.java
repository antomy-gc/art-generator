package ru.art.generator.javac.model;

import com.sun.tools.javac.tree.JCTree.*;
import com.sun.tools.javac.util.*;
import lombok.*;
import lombok.experimental.*;
import static com.sun.tools.javac.util.List.*;
import static java.util.stream.Collectors.*;
import static ru.art.generator.javac.context.GenerationContext.*;
import java.util.List;
import java.util.*;
import java.util.function.*;

@Getter
@Setter
@Accessors(fluent = true)
public class NewMethod {
    private String name;
    private long modifiers;
    private TypeModel returnType;

    private java.util.List<ParameterModel> parameters = new LinkedList<>();
    private java.util.List<Supplier<JCStatement>> statements = new LinkedList<>();

    public NewMethod parameter(ParameterModel parameter) {
        parameters.add(parameter);
        return this;
    }

    public NewMethod statement(Supplier<JCStatement> statement) {
        statements.add(statement);
        return this;
    }

    public JCMethodDecl generate() {
        JCModifiers modifiers = maker().Modifiers(this.modifiers);
        Name name = elements().getName(this.name);
        JCExpression type = returnType.generate();
        JCBlock body = maker().Block(0L, from(statements.stream().map(Supplier::get).collect(toList())));
        List<JCVariableDecl> parameters = this.parameters.stream().map(ParameterModel::generate).collect(toList());
        return maker().MethodDef(modifiers, name, type, nil(), from(parameters), nil(), body, null);
    }

    public static NewMethod newMethod() {
        return new NewMethod();
    }
}