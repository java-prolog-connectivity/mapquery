package org.jpc.examples.osm.imp;

import java.util.List;
import java.util.Map;

import org.jpc.converter.fromterm.TermToObjectConverter;
import org.jpc.converter.fromterm.fromlistterm.ListTermToListConverter;
import org.jpc.converter.fromterm.fromlistterm.ListTermToMapConverter;
import org.jpc.examples.osm.Way;
import org.jpc.term.Atom;
import org.jpc.term.Term;

public class TermToWayConverter implements TermToObjectConverter<Way> {

	@Override
	public Way apply(Term term) {
		String id = ((Atom)term.arg(1)).getName();
		List nodesIds = new ListTermToListConverter().apply(term.arg(2));
		Map tags = new ListTermToMapConverter().apply(term.arg(3));
		return new WayImp(id, nodesIds, tags);
	}

}
