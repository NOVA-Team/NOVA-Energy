/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nova.energy.wrapper.mc.forge.v1_11.wrapper.energy.forward;

import net.minecraftforge.energy.IEnergyStorage;
import nova.energy.EnergyStorage;
import nova.energy.UnitConversion;
import nova.energy.UnitDisplay;

/**
 * A FWEnergyStorage is a NOVA energy capability.
 *
 * @author ExE Boss
 */
public class FWEnergyStorage implements IEnergyStorage {

	private static final UnitConversion RF_JOULE = UnitConversion.getConvertion(UnitDisplay.Unit.REDFLUX, UnitDisplay.Unit.JOULE).get();
	private static final UnitConversion JOULE_RF = RF_JOULE.reverse();

	private final EnergyStorage energyStorage;

	public FWEnergyStorage(EnergyStorage energyStorage) {
		this.energyStorage = energyStorage;
	}

	@Override
	public int receiveEnergy(int maxReceive, boolean simulate) {
		if (energyStorage != null)
			return (int) Math.round(JOULE_RF.convert(energyStorage.recharge(RF_JOULE.convert(maxReceive), !simulate)));
		return 0;
	}

	@Override
	public int extractEnergy(int maxExtract, boolean simulate) {
		if (energyStorage != null)
			return (int) Math.round(JOULE_RF.convert(energyStorage.discharge(RF_JOULE.convert(maxExtract), !simulate)));
		return 0;
	}

	@Override
	public int getEnergyStored() {
		if (energyStorage != null)
			return (int) Math.round(JOULE_RF.convert(energyStorage.getEnergy()));
		return 0;
	}

	@Override
	public int getMaxEnergyStored() {
		if (energyStorage != null)
			return (int) Math.round(JOULE_RF.convert(energyStorage.getEnergyCapacity()));
		return 0;
	}

	@Override
	public boolean canExtract() {
		if (energyStorage != null)
			return energyStorage.canDischarge();
		return false;
	}

	@Override
	public boolean canReceive() {
		if (energyStorage != null)
			return energyStorage.canRecharge();
		return false;
	}
}
