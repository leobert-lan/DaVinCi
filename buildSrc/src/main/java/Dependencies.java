import java.util.Arrays;
import java.util.List;

/**
 * <p><b>Package:</b> PACKAGE_NAME </p>
 * <p><b>Project:</b> DaVinCi </p>
 * <p><b>Classname:</b> Dependencies </p>
 * Created by leobert on 2021/6/15.
 */
interface Dependencies {
    String guava = "com.google.guava:guava:30.1.1-jre";

    interface AutoService {
        String version = "1.0";
        String annotations = "com.google.auto.service:auto-service-annotations:" + version;
        String compiler = "com.google.auto.service:auto-service:" + version;
//        String ksp = "dev.zacsweers.autoservice:auto-service-ksp:0.4.2";
    }

    AutoService autoService = new AutoService() {
    };

    interface Kotlin {
        String version = "1.5.10";
        String dokkaVersion = "1.4.32";
        String jvmTarget = "1.8";
        List<String> defaultFreeCompilerArgs = Arrays.asList("-Xjsr305=strict", "-progressive");
        String compilerEmbeddable = "org.jetbrains.kotlin:kotlin-compiler-embeddable:" + version;

        interface Ksp {
            String version = "1.5.10-1.0.0-beta01";
            String api = "com.google.devtools.ksp:symbol-processing-api:" + version;
            String ksp = "com.google.devtools.ksp:symbol-processing:" + version;
        }

        Ksp ksp = new Ksp() {
        };
    }

    Kotlin kotlin = new Kotlin() {
    };

    interface KotlinPoet {
        String version = "1.8.0";
        String kotlinPoet = "com.squareup:kotlinpoet:" + version;
    }

    KotlinPoet kotlinPoet = new KotlinPoet() {
    };

    interface Testing {
        String compileTesting = "com.github.tschuchortdev:kotlin-compile-testing:1.4.2";
        String kspCompileTesting = "com.github.tschuchortdev:kotlin-compile-testing-ksp:1.4.2";
        String junit = "junit:junit:4.13.2";
        String truth = "com.google.truth:truth:1.1.2";
    }

    Testing testing = new Testing() {
    };
}
