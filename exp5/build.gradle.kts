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
    maven {
        url = uri("https://maven.aliyun.com/repository/central")
    }
    maven {
        url = uri("https://maven.aliyun.com/repository/public")
    }
}

dependencies {
    implementation("mysql:mysql-connector-java:8.0.28")
    implementation("org.opengauss:opengauss-jdbc:3.1.1")
    implementation("org.projectlombok:lombok:1.18.26")
    annotationProcessor("org.projectlombok:lombok:1.18.26")
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}

tasks.compileJava{
    options.encoding = "UTF-8"
}

task<JavaExec>("run") {
    mainClass.set("cn.noy.javahw.Main")
    classpath = sourceSets["main"].runtimeClasspath
    standardInput = System.`in`
    workingDir = File("../")
    jvmArgs("-Dfile.encoding=UTF-8")
}