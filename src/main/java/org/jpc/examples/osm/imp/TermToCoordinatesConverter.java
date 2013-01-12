package org.jpc.examples.osm.imp;

import org.jpc.converter.fromterm.TermToObjectConverter;
import org.jpc.examples.osm.Coordinates;
import org.jpc.term.FloatTerm;
import org.jpc.term.Term;

public class TermToCoordinatesConverter implements TermToObjectConverter<Coordinates> {

	@Override
	public Coordinates apply(Term term) {
		return new CoordinatesImp(((FloatTerm)term.arg(1)).doubleValue(), ((FloatTerm)term.arg(2)).doubleValue());
	}

}
