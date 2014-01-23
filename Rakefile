task :clean do |t|
  sh "ant clean"
end

task :'compile-debug' do |t|
  sh "ant debug"
end

task :'install-debug' do |t|
  sh "adb install -r bin/bootstragram-android-debug.apk"
end

task :'run-debug' do |t|
  sh "adb shell am start -n com.bootstragram.demo/.MainActivity"
end
