% loader of all libraries

:- initialization((
	set_logtalk_flag(report, warnings),
	set_logtalk_flag(dynamic_declarations, allow),
	logtalk_load(library(types_loader)),
	logtalk_load(coordinate),
	logtalk_load(taggeable),
	logtalk_load(osm),
	logtalk_load(node),
	logtalk_load(way),
	logtalk_load(lgtunit(loader)),
	logtalk_load(osm_tests, [hook(lgtunit)])
)).
