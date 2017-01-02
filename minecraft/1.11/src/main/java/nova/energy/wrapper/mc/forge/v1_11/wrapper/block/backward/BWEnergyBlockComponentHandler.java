/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nova.energy.wrapper.mc.forge.v1_11.wrapper.block.backward;

import net.minecraft.block.Block;
import net.minecraftforge.energy.IEnergyStorage;
import nova.core.wrapper.mc.forge.v1_11.wrapper.block.backward.BWBlock;
import nova.core.wrapper.mc.forge.v1_11.wrapper.block.backward.BWBlockComponentHandler;
import nova.energy.wrapper.mc.forge.v1_11.wrapper.energy.backward.BWEnergyStorage;

/**
 *
 * @author ExE Boss
 */
public class BWEnergyBlockComponentHandler implements BWBlockComponentHandler {

	@Override
	public void addComponents(BWBlock novaBlock, Block mcBlock) {
		if (novaBlock.getTileEntity() instanceof IEnergyStorage)
			novaBlock.components.add(new BWEnergyStorage((IEnergyStorage) novaBlock.getTileEntity()));
	}
}
