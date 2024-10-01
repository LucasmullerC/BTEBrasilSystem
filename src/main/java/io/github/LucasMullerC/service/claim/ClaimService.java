package io.github.LucasMullerC.service.claim;

import java.io.File;

import org.bukkit.entity.Player;
import java.util.ArrayList;

import io.github.LucasMullerC.BTEBrasilSystem.BTEBrasilSystem;
import io.github.LucasMullerC.model.Claim;
import io.github.LucasMullerC.service.WorldGuardService;
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

    public ArrayList<Claim> getClaimList(){
        return claim.getValues();
    }
}
