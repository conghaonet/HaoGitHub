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
gradlew :app:dependencies --configuration releaseCompileClasspath >tree_%datetime%.txt
