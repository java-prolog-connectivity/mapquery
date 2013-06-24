package org.jpc.examples.osm.model.jpcconverters;

import org.jpc.Jpc;
import org.jpc.converter.JpcConverter;
import org.jpc.examples.osm.model.Osm;
import org.jpc.term.Atom;

public class OsmJpcConverter extends JpcConverter<Osm, Atom> {
	
	public static final String OSM_NAME = "osm"; //osm prolog functor
	
	@Override
	public Atom toTerm(Osm way, Jpc context) {
		return new Atom(OSM_NAME);
	}
	
}
