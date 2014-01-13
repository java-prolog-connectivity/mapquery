package org.jpc.examples.osm.model.jpcconverters;

import static java.util.Arrays.asList;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import org.jconverter.converter.ConversionException;
import org.jpc.Jpc;
import org.jpc.converter.FromTermConverter;
import org.jpc.converter.ToTermConverter;
import org.jpc.examples.osm.model.Way;
import org.jpc.examples.osm.model.imp.WayJpc;
import org.jpc.term.Compound;
import org.jpc.term.IntegerTerm;

public class WayJpcConverter implements ToTermConverter<Way, Compound>, FromTermConverter<Compound, Way> {

	public static final String WAY_FUNCTOR = "way"; //way prolog functor
	
	@Override
	public Way fromTerm(Compound term, Type type, Jpc context) {
		if(!term.hasFunctor(WAY_FUNCTOR, 3))
			throw new ConversionException();
		Long id = ((IntegerTerm)term.arg(1)).longValue();
		List nodesIds = (List) context.fromTerm(term.arg(2));
		Map tags = (Map) context.fromTerm(term.arg(3), Map.class);
		return new WayJpc(id, nodesIds, tags);
	}

	@Override
	public Compound toTerm(Way way, Class<Compound> termClass, Jpc context) {
		return context.toCompound(WAY_FUNCTOR, asList(way.getId(), way.getNodesIds(), way.getTags()));
	}

}
