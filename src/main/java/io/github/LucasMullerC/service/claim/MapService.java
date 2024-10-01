package io.github.LucasMullerC.service.claim;

import java.io.File;
import io.github.LucasMullerC.BTEBrasilSystem.BTEBrasilSystem;
import io.github.LucasMullerC.util.MapUtils;

public class MapService {
    public MapUtils map;

    public MapService(){
        BTEBrasilSystem plugin = BTEBrasilSystem.getPlugin();
        String pluginFolder = plugin.getDataFolder().getAbsolutePath();
        (new File(pluginFolder)).mkdirs();
        this.map = new MapUtils(new File(pluginFolder + File.separator + "claims.kml"));
    }

    public void generateMap(){
        map.save();
    }

    
}
