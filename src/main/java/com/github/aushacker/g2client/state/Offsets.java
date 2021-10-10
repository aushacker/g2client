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

package com.github.aushacker.g2client.state;

import java.math.BigDecimal;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * Work offsets.
 *
 * @author Stephen Davies
 * @since September 2019
 */
public class Offsets {

    private CoordinateSystem coordSystem;

    private SimpleObjectProperty<BigDecimal> a;
    private SimpleObjectProperty<BigDecimal> b;
    private SimpleObjectProperty<BigDecimal> c;
    private SimpleObjectProperty<BigDecimal> x;
    private SimpleObjectProperty<BigDecimal> y;
    private SimpleObjectProperty<BigDecimal> z;

    public Offsets(CoordinateSystem coordSystem) {
        this.coordSystem = coordSystem;
        a = createProperty();
        b = createProperty();
        c = createProperty();
        x = createProperty();
        y = createProperty();
        z = createProperty();
    }

    private SimpleObjectProperty<BigDecimal> createProperty() {
        BigDecimal zero = new BigDecimal(0);
        return new SimpleObjectProperty<>(zero);
    }

    public CoordinateSystem getCoordSystem() {
        return coordSystem;
    }

    public ObjectProperty<BigDecimal> aProperty() {
        return a;
    }

    public ObjectProperty<BigDecimal> bProperty() {
        return b;
    }

    public ObjectProperty<BigDecimal> cProperty() {
        return c;
    }

    public BigDecimal getA() {
        return a.get();
    }

    public BigDecimal getB() {
        return b.get();
    }

    public BigDecimal getC() {
        return c.get();
    }

    public BigDecimal getX() {
        return x.get();
    }

    public BigDecimal getY() {
        return y.get();
    }

    public BigDecimal getZ() {
        return z.get();
    }

    public void setA(BigDecimal aValue) {
        a.set(aValue);
    }

    public void setB(BigDecimal bValue) {
        b.set(bValue);
    }

    public void setC(BigDecimal cValue) {
        c.set(cValue);
    }

    public void setX(BigDecimal xValue) {
        x.set(xValue);
    }

    public void setY(BigDecimal yValue) {
        y.set(yValue);
    }

    public void setZ(BigDecimal zValue) {
        z.set(zValue);
    }

    public ObjectProperty<BigDecimal> xProperty() {
        return x;
    }

    public ObjectProperty<BigDecimal> yProperty() {
        return y;
    }

    public ObjectProperty<BigDecimal> zProperty() {
        return z;
    }
}
