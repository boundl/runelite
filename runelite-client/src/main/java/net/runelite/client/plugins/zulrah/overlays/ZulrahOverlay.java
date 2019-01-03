package net.runelite.client.plugins.zulrah.overlays;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.Perspective;
import net.runelite.api.Point;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.plugins.zulrah.ZulrahInstance;
import net.runelite.client.plugins.zulrah.ZulrahPlugin;
import net.runelite.client.plugins.zulrah.phase.ZulrahPhase;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayUtil;

public class ZulrahOverlay extends Overlay
{
    private static final Color TILE_BORDER_COLOR = new Color(0, 0, 0, 150);
    private static final Color NEXT_TEXT_COLOR = new Color(255, 255, 255, 150);

    private final Client client;
    private final ZulrahPlugin plugin;

    @Inject
    ZulrahOverlay(Client client, ZulrahPlugin plugin)
    {
        setPosition(OverlayPosition.DYNAMIC);
        this.client = client;
        this.plugin = plugin;
    }

    @Override
    public Dimension render(Graphics2D graphics)
    {
        ZulrahInstance instance = plugin.getInstance();

        if (instance == null)
            return null;

        ZulrahPhase currentPhase = instance.getPhase();
        ZulrahPhase nextPhase = instance.getNextPhase();

        if (currentPhase == null)
            return null;

        WorldPoint startTile = instance.getStartLocation();
        startTile = new WorldPoint(startTile.getX() + 2, startTile.getY() + 2, startTile.getPlane());

        if (nextPhase != null && currentPhase.getStandLocation() == nextPhase.getStandLocation())
        {
            drawStandTiles(graphics, startTile, currentPhase, nextPhase);
        }
        else
        {
            drawStandTile(graphics, startTile, currentPhase, false);
            drawStandTile(graphics, startTile, nextPhase, true);
        }
        drawZulrahTileMinimap(graphics, startTile, currentPhase, false);
        drawZulrahTileMinimap(graphics, startTile, nextPhase, true);

        return null;
    }

    private void drawStandTiles(Graphics2D graphics, WorldPoint startTile, ZulrahPhase currentPhase, ZulrahPhase nextPhase)
    {
        LocalPoint localTile = LocalPoint.fromWorld(client, currentPhase.getStandTile(startTile));

        if (localTile == null)
            return;

        Polygon northPoly = getCanvasTileNorthPoly(client, localTile);
        Polygon southPoly = getCanvasTileSouthPoly(client, localTile);
        Polygon poly = Perspective.getCanvasTilePoly(client, localTile);

        Point textLoc = Perspective.getCanvasTextLocation(client, graphics, localTile, "Stand/Next", 0);

        if (northPoly != null && southPoly != null && poly != null && textLoc != null)
        {
            OverlayUtil.renderPolygon(graphics, northPoly, currentPhase.getColor());
            OverlayUtil.renderPolygon(graphics, southPoly, nextPhase.getColor());
            OverlayUtil.renderPolygon(graphics, poly, TILE_BORDER_COLOR);
            OverlayUtil.renderTextLocation(graphics, textLoc, "Stand/Next", NEXT_TEXT_COLOR);
        }

        if (!nextPhase.isJad())
            return;

        BufferedImage jadPrayerImg = ZulrahImageManager.getProtectionPrayerBufferedImage(nextPhase.getPrayer());

        if (jadPrayerImg == null)
            return;

        Point imageLoc = Perspective.getCanvasImageLocation(client, localTile, jadPrayerImg, 0);

        if (imageLoc == null)
            return;

        OverlayUtil.renderImageLocation(graphics, imageLoc, jadPrayerImg);
    }

    private void drawStandTile(Graphics2D graphics, WorldPoint startTile, ZulrahPhase phase, boolean next)
    {
        if (phase == null)
            return;

        LocalPoint localTile = LocalPoint.fromWorld(client, phase.getStandTile(startTile));

        if (localTile == null)
            return;

        Polygon poly = Perspective.getCanvasTilePoly(client, localTile);

        if (poly != null)
        {
            OverlayUtil.renderPolygon(graphics, poly, phase.getColor());

            if (!next)
            {
                Point textLoc = Perspective.getCanvasTextLocation(client, graphics, localTile, "Stand", 0);

                if (textLoc != null)
                    OverlayUtil.renderTextLocation(graphics, textLoc, "Stand", NEXT_TEXT_COLOR);
            }
        }

        if (next)
        {
            Point textLoc = Perspective.getCanvasTextLocation(client, graphics, localTile, "Next", 0);

            if (textLoc != null)
                OverlayUtil.renderTextLocation(graphics, textLoc, "Next", NEXT_TEXT_COLOR);

            if (phase.isJad())
            {
                BufferedImage jadPrayerImg = ZulrahImageManager.getProtectionPrayerBufferedImage(phase.getPrayer());

                if (jadPrayerImg != null)
                {
                    Point imageLoc = Perspective.getCanvasImageLocation(client, localTile, jadPrayerImg, 0);

                    if (imageLoc != null)
                        OverlayUtil.renderImageLocation(graphics, imageLoc, jadPrayerImg);
                }
            }
        }
    }

    private void drawZulrahTileMinimap(Graphics2D graphics, WorldPoint startTile, ZulrahPhase phase, boolean next)
    {
        if (phase == null)
        {
            return;
        }
        LocalPoint zulrahLocalTile = LocalPoint.fromWorld(client, phase.getZulrahTile(startTile));

        if (zulrahLocalTile == null)
            return;

        if (next)
        {
            Polygon poly = Perspective.getCanvasTilePoly(client, zulrahLocalTile);
            Point textLoc = Perspective.getCanvasTextLocation(client, graphics, zulrahLocalTile, "Next", 0);
            OverlayUtil.renderPolygon(graphics, poly, phase.getColor());
            OverlayUtil.renderTextLocation(graphics, textLoc, "Next", phase.getColor(255));
        }

        /*Point zulrahMinimapPoint = Perspective.localToMinimap(client, zulrahLocalTile);

        if (zulrahMinimapPoint == null)
            return;

        graphics.setColor(phase.getColor());
        graphics.fillOval(zulrahMinimapPoint.getX(), zulrahMinimapPoint.getY(), 5, 5);
        graphics.setColor(TILE_BORDER_COLOR);
        graphics.setStroke(new BasicStroke(1));
        graphics.drawOval(zulrahMinimapPoint.getX(), zulrahMinimapPoint.getY(), 5, 5);

        if (next)
        {
            graphics.setColor(NEXT_TEXT_COLOR);
            FontMetrics fm = graphics.getFontMetrics();
            graphics.drawString("Next", zulrahMinimapPoint.getX() - fm.stringWidth("Next") / 2, zulrahMinimapPoint.getY() - 2);
        }*/
    }

    private Polygon getCanvasTileNorthPoly(Client client, LocalPoint localLocation)
    {
        int plane = client.getPlane();
        int halfTile = Perspective.LOCAL_TILE_SIZE / 2;

        Point p1 = Perspective.localToCanvas(client, new LocalPoint(localLocation.getX() - halfTile, localLocation.getY() - halfTile), plane);
        Point p2 = Perspective.localToCanvas(client, new LocalPoint(localLocation.getX() - halfTile, localLocation.getY() + halfTile), plane);
        Point p3 = Perspective.localToCanvas(client, new LocalPoint(localLocation.getX() + halfTile, localLocation.getY() + halfTile), plane);

        if (p1 == null || p2 == null || p3 == null)
        {
            return null;
        }

        Polygon poly = new Polygon();
        poly.addPoint(p1.getX(), p1.getY());
        poly.addPoint(p2.getX(), p2.getY());
        poly.addPoint(p3.getX(), p3.getY());

        return poly;
    }

    private Polygon getCanvasTileSouthPoly(Client client, LocalPoint localLocation)
    {
        int plane = client.getPlane();
        int halfTile = Perspective.LOCAL_TILE_SIZE / 2;

        Point p1 = Perspective.localToCanvas(client, new LocalPoint(localLocation.getX() - halfTile, localLocation.getY() - halfTile), plane);
        Point p2 = Perspective.localToCanvas(client, new LocalPoint(localLocation.getX() + halfTile, localLocation.getY() + halfTile), plane);
        Point p3 = Perspective.localToCanvas(client, new LocalPoint(localLocation.getX() + halfTile, localLocation.getY() - halfTile), plane);

        if (p1 == null || p2 == null || p3 == null)
        {
            return null;
        }

        Polygon poly = new Polygon();
        poly.addPoint(p1.getX(), p1.getY());
        poly.addPoint(p2.getX(), p2.getY());
        poly.addPoint(p3.getX(), p3.getY());

        return poly;
    }
}