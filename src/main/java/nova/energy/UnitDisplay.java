/*
 * Copyright (c) 2015 NOVA, All rights reserved.
 * This library is free software, licensed under GNU Lesser General Public License version 3
 *
 * This file is part of NOVA.
 *
 * NOVA is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * NOVA is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with NOVA.  If not, see <http://www.gnu.org/licenses/>.
 */

package nova.energy;

/**
 * An easy way to display information on electricity for the client.
 *
 * @author Calclavia
 */
public class UnitDisplay {
	public final Unit unit;
	public final double value;
	public final boolean useSymbol;
	public final int decimalPlaces;
	public final boolean isSimple;

	public UnitDisplay(Unit unit, double value, boolean useSymbol, int decimalPlaces, boolean simple) {
		this.unit = unit;
		this.value = value;
		this.useSymbol = useSymbol;
		this.decimalPlaces = decimalPlaces;
		this.isSimple = simple;
	}

	public UnitDisplay(Unit unit, double value, boolean useSymbol, boolean simple) {
		this(unit, value, useSymbol, 2, simple);
	}

	public UnitDisplay(Unit unit, double value, int decimalPlaces, boolean simple) {
		this(unit, value, false, decimalPlaces, simple);
	}

	public UnitDisplay(Unit unit, double value, boolean useSymbol, int decimalPlaces) {
		this(unit, value, useSymbol, decimalPlaces, false);
	}

	public UnitDisplay(Unit unit, double value, int decimalPlaces) {
		this(unit, value, false, decimalPlaces, false);
	}

	public UnitDisplay(Unit unit, double value, boolean simple) {
		this(unit, value, false, 2, simple);
	}

	public UnitDisplay(Unit unit, double value) {
		this(unit, value, false, 2, false);
	}

	/**
	 * Rounds a number to a specific number place places
	 *
	 * @param d - the number
	 * @return The rounded number
	 */
	public static double roundDecimals(double d, int decimalPlaces) {
		long j = Math.round(d * Math.pow(10, decimalPlaces));
		return j / Math.pow(10, decimalPlaces);
	}

	public static double roundDecimals(double d) {
		return roundDecimals(d, 2);
	}

	public UnitDisplay multiply(double value) {
		return new UnitDisplay(unit, value * this.value);
	}

	public UnitDisplay simple() {
		return (isSimple ? this : new UnitDisplay(unit, value, true));
	}

	public UnitDisplay notSimple() {
		return (!isSimple ? this : new UnitDisplay(unit, value, false));
	}

	public UnitDisplay symbol() {
		return symbol(true);
	}

	public UnitDisplay symbol(boolean useSymbol) {
		return (this.useSymbol ^ useSymbol ? new UnitDisplay(unit, value, isSimple, decimalPlaces, useSymbol) : this);
	}

	public UnitDisplay decimal(int decimalPlaces) {
		return (this.decimalPlaces == decimalPlaces ? this : new UnitDisplay(unit, value, isSimple, decimalPlaces, useSymbol));
	}

	@Override
	public String toString() {
		String unitName = unit.name;
		String prefix = "";
		double value = this.value;

		if (isSimple) {
			if (value > 1) {
				if (decimalPlaces < 1) {
					return (int) value + " " + unit.getPlural();
				}

				return roundDecimals(value, decimalPlaces) + " " + unit.getPlural();
			}

			if (decimalPlaces < 1) {
				return (int) value + " " + unit.name;
			}

			return roundDecimals(value, decimalPlaces) + " " + unit.name;
		}

		if (value < 0) {
			value = Math.abs(value);
			prefix = "-";
		}

		if (useSymbol) {
			unitName = unit.symbol;
		} else if (value > 1) {
			unitName = unit.getPlural();
		}

		if (value == 0) {
			return value + " " + unitName;
		} else {
			for (int i = 0; i < Unit.Prefix.getPrefixes().size(); i++) {
				Unit.Prefix lowerMeasure = Unit.Prefix.getPrefixes().get(i);

				if (lowerMeasure.isBellow(value) && i == 0) {
					return prefix + roundDecimals(lowerMeasure.process(value), decimalPlaces) + " " + lowerMeasure.getName(useSymbol) + unitName;
				}
				if (i + 1 >= Unit.Prefix.getPrefixes().size()) {
					return prefix + roundDecimals(lowerMeasure.process(value), decimalPlaces) + " " + lowerMeasure.getName(useSymbol) + unitName;
				}

				Unit.Prefix upperMeasure = Unit.Prefix.getPrefixes().get(i + 1);

				if ((lowerMeasure.isAbove(value) && upperMeasure.isBellow(value)) || lowerMeasure.value == value) {
					return prefix + roundDecimals(lowerMeasure.process(value), decimalPlaces) + " " + lowerMeasure.getName(useSymbol) + unitName;
				}
			}
		}

		return prefix + roundDecimals(value, decimalPlaces) + " " + unitName;
	}
}
