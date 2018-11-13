package net.runelite.mixins;

import net.runelite.api.FreezeInfo;
import net.runelite.api.Perspective;
import net.runelite.api.Point;
import net.runelite.api.mixins.Copy;
import net.runelite.api.mixins.Inject;
import net.runelite.api.mixins.Mixin;
import net.runelite.api.mixins.Replace;
import net.runelite.api.mixins.Shadow;
import net.runelite.rs.api.*;

@Mixin(RSClient.class)
public abstract class FreezeBarMixin implements RSClient
{
    @Shadow("clientInstance")
    private static RSClient client;

    @Inject
    Boolean drawFreezeBar;

    @Inject
    public boolean getDrawFreezeBar()
    {
        if (drawFreezeBar == null)
            drawFreezeBar = false;

        return drawFreezeBar;
    }

    @Inject
    public void setDrawFreezeBar(boolean draw)
    {
        drawFreezeBar = draw;
    }

    @Copy("draw2DExtras")
    public static final void rs$draw2DExtras(RSActor actor, int var1, int var2, int var3, int var4, int var5)
    {
        throw new RuntimeException();
    }

    @Replace("draw2DExtras")
    public static final void rl$draw2DExtras(RSActor actor, int var1, int var2, int var3, int var4, int var5)
    {
        if (actor != null/* && client.getDrawFreezeBar()*/)
        {
            FreezeInfo freezeInfo = null;//actor.getFreeze();

            boolean hasComposition = false;

            if (actor instanceof RSNPC) {
                hasComposition = ((RSNPC) actor).getComposition() != null;
            }
            else if (actor instanceof RSPlayer) {
                hasComposition = ((RSPlayer) actor).getPlayerComposition() != null;
            }

            if (hasComposition && freezeInfo != null && (freezeInfo.isFrozen() || freezeInfo.isImmune()))
            {
                if (actor instanceof RSNPC)
                {
                    RSNPCComposition actorComposition = ((RSNPC) actor).getComposition();

                    if (actorComposition.getConfigs() != null)
                    {
                        actorComposition = actorComposition.transform();
                    }

                    if (actorComposition == null)
                    {
                        return;
                    }
                }

                int offset = 3;
                int maxWidth;

                int height = actor.getLogicalHeight() + 15;

                Point canvasPoint = Perspective.localToCanvas(client, actor.getLocalLocation(), client.getPlane(), height);
                //Point canvasPoint = Perspective.worldToCanvas(client, actor.getX(), actor.getY(), height, client.getPlane());

                if (canvasPoint != null)
                {
                    maxWidth = 32;

                    int x;
                    int y;

                    x = var2 + canvasPoint.getX() - ((maxWidth - 2) >> 1);
                    y = var3 + canvasPoint.getY() - offset;

                    if (freezeInfo.isFrozen())
                    {
                        int width = freezeInfo.getFrozen() * (maxWidth / 32);

                        //client.Rasterizer2D_fillRectangle(x, y + 7, width, 3, 0x36cfe0);
                    }
                    else if (freezeInfo.isImmune())
                    {
                        int width = freezeInfo.getImmune() * (maxWidth / 6);

                        //client.Rasterizer2D_fillRectangle(x, y + 7, width, 3, 0xe09736);
                    }
                }
            }
        }

        rs$draw2DExtras(actor, var1, var2, var3, var4, var5);
    }
}