package org.jpc.examples.osm.model.jpcconverters;

import java.util.Map;

import org.jpc.converter.fromterm.FromTermConverter;
import org.jpc.converter.fromterm.fromlistterm.ListTermToMapConverter;
import org.jpc.examples.osm.model.Coordinate;
import org.jpc.examples.osm.model.Node;
import org.jpc.examples.osm.model.imp.NodeJpc;
import org.jpc.term.IntegerTerm;
import org.jpc.term.Term;

public class TermToNodeConverter implements FromTermConverter<Node> {

	@Override
	public Node apply(Term term) {
		Long id = ((IntegerTerm)term.arg(1)).longValue();
		Coordinate coordinate = new TermToCoordinateConverter().apply(term.arg(2));
		Map tags = new ListTermToMapConverter().apply(term.arg(3));
		return new NodeJpc(id, coordinate, tags);
	}

}
