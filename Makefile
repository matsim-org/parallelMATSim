export QUICK=-Dmaven.test.skip -Dmaven.javadoc.skip -Dsource.skip -Dassembly.skipAssembly=true -DskipTests
export MPJ_HOME=/Users/kainagel/sw/mpj/mpj-v0_44/
#export JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk1.8.0_181.jdk/Contents/Home/

default:
	mvn -f ../matsim/matsim/pom.xml install ${QUICK}
	mvn -f ./pom.xml package ${QUICK}
	/Users/kainagel/sw/mpj/mpj-v0_44/bin/mpjrun.sh -np 2 -jar target/parallel-0.0.1-SNAPSHOT-jar-with-dependencies.jar 
