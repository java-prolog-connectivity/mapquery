%the objects defined in this file are experimental and incomplete.

:- object(coordenade_json_converter).

	:- info([
		author is 'Sergio Castro',
		comment is 'A Converter from a Coordenade to its Json representation',
		parameters is []
			]).

	:- public([to_json/2]).
		
	to_json(Coordenade, Json) :- Coordenade::lon(Lon), Coordenade::lat(Lat), term_to_atom(Lon, LonAtom), term_to_atom(Lat, LatAtom), atom_concat('[',LonAtom,C1), atom_concat(C1,',',C2), atom_concat(C2,LatAtom,C3), atom_concat(C3,']',Json).

:- end_object.



:- object(tags_json_converter).

:- info([
	author is 'Sergio Castro',
	comment is 'A Converter from a list of tags to its Json representation',
	parameters is []
		]).

:- public([to_json/2]).

	to_json(Tags, Json).
	
:- end_object.



:- object(node_json_converter).

	:- info([
		author is 'Sergio Castro',
		comment is 'A Converter from a Node to its Json representation',
		parameters is []
			]).

	:- public([to_json/2]).
		
	to_json(Node, Json).

:- end_object.



:- object(way_json_converter).

	:- info([
		author is 'Sergio Castro',
		comment is 'A Converter from a Way to its Json representation',
		parameters is []
			]).

	:- public([to_json/2]).
		
	to_json(Way, Json).

:- end_object.



:- object(osm_json_converter).

	:- info([
		author is 'Sergio Castro',
		comment is 'A Converter from an Osm fragment to its Json representation',
		parameters is []
			]).

	:- public([to_json/2]).
		
	to_json(OsmFragment, Json).

:- end_object.
