package io.github.LucasMullerC.service.claim;

import java.sql.Connection;
import java.sql.SQLException;

import org.bukkit.entity.Player;

import io.github.LucasMullerC.service.WorldGuardService;
import io.github.LucasMullerC.service.data.DatabaseService;
import io.github.LucasMullerC.util.DatabaseConnection;

public class ClaimService {
    
    public void addClaim(String name, String id, String points, Player player) throws SQLException{
        String[]claim = {id,name,points,player.getUniqueId().toString(),"","F","","0"};
        Connection conn = DatabaseConnection.getConnection();
        DatabaseService databaseService = new DatabaseService();
        databaseService.addRecord(conn, "claim", claim);
        WorldGuardService worldGuardService = new WorldGuardService();
        worldGuardService.AddRegion(points, id, player);
    }

    public void removeClaim(String claimId,Player player) throws SQLException{
        Connection conn = DatabaseConnection.getConnection();
        DatabaseService databaseService = new DatabaseService();
        databaseService.removeRecord(conn, "claims", "claim = '" +claimId + "'");
        databaseService.removeRecord(conn, "pending", "regionId = '" +claimId + "'");
        WorldGuardService worldGuardService = new WorldGuardService();
        worldGuardService.RemoveRegion(claimId, player);
    }
}
