package io.github.LucasMullerC.service.builder;

import de.schlichtherle.io.File;
import io.github.LucasMullerC.BTEBrasilSystem.BTEBrasilSystem;
import io.github.LucasMullerC.model.Builder;
import io.github.LucasMullerC.util.ListUtil;

public class BuilderService {
    public ListUtil<Builder> builder;
    
    public BuilderService() {
        BTEBrasilSystem plugin = BTEBrasilSystem.getPlugin();
        String pluginFolder = plugin.getDataFolder().getAbsolutePath();
        (new File(pluginFolder)).mkdirs();
        this.builder = new ListUtil<Builder>(new File(pluginFolder + File.separator + "builders.txt"));
        builder.load(Builder.class);
    }

    public void addBuilder(Builder builder){
        Builder foundBuilder = getBuilderUuid(builder.getUUID());
        if(foundBuilder == null){
            this.builder.add(builder);
            this.builder.save();
        }
    }

    public void removeBuilder(Builder builder){
        this.builder.remove(builder);
        this.builder.save();
    }

    public void updateClaim(Builder builder){
       Builder oldBuilder = getBuilderUuid(builder.getUUID());
       if(oldBuilder != null){
        oldBuilder = builder;
        this.builder.save();
       }
    }

    public Builder getBuilderUuid(String uuid){
        for (Builder builder : builder.getValues()) {
            if (builder.getUUID() != null && builder.getUUID().contains(uuid)) {
                return builder;
            }
        }
        return null;
    }

    public Builder getBuilderDiscord(String discord){
        for (Builder builder : builder.getValues()) {
            if (builder.getDiscord() != null && builder.getDiscord().contains(discord)) {
                return builder;
            }
        }
        return null;
    }
}