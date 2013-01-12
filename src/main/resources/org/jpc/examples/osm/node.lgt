:- object(node(_Id,_Coordinates,_Tags), imports(taggeable(_Tags))).

	:- info([
		author is 'Sergio Castro',
		comment is 'A OSM node',
		parameters is [
			'Id' - 'The id of the node',
			'Coordinates' - 'The coordinates of the node',
			'Tags' - 'The tags of the node']
			]).

	:- public([id/1, coordinates/1, distancekm/2, near/2]).
		
	id(Id) :- parameter(1, Id).
		
	coordinates(Coordinates) :- parameter(2, Coordinates).

	distancekm(ThatCoordinates, Km) :- coordinates(ThisCoordinates), ThisCoordinates::distancekm(ThatCoordinates,Km).

	near(ThatCoordinates, Km) :- coordinates(ThisCoordinates), ThisCoordinates::near(ThatCoordinates, Km).

:- end_object.
