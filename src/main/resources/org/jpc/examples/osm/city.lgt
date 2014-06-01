:- object(city(_Name)).

	:- info([
		author is 'Sergio Castro',
		comment is 'A city',
		parameters is [
			'Name' - 'The name of the city']
	]).
	
	:- info(coordinates/1, [
    	comment is 'Coordinates is the coordinates of the city.',
    	argnames is ['Coordinates']
	]).
	:- public(coordinates/1).
	coordinates(coordinates(4.3524950, 50.8467493)) :- 
		parameter(1, 'brussels').

:- end_object.