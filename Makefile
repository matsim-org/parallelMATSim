export QUICK=-Dmaven.test.skip -Dmaven.javadoc.skip -Dsource.skip -Dassembly.skipAssembly=true -DskipTests
export MPJ_HOME=/Users/kainagel/sw/mpj/mpj-v0_44/

default:
	mvn --offline -f ../matsim/matsim/pom.xml install ${QUICK}
	mvn --offline -f ./pom.xml package ${QUICK}
	/Users/kainagel/sw/mpj/mpj-v0_44/bin/mpjrun.sh -np 2 -jar target/parallel-0.0.1-SNAPSHOT-jar-with-dependencies.jar 
