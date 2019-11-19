# Parallel MATSim

### Context

The group of Gustavo Alonso (https://www.systems.ethz.ch) is working on a parallel implementation of MATSim.  While they have concentrated on finding the right data structures and the right software, we have started (!) to make parallel what we have.  This was facilitated by our earlier experiences (http://www.strc.ch/2003/cetin.pdf).  The idea essentially is (assuming N computational nodes (=CPNs)):

* Start these N CPNs.

* Have each CPN be responsible for a domain (i.e. we are using domanin decomposition).

* During MATSim replanning, each CPN performs replanning for those agents who start the simulation in that domain (essentially those who are at home in the domain).  This is "trivially parallel", so not investigated further for the prototype here.

* During MATSim mobsim, each CPN is responsible for the traffic flow simulation within its domain.  At the boundaries, it needs to:
  - Send vehicles to downstream CPNS; receive vehicles from upstream CPNs. (+)
  - Send number of available spaces to upstream CPNs; receive number of available spaces from downstream CPNs.


* The main problem is point (+), since the vehicle object needs to be serialized, sent to the other CPN, received there, and deserialized.  Within the vehicle there are persons, each person has a plan, which consists of activities and legs, etc.  The Java default serialization was used here, with the hope that we could replace it by something better eventually.  

The work was pushed far enough that one could start with replacing the serialization and/or setting up the remaining elements (e.g. an automatic domain decomposition; currently it is only cutting the equil scenario into two).  However, no more progress has happened since then.  In other words, this shows first steps towards parallelization, but is far from being done or even usable.

:-(


### Setup

This is meant to work together with the `parallelMatsim` branch of the 'matsim' repository.

Some steps that I remember to set this up:

* I used the mpi implementation from http://mpj-express.org.

* The log4j library in the lib directory of that distribution needed to be replaced by a more modern version.  We used`log4j-1.2.17.jar`.  This also needs to be accordingly replaced in the `build.xml`.

* I had mavenized mpj installed, coming from https://github.com/victorskl/mpj-maven.

* There is an old-fashioned `Makefile` which maven-compiles everything and runs it.  Note that one also has to maven-compile _and_ install matsim when changing anything there (which will happen when implementing the parallel communication).

* The general approach is to start several matsim `Control(l)er`s, and have each of them feel responsible for only part of the population and network.

* Changes are accordingly in the factories, currently in `QNetworkFactory` and in `PopulationAgentSource`.  We will need the full network (but not the full `QNetwork`!) on every computational node for the routing later; in contrast, we would actually _not_ need the full population.  But such optimizations should be done later.

* The mobsim communication is in `QNetsimEngine`.  This is so far sending an int back and forth, but not more.
