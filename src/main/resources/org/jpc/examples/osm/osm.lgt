:- object(osm).

	:- info([
		author is 'Sergio Castro',
		comment is 'A OSM logic theory'
			]).

	:- public([node/3, node/2, node/1, number_nodes/1, way/3, way/2, way/1, number_ways/1]).
	:- dynamic([way/3, node/3]).

	:- info(number_nodes/1, [
    	comment is 'NumberNodes is the number of nodes.',
    	argnames is ['NumberNodes']
	]).
	number_nodes(N) :- findall(Node,::node(Node),L), list::length(L,N).
	
	:- info(node/1, [
    	comment is 'Node is a node.',
    	argnames is ['Node']
	]).
	node(Node) :- node(_, Node).
	
	:- info(node/2, [
    	comment is 'Node is a node with identifier Id.',
    	argnames is ['Id','Node']
	]).
	node(Id, node(Id, Coordinates, Tags)) :- node(Id, Coordinates, Tags).
	
	:- info(number_ways/1, [
    	comment is 'NumberWays is the number of ways.',
    	argnames is ['NumberWays']
	]).
	number_ways(N) :- findall(Way,::way(Way),L), list::length(L,N).
	
	:- info(way/1, [
    	comment is 'Way is a way.',
    	argnames is ['Way']
	]).
	way(Way) :- way(_, Way).
	
	:- info(way/2, [
    	comment is 'Way is a way with identifier Id.',
    	argnames is ['Id','Way']
	]).
	way(Id, way(Id, Nodes, Tags)) :- way(Id, Nodes, Tags).
	
:- end_object.
