package io.github.LucasMullerC.service;

import java.io.File;

import io.github.LucasMullerC.BTEBrasilSystem.BTEBrasilSystem;
import io.github.LucasMullerC.model.Awards;
import io.github.LucasMullerC.util.ListUtil;

public class AwardService {
    public ListUtil<Awards> awards; 

    public AwardService(){
        BTEBrasilSystem plugin = BTEBrasilSystem.getPlugin();
        String pluginFolder = plugin.getDataFolder().getAbsolutePath();
        (new File(pluginFolder)).mkdirs();
        this.awards = new ListUtil<Awards>(new File(pluginFolder + File.separator + "conquistas.txt"));
        awards.load(Awards.class);
    }

    public Awards getAward(String awardId){
        for (Awards award : awards.getValues()) {
            if (award.getID() != null && award.getID().contains(awardId)) {
                return award;
            }
        }
        return null;
    }
}
