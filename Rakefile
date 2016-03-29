require 'rake'

ADB = "$ANDROID_HOME/platform-tools/adb "
ZIPALIGN = "$ANDROID_HOME/tools/zipalign "

desc "Lauches the Android emulator"
task :emulator do
	system('"$ANDROID_HOME/tools/emulator" -avd TopoSuiteDevice&')
end

# TODO: update this task
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

desc "Launches the application on the Android device"
task :run do
	system(ADB + 'shell am start \
		   -a android.intent.action.Main -n ch.hgdev.toposuite/.entry.MainActivity')
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
