package org.jpc.examples.osm.imp;

import static org.jpc.util.concurrent.ThreadLocalPrologEngine.getPrologEngine;

import org.jpc.util.LogicResourceLoader;

public class MapQuery {

	public static String LOADER_FILE = "org/jpc/examples/osm/load_all";
	
	public static boolean loadAll() {
		return new LogicResourceLoader(getPrologEngine()).logtalkLoad(LOADER_FILE);
	}
	
	public static void importData() {
		new OsmDataLoader(getPrologEngine()).loadDefault();
	}
	
}
