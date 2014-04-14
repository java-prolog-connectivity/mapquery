% loader of all libraries

:- initialization((
	set_logtalk_flag(report, warnings),
	set_logtalk_flag(dynamic_declarations, allow),
	logtalk_load(library(types_loader)),
	logtalk_load([coordinates,taggeable,osm,node,way,map,city]),
	logtalk_load(lgtunit(loader)),
	logtalk_load(osm_tests, [hook(lgtunit)])
)).
