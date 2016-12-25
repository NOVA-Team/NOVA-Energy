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
package nova.energy.wrapper.mc.forge.v1_11.wrapper.energy.backward;

import net.minecraftforge.energy.IEnergyStorage;
import nova.energy.EnergyStorage;
import nova.energy.UnitConversion;
import nova.energy.UnitDisplay;

/**
 *
 * @author ExE Boss
 */
public class BWEnergyStorage extends EnergyStorage {

	private static final UnitConversion RF_JOULE = UnitConversion.getConvertion(UnitDisplay.Unit.REDFLUX, UnitDisplay.Unit.JOULE).get();
	private static final UnitConversion JOULE_RF = RF_JOULE.reverse();

	private final IEnergyStorage mcEnergyStorage;

	public BWEnergyStorage(IEnergyStorage mcEnergyStorage) {
		this.mcEnergyStorage = mcEnergyStorage;
	}

	@Override
	public double recharge(double energy, boolean doRecharge) {
		return RF_JOULE.convert(mcEnergyStorage.receiveEnergy((int) Math.round(JOULE_RF.convert(energy)), !doRecharge));
	}

	@Override
	public double discharge(double energy, boolean doDischarge) {
		return RF_JOULE.convert(mcEnergyStorage.extractEnergy((int) Math.round(JOULE_RF.convert(energy)), !doDischarge));
	}

	@Override
	public double getEnergy() {
		return RF_JOULE.convert(mcEnergyStorage.getEnergyStored());
	}

	@Override
	public void setEnergy(double energy) {
		int rf = (int) Math.round(JOULE_RF.convert(energy)) - mcEnergyStorage.getEnergyStored();
		if (rf > 0) {
			mcEnergyStorage.receiveEnergy(rf, false);
		} else if (rf < 0) {
			mcEnergyStorage.extractEnergy(-rf, false);
		}
	}

	@Override
	public double getEnergyCapacity() {
		return RF_JOULE.convert(mcEnergyStorage.getMaxEnergyStored());
	}

	@Override
	public boolean canDischarge() {
		return mcEnergyStorage.canExtract();
	}

	@Override
	public boolean canRecharge() {
		return mcEnergyStorage.canReceive();
	}
}
