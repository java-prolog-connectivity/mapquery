:- object(way(_Id,_NodesIds,_Tags), imports(taggeable(_Tags))).

	:- info([
		author is 'Sergio Castro',
		comment is 'A OSM way',
		parameters is [
			'Id' - 'The id of the way',
			'Nodes' - 'The nodes composing the way',
			'Tags' - 'The tags of the way']
			]).

	:- public([id/1, nodes_ids/1, nodes/1, node/1, distancekm/2, near/2]).
	
	id(Id) :- parameter(1, Id).
		
	nodes_ids(NodesIds) :- parameter(2, NodesIds).
		
	nodes(Nodes) :- nodes_ids(NodesIds), findall(Node, (list::member(NodeId, NodesIds), osm::node(NodeId, Node)), Nodes).
	
	node(Node) :- nodes(Nodes), list::member(Node, Nodes).
	
	tags(Tags) :- parameter(3, Tags).

	near(ThatCoordinate, Km) :- once((::nodes(Nodes), list::member(Node, Nodes), Node::near(ThatCoordinate, Km))).
	
	distancekm(ThatCoordinate, Distance) :- nodes(Nodes), min_distancekm(ThatCoordinate, Nodes, Distance).
	
	min_distancekm(ThatCoordinate, [Node|Rest], Distance) :- Node::distancekm(ThatCoordinate, Km), min_distancekm(ThatCoordinate, Rest, Km, Distance).
	
	min_distancekm(_, [], Distance, Distance).
	min_distancekm(ThatCoordinate, [Node|Rest], PreviousMinDistance, Distance) :- Node::distancekm(ThatCoordinate, NodeDistance), NewMinDistance is min(NodeDistance, PreviousMinDistance),
	min_distancekm(ThatCoordinate, Rest, NewMinDistance, Distance).

:- end_object.
