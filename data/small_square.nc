(Small square, clockwise)
(Using QC5000)
G21 (mm)
G17 (XY)
G90 (absolute)
G61 (exact path)
G54
M3 S20000
G4 P2
G0 X5 Y5
(solenoid on)
M8
G4 P0.5
G1 X5 Y-5 F400
G1 X-5 Y-5
G1 X-5 Y5
G1 X5 Y5
(solenoid off)
M9
G4 P0.5
G0 X0 Y0
M30
