dependencies {
    implementation(project(":javac"))
    implementation(project(":core"))
    implementation(project(":server"))
    implementation(project(":entity"))
    implementation(project(":logging"))
    implementation(project(":launcher"))
    implementation(project(":model"))
    implementation(project(":xml"))
    implementation(project(":rsocket"))
    implementation(project(":json"))
    implementation(project(":protobuf"))
    implementation(project(":message-pack"))
    annotationProcessor(project(":javac"))
}
tasks["compileJava"].dependsOn("clean").dependsOn(project(":javac").tasks["build"])
