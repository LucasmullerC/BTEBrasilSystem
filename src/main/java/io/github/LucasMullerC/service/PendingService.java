package io.github.LucasMullerC.service;

import java.util.List;

import io.github.LucasMullerC.model.Pending;
import io.github.LucasMullerC.util.DatabaseUtils;

public class PendingService {
    
    public void addPending(Pending pending){
        String[] values = { pending.getUUID(),pending.getregionId(),pending.getisApplication().toString(),pending.getbuilds()};
         DatabaseUtils.addToDatabase(values, "pending");
    }

    public void removePending(String regionId){
        DatabaseUtils.removeFromDatabase("pending", "regionId = '" + regionId + "'");
    }

    public void getPending(String regionId, String condition){
        List<String[]> response = DatabaseUtils.getFromDatabase("pending WHERE regionId = '" + regionId + "'", condition);
        stringToPending(response.get(0));
    }

    private Pending stringToPending(String[] values) {
        if (values.length == 4) {
            Pending pending = new Pending(values[0]);
            pending.setregionId(values[1]);
            pending.setisApplication(Boolean.parseBoolean(values[2]));
            pending.setbuilds(values[3]);
            return pending;
        } else {
            return null;
        }
    }
}
