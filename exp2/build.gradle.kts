
plugins {
    id("java")
}

group = "cn.noy.javahw"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    implementation("org.jsoup:jsoup:1.16.1")
    implementation("org.apache.httpcomponents:httpclient:4.5.13")
    implementation("org.jetbrains:annotations:24.0.0")
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

//tasks.test {
//    useJUnitPlatform()
//}

tasks.compileJava{
    options.encoding = "UTF-8"
}

task<JavaExec>("run") {
    mainClass.set("cn.noy.javahw.Main")
    classpath = sourceSets["main"].runtimeClasspath
    jvmArgs("-Dfile.encoding=UTF-8")
//    args("http://www.zzzcn.org/197_197480/")
//    args("https://www.ishuquge.la/txt/158004/index.html")
    args("http://www.biquge66.net/book/697/");
}
