# Parallel MATSim

Meant to work together with the 'parallelMatsim' branch of the 'matsim' repository.

Some steps that I remember to set this up:

* I used the mpi implementation from http://mpj-express.org.

* The log4j library in the lib directory of that distribution needed to be replaced by a more modern version.  We used`log4j-1.2.17.jar`.  This also needs to be accordingly replaced in the `build.xml`.

* I had mavenized mpj installed, coming from https://github.com/victorskl/mpj-maven.

* There is an old-fashioned `Makefile` which maven-compiles everything and runs it.  Note that one also has to maven-compile _and_ install matsim when changing anything there (which will happen when implementing the parallel communication).

* The general approach is to start several matsim `Control(l)er`s, and have each of them feel responsible for only part of the population and network.

* Changes are accordingly in the factories, currently in `QNetworkFactory` and in `PopulationAgentSource`.  We will need the full network (but not the full `QNetwork`!) on every computational node for the routing later; in contrast, we would actually _not_ need the full population.  But such optimizations should be done later.

* The mobsim communication is in `QNetsimEngine`.  This is so far sending an int back and forth, but not more.

General information of how we did this type of domain decomposition last time is available here: http://www.strc.ch/2003/cetin.pdf.
