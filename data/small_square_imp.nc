(Small square, clockwise)
(Using QC5000)
G20 (Inch)
G17 (XY)
G90 (absolute)
G61 (exact path)
G54
M3 S20000
G4 P2
G0 X0.5 Y0.5
(solenoid on)
M8
G4 P0.5
G1 X0.5 Y-0.5 F16
G1 X-0.5 Y-0.5
G1 X-0.5 Y0.5
G1 X0.5 Y0.5
(solenoid off)
M9
G4 P0.5
G0 X0 Y0
M30
