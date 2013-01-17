:- object(node(_Id,_Coordinate,_Tags), imports(taggeable(_Tags))).

	:- info([
		author is 'Sergio Castro',
		comment is 'A OSM node',
		parameters is [
			'Id' - 'The id of the node',
			'Coordinate' - 'The coordinate of the node',
			'Tags' - 'The tags of the node']
			]).

	:- public([id/1, coordinate/1, distancekm/2, near/2]).
		
	id(Id) :- parameter(1, Id).
		
	coordinate(Coordinate) :- parameter(2, Coordinate).

	distancekm(ThatCoordinate, Km) :- coordinate(ThisCoordinate), ThisCoordinate::distancekm(ThatCoordinate,Km).

	near(ThatCoordinate, Km) :- coordinate(ThisCoordinate), ThisCoordinate::near(ThatCoordinate, Km).

:- end_object.
