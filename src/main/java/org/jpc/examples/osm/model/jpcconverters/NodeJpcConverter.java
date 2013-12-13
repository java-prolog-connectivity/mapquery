package org.jpc.examples.osm.model.jpcconverters;

import static java.util.Arrays.asList;

import java.lang.reflect.Type;
import java.util.Map;

import org.jconverter.converter.ConversionException;
import org.jpc.Jpc;
import org.jpc.converter.FromTermConverter;
import org.jpc.converter.ToTermConverter;
import org.jpc.examples.osm.model.Coordinate;
import org.jpc.examples.osm.model.Node;
import org.jpc.examples.osm.model.imp.NodeJpc;
import org.jpc.term.Compound;
import org.jpc.term.IntegerTerm;

public class NodeJpcConverter implements ToTermConverter<Node, Compound>, FromTermConverter<Compound, Node> {

	public static final String NODE_FUNCTOR = "node"; //node prolog functor
	
	@Override
	public Node fromTerm(Compound term, Type type, Jpc context) {
		if(!term.hasFunctor(NODE_FUNCTOR, 3))
			throw new ConversionException();
		Long id = ((IntegerTerm)term.arg(1)).longValue();
		Coordinate coordinate = (Coordinate)context.fromTerm(term.arg(2));
		Map tags = (Map)context.fromTerm(term.arg(3), Map.class);
		return new NodeJpc(id, coordinate, tags);
	}

	@Override
	public Compound toTerm(Node node, Class<Compound> termClass, Jpc context) {
		return context.toTerm(NODE_FUNCTOR, asList(node.getId(), node.getCoordinate(), node.getTags()));
	}
	
}
