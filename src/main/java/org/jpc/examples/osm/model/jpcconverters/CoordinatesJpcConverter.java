package org.jpc.examples.osm.model.jpcconverters;

import static java.util.Arrays.asList;

import java.lang.reflect.Type;

import org.jconverter.converter.ConversionException;
import org.jpc.Jpc;
import org.jpc.converter.FromTermConverter;
import org.jpc.converter.ToTermConverter;
import org.jpc.examples.osm.model.Coordinates;
import org.jpc.examples.osm.model.imp.CoordinatesJpc;
import org.jpc.term.Compound;
import org.jpc.term.FloatTerm;

public class CoordinatesJpcConverter implements ToTermConverter<Coordinates, Compound>, FromTermConverter<Compound, Coordinates> {

	public static final String COORDINATE_FUNCTOR = "coordinates"; //coordinate prolog functor
	
	@Override
	public Coordinates fromTerm(Compound term, Type type, Jpc context) {
		if(!term.hasFunctor(COORDINATE_FUNCTOR, 2))
			throw new ConversionException();
		return new CoordinatesJpc(((FloatTerm)term.arg(1)).doubleValue(), ((FloatTerm)term.arg(2)).doubleValue());
	}

	@Override
	public Compound toTerm(Coordinates coordinates, Class<Compound> termClass, Jpc context) {
		return new Compound(COORDINATE_FUNCTOR, asList(new FloatTerm(coordinates.getLon()), new FloatTerm(coordinates.getLat())));
	}

}
