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
package nova.energy.wrapper.mc.forge.v1_11;

import net.minecraft.util.EnumFacing;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import nova.core.event.bus.GlobalEvents;
import nova.core.loader.Loadable;
import nova.core.loader.Mod;
import nova.core.wrapper.mc.forge.v1_11.util.WrapperEvent;
import nova.energy.EnergyStorage;
import nova.energy.wrapper.mc.forge.v1_11.wrapper.energy.backward.BWEnergyStorage;
import nova.energy.wrapper.mc.forge.v1_11.wrapper.energy.forward.FWEnergyStorage;

/**
 * Compatibility with Forge Energy, which has been in Minecraft Forge since Minecraft 1.10.2.
 *
 * @author ExE Boss
 */
@Mod(id = NovaMinecraftEnergy.id, name = NovaMinecraftEnergy.name, version = NovaMinecraftEnergy.version, novaVersion = "0.1.0")
public class NovaMinecraftEnergy implements Loadable {

	public static final String version = "0.0.1";
	public static final String id = "novaenergy";
	public static final String name = "NOVA Energy";

	private final GlobalEvents events;

	public NovaMinecraftEnergy(GlobalEvents events) {
		this.events = events;
	}

	@Override
	public void preInit() {
		events.on(WrapperEvent.BWBlockCreate.class).bind(evt -> {
			IEnergyStorage energyCapability = null;
			for (EnumFacing facing : EnumFacing.values()) {
				if (!evt.novaBlock.getTileEntity().hasCapability(CapabilityEnergy.ENERGY, facing))
					continue;

				energyCapability = evt.novaBlock.getTileEntity().getCapability(CapabilityEnergy.ENERGY, facing);
				if (energyCapability != null)
					break; // NOVA-Energy is Unsided
			}

			if (energyCapability == null && evt.novaBlock.getTileEntity().hasCapability(CapabilityEnergy.ENERGY, null))
				energyCapability = evt.novaBlock.getTileEntity().getCapability(CapabilityEnergy.ENERGY, null);

			if (energyCapability != null)
				evt.novaBlock.components.add(new BWEnergyStorage(energyCapability));
		});

		events.on(WrapperEvent.BWItemCreate.class).bind(evt -> {
			if (evt.itemStack.isPresent() && evt.itemStack.get().hasCapability(CapabilityEnergy.ENERGY, null))
				evt.novaItem.components.add(new BWEnergyStorage(evt.itemStack.get().getCapability(CapabilityEnergy.ENERGY, null)));
		});

		events.on(WrapperEvent.BWEntityCreate.class).bind(evt -> {
			if (evt.mcEntity.hasCapability(CapabilityEnergy.ENERGY, null))
				evt.novaEntity.components.add(new BWEnergyStorage(evt.mcEntity.getCapability(CapabilityEnergy.ENERGY, null)));
		});

		events.on(WrapperEvent.FWTileCreate.class).bind(evt -> {
			if (evt.novaBlock.components.has(EnergyStorage.class)) // Components are unsided
				evt.tileEntity.addCapability(CapabilityEnergy.ENERGY, new FWEnergyStorage(evt.novaBlock.components.get(EnergyStorage.class)), null);
		});

		events.on(WrapperEvent.FWItemInitCapabilities.class).bind(evt -> {
			if (evt.novaItem.components.has(EnergyStorage.class)) // Components are unsided
				evt.capabilityProvider.addCapability(CapabilityEnergy.ENERGY, new FWEnergyStorage(evt.novaItem.components.get(EnergyStorage.class)), null);
		});

		events.on(WrapperEvent.FWEntityCreate.class).bind(evt -> {
			if (evt.novaBlock.components.has(EnergyStorage.class)) // Components are unsided
				evt.mcEntity.addCapability(CapabilityEnergy.ENERGY, new FWEnergyStorage(evt.novaBlock.components.get(EnergyStorage.class)), null);
		});
	}
}
