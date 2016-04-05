require 'rake'

ADB = "$ANDROID_HOME/platform-tools/adb "

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
