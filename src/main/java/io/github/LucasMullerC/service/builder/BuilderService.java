package io.github.LucasMullerC.service.builder;

import java.io.File;

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

    public boolean buildBuilder(String uuid, String discord){
        if(builder.getValues().isEmpty() == true){
            Builder builder = new Builder(uuid);
            builder.setDiscord(discord);
            builder.setAwards("nulo");
            builder.setBuilds(0);
            builder.setPoints(0);
            builder.setTier(1);
            builder.setFeatured("nulo");
            this.builder.add(builder);
            this.builder.save();
            return true;
        } else{
            Builder builderBase = getBuilderUuid(uuid);
            if(builderBase == null){
                Builder builder = new Builder(uuid);
                builder.setDiscord(discord);
                builder.setAwards("nulo");
                builder.setBuilds(0);
                builder.setPoints(0);
                builder.setTier(1);
                builder.setFeatured("nulo");
                this.builder.add(builder);
                this.builder.save();
                return true;
            } else if(builderBase.getDiscord().equals("nulo") || builderBase.getDiscord().equals("null")){
                builderBase.setDiscord(discord);
                this.builder.save();
                return true;
            }
        }
        return false;
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