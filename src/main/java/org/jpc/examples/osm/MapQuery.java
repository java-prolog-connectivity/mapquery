package org.jpc.examples.osm;

import static org.jpc.engine.provider.PrologEngineProviderManager.getPrologEngine;

import org.jpc.util.PrologResourceLoader;

public class MapQuery {

	public static String LOADER_FILE = "org/jpc/examples/osm/load_all";
	
	public static boolean loadAll() {
		return new PrologResourceLoader(getPrologEngine()).logtalkLoad(LOADER_FILE);
	}
	
	public static void importData() {
		new OsmDataLoader(getPrologEngine()).loadDefault();
	}
	
}
