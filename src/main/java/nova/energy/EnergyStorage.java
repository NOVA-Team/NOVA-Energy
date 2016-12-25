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
 *
 * @author ExE Boss
 */
public class EnergyStorage extends Component implements IEnergyStorage, Storable {

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
		this.energy = energy;
		this.maxEnergy = maxEnergy;
		this.maxRecharge = maxRecharge;
		this.maxDischarge = maxDischarge;
	}

	@Override
	public double recharge(double energy, boolean doRecharge) {
		if (!canRecharge()) return 0;

		energy = Math.min(this.maxEnergy - this.energy, Math.min(this.maxRecharge, energy));
		if (doRecharge) this.energy += energy;

		return energy;
	}

	@Override
	public double discharge(double energy, boolean doDischarge) {
		if (!canRecharge()) return 0;

		energy = Math.min(this.energy, Math.min(this.maxDischarge, energy));
		if (doDischarge) this.energy -= energy;

		return energy;
	}

	@Override
	public double getEnergy() {
		return this.energy;
	}

	@Override
	public void setEnergy(double energy) {
		this.energy = Math.min(0, Math.max(energy, maxEnergy));
	}

	@Override
	public double getEnergyCapacity() {
		return maxEnergy;
	}

	@Override
	public boolean canRecharge() {
		return maxRecharge > 0;
	}

	@Override
	public boolean canDischarge() {
		return maxDischarge > 0;
	}
}
