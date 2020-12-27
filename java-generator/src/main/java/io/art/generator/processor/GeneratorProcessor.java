package io.art.generator.processor;

import com.google.auto.service.*;
import com.sun.source.util.*;
import com.sun.tools.javac.api.*;
import com.sun.tools.javac.main.*;
import com.sun.tools.javac.model.*;
import com.sun.tools.javac.processing.*;
import com.sun.tools.javac.tree.*;
import com.sun.tools.javac.util.*;
import io.art.generator.context.*;
import io.art.generator.context.GeneratorContextConfiguration.*;
import io.art.generator.scanner.*;
import static io.art.core.extensions.CollectionExtensions.*;
import static io.art.generator.constants.GeneratorConstants.Annotations.*;
import static io.art.generator.constants.GeneratorConstants.ProcessorOptions.*;
import static io.art.generator.context.GeneratorContext.*;
import static io.art.generator.service.GenerationService.*;
import static io.art.generator.state.GenerationState.*;
import static javax.lang.model.SourceVersion.*;
import javax.annotation.processing.*;
import javax.lang.model.element.*;
import java.util.*;

@AutoService(javax.annotation.processing.Processor.class)
@SupportedAnnotationTypes(CONFIGURATOR_ANNOTATION_NAME)
@SupportedSourceVersion(RELEASE_8)
public class GeneratorProcessor extends AbstractProcessor {
    private JavacTrees trees;
    private JavacProcessingEnvironment processingEnvironment;
    private final GeneratorContextConfigurationBuilder configurationBuilder = GeneratorContextConfiguration.builder();

    @Override
    public void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        this.processingEnvironment = (JavacProcessingEnvironment) processingEnvironment;
        trees = (JavacTrees) Trees.instance(processingEnvironment);
    }

    @Override
    public Set<String> getSupportedOptions() {
        return addToSet(DISABLE_OPTION, super.getSupportedOptions());
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnvironment) {
        if (this.processingEnvironment.getOptions().get(DISABLE_OPTION) != null) {
            return true;
        }
        if (GeneratorContext.isInitialized()) {
            if (completed()) {
                return true;
            }
            generate();
            complete();
            return true;
        }
        JavacElements elements = JavacElements.instance(processingEnvironment.getContext());
        configurationBuilder
                .options(Options.instance(processingEnvironment.getContext()))
                .processingEnvironment(processingEnvironment)
                .compiler(JavaCompiler.instance(processingEnvironment.getContext()))
                .elements(elements)
                .maker(TreeMaker.instance(processingEnvironment.getContext()));
        GeneratorScanner scanner = new GeneratorScanner(elements, configurationBuilder);
        for (Element rootElement : roundEnvironment.getRootElements()) {
            scanner.scan(trees.getPath(rootElement), trees);
        }
        initialize(configurationBuilder.build());
        return true;
    }
}
