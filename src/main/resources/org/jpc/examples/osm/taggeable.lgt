:- category(taggeable(_Tags)).

	:- info([
		author is 'Sergio Castro',
		comment is 'A Taggeable object',
		parameters is [
			'Tags' - 'The tags of this object']
			]).

	:- public([tags/1, tag/2, has_tags/1]).

	:- info(tags/1, [
    	comment is 'Tags is the tags in this taggeable.',
    	argnames is ['Tags']
	]).
	
	tags(Tags) :- parameter(1, Tags).


	:- info(tag/2, [
    	comment is 'Value is a tag value associated to the tag name Key.',
    	argnames is ['Key','Value']
	]).
	
	tag(Key, Value) :- tags(Tags), list::member(Key-Value,Tags).


	:- info(hastags/1, [
    	comment is 'SomeTags is a list of tags owned by this taggeable.',
    	argnames is ['SomeTags']
	]).
	
	has_tags(SomeTags) :- forall(list::member(Key-Value, SomeTags), ::tag(Key, Value)).

:- end_category.
