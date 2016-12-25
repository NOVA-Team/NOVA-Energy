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

import net.minecraft.nbt.NBTTagCompound;
import nova.core.block.Block;
import nova.core.component.Updater;
import nova.core.wrapper.mc.forge.v1_11.asm.lib.ComponentInjector;
import nova.core.wrapper.mc.forge.v1_11.wrapper.block.forward.FWCustomTileLoader;
import nova.core.wrapper.mc.forge.v1_11.wrapper.block.forward.FWTile;
import nova.energy.EnergyStorage;

/**
 *
 * @author ExE Boss
 */
public class FWTileEnergyLoader implements FWCustomTileLoader {

	private static ComponentInjector<FWTileEnergy> injector = new ComponentInjector<>(FWTileEnergy.class);
	private static ComponentInjector<FWTileEnergyUpdater> updaterInjector = new ComponentInjector<>(FWTileEnergyUpdater.class);

	@Override
	public boolean isBlockSupported(Block block) {
		return (block.components.has(EnergyStorage.class));
	}

	@Override
	public FWTile loadTile(Block block, NBTTagCompound data) {
		if (block.components.has(EnergyStorage.class)) {
			FWTile tile = (block instanceof Updater) ? updaterInjector.inject(block, new Class[0], new Object[0]) : injector.inject(block, new Class[0], new Object[0]);
			tile.setBlock(block);
			return tile;
		}
		return null;
	}

	@Override
	public FWTile loadTile(Block block, String blockID) {
		if (block.components.has(EnergyStorage.class)) {

		}
		return null;
	}
}
