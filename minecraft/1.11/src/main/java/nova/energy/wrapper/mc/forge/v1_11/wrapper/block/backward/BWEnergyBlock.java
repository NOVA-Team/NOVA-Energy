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
package nova.energy.wrapper.mc.forge.v1_11.wrapper.block.backward;

import nova.energy.wrapper.mc.forge.v1_11.wrapper.energy.backward.BWEnergyStorage;
import net.minecraft.block.Block;
import net.minecraftforge.energy.IEnergyStorage;
import nova.core.world.World;
import nova.core.wrapper.mc.forge.v1_11.wrapper.block.backward.BWBlock;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 *
 * @author ExE Boss
 */
public class BWEnergyBlock extends BWBlock {

	private IEnergyStorage mcEnergyStorage;

	public BWEnergyBlock(Block block) {
		super(block);
	}

	public BWEnergyBlock(Block block, World world, Vector3D pos) {
		super(block, world, pos);
		if (getEnergyStorage() != null) {
			components.add(new BWEnergyStorage(mcEnergyStorage));
		}
	}

	public IEnergyStorage getEnergyStorage() {
		if (mcEnergyStorage == null) {
			if (getTileEntity() instanceof IEnergyStorage) {
				mcEnergyStorage = (IEnergyStorage) getTileEntity();
			} else if (mcBlock instanceof IEnergyStorage) {
				mcEnergyStorage = (IEnergyStorage) mcBlock;
			}
		}
		return mcEnergyStorage;
	}
}
