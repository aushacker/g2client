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

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Stephen Davies
 * @since
 */
public class Excellon {

    /**
     * End of file.
     */
    public static final String M30 = "M30";

    /**
     * Start of header.
     */
    public static final String M48 = "M48";

    /**
     * METRIC
     */
    public static final String M71 = "M71";

    /**
     * INCH
     */
    public static final String M72 = "M72";

    /**
     * Tool definition in header e.g T01C0.0276
     */
    private static final Pattern TOOL_DEF = Pattern.compile("^T(\\d+)C(\\d+\\.\\d+)$");

    /**
     * Tool context (body subsection).
     */
    private static final Pattern TOOL_SELECTION = Pattern.compile("^T(\\d+)$");

    private static final Pattern POINT = Pattern.compile("^X(\\d{0,2}\\.?\\d{0,4})Y(\\d{0,2}\\.?\\d{0,4})$");

    private Units units;

    private Map<String, Tool> tools;

    /**
     * Current tool.
     */
    private Tool tool;

    public Excellon() {
        this.tools = new HashMap<>();
    }

    public boolean isPoint(String s) {
        return POINT.matcher(s).matches();
    }

    public boolean isToolSelection(String s) {
        return TOOL_SELECTION.matcher(s).matches();
    }

    public int getToolCount() {
        return tools.size();
    }

    public Units getUnits() {
        return units;
    }
    
    public void processHeader(String hdr) {
        if (M71.equals(hdr)) {
            units = Units.METRIC;
        } else if (M72.equals(hdr)) {
            units = Units.INCH;
        } else if (hdr.startsWith("T")) {
            Matcher m = TOOL_DEF.matcher(hdr);
            m.matches();
            tools.put(m.group(1), new Tool(m.group(1), m.group(2)));
        }
    }

    /**
     * Client should have previously called isToolSelection.
     * 
     * @param s - E.g. T01
     */
    public void selectTool(String s) {
        setTool(tools.get(s));
    }

    public void setTool(Tool tool) {
        this.tool = tool;
    }

    public void setUnits(Units units) {
        this.units = units;
    }

    public BigDecimal toDecimal(String s) {
        return units.toDecimal(s);
    }

    public class Tool {
        private String name;
        private BigDecimal diameter;
        
        public Tool(String name, String diam) {
            this.name = name;
            this.diameter = new BigDecimal(diam);
        }

        public BigDecimal getDiameter() {
            return diameter;
        }

        public String getName() {
            return name;
        }
    }

    public enum Units {
        INCH {
            protected BigDecimal toDecimal(String s) {
                if (s.contains(".")) {
                    return new BigDecimal(s).stripTrailingZeros();
                }
                if (s.startsWith("0")) {
                    switch (s.length()) {
                    case 1:
                        return new BigDecimal("0.0");
                    case 2:
                        return new BigDecimal(s + ".0");
                    default:
                        return new BigDecimal(s.substring(0, 2) + "." + s.substring(2));
                    }
                } else if (s.endsWith("0")) {
                    switch (s.length()) {
                    case 1:
                        return new BigDecimal("0.0");
                    case 2:
                        return new BigDecimal("00.00" + s).stripTrailingZeros();
                    case 3:
                        return new BigDecimal("00.0" + s).stripTrailingZeros();
                    case 4:
                        return new BigDecimal("00." + s).stripTrailingZeros();
                    case 5:
                        return new BigDecimal("0" + s.substring(0, 1) + "." + s.substring(1, 5)).stripTrailingZeros();
                    default:
                        return new BigDecimal(s.substring(0, 2) + "." + s.substring(2, 6)).stripTrailingZeros();
                    }
                } else {
                    
                }
                return null;
            }
        },
        METRIC {
            protected BigDecimal toDecimal(String s) {
                return null;
            }
        };
        
        protected abstract BigDecimal toDecimal(String s);
    }
}
