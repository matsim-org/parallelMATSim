package org.matsim.parallel;

import mpi.MPI;
import org.apache.log4j.Logger;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.network.Node;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.config.groups.QSimConfigGroup;
import org.matsim.core.controler.Controler;
import org.matsim.core.mobsim.qsim.QSim;
import org.matsim.core.scenario.ScenarioUtils;
import org.matsim.core.utils.io.IOUtils;
import org.matsim.examples.ExamplesUtils;

import java.net.URL;

import static org.matsim.core.controler.OutputDirectoryHierarchy.OverwriteFileSetting.deleteDirectoryIfExists;

class RunParallel {
	private static final Logger log = Logger.getLogger( RunParallel.class ) ;
	
	public static void main ( String [] args ) {
		
		System.out.println("here10") ;
		
		int thisCpn = 0 ;
		
		MPI.Init(args) ;

		thisCpn = MPI.COMM_WORLD.Rank() ;
		
		System.out.println("Hi from <"+ thisCpn + ">" ) ;

		final URL url = ExamplesUtils.getTestScenarioURL( "equil" );
		final Config config = ConfigUtils.loadConfig( IOUtils.newUrl( url, "config.xml" ) );

		config.controler().setOverwriteFileSetting( deleteDirectoryIfExists );
		config.controler().setLastIteration( 0 );
		config.controler().setOutputDirectory( "./output" + thisCpn );
		
		config.qsim().setStartTime( 7. * 3600. );
		config.qsim().setSimStarttimeInterpretation( QSimConfigGroup.StarttimeInterpretation.onlyUseStarttime );
		// yyyy need to assert this for parallel version
		
		config.qsim().setEndTime( 24.*3600. );
		config.qsim().setSimEndtimeInterpretation( QSimConfigGroup.EndtimeInterpretation.onlyUseEndtime );
		// yyyy need to assert this for parallel version

		config.qsim().setUsingThreadpool( true );
		// yyyy need to assert this for parallel version

		final Scenario scenario = ScenarioUtils.loadScenario( config );

		for ( Node node : scenario.getNetwork().getNodes().values() ) {
			if ( Integer.parseInt( node.getId().toString() ) <= 11 ) {
				node.getAttributes().putAttribute( QSim.CPN_ATTRIBUTE, 0 ) ;
				if ( thisCpn==0 ) {
					node.getAttributes().putAttribute( QSim.IS_LOCAL_ATTRIBUTE, true ) ;
				} else {
					node.getAttributes().putAttribute( QSim.IS_LOCAL_ATTRIBUTE, false ) ;
				}
			} else {
				node.getAttributes().putAttribute( QSim.CPN_ATTRIBUTE, 1 ) ;
				if ( thisCpn==1 ) {
					node.getAttributes().putAttribute( QSim.IS_LOCAL_ATTRIBUTE, true ) ;
				} else {
					node.getAttributes().putAttribute( QSim.IS_LOCAL_ATTRIBUTE, false ) ;
				}
			}
		}
		
		final Controler controler = new Controler( scenario );

		controler.run() ;
		
		MPI.Finalize();
		
		System.out.println("here99") ;
	
	}
	
}
