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

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import nova.core.loader.Loadable;
import nova.core.wrapper.mc.forge.v1_11.launcher.NovaMinecraft;
import nova.core.wrapper.mc.forge.v1_11.wrapper.block.forward.FWTileLoader;
import nova.energy.wrapper.mc.forge.v1_11.wrapper.block.forward.FWTileEnergyLoader;

/**
 *
 * @author ExE Boss
 */
@Mod(modid = NovaMinecraftEnergy.id, name = NovaMinecraftEnergy.name, version = NovaMinecraftEnergy.version, acceptableRemoteVersions = "*")
public class NovaMinecraftEnergy implements Loadable {

	public static final String version = "0.0.1";
	public static final String id = "novaenergy";
	public static final String name = "NOVA Energy";

	public NovaMinecraftEnergy() {
		NovaMinecraft.registerNovaWrapper(this);
	}

	@Override
	public void preInit() {
		FWTileLoader.registerTileLoader(new FWTileEnergyLoader());
	}
}
