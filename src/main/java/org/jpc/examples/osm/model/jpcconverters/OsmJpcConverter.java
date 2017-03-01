package org.jpc.examples.osm.model.jpcconverters;

import org.jpc.Jpc;
import org.jpc.mapping.converter.ToTermConverter;
import org.jpc.examples.osm.model.Osm;
import org.jpc.term.Atom;

public class OsmJpcConverter implements ToTermConverter<Osm, Atom> {
	
	public static final String OSM_FUNCTOR_NAME = "osm";
	
	@Override
	public Atom toTerm(Osm osm, Class<Atom> termClass, Jpc jpc) {
		return new Atom(OSM_FUNCTOR_NAME);
	}
	
}
