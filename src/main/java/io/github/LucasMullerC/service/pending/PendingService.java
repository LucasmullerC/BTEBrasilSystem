package io.github.LucasMullerC.service.pending;

import java.io.File;
import java.util.ArrayList;

import io.github.LucasMullerC.BTEBrasilSystem.BTEBrasilSystem;
import io.github.LucasMullerC.model.Pending;
import io.github.LucasMullerC.util.ListUtil;

public class PendingService {
    public ListUtil<Pending> pending;
    
    public PendingService() {
        BTEBrasilSystem plugin = BTEBrasilSystem.getPlugin();
        String pluginFolder = plugin.getDataFolder().getAbsolutePath();
        (new File(pluginFolder)).mkdirs();
        this.pending = new ListUtil<Pending>(new File(pluginFolder + File.separator + "pendente.txt"));
        pending.load(Pending.class);
    }
    
    public void addPending(Pending pending){
        this.pending.add(pending);
        this.pending.save();
    }

    public void removePending(Pending pending){
        this.pending.remove(pending);
        this.pending.save();
    }

    public void updatePending(Pending pending) {
        Pending oldPending = null;
        if (pending.getisApplication()) {
            oldPending = getPendingApplication(pending.getUUID());
        } else {
            oldPending = getPendingClaim(pending.getregionId());
        }
        if (oldPending != null) {
            oldPending = pending;
            this.pending.save();
        }
    }

    public int getTotalPendingCount(boolean isApplication) {
        int count = 0;
        for (Pending pending : pending.getValues()) {
            if(isApplication){
                if (pending.getisApplication() == true) {
                    count++;
                }
                else{
                    if (pending.getisApplication() == false) {
                        count++;
                    }
                }
            }
        }
        return count;
    }

    public ArrayList<Pending> getPendingPlayer(String UUID){
        ArrayList<Pending> pendingPlayer = new ArrayList<>();
        for (Pending pending : pending.getValues()) {
            if (pending.getUUID() != null && pending.getUUID().contains(UUID)) {
                if (pending.getisApplication() == false) {
                    pendingPlayer.add(pending);
                }
            }
        }
        return pendingPlayer;
    }

    public Pending getPendingApplication(String UUID) {
        for (Pending pending : pending.getValues()) {
            if (pending.getUUID() != null && pending.getUUID().contains(UUID)) {
                if (pending.getisApplication() == true) {
                    return pending;
                }
            }
        }
        return null;
    }

    public Pending getPendingClaim(String claimId) {
        for (Pending pending : pending.getValues()) {
            if (pending.getregionId() != null && pending.getregionId().contains(claimId)) {
                if (pending.getisApplication() == false) {
                    return pending;
                }
            }
        }
        return null;
    }

    public Pending getNextPendingClaim(String UUID){
        for (Pending pending : pending.getValues()) {
            if (pending.getUUID() != null && !pending.getUUID().contains(UUID)) {
                if (pending.getisApplication() == false) {
                    return pending;
                }
            }
        }
        return null;
    }


}
