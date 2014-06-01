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
	
	:- info(id/1, [
    	comment is 'Id is the identifier of this node.',
    	argnames is ['Id']
	]).
	
	id(Id) :- parameter(1, Id).
		
		
	:- info(coordinates/1, [
    	comment is 'Coordinates is the pair of coordinates of this node.',
    	argnames is ['Coordinates']
	]).
		
	coordinates(Coordinates) :- parameter(2, Coordinates).


	:- info(near/2, [
		comment is 'This node is near to coordinates Coordinates according to the distance in kilometers Km.',
		argnames is ['Coordinates', 'Km']]).
			
	near(ThatCoordinates, Km) :- coordinates(ThisCoordinates), ThisCoordinates::near(ThatCoordinates, Km).
	
	
	:- info(distancekm/2, [
		comment is 'Km is the distance in kilometers to coordinates Coordinates.',
		argnames is ['Coordinates', 'Km']]).
		
	distancekm(ThatCoordinates, Km) :- coordinates(ThisCoordinates), ThisCoordinates::distancekm(ThatCoordinates,Km).

:- end_object.
