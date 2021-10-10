/*
 * Copyright 2019 Stephen Davies
 *
 * This file is part of g2client.
 *
 * g2client is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * g2client is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with g2client. If not, see <https://www.gnu.org/licenses/>.
 */

package com.github.aushacker.g2client.pcb;

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;
import java.text.ParseException;

/**
 * Parse an Excellon drill file.
 *
 * @author Stephen Davies
 * @since August 2019
 */
public class Parser {

    private static String COMMENT = ";";
    private static String H_DELIM = "%";

    private LineNumberReader in;

    private State state;

    private Excellon excellon;

    private String currentLine;

    public static Excellon parse(Reader in) throws IOException, ParseException {
        return new Parser(in).parse();
    }

    private Parser(Reader in) {
        this.in = new LineNumberReader(in);
        this.state = State.H_START;
        this.excellon = new Excellon();
    }

    public State getState() {
        return state;
    }

    /**
     * Set up the next non-blank, non-comment line in the source file.
     */
    private void nextLine() throws IOException {
        while (true) {
            String s = in.readLine();
            s = s.trim();
            if (s.length() > 0 && !s.startsWith(COMMENT)) {
                currentLine = s;
                break;
            }
        }
    }

    private Excellon parse() throws IOException, ParseException {
        processHeader();
        processBody();

        return excellon;
    }

    private void processBody() throws IOException, ParseException {
        nextLine();
        
        // Expecting either a tool selection or an M30
        while (true) {
            if (Excellon.M30.equals(currentLine)) {
                break;
            } else if (excellon.isToolSelection(currentLine)) {
                excellon.selectTool(currentLine);
                state = State.TOOL;
                processPoints();
            } else {
                throw new ParseException("Expecting M30 or tool selection", in.getLineNumber());
            }
        }
    }

    /**
     * Process the Excellon file header.
     * i.e. everything between the % - %
     */
    private void processHeader() throws IOException, ParseException {
        // Consume initial % (MUST)
        nextLine();
        if (H_DELIM.equalsIgnoreCase(currentLine)) {
            state = State.H_COMMAND;
        } else {
            throw new ParseException("Failed to find start of header", in.getLineNumber());
        }

        // Consume initial M48 (MUST)
        nextLine();
        if (Excellon.M48.equalsIgnoreCase(currentLine)) {
            state = State.H_FIELD;
        } else {
            throw new ParseException("Failed to find M48 (start of header)", in.getLineNumber());
        }

        // Units, tools etc
        nextLine();
        while ( ! (H_DELIM.equals(currentLine))) {
            excellon.processHeader(currentLine);
            nextLine();
        }

        // Current line is %, header is complete
        state = State.BODY;
    }

    private void processPoints() throws IOException, ParseException {
        nextLine();

        // Expecting a tool selection, M30 or point
        while (true) {
            if ( (Excellon.M30.equals(currentLine)) || (excellon.isToolSelection(currentLine)) ) {
                break;
            } else if (excellon.isPoint(currentLine)) {
            } else {	
                throw new ParseException("Expecting M30 or tool selection", in.getLineNumber());
            }
            
            nextLine();
        }
    }

    private enum State {
        H_START,	// Expecting %
        H_COMMAND,	// Expecting M48
        H_FIELD,	// Expecting header field or %
        BODY,		// Expecting tool or M30
        TOOL,		// Expecting next tool, point or M30
        COMPLETE;
    }
}
