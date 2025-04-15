/*
 * Project build instructions and dependencies (for Gradle).
 */

plugins {
    id("java")
    idea
}

// Project dependencies versions (in alphabetical order).
val iTextPdfVersion = "9.1.0"
val junitVersion = "5.12.2"
val log4jVersion = "2.24.3"

dependencies {
    implementation("com.itextpdf:kernel:$iTextPdfVersion") {
        exclude(group = "org.slf4j")
    }
    implementation("org.apache.logging.log4j:log4j-api:$log4jVersion")
    implementation("org.apache.logging.log4j:log4j-core:$log4jVersion")
    implementation("org.apache.logging.log4j:log4j-slf4j-impl:$log4jVersion")

    testImplementation("org.junit.jupiter:junit-jupiter:$junitVersion")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

// Define project specific directories (intentionally use custom project structure).
layout.buildDirectory = file("build")
val workDir = "$projectDir/work"
val programDirName = "program"
val programDir = "$workDir/$programDirName"
val testsDirName = "tests"
val libraryDirName = "library"
val testsDir = "$workDir/$testsDirName"
val distDir = "${layout.buildDirectory.get()}/distribution"
val autoDocDir = "$distDir/autodoc"

// Configure project source and compilation directories.
sourceSets {
    main {
        java {
            java.setSrcDirs(listOf("source/main/java"))
            java.destinationDirectory.set(file(programDir))
        }
    }
    test {
        java {
            java.setSrcDirs(listOf("source/test/java"))
            java.destinationDirectory.set(file(testsDir))
        }
    }
}

// Define custom build tasks.
tasks.register("checkEnv") {
    description = "Check the build pre-conditions"

    val javaVersion = JavaVersion.current()
    val minJavaVersion = project.java.targetCompatibility
    if (javaVersion < minJavaVersion) {
        throw GradleException("Inappropriate Java version ($javaVersion). " +
                "Needs ($minJavaVersion) or higher.")
    }

    val gradleVersion = GradleVersion.version(project.gradle.gradleVersion)
    val minGradleVersion = GradleVersion.version(project.extra["minGradleVersion"] as String)
    if (gradleVersion < minGradleVersion) {
        throw GradleException("Inappropriate Gradle version ($gradleVersion)." +
                " Needs ($minGradleVersion) or higher.")
    }
}

tasks.register<Copy>("dist") {
    description = "Create project distribution"

    // Ensure ordered execution of dependent tasks (this is workaround for Gradle design weakness).
    val tList = listOf("clean", "checkEnv", "build")
        .stream().map { t -> tasks[t] }.toList()
    for (i in 0 until tList.size - 1) {
        tList[i + 1].mustRunAfter(tList[i])
    }
    dependsOn(tList)

    destinationDir = file(distDir)
    into(project.extra["APP_NAME"] as String) {
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        from(workDir)
        exclude(testsDirName, "testing")
        into(libraryDirName) {
            from(project.configurations.runtimeClasspath)
        }
        includeEmptyDirs = true
    }
}

// Customize project build tasks.
tasks {
    clean {
        // Clean the compilation target directories.
        delete(rootProject.layout.buildDirectory)
        delete(programDir)
        delete(testsDir)
    }

    build {
        // Check build tools versions first.
        dependsOn("checkEnv")
    }

    test {
        // Workaround for test executions from Intellij Idea IDE.
        useJUnitPlatform()
    }

    jar {
        archiveBaseName.set(project.extra["APP_NAME"] as String)
        manifest {
            attributes["Specification-Title"] = project.name
            attributes["Specification-Version"] = version
            attributes["Implementation-Vendor"] = "DSK"
            attributes["Main-Class"] = "dsk.anotex.ConsoleRunner"
        }
    }
    // Do not produce jar file for the project.
    //jar.get().enabled = false

    java {
        group = "dsk"
        version = project.extra["APP_VERSION"] as String
    }

    javadoc {
        setDestinationDir(file(autoDocDir))
        options {
            this as StandardJavadocDocletOptions
            addStringOption("Xdoclint:none", "-quiet")
        }
    }

    // Force IntelliJ Idea IDE to use the same build directories as Gradle (avoid recompilation).
    idea {
        module {
            outputDir = file(programDir)
            testOutputDir = file(testsDir)
            //downloadJavadoc = true
            //downloadSources = true
        }
    }
}

defaultTasks("dist")
