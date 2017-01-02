/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nova.energy.wrapper.mc.forge.v1_11.wrapper.item.backward;

import net.minecraft.item.Item;
import net.minecraftforge.energy.IEnergyStorage;
import nova.core.wrapper.mc.forge.v1_11.wrapper.item.backward.BWItem;
import nova.core.wrapper.mc.forge.v1_11.wrapper.item.backward.BWItemComponentHandler;
import nova.energy.wrapper.mc.forge.v1_11.wrapper.energy.backward.BWEnergyStorage;

/**
 *
 * @author ExE Boss
 */
public class BWEnergyItemComponentHandler implements BWItemComponentHandler {

	@Override
	public void addComponents(BWItem novaItem, Item mcItem) {
		if (mcItem instanceof IEnergyStorage)
			novaItem.components.add(new BWEnergyStorage((IEnergyStorage) mcItem));
	}
}
