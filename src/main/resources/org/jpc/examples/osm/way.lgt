:- object(way(_Id,_NodesIds,_Tags), imports(taggeable(_Tags))).

	:- info([
		author is 'Sergio Castro',
		comment is 'A OSM way',
		parameters is [
			'Id' - 'The id of the way',
			'Nodes' - 'The nodes composing the way',
			'Tags' - 'The tags of the way']
			]).

	:- public([id/1, nodes_ids/1, nodes/1, tags/1, tag/2, near/2, distancekm/2]).
	
	id(Id) :- parameter(1, Id).
		
	nodes_ids(NodesIds) :- parameter(2, NodesIds).
		
	nodes(Nodes) :- nodes_ids(NodesIds), findall(Node, (list::member(NodeId, NodesIds), osm::node(NodeId, Node)), Nodes).
		
	tags(Tags) :- parameter(3, Tags).

	near(ThatCoordinates, Km) :- once((::nodes(Nodes), list::member(Node, Nodes), Node::near(ThatCoordinates, Km))).
	
	distancekm(ThatCoordinates, Distance) :- nodes(Nodes), min_distancekm(ThatCoordinates, Nodes, Distance).
	
	min_distancekm(ThatCoordinates, [Node|Rest], Distance) :- Node::distancekm(ThatCoordinates, Km), min_distancekm(ThatCoordinates, Rest, Km, Distance).
	
	min_distancekm(_, [], Distance, Distance).
	min_distancekm(ThatCoordinates, [Node|Rest], PreviousMinDistance, Distance) :- Node::distancekm(ThatCoordinates, NodeDistance), NewMinDistance is min(NodeDistance, PreviousMinDistance),
	min_distancekm(ThatCoordinates, Rest, NewMinDistance, Distance).

:- end_object.
