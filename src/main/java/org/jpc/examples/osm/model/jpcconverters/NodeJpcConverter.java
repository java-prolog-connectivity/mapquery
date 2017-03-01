package org.jpc.examples.osm.model.jpcconverters;

import static java.util.Arrays.asList;

import java.lang.reflect.Type;
import java.util.Map;

import org.jpc.Jpc;
import org.jpc.mapping.converter.FromTermConverter;
import org.jpc.mapping.converter.ToTermConverter;
import org.jpc.examples.osm.model.Coordinates;
import org.jpc.examples.osm.model.Node;
import org.jpc.examples.osm.model.imp.NodeJpc;
import org.jpc.term.Compound;

public class NodeJpcConverter implements ToTermConverter<Node, Compound>, FromTermConverter<Compound, Node> {

	public static final String NODE_FUNCTOR_NAME = "node";
	
	@Override
	public Node fromTerm(Compound term, Type type, Jpc jpc) {
		long id = jpc.fromTerm(term.arg(1));
		Coordinates coordinates = jpc.fromTerm(term.arg(2));
		Map<String, String> tags = jpc.fromTerm(term.arg(3), Map.class);
		return new NodeJpc(id, coordinates, tags);
	}

	@Override
	public Compound toTerm(Node node, Class<Compound> termClass, Jpc jpc) {
		return jpc.toCompound(NODE_FUNCTOR_NAME, asList(node.getId(), node.getCoordinate(), node.getTags()));
	}
	
}
