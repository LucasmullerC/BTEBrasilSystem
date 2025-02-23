package io.github.LucasMullerC.service.claim;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import java.util.ArrayList;

import io.github.LucasMullerC.BTEBrasilSystem.BTEBrasilSystem;
import io.github.LucasMullerC.model.Claim;
import io.github.LucasMullerC.model.Pending;
import io.github.LucasMullerC.service.WorldGuardService;
import io.github.LucasMullerC.service.pending.PendingService;
import io.github.LucasMullerC.util.ListUtil;

public class ClaimService {
    public ListUtil<Claim> claim;
    
    public ClaimService() {
        BTEBrasilSystem plugin = BTEBrasilSystem.getPlugin();
        String pluginFolder = plugin.getDataFolder().getAbsolutePath();
        (new File(pluginFolder)).mkdirs();
        this.claim = new ListUtil<Claim>(new File(pluginFolder + File.separator + "areas.txt"));
        claim.load(Claim.class);
    }

    public void addClaim(Claim claim, Player player) {
        if(claim != null){
            this.claim.add(claim);
            this.claim.save();
            WorldGuardService worldGuardService = new WorldGuardService();
            worldGuardService.AddRegion(claim.getPoints(), claim.getClaim(), player);
        }
    }

    public void removeClaim(Claim claim, Player player) {
        this.claim.remove(claim);
        this.claim.save();
        WorldGuardService worldGuardService = new WorldGuardService();
        worldGuardService.RemoveRegion(claim.getClaim(), player);
        removePendentandParticipants(claim);
    }

    public void removeCopyClaim(Claim claim, Player player){
        claim.setPlayer("nulo");
        claim.setParticipants("nulo");
        claim.setDeadline("nulo");
        this.claim.save();

        WorldGuardService worldGuardService = new WorldGuardService();
        worldGuardService.RemoveRegion("copy"+claim.getClaim(), player);
    }

    public void updateClaim(Claim claim){
       Claim oldClaim = getClaim(claim.getClaim());
       if(oldClaim != null){
        oldClaim = claim;
        this.claim.save();
       }
    }

    public Claim getClaim(String claimId) {
        for (Claim claim : this.claim.getValues()) {
            if (claim.getClaim() != null && claim.getClaim().contains(claimId)) {
                if (claim.getClaim().equals(claimId)) {
                    return claim;
                }
            }
        }
        return null;
    }

    public int getClaimQtdByPlayer(String playerId) {
        int cont = 0;
        for (Claim claim : this.claim.getValues()) {
            if (claim.getPlayer() != null && claim.getPlayer().contains(playerId) && claim.getStatus().equals("F")) {
                cont++;
            }
        }
        return cont;
    }

    public ArrayList<Claim> getClaimListByPlayer(String playerId) {
        ArrayList<Claim> playerClaim = new ArrayList<>();
        for (Claim claim : this.claim.getValues()) {
            if (claim.getPlayer() != null && claim.getPlayer().contains(playerId) && claim.getStatus().equals("F")) {
                playerClaim.add(claim);
            }
        }
        return playerClaim;
    }

    public int getCompletedClaimQtdByPlayer(String playerId) {
        int cont = 0;
        for (Claim claim : this.claim.getValues()) {
            if (claim.getPlayer() != null && claim.getPlayer().contains(playerId) && claim.getStatus().equals("T")) {
                cont++;
            }
        }
        return cont;
    }

    public ArrayList<Claim> getCompletedClaimListByPlayer(String playerId) {
        ArrayList<Claim> playerClaim = new ArrayList<>();
        for (Claim claim : this.claim.getValues()) {
            if (claim.getPlayer() != null && claim.getPlayer().contains(playerId) && claim.getStatus().equals("T")) {
                playerClaim.add(claim);
            }
        }
        return playerClaim;
    }

    public ArrayList<Claim> getClaimList(){
        return claim.getValues();
    }

    private void removePendentandParticipants(Claim claim){
        PendingService pendingService = new PendingService();
        Pending pending = pendingService.getPendingClaim(claim.getClaim());
        if(pending != null){
            pendingService.removePending(pending);
        }
        String participants = claim.getParticipants();
        
        if(!participants.equals("nulo")){
            WorldGuardService worldGuardService = new WorldGuardService();
            String[] participantList = participants.split(",");
            for (int i = 0; i < participantList.length; i++) {
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(participantList[i]);
                worldGuardService.RemoveRegion(claim.getClaim(),offlinePlayer.getPlayer());
            }
        } 
    }

    public void saveClaim(){
        claim.save();
    }
}
