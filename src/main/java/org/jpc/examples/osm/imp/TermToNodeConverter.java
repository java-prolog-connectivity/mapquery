package org.jpc.examples.osm.imp;

import java.util.Map;

import org.jpc.converter.fromterm.TermToObjectConverter;
import org.jpc.converter.fromterm.fromlistterm.ListTermToMapConverter;
import org.jpc.examples.osm.Coordinates;
import org.jpc.examples.osm.Node;
import org.jpc.term.Atom;
import org.jpc.term.Term;

public class TermToNodeConverter implements TermToObjectConverter<Node> {

	@Override
	public Node apply(Term term) {
		String id = ((Atom)term.arg(1)).getName();
		Coordinates coordinates = new TermToCoordinatesConverter().apply(term.arg(2));
		Map tags = new ListTermToMapConverter().apply(term.arg(3));
		return new NodeImp(id, coordinates, tags);
	}

}
