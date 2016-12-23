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

import nova.core.loader.Mod;
import nova.core.util.id.Identifiable;
import nova.core.util.id.Identifier;
import nova.core.util.id.StringIdentifier;
import nova.internal.core.launch.ModLoader;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * An easy way to display information on electricity for the client.
 * @author Calclavia
 */
public class UnitDisplay {
	public Unit unit;
	public double value;
	public boolean useSymbol = false;
	public int decimalPlaces = 2;
	public boolean isSimple = false;

	public UnitDisplay(Unit unit, double value, boolean simple) {
		this.unit = unit;
		this.value = value;
	}

	public UnitDisplay(Unit unit, double value) {
		this(unit, value, false);
	}

	@Deprecated
	public UnitDisplay(double value, Unit unit) {
		this(unit, value);
	}

	/**
	 * Rounds a number to a specific number place places
	 * @param d - the number
	 * @return The rounded number
	 */
	public static double roundDecimals(double d, int decimalPlaces) {
		int j = (int) (d * Math.pow(10, decimalPlaces));
		return j / Math.pow(10, decimalPlaces);
	}

	public static double roundDecimals(double d) {
		return roundDecimals(d, 2);
	}

	public UnitDisplay multiply(double value) {
		this.value *= value;
		return this;
	}

	public UnitDisplay simple() {
		isSimple = true;
		return this;
	}

	public UnitDisplay symbol() {
		return symbol(true);
	}

	public UnitDisplay symbol(boolean useSymbol) {
		this.useSymbol = useSymbol;
		return this;
	}

	public UnitDisplay decimal(int decimalPlaces) {
		this.decimalPlaces = decimalPlaces;
		return this;
	}

	@Override
	public String toString() {
		String unitName = unit.name;
		String prefix = "";

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
			for (int i = 0; i < UnitPrefix.unitPrefixes.size(); i++) {
				UnitPrefix lowerMeasure = UnitPrefix.unitPrefixes.get(i);

				if (lowerMeasure.isBellow(value) && i == 0) {
					return prefix + roundDecimals(lowerMeasure.process(value), decimalPlaces) + " " + lowerMeasure.getName(useSymbol) + unitName;
				}
				if (i + 1 >= UnitPrefix.unitPrefixes.size()) {
					return prefix + roundDecimals(lowerMeasure.process(value), decimalPlaces) + " " + lowerMeasure.getName(useSymbol) + unitName;
				}

				UnitPrefix upperMeasure = UnitPrefix.unitPrefixes.get(i + 1);

				if ((lowerMeasure.isAbove(value) && upperMeasure.isBellow(value)) || lowerMeasure.value == value) {
					return prefix + roundDecimals(lowerMeasure.process(value), decimalPlaces) + " " + lowerMeasure.getName(useSymbol) + unitName;
				}
			}
		}

		return prefix + roundDecimals(value, decimalPlaces) + " " + unitName;
	}

	/**
	 * Universal Electricity's units are in KILOJOULES, KILOWATTS and KILOVOLTS. Try to make your
	 * energy ratio as close to real life as possible.
	 */
	public static class Unit implements Identifiable {
		private static final Map<Identifier, Unit> UNIT_MAP = new HashMap<>();

		public static final Unit AMPERE = new Unit("nova:ampere", "Amp", "I");
		public static final Unit AMP_HOUR = new Unit("nova:amp_hour", "Amp Hour", "Ah");
		public static final Unit VOLTAGE = new Unit("nova:voltage", "Volt", "V");
		public static final Unit WATT = new Unit("nova:watt", "Watt", "W");
		public static final Unit WATT_HOUR = new Unit("nova:watt_hour", "Watt Hour", "Wh");
		public static final Unit RESISTANCE = new Unit("nova:resistance", "Ohm", "R");
		public static final Unit CONDUCTANCE = new Unit("nova:conductance", "Siemen", "S");
		public static final Unit JOULE = new Unit("nova:joule", "Joule", "J");
		public static final Unit LITER = new Unit("nova:liter", "Liter", "L");
		public static final Unit NEWTON_METER = new Unit("nova:newton_meter", "Newton Meter", "Nm");

		public static final Unit REDFLUX = new Unit("forge:redstone_flux", "Redstone-Flux", "RF").setPlural("Redstone-Flux");
		public static final Unit MINECRAFT_JOULES = new Unit("buildcraft:minecraft_joule", "Minecraft-Joule", "McJ"); // MJ is confusing
		public static final Unit ELECTRICAL_UNITS = new Unit("ic2:electrical_unit", "Electrical-Unit", "EU");

		private final String id;
		public final String name;
		public final String symbol;
		private String plural = null;

		private Unit(String id, String name, String symbol) {
			this.id = Identifiable.addPrefix(id, false);
			this.name = name;
			this.symbol = symbol;

			UNIT_MAP.put(getID(), this);
		}

		private Unit setPlural(String plural) {
			this.plural = plural;
			return this;
		}

		public String getPlural() {
			return this.plural == null ? this.name + "s" : this.plural;
		}

		@Override
		public Identifier getID() {
			return new StringIdentifier(id);
		}

		public static Set<Unit> getUnitsForMod(String modId) {
			return UNIT_MAP.values().stream().filter((e) -> {
				String id = e.getID().asString();
				if (id.contains(":")) {
					return id.substring(0, id.lastIndexOf(':')).startsWith(modId);
				} else {
					return modId == null || modId.isEmpty();
				}
			}).collect(Collectors.toSet());
		}

		public static Optional<Unit> getUnit(String id) {
			return Optional.ofNullable(UNIT_MAP.get(new StringIdentifier(id)));
		}

		public static Unit getOrCreateUnit(String id, String name, String unit) {
			StringIdentifier idRaw = new StringIdentifier(id);
			StringIdentifier idNamespaced = new StringIdentifier(Identifiable.addPrefix(id, false));
			if (UNIT_MAP.containsKey(idNamespaced)) return UNIT_MAP.get(idNamespaced);
			if (UNIT_MAP.containsKey(idRaw)) return UNIT_MAP.get(idRaw);

			Unit unitObj = new Unit(idNamespaced.asString(), name, unit);
			return unitObj;
		}

		public static Unit getOrCreateUnit(String id, String name, String unit, String plural) {
			StringIdentifier idRaw = new StringIdentifier(id);
			StringIdentifier idNamespaced = new StringIdentifier(Identifiable.addPrefix(id, false));
			if (UNIT_MAP.containsKey(idNamespaced)) return UNIT_MAP.get(idNamespaced);
			if (UNIT_MAP.containsKey(idRaw)) return UNIT_MAP.get(idRaw);

			Unit unitObj = new Unit(idNamespaced.asString(), name, unit);
			return unitObj.setPlural(plural);
		}
	}

	/**
	 * Metric system of measurement.
	 */
	public static class UnitPrefix {
		public static final List<UnitPrefix> unitPrefixes = new LinkedList();

		public static final UnitPrefix MICRO = new UnitPrefix("Micro", "u", 0.000001);
		public static final UnitPrefix MILLI = new UnitPrefix("Milli", "m", 0.001);
		public static final UnitPrefix BASE = new UnitPrefix("", "", 1);
		public static final UnitPrefix KILO = new UnitPrefix("Kilo", "k", 1000);
		public static final UnitPrefix MEGA = new UnitPrefix("Mega", "M", 1000000);
		public static final UnitPrefix GIGA = new UnitPrefix("Giga", "G", 1000000000);
		public static final UnitPrefix TERA = new UnitPrefix("Tera", "T", 1000000000000d);
		public static final UnitPrefix PETA = new UnitPrefix("Peta", "P", 1000000000000000d);
		public static final UnitPrefix EXA = new UnitPrefix("Exa", "E", 1000000000000000000d);
		public static final UnitPrefix ZETTA = new UnitPrefix("Zetta", "Z", 1000000000000000000000d);
		public static final UnitPrefix YOTTA = new UnitPrefix("Yotta", "Y", 1000000000000000000000000d);
		/**
		 * long name for the unit
		 */
		public final String name;
		/**
		 * short unit version of the unit
		 */
		public final String symbol;
		/**
		 * Point by which a number is consider to be of this unit
		 */
		public final double value;

		private UnitPrefix(String name, String symbol, double value) {
			this.name = name;
			this.symbol = symbol;
			this.value = value;
			unitPrefixes.add(this);
		}

		public String getName(boolean getShort) {
			if (getShort) {
				return symbol;
			} else {
				return name;
			}
		}

		/**
		 * Divides the value by the unit value start
		 */
		public double process(double value) {
			return value / this.value;
		}

		/**
		 * Checks if a value is above the unit value start
		 */
		public boolean isAbove(double value) {
			return value > this.value;
		}

		/**
		 * Checks if a value is lower than the unit value start
		 */
		public boolean isBellow(double value) {
			return value < this.value;
		}
	}
}
