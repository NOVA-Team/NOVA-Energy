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
package nova.energy.wrapper.mc.forge.v1_11.wrapper.block.forward;

import net.minecraftforge.energy.IEnergyStorage;
import nova.core.wrapper.mc.forge.v1_11.wrapper.block.forward.FWTile;
import nova.energy.EnergyStorage;
import nova.energy.UnitConversion;
import nova.energy.UnitDisplay;

/**
 *
 * @author ExE Boss
 */
public class FWTileEnergy extends FWTile implements IEnergyStorage {

	private static final UnitConversion RF_JOULE = UnitConversion.getConvertion(UnitDisplay.Unit.REDFLUX, UnitDisplay.Unit.JOULE).get();
	private static final UnitConversion JOULE_RF = RF_JOULE.reverse();

	public FWTileEnergy() {
	}

	public FWTileEnergy(String blockID) {
		this.blockID = blockID;
	}

	@Override
	public int receiveEnergy(int maxReceive, boolean simulate) {
		return (int) Math.round(JOULE_RF.convert(getBlock().components.get(EnergyStorage.class).recharge(RF_JOULE.convert(maxReceive), !simulate)));
	}

	@Override
	public int extractEnergy(int maxExtract, boolean simulate) {
		return (int) Math.round(JOULE_RF.convert(getBlock().components.get(EnergyStorage.class).discharge(RF_JOULE.convert(maxExtract), !simulate)));
	}

	@Override
	public int getEnergyStored() {
		return (int) Math.round(JOULE_RF.convert(getBlock().components.get(EnergyStorage.class).getEnergy()));
	}

	@Override
	public int getMaxEnergyStored() {
		return (int) Math.round(JOULE_RF.convert(getBlock().components.get(EnergyStorage.class).getEnergyCapacity()));
	}

	@Override
	public boolean canExtract() {
		return getBlock().components.get(EnergyStorage.class).canDischarge();
	}

	@Override
	public boolean canReceive() {
		return getBlock().components.get(EnergyStorage.class).canRecharge();
	}
}
