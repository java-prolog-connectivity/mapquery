:- object(osm).

	:- info([
		author is 'Sergio Castro',
		comment is 'A OSM logic theory'
			]).

	:- public([node/3, node/2, node/1, number_nodes/1, way/3, way/2, way/1, number_ways/1]).
	:- dynamic([way/3, node/3]).

	number_nodes(N) :- findall(Node,::node(Node),L), list::length(L,N).
	
	node(Node) :- node(_, Node).
	
	node(Id, node(Id, Coordinate, Tags)) :- node(Id, Coordinate, Tags).
	
	number_ways(N) :- findall(Way,::way(Way),L), list::length(L,N).
	
	way(Way) :- way(_, Way).
	
	way(Id, way(Id, Nodes, Tags)) :- way(Id, Nodes, Tags).
	
:- end_object.
