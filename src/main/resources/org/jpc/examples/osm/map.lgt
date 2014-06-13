:- object(map).
	
	:- info([
		author is 'Sergio Castro',
		comment is 'A visual map',
		parameters is []
	]).
	
	:- info(load_osm/1, [
    	comment is 'Loads a OSM theory from the file File, which follows the OSM format.',
    	argnames is ['File']
	]).
	:- public(load_osm/1).
	load_osm(File) :-
		prolog_engines::this_engine(Engine), 
		jobject('org.jpc.examples.osm.OsmDataLoader'(Engine))::load(File).
		%java::eval((class([org,jpc,examples,osm], ['OsmDataLoader'])::new(Engine))::load(File)).
		
:- end_object.
