package cofh.thermaldynamics.render;

import codechicken.lib.model.bakery.generation.IItemBakery;
import codechicken.lib.render.CCRenderState;
import codechicken.lib.render.buffer.BakingVertexBuffer;
import cofh.core.util.helpers.RenderHelper;
import cofh.thermaldynamics.block.BlockDuct;
import cofh.thermaldynamics.duct.Duct;
import cofh.thermaldynamics.duct.TDDucts;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import org.lwjgl.opengl.GL11;

import java.util.LinkedList;
import java.util.List;

public class DuctItemModelBakery implements IItemBakery {

	public static final DuctItemModelBakery INSTANCE = new DuctItemModelBakery();

	@Override
	public List<BakedQuad> bakeItemQuads(EnumFacing face, ItemStack stack) {

		List<BakedQuad> quads = new LinkedList<>();
		if (face == null) {
			CCRenderState ccrs = CCRenderState.instance();
			BakingVertexBuffer buffer = BakingVertexBuffer.create();
			buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.ITEM);
			ccrs.reset();
			ccrs.bind(buffer);

			Block blockFromItem = Block.getBlockFromItem(stack.getItem());

			Duct duct = TDDucts.getDuct(((BlockDuct) blockFromItem).offset + stack.getItemDamage());
			int metadata = duct.id;

			RenderDuct.INSTANCE.renderBase(ccrs, true, metadata, RenderDuct.INV_CONNECTIONS, 0, 0, 0, duct.getBaseTexture(stack));
			RenderDuct.INSTANCE.renderWorldExtra(ccrs, true, metadata, RenderDuct.INV_CONNECTIONS, 0, 0 - RenderHelper.RENDER_OFFSET, 0);

			buffer.finishDrawing();
			quads.addAll(buffer.bake());
		}
		return quads;
	}
}
