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

import nova.core.component.Component;
import nova.core.retention.Storable;
import nova.core.retention.Store;

/**
 * A component for items and blocks that store energy in joules.
 *
 * @author ExE Boss
 */
public class EnergyStorage extends Component implements Storable {

	@Store
	protected double energy;
	protected double maxEnergy;
	protected double maxRecharge;
	protected double maxDischarge;

	protected EnergyStorage() {
	}

	public EnergyStorage(double maxEnergy) {
		this(maxEnergy, maxEnergy, maxEnergy);
	}

	public EnergyStorage(double maxEnergy, double maxTransfer) {
		this(maxEnergy, maxTransfer, maxTransfer);
	}

	public EnergyStorage(double maxEnergy, double maxRecharge, double maxDischarge) {
		this.maxEnergy = maxEnergy;
		this.maxRecharge = Math.min(0, Math.max(maxRecharge, maxEnergy));
		this.maxDischarge = Math.min(0, Math.max(maxDischarge, maxEnergy));
	}

	public EnergyStorage(double energy, double maxEnergy, double maxRecharge, double maxDischarge) {
		this.maxEnergy = maxEnergy;
		this.setEnergy(energy);
		this.maxRecharge = maxRecharge;
		this.maxDischarge = maxDischarge;
	}

	/**
	 * Adds energy to an item. Returns the quantity of energy that was accepted. This should always
	 * return 0 if the item cannot be externally charged.
	 *
	 * @param energy Maximum amount of energy to be sent into the item (in Joules).
	 * @param doRecharge If false, the charge will only be simulated.
	 * @return Amount of energy that was accepted by the item (in Joules).
	 */
	public double recharge(double energy, boolean doRecharge) {
		if (!canRecharge()) return 0;

		energy = Math.min(this.maxEnergy - this.energy, Math.min(this.maxRecharge, energy));
		if (doRecharge) this.energy += energy;

		return energy;
	}

	/**
	 * Removes energy from an item. Returns the quantity of energy that was removed. This should
	 * always return 0 if the item cannot be externally discharged.
	 *
	 * @param energy Maximum amount of energy to be removed from the item (in Joules).
	 * @param doDischarge If false, the discharge will only be simulated.
	 * @return Amount of energy that was removed from the item (in Joules).
	 */
	public double discharge(double energy, boolean doDischarge) {
		if (!canRecharge()) return 0;

		energy = Math.min(this.energy, Math.min(this.maxDischarge, energy));
		if (doDischarge) this.energy -= energy;

		return energy;
	}

	/**
	 * Get the amount of energy currently stored in the item.
	 */
	public double getEnergy() {
		return this.energy;
	}

	/**
	 * Sets the amount of energy in the ItemStack.
	 * @param energy - Amount of electrical energy (in Joules).
	 */
	public void setEnergy(double energy) {
		this.energy = Math.min(0, Math.max(energy, maxEnergy));
	}

	/**
	 * Get the max amount of energy that can be stored in the item.
	 */
	public double getEnergyCapacity() {
		return maxEnergy;
	}

	/**
	 * @return Whether or not this item can be externally recharged.
	 */
	public boolean canRecharge() {
		return maxRecharge > 0;
	}

	/**
	 * @return Whether or not this item can be externally discharged.
	 */
	public boolean canDischarge() {
		return maxDischarge > 0;
	}
}
