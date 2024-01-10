package io.github.LucasMullerC.service.claim;

import java.util.List;

import org.bukkit.entity.Player;

import io.github.LucasMullerC.model.Claim;
import io.github.LucasMullerC.service.WorldGuardService;
import io.github.LucasMullerC.util.DatabaseUtils;

public class ClaimService {

    public void addClaim(Claim claim, Player player) {
        String[] values = { claim.getClaim(), claim.getName(), claim.getPoints(), claim.getPlayer(), claim.getImage(),
                claim.getStatus(), claim.getParticipants(), claim.getBuilds().toString() };
        DatabaseUtils.addToDatabase(values, "claims");
        WorldGuardService worldGuardService = new WorldGuardService();
        worldGuardService.AddRegion(claim.getPoints(), claim.getClaim(), player);
    }

    public void removeClaim(String claimId, Player player) {
        DatabaseUtils.removeFromDatabase("claims", "claim = '" + claimId + "'");
        DatabaseUtils.removeFromDatabase("pending", "regionId = '" + claimId + "'");
        WorldGuardService worldGuardService = new WorldGuardService();
        worldGuardService.RemoveRegion(claimId, player);
    }

    public Claim getClaim(String claimId, String condition) {
        List<String[]> response = DatabaseUtils.getFromDatabase("claims WHERE claim = '" + claimId + "'", condition);
        return stringToClaim(response.get(0));
    }

    private Claim stringToClaim(String[] values) {
        if (values.length == 8) {
            Claim claim = new Claim(values[0]);
            claim.setName(values[1]);
            claim.setPoints(values[2]);
            claim.setPlayer(values[3]);
            claim.setImage(values[4]);
            claim.setStatus(values[5]);
            claim.setParticipants(values[6]);
            claim.setBuilds(Integer.parseInt(values[7]));

            return claim;
        } else {
            return null;
        }
    }
}
