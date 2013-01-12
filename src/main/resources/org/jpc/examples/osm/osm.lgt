:- object(osm).

	:- info([
		author is 'Sergio Castro',
		comment is 'A OSM logic theory'
			]).

	:- public([node/3, node/2, node/1, way/3, way/2, way/1]).
	:- dynamic([way/3, node/3]).


	node(Node) :- node(_, Node).
	
	node(Id, node(Id, Coordinates, Tags)) :- node(Id, Coordinates, Tags).
	
	way(Way) :- way(_, Way).
	
	way(Id, way(Id, Nodes, Tags)) :- way(Id, Nodes, Tags).
	
:- end_object.
