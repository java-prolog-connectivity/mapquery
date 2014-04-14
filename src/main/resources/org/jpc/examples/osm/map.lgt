:- object(map).
	
	:- public(load_osm/1).
	load_osm(File) :-
		prolog_engines::this_engine(Engine), 
		java::eval((class([org,jpc,examples,osm], ['OsmDataLoader'])::new(Engine))::load(File)).
		
:- end_object.
