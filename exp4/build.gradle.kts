plugins {
    id("java")
}

group = "cn.noy.javahw"
version = "unspecified"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.google.guava:guava:31.1-jre")
}

tasks.compileJava{
    options.encoding = "UTF-8"
}

task<JavaExec>("run") {
    mainClass.set("cn.noy.javahw.Main")
    classpath = sourceSets["main"].runtimeClasspath
    standardInput = System.`in`
    jvmArgs("-Dfile.encoding=UTF-8")
}

tasks.jar{
    // 启用jar任务将所有依赖包含进去
    from(configurations.runtimeClasspath.get().map {
        if (it.isDirectory) it else zipTree(it)
    })
    // 排除可能导致冲突的META-INF文件
    exclude("META-INF/*.SF", "META-INF/*.DSA", "META-INF/*.RSA")
    archiveFileName.set("WebServer.jar")
    manifest {
        attributes("Main-Class" to "cn.noy.javahw.Main")
        attributes("Class-Path" to configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) }.joinToString(" "))
        charset("UTF-8")
        manifestContentCharset = "UTF-8"
    }
}