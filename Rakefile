require 'rake'

ADB = "$ANDROID_HOME/platform-tools/adb "
ZIPALIGN = "$ANDROID_HOME/tools/zipalign "

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

desc "Create a release APK"
task :release do
	system('cd "$PWD/TopoSuite" && ant bump-version clean release')
	apk_unsigned = `cd "$PWD/TopoSuite/bin" && ls TopoSuite-*-release-unsigned.apk`
	apk_unsigned = apk_unsigned.strip
	apk = apk_unsigned.sub("-unsigned", "")
	system('cd "$PWD/TopoSuite/bin" && jarsigner -verbose -sigalg SHA1withRSA '\
		   '-digestalg SHA1 -keystore "$HOME/.android-keys/hgdev-toposuite.keystore" '\
		   + apk_unsigned + ' topo')
	system('cd "$PWD/TopoSuite/bin" && jarsigner -verify ' + apk_unsigned)
	system('cd "$PWD/TopoSuite/bin" && ' + ZIPALIGN + '-v 4 ' + apk_unsigned + ' ' + apk)
end

desc "Build and install the application"
task :install do
	system('cd "$PWD/TopoSuite" && ant clean debug install')
end

desc "Uninstall the application and application test (if installed)"
task :uninstall do
	system('cd "$PWD/TopoSuiteTest" && ant uninstall')
end

desc "Launches the application on the Android emulated device"
task :run do
	system(ADB + 'shell am start \
		   -a android.intent.action.Main -n ch.hgdev.toposuite/.entry.MainActivity')
end

desc "Clean TopoSuite and TopoSuiteTest"
task :clean do
	system('cd "$PWD/TopoSuite" && ant clean')
	system('cd "$PWD/TopoSuite" && rm -rvf ./javadoc ./lint_files ./lint.html')
	system('cd "$PWD/TopoSuiteTest" && ant clean')
end

desc "Run all the tests and generate a coverage report"
task :test do
	system('cd "$PWD/TopoSuiteTest" && ant clean emma debug install test')
end

desc "Generate javadoc for the project"
task :doc do
	system('cd "$PWD/TopoSuite" && ant javadoc')
end

desc "Analyze projet with lint"
task :lint do
	system('cd "$PWD/TopoSuite" && ant lint')
end

desc "Pull database from the emulator to /tmp"
task :pulldb do
	system(ADB + 'pull /data/data/ch.hgdev.toposuite/databases/topo_suite.db /tmp')
	puts "Database has been dowloaded to /tmp/topo_suite.db"
end

desc "Remove the database file from the emulator"
task :rmdb do
	system(ADB + 'shell rm /data/data/ch.hgdev.toposuite/databases/topo_suite.db')
end
