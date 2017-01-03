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

import net.minecraftforge.energy.IEnergyStorage;
import nova.core.component.Updater;
import nova.core.event.bus.GlobalEvents;
import nova.core.loader.Loadable;
import nova.core.loader.Mod;
import nova.core.wrapper.mc.forge.v1_11.asm.lib.ComponentInjector;
import nova.core.wrapper.mc.forge.v1_11.util.WrapperEvent;
import nova.energy.EnergyStorage;
import nova.energy.wrapper.mc.forge.v1_11.wrapper.block.forward.FWTileEnergy;
import nova.energy.wrapper.mc.forge.v1_11.wrapper.block.forward.FWTileEnergyUpdater;
import nova.energy.wrapper.mc.forge.v1_11.wrapper.energy.backward.BWEnergyStorage;

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

	private static ComponentInjector<FWTileEnergy> injector = new ComponentInjector<>(FWTileEnergy.class);
	private static ComponentInjector<FWTileEnergyUpdater> updaterInjector = new ComponentInjector<>(FWTileEnergyUpdater.class);

	private final GlobalEvents events;

	public NovaMinecraftEnergy(GlobalEvents events) {
		this.events = events;
	}

	@Override
	public void preInit() {
		events.on(WrapperEvent.BWBlockCreate.class).bind(evt -> {
			if (evt.novaBlock.getTileEntity() instanceof IEnergyStorage)
				evt.novaBlock.components.add(new BWEnergyStorage((IEnergyStorage) evt.novaBlock.getTileEntity()));
		});
		events.on(WrapperEvent.BWItemCreate.class).bind(evt -> {
			if (evt.mcItem instanceof IEnergyStorage)
				evt.novaItem.components.add(new BWEnergyStorage((IEnergyStorage) evt.mcItem));
		});
		events.on(WrapperEvent.FWTileLoad.class).bind(evt -> {
			if (!evt.block.components.has(EnergyStorage.class)) return;
			if (evt.hasResult()) return;

			FWTileEnergy tile;
			if (evt.data.isPresent()) {
				tile = (evt.block instanceof Updater) ?
						updaterInjector.inject(evt.block, new Class[0], new Object[0]) :
						injector.inject(evt.block, new Class[0], new Object[0]);
				tile.setBlock(evt.block);
			} else {
				tile = (evt.block instanceof Updater) ?
						updaterInjector.inject(evt.block, new Class[] { String.class }, new Object[] { evt.block.getID().asString() }) :
						injector.inject(evt.block, new Class[] { String.class }, new Object[] { evt.block.getID().asString() });
				tile.setBlock(evt.block);
			}
			evt.setResult(tile);
		});
	}
}
