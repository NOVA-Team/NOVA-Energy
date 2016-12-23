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
 * An interface for items that store energy in joules.
 */
public interface EnergyItem {
	/**
	 * Adds energy to an item. Returns the quantity of energy that was accepted. This should always
	 * return 0 if the item cannot be externally charged.
	 * @param energy Maximum amount of energy to be sent into the item.
	 * @param doRecharge If false, the charge will only be simulated.
	 * @return Amount of energy that was accepted by the item.
	 */
	public double recharge(double energy, boolean doRecharge);

	/**
	 * Removes energy from an item. Returns the quantity of energy that was removed. This should
	 * always return 0 if the item cannot be externally discharged.
	 * @param energy Maximum amount of energy to be removed from the item.
	 * @param doDischarge If false, the discharge will only be simulated.
	 * @return Amount of energy that was removed from the item.
	 */
	public double discharge(double energy, boolean doDischarge);

	/**
	 * Get the amount of energy currently stored in the item.
	 */
	public double getEnergy();

	/**
	 * Sets the amount of energy in the ItemStack.
	 * @param energy - Amount of electrical energy.
	 */
	public void setEnergy(double energy);

	/**
	 * Get the max amount of energy that can be stored in the item.
	 */
	public double getEnergyCapacity();
}
