task :default => [:package]

build_version = "SNAPSHOT"
package_name = "scaluxt" 
main_class = "com.thoughtworks.#{package_name}.ScalatraUxTest"

task :compile => [:clean] do
  SRC = FileList["src/main/**/*.scala"]
  mkdir_p "bin/main"
  sh "scalac -cp ./lib/*:. -d ./bin/main #{SRC.join(" ")}"
end

task :compile_test => [:jar] do
  SRC = FileList["src/test/**/*.scala"]
  mkdir_p "bin/test"
  sh "scalac -cp ./lib/*:./deploy/*:. -d ./bin/test #{SRC.join(" ")}"  
end

task :jar => [:compile] do
  mkdir_p "deploy"
  sh "jar -cvmf MANIFEST.MF deploy/#{package_name}-#{build_version}.jar -C bin/main ."
end

task :jar_test => [:compile_test] do
  mkdir_p "deploy"
  sh "jar -cvmf MANIFEST.MF deploy/#{package_name}-#{build_version}-test.jar -C bin/test ."
end

task :package => [:jar, :jar_test] do
  mkdir_p "deploy/lib"
  sh "cp deploy/*.jar deploy/lib/"
  sh "cp lib/*.jar deploy/lib/"
  sh "cd deploy && zip -b lib/ #{package_name}-#{build_version} lib/*"
  sh "cd deploy && md5 -q #{package_name}-#{build_version}.zip > #{package_name}-#{build_version}.md5"
  sh "rm -rf deploy/#{package_name}-#{build_version}.jar deploy/lib"
end

task :run_package => [:package] do
  sh "java -cp deploy/lib/*:. #{main_class}"
end

task :run => [:compile, :compile_test] do
  sh "java -cp lib/*:bin/main:bin/test:. #{main_class}"
end

task :clean do
  sh "rm -rf bin deploy"
end