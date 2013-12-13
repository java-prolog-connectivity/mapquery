package org.jpc.examples.osm.model.jpcconverters;

import org.jpc.Jpc;
import org.jpc.converter.ToTermConverter;
import org.jpc.examples.osm.model.Osm;
import org.jpc.term.Atom;

public class OsmJpcConverter implements ToTermConverter<Osm, Atom> {
	
	public static final String OSM_NAME = "osm"; //osm prolog functor
	
	@Override
	public Atom toTerm(Osm way, Class<Atom> termClass, Jpc context) {
		return new Atom(OSM_NAME);
	}
	
}
