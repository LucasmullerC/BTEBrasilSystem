package io.github.LucasMullerC.service.builder;

import java.util.List;

import io.github.LucasMullerC.model.Builder;
import io.github.LucasMullerC.util.DatabaseUtils;

public class BuilderService {
    
    public void addBuilder(Builder builder){
        String[] values = { builder.getUUID(),builder.getDiscord(),builder.getTier().toString(),Double.toString(builder.getPoints()),builder.getBuilds().toString(),builder.getAwards(),builder.getFeatured()};
        DatabaseUtils.addToDatabase(values, "builders");
    }

    public void removeBuilder(String uuid){
        DatabaseUtils.removeFromDatabase("builders", "UUID = '" + uuid + "'");
    }

    public void getBuilder(String uuid, String condition){
        List<String[]> response = DatabaseUtils.getFromDatabase("builders WHERE UUID = '" + uuid + "'", condition);
        stringToBuilder(response.get(0));
    }

    private Builder stringToBuilder(String[] values) {
        if (values.length == 4) {
            Builder builder = new Builder(values[0]);
            builder.setDiscord(values[1]);
            builder.setTier(Integer.parseInt(values[2]));
            builder.setPoints(Double.parseDouble(values[3]));
            builder.setBuilds(Integer.parseInt(values[4]));
            builder.setAwards(values[5]);
            builder.setFeatured(values[6]);
            return builder;
        } else {
            return null;
        }
    }
}
