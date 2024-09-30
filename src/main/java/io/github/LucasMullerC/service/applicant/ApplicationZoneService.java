package io.github.LucasMullerC.service.applicant;

import java.io.File;
import java.util.ArrayList;

import io.github.LucasMullerC.BTEBrasilSystem.BTEBrasilSystem;
import io.github.LucasMullerC.model.ApplicationZone;
import io.github.LucasMullerC.util.ListUtil;

public class ApplicationZoneService {
    public ListUtil<ApplicationZone> applicationZone;

    public ApplicationZoneService(){
        BTEBrasilSystem plugin = BTEBrasilSystem.getPlugin();
        String pluginFolder = plugin.getDataFolder().getAbsolutePath();
        (new File(pluginFolder)).mkdirs();
        this.applicationZone = new ListUtil<ApplicationZone>(new File(pluginFolder + File.separator + "applicationZones.txt"));
        this.applicationZone.load(ApplicationZone.class);
    }

    public void addAppicationZone(ApplicationZone applicationZone){
        this.applicationZone.add(applicationZone);
        this.applicationZone.save();
    }

    public void removeApplicationZone(ApplicationZone applicationZone){
        this.applicationZone.remove(applicationZone);
        this.applicationZone.save();
    }

    public void updateApplicationZone(ApplicationZone applicationZone){
        ApplicationZone oldApplicationZone = getApplicationZone(applicationZone.getApplicationZone());
        if(oldApplicationZone != null){
            oldApplicationZone = applicationZone;
            this.applicationZone.save();
        }
    }

    public ApplicationZone getApplicationZone(String applicationZoneId){
        for (ApplicationZone applicationZone : applicationZone.getValues()) {
            if (applicationZone.getApplicationZone() != null && applicationZone.getApplicationZone().contains(applicationZoneId)) {
                return applicationZone;
            }
        }
        return null;
    }

    public Integer findAvailableZone(){
        ArrayList<ApplicationZone> applicationZoneList = applicationZone.getValues();
        String cont;
        Integer result;
        for (int i = 0; i < applicationZoneList.size(); i++) {
            result = i + 1;
            cont = result.toString();
            if (getApplicationZone(cont) == null) {
                return result;
            }
        }
        return 0;
    }

    public void saveApplicationZone(){
        applicationZone.save();
    }

    public boolean isEmpty(){
        return applicationZone.getValues().isEmpty();
    }

    public Integer getSize(){
        return applicationZone.getValues().size();
    }
    
}
