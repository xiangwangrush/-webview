package common.rush.com.pluginprocessor;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

import common.rush.com.annotation.RushBridge;

@AutoService(Processor.class)
public class PluginProcessor extends AbstractProcessor {

    private Filer mFiler; //文件相关的辅助类
    /**
     * Elements 一个用来处理Element的工具类
     */
    private Elements mElementUtils; //元素相关的辅助类
    private Messager mMessager; //日志相关的辅助类
    private Map<String, String> pluginMap = new HashMap<>();
    private TypeElement typeElement;

    /**
     * 每个Annotation Processor必须有一个空的构造函数。
     * 编译期间，init()会自动被注解处理工具调用，并传入ProcessingEnvironment参数，
     * 通过该参数可以获取到很多有用的工具类（Element，Filer，Messager等）
     */
    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        mFiler = processingEnv.getFiler();
        mElementUtils = processingEnv.getElementUtils();
        mMessager = processingEnv.getMessager();
        mMessager.printMessage(Diagnostic.Kind.NOTE, "init");
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (pluginMap == null) {
            pluginMap = new HashMap<>();
        }
        mMessager.printMessage(Diagnostic.Kind.NOTE, "start");
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(RushBridge.class);
        for (Element element : elements) {
            typeElement = (TypeElement) element;
            String fullName = typeElement.getQualifiedName().toString();
            mMessager.printMessage(Diagnostic.Kind.NOTE, "fullName=" + fullName);
            if (fullName.isEmpty()) {
                continue;
            }
//            String filedName = element.getAnnotation(RushBridge.class).packageName();
//            mMessager.printMessage(Diagnostic.Kind.NOTE, "filedName=" + filedName);
//            String name = element.getAnnotation(RushBridge.class).name();
//            mMessager.printMessage(Diagnostic.Kind.NOTE, "name=" + name);
            String name = fullName.substring(fullName.lastIndexOf(".")).replace(".", "");
            mMessager.printMessage(Diagnostic.Kind.NOTE, "name=" + name);
            String packageName = fullName.substring(0, fullName.lastIndexOf("."));
            mMessager.printMessage(Diagnostic.Kind.NOTE, "packageName=" + packageName);
            pluginMap.put(name, packageName);
        }
        //创建文件
        generateFile(mFiler);
        return true;
    }

    /**
     * 用于指定你的java版本，一般返回：SourceVersion.latestSupported()
     *
     * @return
     */
    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    /**
     * 用于指定自定义注解处理器(Annotation Processor)是注册给哪些注解的(Annotation),
     * 注解(Annotation)指定必须是完整的包名+类名
     *
     * @return
     */
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new LinkedHashSet<>();
        types.add(RushBridge.class.getCanonicalName());
        return types;
    }

    public void generateFile(Filer filer) {
        if (pluginMap == null && pluginMap.size() <= 0) {
            return;
        }
        //创建方法
        MethodSpec.Builder initMethod = MethodSpec.methodBuilder("init")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class);
        ClassName commandManager = ClassName.get("rush.com.base.cmd", "CommandManager");
        for (Map.Entry<String, String> entry : pluginMap.entrySet()) {
            mMessager.printMessage(Diagnostic.Kind.NOTE, entry.getValue());
            mMessager.printMessage(Diagnostic.Kind.NOTE, entry.getKey());
            ClassName pluginClass = ClassName.get(entry.getValue(), entry.getKey());
            initMethod.addStatement("$T.getInstance().registerCommand(" +
                    "new $T()" +
                    ")", commandManager, pluginClass);
        }

        ClassName initInterface = ClassName.get("com.plugin.api", "PluginInitInterface");
        //generaClass 生成类
        TypeSpec injectClass = TypeSpec.classBuilder("PluginInit" + "$RushBridge")//类名字
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(initInterface)//接口
                //再加入我们的目标方法
                .addMethod(initMethod.build())
                .build();

        String packageName = "com.base";
        try {
            JavaFile.builder(packageName, injectClass)
                    .build().writeTo(mFiler);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
