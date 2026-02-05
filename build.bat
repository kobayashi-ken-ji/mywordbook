@REM .\build.bat で起動 -gオプションでデバッグ
@echo off

@REM 環境変数の影響をバッチ内のみにする
setlocal

@REM オプション引数を初期化
set DEBUG_OPT=

@REM 引数に -g があれば格納
for %%a in (%*) do (
    if "%%~a"=="-g" (
        set DEBUG_OPT=-g
    )
)

@REM コンパイル設定 (servlet-apiのみTomcat側から取得)
set TOMCAT_LIB=..\..\lib\servlet-api.jar
set CLASSPATH=.;%TOMCAT_LIB%;WEB-INF/lib/*;WEB-INF/classes
set SRC_DIR=WEB-INF\source
set OUT_DIR=WEB-INF\classes

@REM .java ファイルを全てリストアップ
dir /s /b "%SRC_DIR%\*.java" > sources.txt

@REM コンパイル
javac %DEBUG_OPT% -encoding UTF-8 -cp "%CLASSPATH%" -d "%OUT_DIR%" @sources.txt

@REM リストを削除
del sources.txt

@REM 入力するまで画面を停止 (VS Code では不要)
@REM pause
