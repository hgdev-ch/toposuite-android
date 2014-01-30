require 'rake'

ADB = "$ANDROID_HOME/platform-tools/adb "

desc "Launches Eclipse"
task :eclipse do
	system("$ANDROID_HOME/../eclipse/eclipse&")
end

desc "Lauches the Android emulator"
task :emulator do
	system('"$ANDROID_HOME/tools/emulator" -avd TopoSuiteDevice&')
end

desc "Build the application"
task :build do
	system('cd "$PWD/TopoSuite" && ant clean debug')
end

desc "Build and install the application on the emulator"
task :install do
	system('cd "$PWD/TopoSuite" && ant clean debug install')
end

desc "Launches the application on the Android emulated device"
task :run do
	system(ADB + 'shell am start \
		   -a android.intent.action.Main -n ch.hgdev.toposuite/.entry.MainActivity')
end

desc "Clean TopoSuite and TopoSuiteTest"
task :clean do
	system('cd "$PWD/TopoSuite" && ant clean')
	system('cd "$PWD/TopoSuiteTest" && ant clean')
end

desc "Run all the tests and generate a coverage report"
task :test do
	system('cd "$PWD/TopoSuiteTest" && ant clean emma debug install test')
end

