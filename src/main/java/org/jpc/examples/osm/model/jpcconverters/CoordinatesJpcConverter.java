package org.jpc.examples.osm.model.jpcconverters;

import static java.util.Arrays.asList;

import java.lang.reflect.Type;

import org.jpc.Jpc;
import org.jpc.converter.FromTermConverter;
import org.jpc.converter.ToTermConverter;
import org.jpc.examples.osm.model.Coordinates;
import org.jpc.examples.osm.model.imp.CoordinatesJpc;
import org.jpc.term.Compound;

public class CoordinatesJpcConverter implements ToTermConverter<Coordinates, Compound>, FromTermConverter<Compound, Coordinates> {

	public static final String COORDINATE_FUNCTOR_NAME = "coordinates";
	
	@Override
	public Coordinates fromTerm(Compound term, Type type, Jpc jpc) {
		double lon = jpc.fromTerm(term.arg(1));
		double lat = jpc.fromTerm(term.arg(2));
		return new CoordinatesJpc(lon, lat);
	}

	@Override
	public Compound toTerm(Coordinates coordinates, Class<Compound> termClass, Jpc jpc) {
		return jpc.toCompound(COORDINATE_FUNCTOR_NAME, asList(coordinates.getLon(), coordinates.getLat()));
	}

}
