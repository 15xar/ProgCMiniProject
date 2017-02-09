// Planet sizes
float earthRadius = 6.378; // original størrelse = 6.378, noget km
float sunRadius = earthRadius * 13; // eneste der ikke er akkurat og bruger jorden til at definere de andre
float mercuryRadius = earthRadius * 0.383;
float venusRadius = earthRadius * 0.95;
float marsRadius = earthRadius * 0.532;
float jupiterRadius = earthRadius * 11;
float saturnRadius = earthRadius * 9;
float uranusRadius = earthRadius * 4;
float neptuneRadius = earthRadius * 3.86;

// Planet distance
// AU = Astronomical Unit = 149,597,870,700 m ~ 149,597,870.7 km  https://en.wikipedia.org/wiki/Astronomical_unit
// using their Semi-Major axis (the medium of their orbits)
// Dividing AU with scale to make it fit the window
float distanceScale = 660000;
float AU = 149597870.7 / distanceScale;
float mercuryDistance = AU * 0.387;
float venusDistance = AU * 0.723;
float earthDistance = AU * 1;
float marsDistance = AU * 1.52;
float jupiterDistance = AU * 5.2;
float saturnDistance = AU * 10;
float uranusDistance = AU * 20.1;
float neptuneDistance = AU * 30.33;

// Planets inclination to Sun's equator in degrees (hvor meget den hælder til siden) (BRUG DET HER TIL HÆLDNING!!!)
float mercuryInclination = 3.38;
float venusInclination = 3.86;
float earthInclination = 7.155;
float marsInclination = 5.65;
float jupiterInclination = 6.09;
float saturnInclination = 5.51;
float uranusInclination = 6.48;
float neptuneInclination = 6.43;

// Orbit speed ( earth's averag orbital speed = 29.78 km/s) (find more precise numbers!!)
float mercurySpeed = 0.0075;
float venusSpeed = 0.006;
float earthSpeed = 0.005;
float marsSpeed = 0.004;
float jupiterSpeed = 0.002;
float saturnSpeed =  0.00165;
float uranusSpeed = 0.00125;
float neptuneSpeed = 0.001;

// Rotation speed (find precise numbers!!)
float mercuryRotation = 0.005;
float venusRotation = 0.005;
float earthRotation = 0.05;
float marsRotation = 0.0075;
float jupiterRotation = 0.02;
float saturnRotation = 0.002;
float uranusRotation = 0.002;
float neptuneRotation = 0.002;