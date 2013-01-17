package org.jpc.examples.osm.model.jpcconverters;

import org.jpc.converter.fromterm.FromTermConverter;
import org.jpc.examples.osm.model.Coordinate;
import org.jpc.examples.osm.model.imp.CoordinateJpc;
import org.jpc.term.FloatTerm;
import org.jpc.term.Term;

public class TermToCoordinateConverter implements FromTermConverter<Coordinate> {

	@Override
	public Coordinate apply(Term term) {
		return new CoordinateJpc(((FloatTerm)term.arg(1)).doubleValue(), ((FloatTerm)term.arg(2)).doubleValue());
	}

}
