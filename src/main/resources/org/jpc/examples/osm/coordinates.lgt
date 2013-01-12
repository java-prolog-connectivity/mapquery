:- object(coordinates(_Lon,_Lat)).

	:- info([
		author is 'Sergio Castro',
		comment is 'A OSM node coordinates',
		parameters is [
			'Lon' - 'The longitud of the node',
			'Lat' - 'The latitude of the node']
			]).

		:- public([lon/1, lat/1, distancekm/2, distancem/2, near/2]).
		
		lon(Lon) :- parameter(1, Lon).
		
		lat(Lat) :- parameter(2, Lat).
		
		near(Coordinates, Km) :- distancekm(Coordinates, D), D=<Km.
		
		:- info(distancekm/2, [
			comment is 'Equivalence between degrees and radians.',
			argnames is ['Coordinates', 'Distance'],
			bibliography is ['http://www.movable-type.co.uk/scripts/latlong.html']]).
			
		distancekm(Coordinates, D) :- earthRadiusKm(R),
			lon(ThisLon), degreesAsRadians(ThisLon, ThisLonRad), Coordinates::lon(ThatLon), degreesAsRadians(ThatLon, ThatLonRad),
			lat(ThisLat), degreesAsRadians(ThisLat, ThisLatRad), Coordinates::lat(ThatLat), degreesAsRadians(ThatLat, ThatLatRad),
			X is (ThatLonRad-ThisLonRad)*cos((ThisLatRad+ThatLatRad)/2),
			Y is ThatLatRad-ThisLatRad,
			hypotenuse(X,Y,Hyp),
			D is Hyp*R.
		
		distancem(Coordinates, D) :- distancekm(Coordinates, Dkm), D is Dkm*1000.
		
		earthRadiusKm(6371). %earth's mean radius in km
		
		hypotenuse(Cat1, Cat2, H) :- H is sqrt((Cat1**2)+(Cat2**2)).
		
		:- info(degreesAsRadians/2, [
			comment is 'Equivalence between degrees and radians.',
			argnames is ['Degrees', 'Radians']]).
		degreesAsRadians(Degrees, Radians) :- \+var(Degrees), Pi is pi, Radians is Degrees*Pi/180.
		degreesAsRadians(Degrees, Radians) :- var(Degrees), \+var(Radians), Pi is pi, Degrees is Radians*180/Pi.
		
:- end_object.
