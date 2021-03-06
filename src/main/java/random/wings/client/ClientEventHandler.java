package random.wings.client;

import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import random.wings.client.renderer.entity.*;
import random.wings.client.renderer.tileentity.DEDNestTileEntityRenderer;
import random.wings.client.renderer.tileentity.HBNestTileEntityRenderer;
import random.wings.entity.item.PlowheadEggEntity;
import random.wings.entity.monster.IcyPlowheadEntity;
import random.wings.entity.passive.DumpyEggDrakeEntity;
import random.wings.entity.passive.HatchetBeakEntity;
import random.wings.entity.passive.MimangoEntity;
import random.wings.tileentity.DEDNestTileEntity;
import random.wings.tileentity.HBNestTileEntity;

public class ClientEventHandler {
    public static void init() {
        ClientRegistry.bindTileEntitySpecialRenderer(DEDNestTileEntity.class, new DEDNestTileEntityRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(HBNestTileEntity.class, new HBNestTileEntityRenderer());

        RenderingRegistry.registerEntityRenderingHandler(DumpyEggDrakeEntity.class, DumpyEggDrakeRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(HatchetBeakEntity.class, HatchetBeakRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(IcyPlowheadEntity.class, IcyPlowheadRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(PlowheadEggEntity.class, PlowheadEggRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(MimangoEntity.class, MimangoRenderer::new);
        //RenderingRegistry.registerEntityRenderingHandler(MimangoEggEntity.class, MimangoEggRenderer::new);
    }
}
