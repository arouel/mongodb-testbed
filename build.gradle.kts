import net.ltgt.gradle.apt.*
import org.gradle.plugins.ide.eclipse.model.EclipseJdt
import org.gradle.plugins.ide.eclipse.model.EclipseModel

plugins {
    java
    application
    eclipse
    checkstyle
    findbugs
    id("net.ltgt.apt")          version "0.18"
    id("net.ltgt.apt-eclipse")  version "0.18"
    id("com.sourcemuse.mongo")  version "1.0.6"
    id("net.ltgt.errorprone")   version "0.6"
    id("nu.studer.credentials") version "1.0.4"
    id("org.sonarqube")         version "2.6.2"
}


apply(from = "gradle/versions.gradle.kts")

val versions = ext["versions"] as HashMap<String, String>

application {
    group = "prototype"

    mainClassName = "prototype.immutables_plus_mongodb.Main"

    applicationDefaultJvmArgs = listOf(
            "-Dlog4j.configurationFile=log4j2.properties",
            "-Djava.util.logging.manager=org.apache.logging.log4j.jul.LogManager"
    )
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

repositories {
    mavenCentral()
}

dependencies {
    description = "Immutables generator dependencies"

    annotationProcessor(group = "com.google.auto.service", name = "auto-service", version = versions["autoService"])
    annotationProcessor(group = "org.immutables", name = "gson", version = versions["immutables"]).isTransitive = false
    annotationProcessor(group = "org.immutables", name = "value", version = versions["immutables"])

    compileOnly(group = "com.google.auto.service", name = "auto-service", version = versions["autoService"])
    compileOnly(group = "org.immutables", name = "builder", version = versions["immutables"])
    compileOnly(group = "org.immutables", name = "value", version = versions["immutables"], classifier = "annotations")

    compile(group = "org.immutables", name = "gson", version = versions["immutables"])
    compile(group = "org.immutables", name = "mongo", version = versions["immutables"]).isTransitive = false

    runtime(group = "org.immutables", name = "gson", version = versions["immutables"]).isTransitive = false
}

dependencies {
    description = "Vertx dependencies"

    compile(group = "io.vertx", name = "vertx-core", version = versions["vertx"])
    compile(group = "io.vertx", name = "vertx-web", version = versions["vertx"])
}

dependencies {
    description = "Dagger dependencies"

    compile(group = "com.google.dagger", name = "dagger", version = versions["dagger"])
    annotationProcessor(group = "com.google.dagger", name = "dagger-compiler", version = versions["dagger"])
}

dependencies {

    compile(group = "com.google.code.findbugs", name = "jsr305", version = "2.0.3")
    compile(group = "com.google.guava", name = "guava", version = versions["guava"])
    compile(group = "org.apache.logging.log4j", name = "log4j-core", version = versions["log4j"])
    compile(group = "org.apache.logging.log4j", name = "log4j-api", version = versions["log4j"])
    compile(group = "org.mongodb", name = "mongo-java-driver", version = versions["mongoJavaDriver"])

    errorprone(group = "com.google.errorprone", name = "error_prone_core", version = versions["errorprone"])
    errorproneJavac(group = "com.google.errorprone", name = "javac", version = versions["errorproneJavac"])

    runtime(group = "org.apache.logging.log4j", name = "log4j-jul", version = versions["log4j"])

    testCompile(group = "org.assertj", name = "assertj-core", version = "3.11.+")
    testCompile(group = "org.mockito", name = "mockito-core", version = "2.22.+")

    // JUnit Jupiter API and TestEngine implementation
    testCompile(group = "org.junit.jupiter", name = "junit-jupiter-api", version = ext.get("junitJupiterVersion").toString())
    testRuntime(group = "org.junit.jupiter", name = "junit-jupiter-engine", version = ext.get("junitJupiterVersion").toString())

    // To avoid compiler warnings about @API annotations in JUnit code
    testCompileOnly(group = "org.apiguardian", name = "apiguardian-api", version = "1.0.0")

}

tasks.withType(Test::class) {
    useJUnitPlatform()
}

configure<EclipseModel> {
    jdt {
        apt {
            // whether annotation processing is enabled in Eclipse
            isAptEnabled = true

            // where Eclipse will output the generated sources; value is interpreted as per project.file()
            setGenSrcDir(file(".apt_generated"))

            // whether annotation processing is enabled in the editor
            isReconcileEnabled = true
        }
        file {
            withProperties {
                setProperty("org.eclipse.jdt.core.formatter.lineSplit", "200")
                setProperty("org.eclipse.jdt.core.formatter.comment.line_length", "180")
                setProperty("org.eclipse.jdt.core.formatter.tabulation.char", "space")
                setProperty("org.eclipse.jdt.core.formatter.tabulation.size", "4")
                setProperty("org.eclipse.jdt.core.formatter.join_wrapped_lines", "false")
                setProperty("org.eclipse.jdt.core.compiler.doc.comment.support", "enabled")
                setProperty("org.eclipse.jdt.core.compiler.problem.missingOverrideAnnotation", "warning")
                setProperty("org.eclipse.jdt.core.compiler.problem.missingJavadocTags", "warning")
                setProperty("org.eclipse.jdt.core.compiler.problem.parameterAssignment", "ignore")
                setProperty("org.eclipse.jdt.core.compiler.problem.syntheticAccessEmulation", "ignore")
                setProperty("org.eclipse.jdt.core.compiler.problem.unqualifiedFieldAccess", "ignore")
            }
        }
    }
    classpath {
        defaultOutputDir = file("build/classes-main-ide")
    }

    factorypath {
        plusConfigurations = listOf(configurations.annotationProcessor, configurations.testAnnotationProcessor)

    }
}

// copy Eclipse JDT code formatting settings
val eclipseJdtPrepare = tasks.create("eclipseJdtPrepare", Copy::class) {
    from(rootProject.file("src/build/eclipse"))
    into(project.file(".settings/"))
    include("*.prefs")
}

tasks[EclipsePlugin.ECLIPSE_JDT_TASK_NAME].dependsOn(eclipseJdtPrepare)

// checkstyle
configure<CheckstyleExtension> {
    toolVersion = "7.7"
    configFile = rootProject.file("src/build/checkstyle/checkstyle.xml")
    isShowViolations = false
    isIgnoreFailures = true
}

tasks.withType(Checkstyle::class) {
    reports {
        xml.isEnabled = true
        html.isEnabled = false
    }
}

// findbugs
configure<FindBugsExtension> {
    sourceSets = listOf(rootProject.sourceSets["main"])
    isIgnoreFailures = true
    setIncludeFilter(rootProject.file("src/build/findbugs/include-bugs.xml"))
    reportLevel = "low"
    visitors = listOf(
            "FindNullDeref", // RCN_REDUNDANT_NULLCHECK_OF_NONNULL_VALUE
            "NumberConstructor", // DM_NUMBER_CTOR
            "SerializableIdiom" // SE_BAD_FIELD
    )
}

tasks.withType(FindBugs::class) {
    reports {
        xml.isEnabled = true
        html.isEnabled = false
    }

    include("**/*.java")
    dependsOn(tasks["compileJava"])
}
