package org.jpc.examples.osm.model.jpcconverters;

import static java.util.Arrays.asList;

import java.lang.reflect.Type;

import org.jconverter.converter.ConversionException;
import org.jpc.Jpc;
import org.jpc.converter.FromTermConverter;
import org.jpc.converter.ToTermConverter;
import org.jpc.examples.osm.model.Coordinate;
import org.jpc.examples.osm.model.imp.CoordinateJpc;
import org.jpc.term.Compound;
import org.jpc.term.FloatTerm;

public class CoordinateJpcConverter implements ToTermConverter<Coordinate, Compound>, FromTermConverter<Compound, Coordinate> {

	public static final String COORDINATE_FUNCTOR = "coordinate"; //coordinate prolog functor
	
	@Override
	public Coordinate fromTerm(Compound term, Type type, Jpc context) {
		if(!term.hasFunctor(COORDINATE_FUNCTOR, 2))
			throw new ConversionException();
		return new CoordinateJpc(((FloatTerm)term.arg(1)).doubleValue(), ((FloatTerm)term.arg(2)).doubleValue());
	}

	@Override
	public Compound toTerm(Coordinate coordinate, Class<Compound> termClass, Jpc context) {
		return new Compound(COORDINATE_FUNCTOR, asList(new FloatTerm(coordinate.getLon()), new FloatTerm(coordinate.getLat())));
	}

}
