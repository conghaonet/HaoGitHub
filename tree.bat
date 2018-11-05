REM gradlew :app:dependencies --configuration debugCompileClasspath

REM 获取当前日期(YYYYMMDD-hhmmss)
REM set "YMDhm=%date:~,4%%date:~5,2%%date:~8,2%%time:~0,2%%time:~3,2%"
set year=%date:~,4%
set month=%date:~5,2%
set day=%date:~8,2%
set hour=%time:~0,2%
if %hour:~0,1%"" == "" (
    set hour=0%hour:~1,1%
)
set min=%time:~3,2%
set sec=%time:~6,2%
set datetime=%year%%month%%day%-%hour%%min%%sec%

gradlew :app:dependencies >tree_all_%datetime%.txt
REM gradlew :app:dependencies --configuration debugCompileClasspath >tree_debugCompile_%datetime%.txt
REM gradlew :app:dependencies --configuration debugAndroidTestCompileClasspath >tree_debugAndroidTestCompile_%datetime%.txt
REM gradlew :app:dependencies --configuration debugAndroidTestRuntimeClasspath >tree_debugAndroidTestRuntime_%datetime%.txt
REM gradlew :app:dependencies --configuration debugAnnotationProcessorClasspath >tree_debugAnnotationProcessor_%datetime%.txt
REM gradlew :app:dependencies --configuration debugRuntimeClasspath >tree_debugRuntime_%datetime%.txt
REM gradlew :app:dependencies --configuration debugUnitTestCompileClasspath >tree_debugUnitTestCompile_%datetime%.txt
REM gradlew :app:dependencies --configuration debugUnitTestRuntimeClasspath >tree_debugUnitTestRuntime_%datetime%.txt
REM gradlew :app:dependencies --configuration releaseCompileClasspath >tree_releaseCompile_%datetime%.txt
REM gradlew :app:dependencies --configuration releaseAnnotationProcessorClasspath >tree_releaseAnnotationProcessor_%datetime%.txt
REM gradlew :app:dependencies --configuration releaseRuntimeClasspath >tree_releaseRuntime_%datetime%.txt
REM gradlew :app:dependencies --configuration releaseUnitTestCompileClasspath >tree_releaseUnitTestCompile_%datetime%.txt
REM gradlew :app:dependencies --configuration releaseUnitTestRuntimeClasspath >tree_releaseUnitTestRuntime_%datetime%.txt
