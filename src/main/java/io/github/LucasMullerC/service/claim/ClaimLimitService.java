package io.github.LucasMullerC.service.claim;

import java.util.UUID;

import org.bukkit.entity.Player;

import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.session.SessionManager;
import com.sk89q.worldedit.util.formatting.text.TextComponent;
import com.sk89q.worldedit.world.World;

import io.github.LucasMullerC.util.MessageUtils;
import io.github.LucasMullerC.util.RegionUtils;

public class ClaimLimitService {
    private static final int LIMIT_TIER_1_2 = 512;
    private static final int LIMIT_TIER_3_4 = 1312;
    private static final int LIMIT_TIER_5_6 = 2412;
    private static final int LIMIT_TIER_7_8 = 3512;
    private static final int LIMIT_TIER_9_10 = 4412;
    private static final int LIMIT_MAX = 9999;

    Player player;
    com.sk89q.worldedit.entity.Player actor;
    SessionManager manager;
    LocalSession localSession;
    World world;

    public ClaimLimitService(Player player) {
        this.player = player;
        this.actor = BukkitAdapter.adapt(player);
        this.manager = WorldEdit.getInstance().getSessionManager();
        this.localSession = manager.get(actor);
        this.world = localSession.getSelectionWorld();
    }

    public boolean getLimitTier(int Tier, Player player) {
        UUID id = player.getUniqueId();
        int limit = 3;
        ClaimService claimService = new ClaimService();

        if (Tier >= 1 && Tier <= 3) {
            limit = 3;
        } else if (Tier >= 4 && Tier <= 6) {
            limit = 4;
        } else if (Tier >= 7 && Tier <= 9) {
            limit = 5;
        } else if (Tier >= 10 && Tier <= 12) {
            limit = 6;
        } else if (Tier >= 13 && Tier <= 15) {
            limit = 7;
        } else {
            limit = Tier - 7;
        }

        if (player.hasPermission("group.apoiador")) {
            limit = limit + 10;
        }
        int claimQtd = claimService.getClaimQtdByPlayer(id.toString());
        if(claimQtd == limit){
            return true;
        } else{
            return false;
        }
    }

    public String VerifyClaimLimits(Player player, int Tier) {
        Region region = getSelection();
        if (region != null) {
            Integer selectionWidth = RegionUtils.getSelectionDimension(localSession, world, "WIDTH");
            Integer selectionLength = RegionUtils.getSelectionDimension(localSession, world, "LENGTH");

            if (player.hasPermission("btebrasil.nolimit")) {
                return RegionUtils.convertToPolygonal2DRegion(region);
            } else {
                int limit = getLimitSelection(Tier);
                if (selectionWidth <= limit && selectionLength <= limit) {
                    return RegionUtils.convertToPolygonal2DRegion(region);
                }else {
                    return "2"; // 2 = Off limits Selection
                }
            }
        } else {
            return "3"; // 3 = Selection not found
        }
    }

    private Region getSelection() {
        try {
            if (world == null)
                throw new IncompleteRegionException();
            return localSession.getSelection(world);

        } catch (IncompleteRegionException ex) {
            actor.printError(TextComponent.of(MessageUtils.getMessage("MakeSelectionFirst", player)));
            return null;
        }
    }

    private int getLimitSelection(int Tier) {
        if (Tier >= 1 && Tier <= 2) {
            return LIMIT_TIER_1_2;
        } else if (Tier >= 3 && Tier <= 4) {
            return LIMIT_TIER_3_4;
        } else if (Tier >= 5 && Tier <= 6) {
            return LIMIT_TIER_5_6;
        } else if (Tier >= 7 && Tier <= 8) {
            return LIMIT_TIER_7_8;
        } else if (Tier >= 9 && Tier <= 10) {
            return LIMIT_TIER_9_10;
        } else {
            return LIMIT_MAX;
        }
    }
}
