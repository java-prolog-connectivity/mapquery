% loader of all libraries

:- initialization((
	set_logtalk_flag(report, warnings),
	set_logtalk_flag(dynamic_declarations, allow),
	logtalk_load(library(types_loader), [reload(skip)]),
	logtalk_load(coordinates),
	logtalk_load(taggeable),
	logtalk_load(osm),
	logtalk_load(node),
	logtalk_load(way),
	logtalk_load(lgtunit(loader), [reload(skip)]),
	logtalk_load(osm_tests, [hook(lgtunit)])
)).
