package io.github.LucasMullerC.service.applicant;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import io.github.LucasMullerC.discord.DiscordActions;
import io.github.LucasMullerC.model.Applicant;
import io.github.LucasMullerC.model.ApplicationZone;
import io.github.LucasMullerC.model.Pending;
import io.github.LucasMullerC.service.WorldGuardService;
import io.github.LucasMullerC.service.pending.PendingService;
import io.github.LucasMullerC.util.MessageUtils;

public class DeadlineService {
    Player player;

    public DeadlineService(Player player) {
        this.player = player;
        deleteDeadlines();
    }

    private void deleteDeadlines(){
        ArrayList<Applicant> applicantList = new ArrayList<Applicant>();
        ApplicantService applicantService = new ApplicantService();
        ApplicationZoneService applicationZoneService = new ApplicationZoneService();
        WorldGuardService worldGuardService = new WorldGuardService();
        PendingService pendingService = new PendingService();
        applicantList = applicantService.getApplicantList();
        String date = getDate();
        Integer cont = 0;
        for(Applicant applicant: applicantList){
            if(applicant.getDeadLine() != null && applicant.getDeadLine().contains(date)){
                if (Bukkit.getOnlinePlayers().size() != 0) {
                    ApplicationZone applicationZone = applicationZoneService.getApplicationZone(applicant.getgetZone());
                    Pending pending = pendingService.getPendingApplication(applicant.getUUID());
                    worldGuardService.RemoveRegion("apply" + applicant.getgetZone() + "d", player);
                    worldGuardService.RemoveRegion("apply" + applicant.getgetZone() + "c", player);
                    worldGuardService.RemoveRegion("apply" + applicant.getgetZone() + "b", player);
                    worldGuardService.RemoveRegion("apply" + applicant.getgetZone() + "a", player);

                    applicantService.removeApplicant(applicant);
                    applicationZoneService.removeApplicationZone(applicationZone);

                    if(pending != null){
                        pendingService.removePending(pending);
                    }
                    cont++;
                }
            }
        }
        if (cont > 0) {
            String logMessage = cont.toString() + MessageUtils.getMessageConsole("TimesUpAdm");
            DiscordActions.sendLogMessage(logMessage);
        }
    }

    private String getDate(){
        LocalDate deadline = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd LLLL yyyy");
        String date = deadline.format(formatter);

        return date;
    }
    
}
