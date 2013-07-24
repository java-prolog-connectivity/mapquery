package org.jpc.examples.osm.model.jpcconverters;

import static java.util.Arrays.asList;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import org.jpc.Jpc;
import org.jpc.converter.JpcConversionException;
import org.jpc.converter.JpcConverter;
import org.jpc.examples.osm.model.Way;
import org.jpc.examples.osm.model.imp.WayJpc;
import org.jpc.term.Compound;
import org.jpc.term.IntegerTerm;

public class WayJpcConverter extends JpcConverter<Way, Compound> {

	public static final String WAY_FUNCTOR = "way"; //way prolog functor
	
	@Override
	public Way fromTerm(Compound term, Type type, Jpc context) {
		if(!term.hasFunctor(WAY_FUNCTOR, 3))
			throw new JpcConversionException();
		Long id = ((IntegerTerm)term.arg(1)).longValue();
		List nodesIds = (List) context.fromTerm(term.arg(2));
		Map tags = (Map) context.fromTerm(term.arg(3), Map.class);
		return new WayJpc(id, nodesIds, tags);
	}

	@Override
	public Compound toTerm(Way way, Jpc context) {
		return context.toTerm(WAY_FUNCTOR, asList(way.getId(), way.getNodesIds(), way.getTags()));
	}

}
