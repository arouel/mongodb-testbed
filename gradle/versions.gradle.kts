configure<ExtraPropertiesExtension> {
    set("junitPlatformVersion", "1.0.3")
    set("junitJupiterVersion", "5.3.1")
    set("versions", hashMapOf(
            "errorprone" to "2.3.1",
            "errorproneJavac" to "9+181-r4173-1",
            "guava" to "26.0-jre",
            "immutables" to "2.7.1",
            "log4j" to "2.11.1",
            "mongoJavaDriver" to "3.8.1",
            "vertx" to "3.5.3"))
}

