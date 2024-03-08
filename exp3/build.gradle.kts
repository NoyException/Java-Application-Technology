plugins {
    id("java")
}

group = "cn.noy.javahw"
version = "unspecified"

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