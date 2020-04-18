package random.wings.client.renderer.tileentity;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.util.ResourceLocation;
import random.wings.WingsAndClaws;
import random.wings.client.renderer.tileentity.model.HBNestModel;
import random.wings.tileentity.HBNestTileEntity;

public class HBNestTileEntityRenderer extends TileEntityRenderer<HBNestTileEntity> {
    private static final ResourceLocation[] TEXTURES = new ResourceLocation[]{new ResourceLocation(WingsAndClaws.MOD_ID, "textures/entities/nest/hatchet_beak/empty.png"), new ResourceLocation(WingsAndClaws.MOD_ID, "textures/entities/nest/hatchet_beak/egg.png")};
    private final HBNestModel model = new HBNestModel();

    @Override
    public void render(HBNestTileEntity tileEntityIn, double x, double y, double z, float partialTicks, int destroyStage) {
        super.render(tileEntityIn, x, y, z, partialTicks, destroyStage);
        this.bindTexture(TEXTURES[tileEntityIn.getEggCount()]);
        GlStateManager.pushMatrix();
        GlStateManager.translatef((float) x + 0.5f, (float) y + 1.5f, (float) z + 0.5f);
        GlStateManager.rotatef(180.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.enableRescaleNormal();
        this.model.render();
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.popMatrix();
    }
}
