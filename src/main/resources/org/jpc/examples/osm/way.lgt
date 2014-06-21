:- object(way(_Id,_NodesIds,_Tags), imports(taggable(_Tags))).

	:- info([
		author is 'Sergio Castro',
		comment is 'A OSM way',
		parameters is [
			'Id' - 'The id of the way',
			'Nodes' - 'The nodes composing the way',
			'Tags' - 'The tags of the way']
			]).

	:- public([id/1, nodes_ids/1, nodes/1, node/1, distancekm/2, near/2]).
	
	
	:- info(id/1, [
    	comment is 'Id is the identifier of this way.',
    	argnames is ['Id']
	]).
	
	id(Id) :- parameter(1, Id).


	:- info(nodes_ids/1, [
    	comment is 'NodesIds is a list with the identifiers of the nodes composing this way.',
    	argnames is ['NodesIds']
	]).
	
	nodes_ids(NodesIds) :- parameter(2, NodesIds).


	:- info(nodes/1, [
    	comment is 'Nodes is a list with the nodes composing this way.',
    	argnames is ['Nodes']
	]).
	
	nodes(Nodes) :- nodes_ids(NodesIds), findall(Node, (list::member(NodeId, NodesIds), osm::node(NodeId, Node)), Nodes).
	
	
	:- info(node/1, [
    	comment is 'Node is a node composing this way.',
    	argnames is ['Node']
	]).
	
	node(Node) :- nodes(Nodes), list::member(Node, Nodes).
	

	:- info(near/2, [
		comment is 'This way is near to coordinates Coordinates according to the distance in kilometers Km.',
		argnames is ['Coordinates', 'Km']]).

	near(ThatCoordinates, Km) :- once((::nodes(Nodes), list::member(Node, Nodes), Node::near(ThatCoordinates, Km))).
	
	
	:- info(distancekm/2, [
		comment is 'Km is the distance in kilometers to coordinates Coordinates.',
		argnames is ['Coordinates', 'Km']]).
		
	distancekm(ThatCoordinates, Distance) :- nodes(Nodes), min_distancekm(ThatCoordinates, Nodes, Distance).
	
	min_distancekm(ThatCoordinates, [Node|Rest], Distance) :- Node::distancekm(ThatCoordinates, Km), min_distancekm(ThatCoordinates, Rest, Km, Distance).
	
	min_distancekm(_, [], Distance, Distance).
	min_distancekm(ThatCoordinates, [Node|Rest], PreviousMinDistance, Distance) :- Node::distancekm(ThatCoordinates, NodeDistance), NewMinDistance is min(NodeDistance, PreviousMinDistance),
	min_distancekm(ThatCoordinates, Rest, NewMinDistance, Distance).

:- end_object.
