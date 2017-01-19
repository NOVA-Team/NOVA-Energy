/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nova.energy;

import nova.core.util.Identifiable;
import nova.core.util.registry.Registry;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author ExE Boss
 */
public final class Unit implements Identifiable {
	private static final Registry<Unit> REGISTRY = new Registry<>();

	public static final Unit METRE = getOrCreateUnit("nova:metre", "Meter", "m");
	public static final Unit GRAM = getOrCreateUnit("nova:gram", "Gram", "g", Prefix.KILO);

	public static final Unit AMPERE = getOrCreateUnit("nova:ampere", "Amp", "I");
	public static final Unit AMP_HOUR = getOrCreateUnit("nova:amp_hour", "Amp Hour", "Ah");
	public static final Unit VOLTAGE = getOrCreateUnit("nova:voltage", "Volt", "V");
	public static final Unit WATT = getOrCreateUnit("nova:watt", "Watt", "W");
	public static final Unit WATT_HOUR = getOrCreateUnit("nova:watt_hour", "Watt Hour", "Wh");
	public static final Unit RESISTANCE = getOrCreateUnit("nova:resistance", "Ohm", "R");
	public static final Unit CONDUCTANCE = getOrCreateUnit("nova:conductance", "Siemen", "S");
	public static final Unit JOULE = getOrCreateUnit("nova:joule", "Joule", "J");
	public static final Unit LITER = getOrCreateUnit("nova:liter", "Liter", "L");
	public static final Unit NEWTON_METER = getOrCreateUnit("nova:newton_meter", "Newton Meter", "Nm");

	/**
	 * Redstone Flux, the default energy unit in Minecraft Forge since 1.10-ish.
	 */
	public static final Unit REDFLUX = getOrCreateUnit("forge:redstone_flux", "Redstone-Flux", "RF", "Redstone-Flux");
	public static final Unit MINECRAFT_JOULES = getOrCreateUnit("buildcraft:minecraft_joule", "Minecraft-Joule", "McJ"); // MJ is confusable with Megajoules
	public static final Unit ELECTRICAL_UNITS = getOrCreateUnit("ic2:electrical_unit", "Electrical-Unit", "EU");

	private final String id;
	public final String name;
	public final String symbol;
	private String plural = null;
	private Prefix basePrefix = null;

	private Unit(String id, String name, String symbol) {
		this.id = id;
		this.name = name;
		this.symbol = symbol;
	}

	private Unit setPlural(String plural) {
		if (this.plural == null)
			this.plural = plural;
		return this;
	}

	private Unit setBasePrefix(Prefix basePrefix) {
		if (this.basePrefix == null)
			this.basePrefix = basePrefix;
		return this;
	}

	public String getPlural() {
		return this.plural == null ? this.name + "s" : this.plural;
	}

	@Override
	public String getID() {
		return id;
	}

	public Set<Unit> getUnitsFromMod(String modId) {
		return REGISTRY.stream().filter((e) -> {
			String id = e.getID();
			if (id.contains(":")) {
				return id.substring(0, id.lastIndexOf(':')).startsWith(modId);
			} else {
				return modId == null || modId.isEmpty();
			}
		}).collect(Collectors.toSet());
	}

	public Optional<Unit> getUnit(String id) {
		return REGISTRY.get(id);
	}

	public static Unit getOrCreateUnit(String id, String name, String unit) {
		if (REGISTRY.contains(id)) return REGISTRY.get(id).get();
		return REGISTRY.register(new Unit(id, name, unit));
	}

	public static Unit getOrCreateUnit(String id, String name, String unit, String plural) {
		if (REGISTRY.contains(id)) return REGISTRY.get(id).get();
		return REGISTRY.register(new Unit(id, name, unit)).setPlural(plural);
	}

	public static Unit getOrCreateUnit(String id, String name, String unit, Prefix basePrefix) {
		if (REGISTRY.contains(id)) return REGISTRY.get(id).get();
		return REGISTRY.register(new Unit(id, name, unit)).setBasePrefix(basePrefix);
	}

	public static Unit getOrCreateUnit(String id, String name, String unit, String plural, Prefix basePrefix) {
		if (REGISTRY.contains(id)) return REGISTRY.get(id).get();
		return REGISTRY.register(new Unit(id, name, unit)).setPlural(plural).setBasePrefix(basePrefix);
	}

	/**
	 * Metric system of measurement.
	 */
	public static class Prefix {
		private static final List<Prefix> UNIT_PREFIXES = new LinkedList<>();

//		public static final Prefix YOCTO = new Prefix("Yocto", "y", 0.000000000000000000000001);
//		public static final Prefix ZEPTO = new Prefix("Zepto", "z", 0.000000000000000000001);
//		public static final Prefix ATTO  = new Prefix("Atto",  "a", 0.000000000000000001);
//		public static final Prefix FEMTO = new Prefix("Femto", "p", 0.000000000000001);
//		public static final Prefix PICO  = new Prefix("Pico",  "p", 0.000000000001);
//		public static final Prefix NANO  = new Prefix("Nano",  "n", 0.000000001);
		public static final Prefix MICRO = new Prefix("Micro", "μ", 0.000001);
		public static final Prefix MILLI = new Prefix("Milli", "m", 0.001);
		public static final Prefix BASE  = new Prefix("",      "",  1);
		public static final Prefix KILO  = new Prefix("Kilo",  "k", 1000);
		public static final Prefix MEGA  = new Prefix("Mega",  "M", 1000000);
		public static final Prefix GIGA  = new Prefix("Giga",  "G", 1000000000);
		public static final Prefix TERA  = new Prefix("Tera",  "T", 1000000000000d);
		public static final Prefix PETA  = new Prefix("Peta",  "P", 1000000000000000d);
		public static final Prefix EXA   = new Prefix("Exa",   "E", 1000000000000000000d);
		public static final Prefix ZETTA = new Prefix("Zetta", "Z", 1000000000000000000000d);
		public static final Prefix YOTTA = new Prefix("Yotta", "Y", 1000000000000000000000000d);
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

		private Prefix(String name, String symbol, double value) {
			this.name = name;
			this.symbol = symbol;
			this.value = value;
			UNIT_PREFIXES.add(this);
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

		public static List<Prefix> getPrefixes() {
			return Collections.unmodifiableList(UNIT_PREFIXES);
		}
	}
}
