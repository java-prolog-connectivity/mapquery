package org.jpc.examples.osm.model.jpcconverters;

import java.util.List;
import java.util.Map;

import org.jpc.converter.fromterm.FromTermConverter;
import org.jpc.converter.fromterm.fromlistterm.ListTermToListConverter;
import org.jpc.converter.fromterm.fromlistterm.ListTermToMapConverter;
import org.jpc.examples.osm.model.Way;
import org.jpc.examples.osm.model.imp.WayJpc;
import org.jpc.term.IntegerTerm;
import org.jpc.term.Term;

public class TermToWayConverter implements FromTermConverter<Way> {

	@Override
	public Way apply(Term term) {
		Long id = ((IntegerTerm)term.arg(1)).longValue();
		List nodesIds = new ListTermToListConverter().apply(term.arg(2));
		Map tags = new ListTermToMapConverter().apply(term.arg(3));
		return new WayJpc(id, nodesIds, tags);
	}

}
