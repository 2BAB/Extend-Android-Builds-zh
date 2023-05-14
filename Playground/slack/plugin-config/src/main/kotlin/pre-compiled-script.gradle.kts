// 4.1
val propertyInPreCompiledScript = 0
// Can not access $propertyInPreCompiledScript while debugging
// However the break point is valid, it will suspend at here if you add one
project.logger.lifecycle("[Pg][DebugTips]: 4.1 " +
            "propertyInPreCompiledScript=$propertyInPreCompiledScript")

// 4.2
project.afterEvaluate {
    val taskCount = tasks.count()
    // Can not access $propertyInPreCompiledScript while debugging
//    project.logger.lifecycle("[Pg][DebugTips]: 4.2 " +
//            "propertyInPreCompiledScript=$propertyInPreCompiledScript")
    project.logger.lifecycle("[Pg][DebugTips]: 4.2 " +
                "taskCount=$taskCount")
}

// 4.3
//project.extensions
//    .findByType(AppExtension::class.java)!!
//    .buildTypes
//    .first()
//    .let {
//        project.logger.lifecycle("[Pg][DebugTips]: 4.3 $it.name")
//    }