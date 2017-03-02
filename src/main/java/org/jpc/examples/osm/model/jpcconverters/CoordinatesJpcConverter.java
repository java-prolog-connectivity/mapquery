package org.jpc.examples.osm.model.jpcconverters;

import static java.util.Arrays.asList;

import org.jconverter.converter.TypeDomain;
import org.jpc.Jpc;
import org.jpc.examples.osm.model.Coordinates;
import org.jpc.examples.osm.model.imp.CoordinatesJpc;
import org.jpc.mapping.converter.FromTermConverter;
import org.jpc.mapping.converter.ToTermConverter;
import org.jpc.term.Compound;

public class CoordinatesJpcConverter implements ToTermConverter<Coordinates, Compound>, FromTermConverter<Compound, Coordinates> {

	public static final String COORDINATE_FUNCTOR_NAME = "coordinates";
	
	@Override
	public Coordinates fromTerm(Compound term, TypeDomain typeDomain, Jpc jpc) {
		double lon = jpc.fromTerm(term.arg(1));
		double lat = jpc.fromTerm(term.arg(2));
		return new CoordinatesJpc(lon, lat);
	}

	@Override
	public Compound toTerm(Coordinates coordinates, TypeDomain typeDomain, Jpc jpc) {
		return jpc.toCompound(COORDINATE_FUNCTOR_NAME, asList(coordinates.getLon(), coordinates.getLat()));
	}

}
