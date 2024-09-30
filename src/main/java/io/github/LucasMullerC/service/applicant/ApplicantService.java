package io.github.LucasMullerC.service.applicant;

import java.io.File;
import java.util.ArrayList;

import io.github.LucasMullerC.BTEBrasilSystem.BTEBrasilSystem;
import io.github.LucasMullerC.model.Applicant;
import io.github.LucasMullerC.util.ListUtil;

public class ApplicantService {
    public ListUtil<Applicant> applicant;

    public ApplicantService(){
        BTEBrasilSystem plugin = BTEBrasilSystem.getPlugin();
        String pluginFolder = plugin.getDataFolder().getAbsolutePath();
        (new File(pluginFolder)).mkdirs();
        this.applicant = new ListUtil<Applicant>(new File(pluginFolder + File.separator + "applicant.txt"));
        applicant.load(Applicant.class);
    }

    public void addApplicant(Applicant applicant){
        this.applicant.add(applicant);
        this.applicant.save();
    }

    public void removeApplicant(Applicant applicant){
        this.applicant.remove(applicant);
        this.applicant.save();
    }

    public void updateApplicant(Applicant applicant){
        Applicant oldApplicant = getApplicant(applicant.getUUID());
        if(oldApplicant != null){
            oldApplicant = applicant;
            this.applicant.save();
        }
    }

    public Applicant getApplicant(String uuid) {
        for (Applicant applicant : applicant.getValues()) {
            if (applicant.getUUID() != null && applicant.getUUID().contains(uuid)) {
                return applicant;
            }
        }
        return null;
    }

    public void saveApplicant(){
        applicant.save();
    }

    public ArrayList<Applicant> getApplicantList(){
        return this.applicant.getValues();
    }
    
}
