package io.github.LucasMullerC.commands;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.jetbrains.annotations.NotNull;

import github.scarsz.discordsrv.DiscordSRV;
import io.github.LucasMullerC.BTEBrasilSystem.BTEBrasilSystem;
import io.github.LucasMullerC.model.Builder;
import io.github.LucasMullerC.model.Claim;
import io.github.LucasMullerC.model.Pending;
import io.github.LucasMullerC.service.MessageService;
import io.github.LucasMullerC.service.builder.BuilderService;
import io.github.LucasMullerC.service.claim.ClaimLimitService;
import io.github.LucasMullerC.service.claim.ClaimPromptService;
import io.github.LucasMullerC.service.claim.ClaimService;
import io.github.LucasMullerC.service.pending.PendingService;
import io.github.LucasMullerC.util.ClaimUtils;
import io.github.LucasMullerC.util.LocationUtil;
import io.github.LucasMullerC.util.MessageUtils;
import io.github.LucasMullerC.util.PendingUtils;
import io.github.LucasMullerC.util.RegionUtils;
import io.github.LucasMullerC.util.ZoneUtils;
import net.kyori.adventure.inventory.Book;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

public class claim implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command arg1, @NotNull String arg2,
            @NotNull String[] arg3) {
            if(arg3.length == 0){
                return false;
            }

            Player player = (Player) sender;

            UUID id = player.getUniqueId();
            String discordId = DiscordSRV.getPlugin().getAccountLinkManager().getDiscordId(id);
            if (discordId == null) {
                player.sendMessage(Component.text(MessageUtils.getMessage("discordnotlinked1", player)).color(NamedTextColor.GOLD));
                player.sendMessage(Component.text(MessageUtils.getMessage("discordnotlinked2", player)).color(NamedTextColor.GOLD));
                return true;
            }

            if(arg3[0].equalsIgnoreCase("iniciar") || arg3[0].equals("start")){
                String name="";
                String difficulty="";

                if (arg3.length == 2) {
                    difficulty = arg3[1];
                }
                if (arg3.length == 3) {
                    difficulty = arg3[1];
                    name = arg3[2];
                }
            
                try {
                    int difficultyValue = Integer.parseInt(difficulty);
            
                    if (difficultyValue < 1 || difficultyValue > 3) {
                        player.sendMessage(MessageUtils.getMessage("joinclaimerror4", player)); 
                        return true; 
                    }

                    BuilderService builderService = new BuilderService();
                    Builder builder = builderService.getBuilderUuid(id.toString());
                    if(builder != null){
                        ClaimService claimService = new ClaimService();
                        ArrayList<Claim> claimList = claimService.getClaimListByPlayer(id.toString());
                        for(Claim claim:claimList){
                            if(claim.getDifficulty()>0 && ClaimUtils.verifyClaimProperties(claim, player, false) == true){
                                player.sendMessage(Component.text(MessageUtils.getMessage("claimrunning", player)).color(NamedTextColor.RED));
                                return true;
                            }
                        }
                    }
            
                    Claim claim = ClaimUtils.chooseClaim(name, difficultyValue, id);

                    if(claim != null){
                        //Build builder to database
                        builderService.buildBuilder(id.toString(), discordId);
    
                        Location areaLocation;
                        areaLocation = ZoneUtils.buildClaimCopy(player, -596, 45, 1061, claim.getClaim());
                        if(areaLocation == null){
                            player.sendMessage(Component.text(MessageUtils.getMessage("joinclaimerror3", player)).color(NamedTextColor.RED));
                            return true;
                        }
                        boolean isCopied = RegionUtils.copyClaim(claim.getClaim(), player, areaLocation);

                        String[] ary = claim.getPoints().split(",");
                        double[] coords = RegionUtils.toGeo(Integer.parseInt(ary[0].split("\\.")[0]),
                        Integer.parseInt(ary[1].split("\\.")[0]));
                        String coordinates = coords[1]+","+coords[0];
    
                        if(isCopied){
                            player.teleport(areaLocation);
                            final String input = MessageUtils.getMessage("joinclaiminfo1", player)+" <a:https://www.google.com.br/maps/place/"+coordinates+">"+coordinates+"</a>";
                            MessageService messageService = new MessageService();
                            player.sendMessage(messageService.getMessageWithURL(input).color(NamedTextColor.GREEN));
                            createBook(player, input, claim);

                            return true;
                        } else{
                            player.sendMessage(Component.text(MessageUtils.getMessage("joinclaimerror1", player)).color(NamedTextColor.RED));
                            return true;
                        }
                    } else{
                        player.sendMessage(Component.text(MessageUtils.getMessage("joinclaimerror1", player)).color(NamedTextColor.RED));
                        return true;
                    }
            
                } catch (NumberFormatException e) {
                    player.sendMessage(MessageUtils.getMessage("joinclaimerror4", player));
                    return true;
                }
            }

            if (player.hasPermission("group.b_br") == false && player.hasPermission("group.builder_not") == false) {
                player.sendMessage(Component.text(MessageUtils.getMessage("notabuilder1", player)).color(NamedTextColor.GOLD));
                player.sendMessage(Component.text(MessageUtils.getMessage("notabuilder2", player)).color(NamedTextColor.GOLD));
                return true;
            }

            BTEBrasilSystem plugin = BTEBrasilSystem.getPlugin();
            //Build builder to database
            BuilderService builderService = new BuilderService();
            builderService.buildBuilder(id.toString(), discordId);

            ConversationFactory cf = new ConversationFactory(plugin);

            //ADD CLAIM
            if(arg3[0].equalsIgnoreCase("add")){
                boolean event = false;
                String awardId = "nulo";
                if(arg3.length == 3){
                    if(player.hasPermission("btebrasil.evento")){
                        if(arg3[1].equals("evento") || arg3[1].equals("event")){
                            event = true;
                            awardId = arg3[2];
                            player.sendMessage(Component.text(MessageUtils.getMessage("eventident", player)).color(NamedTextColor.GREEN));
                        }
                        else{
                            player.sendMessage(Component.text(MessageUtils.getMessage("addeventcommand", player)).color(NamedTextColor.YELLOW));
                            return true;
                        }
                    } else{
                        player.sendMessage(Component.text(MessageUtils.getMessage("Perm1", player)).color(NamedTextColor.RED));
                        return true;
                    }
                }
                Builder builder = builderService.getBuilderUuid(id.toString());
                ClaimLimitService claimLimitService = new ClaimLimitService(player);
                if(claimLimitService.getLimitTier(builder.getTier(), player) == false || player.hasPermission("group.nolimit")){
                    String selectionPoints = claimLimitService.VerifyClaimLimits(player,builder.getTier());
                    if(selectionPoints.length()==1){
                        switch (selectionPoints) {
                            case "1":
                                player.sendMessage(Component.text(MessageUtils.getMessage("AreasIntersec", player)).color(NamedTextColor.RED));
                                player.sendMessage(Component.text(MessageUtils.getMessage("LinkMapa", player)).color(NamedTextColor.GOLD));
                                return true;
                            case "2":
                                player.sendMessage(Component.text(MessageUtils.getMessage("AreasLimite3", player)).color(NamedTextColor.RED));
                                player.sendMessage(Component.text(MessageUtils.getMessage("AreasLimite2", player)).color(NamedTextColor.GOLD));
                                return true;
                            case "3":
                                player.sendMessage(Component.text(MessageUtils.getMessage("AreasSelecao", player)).color(NamedTextColor.RED));
                                return true;
                        }
                    } else{
                        ClaimPromptService claimPromptService = new ClaimPromptService(player, selectionPoints);
                        Conversation conv = cf.withFirstPrompt(claimPromptService.Add).withLocalEcho(true)
                        .buildConversation(player);
                        claimPromptService.setEvent(event, awardId);
                        conv.begin();
                        return true;
                    }
                } else{
                    player.sendMessage(Component.text(MessageUtils.getMessage("AreasLimite3", player)).color(NamedTextColor.RED));
                    player.sendMessage(Component.text(MessageUtils.getMessage("AreasLimite2", player)).color(NamedTextColor.GOLD));
                    return true;
                }

            //REMOVE CLAIM
            } else if (arg3[0].equalsIgnoreCase("abandonar") || arg3[0].equalsIgnoreCase("remover")) {
                Conversation conv = cf.withFirstPrompt(new ClaimPromptService(player, "").remove).withLocalEcho(true)
                .buildConversation(player);
                conv.begin();
                return true;

            //FINISH CLAIM
            } else if (arg3[0].equalsIgnoreCase("completo")) {
                Conversation conv = cf.withFirstPrompt(new ClaimPromptService(player, "").completedStart).withLocalEcho(true)
                .buildConversation(player);
                conv.begin();
                return true;

            //CLAIM TEAM MENU
            } else if (arg3[0].equalsIgnoreCase("equipe")) {
                if(arg3.length <= 1){
                    player.sendMessage(Component.text(MessageUtils.getMessage("ClaimCommand1", player)).color(NamedTextColor.RED));
                    return true;
                }
                if (arg3[1].equalsIgnoreCase("add")) {
                    Conversation conv = cf.withFirstPrompt(new ClaimPromptService(player, "").teamAdd).withLocalEcho(true)
                    .buildConversation(player);
                    conv.begin();
                    return true;
                } else if (arg3[1].equalsIgnoreCase("remover")) {
                    Conversation conv = cf.withFirstPrompt(new ClaimPromptService(player, "").teamRemove).withLocalEcho(true)
                    .buildConversation(player);
                    conv.begin();
                    return true;
                } else if (arg3[1].equalsIgnoreCase("sair")) {
                    Conversation conv = cf.withFirstPrompt(new ClaimPromptService(player, "").teamLeave).withLocalEcho(true)
                    .buildConversation(player);
                    conv.begin();
                    return true;
                } else {
                    player.sendMessage(Component.text(MessageUtils.getMessage("ClaimCommand1", player)).color(NamedTextColor.RED));
                    return true;
                }

            //CLAIM EDIT
            }  else if (arg3[0].equalsIgnoreCase("editar")) {
                Conversation conv = cf.withFirstPrompt(new ClaimPromptService(player, "").claimEdit).withLocalEcho(true)
                .buildConversation(player);
                conv.begin();
                return true;

            //CLAIM IMG MENU   
            } else if (arg3[0].equalsIgnoreCase("img")) {
                if(arg3.length <= 1){
                    player.sendMessage(Component.text(MessageUtils.getMessage("ClaimCommand2", player)).color(NamedTextColor.GOLD));
                    return true;
                }
                if (arg3[1].equalsIgnoreCase("add")) {
                    Conversation conv = cf.withFirstPrompt(new ClaimPromptService(player, "").claimImgAdd).withLocalEcho(true)
                    .buildConversation(player);
                    conv.begin();
                    return true;
                } else if (arg3[1].equalsIgnoreCase("remover")) {
                    Conversation conv = cf.withFirstPrompt(new ClaimPromptService(player, "").claimImgRemove).withLocalEcho(true)
                    .buildConversation(player);
                    conv.begin();
                    return true;
                }

            //CLAIM INFO
            } else if (arg3[0].equalsIgnoreCase("info")) {
                    int pageNum = 1;
                    if(arg3.length > 1){
                        try {
                            pageNum = Integer.valueOf(arg3[1]);
                        } catch (NumberFormatException e) {
                            pageNum = 1;
                        }
                    }
                    Map<String, Object> infos = ClaimUtils.getClaimInfos(player.getUniqueId().toString());
                    int qtdClaim = (int) infos.get("qtdClaim");
                    int qtdCompleted = (int) infos.get("qtdCompleted");
                    @SuppressWarnings("unchecked")
                    ArrayList<Claim> notCompleted = (ArrayList<Claim>) infos.get("notCompleted");
                    @SuppressWarnings("unchecked")
                    ArrayList<Claim> completed = (ArrayList<Claim>) infos.get("completed");

                    PendingService pendingService = new PendingService();
                    ArrayList<Pending> pendingList = pendingService.getPendingPlayer(player.getUniqueId().toString());

                    Component.text("=========").color(NamedTextColor.GOLD);
                    //CLAIMS EM CONSTRUÇÃO - 
                    player.sendMessage(Component.text(MessageUtils.getMessage("claimsunderconstruction", player)).color(NamedTextColor.DARK_BLUE)
                    .append(Component.text(qtdClaim).color(NamedTextColor.GOLD)));
                    ClaimUtils.printClaimsMinecraft(notCompleted,player,pageNum);

                    //CLAIMS EM ANALISE -
                    if(!pendingList.isEmpty()) {
                        player.sendMessage(Component.text(MessageUtils.getMessage("claiminfopending", player)).color(NamedTextColor.YELLOW)
                        .append(Component.text(pendingList.size()).color(NamedTextColor.GOLD)));
                        PendingUtils.printPendingMinecraft(pendingList, player, pageNum);
                    }

                    //CLAIMS COMPLETOS -
                    player.sendMessage(Component.text(MessageUtils.getMessage("claiminfocompleted", player)).color(NamedTextColor.GREEN)
                    .append(Component.text(qtdCompleted).color(NamedTextColor.GOLD)));
                    ClaimUtils.printClaimsMinecraft(completed,player,pageNum);
                    Component.text("=========").color(NamedTextColor.GOLD);
                    return true;
                    
                //EVENT ID
                }  else if (arg3[0].equalsIgnoreCase("evento") || arg3[0].equals("event")) {
                    if(arg3.length == 2){
                        ClaimService claimService = new ClaimService();
                        Claim claim = claimService.getClaim(arg3[1]);
                        if(claim != null){
                            if(claim.isEvent()){
                                ClaimUtils.addParticipant(claim, player, claimService);
                                return true;
                            } else {
                                player.sendMessage(Component.text(MessageUtils.getMessage("ClaimNotFound", player)).color(NamedTextColor.RED));
                                return true;
                            }
                        }
                        else{
                            player.sendMessage(Component.text(MessageUtils.getMessage("ClaimNotFound", player)).color(NamedTextColor.RED));
                            return true;
                        }
                    } else{
                        player.sendMessage(Component.text(MessageUtils.getMessage("eventcommand", player)).color(NamedTextColor.YELLOW));
                        return true;
                    }
                } else if(arg3[0].equalsIgnoreCase("criar") || arg3[0].equals("create")){
                    if(arg3.length == 2){ 
                        if (!player.hasPermission("btebrasil.criar") == false){
                            String stringLevel = arg3[1];
                            int level;
                            switch (stringLevel) {
                                case "1":
                                    level = 1;
                                    break;
                                case "2":
                                    level = 2;
                                    break;
                                case "3":
                                    level = 3;
                                    break;
                                default:
                                    player.sendMessage(Component.text(MessageUtils.getMessage("createcommand", player)).color(NamedTextColor.YELLOW));
                                    return true;
                            }
                            ClaimLimitService claimLimitService = new ClaimLimitService(player);
                            String selectionPoints = claimLimitService.VerifyClaimLimits(player,11);
                            if(selectionPoints.length()==1){
                                    switch (selectionPoints) {
                                        case "1":
                                            player.sendMessage(Component.text(MessageUtils.getMessage("AreasIntersec", player)).color(NamedTextColor.RED));
                                            player.sendMessage(Component.text(MessageUtils.getMessage("LinkMapa", player)).color(NamedTextColor.GOLD));
                                            return true;
                                        case "2":
                                            player.sendMessage(Component.text(MessageUtils.getMessage("AreasLimite3", player)).color(NamedTextColor.RED));
                                            player.sendMessage(Component.text(MessageUtils.getMessage("AreasLimite2", player)).color(NamedTextColor.GOLD));
                                            return true;
                                        case "3":
                                            player.sendMessage(Component.text(MessageUtils.getMessage("AreasSelecao", player)).color(NamedTextColor.RED));
                                            return true;
                                    }
                                }
                                String[] ary = selectionPoints.split(",");
                                double[] coords = RegionUtils.toGeo(Integer.parseInt(ary[0].split("\\.")[0]),
                                Integer.parseInt(ary[1].split("\\.")[0]));
                                String name = LocationUtil.getCityName(coords[1], coords[0]);
                                String claimId = ClaimUtils.buildPlayerClaim(player, name, selectionPoints, level);  

                                player.sendMessage(Component.text(MessageUtils.getMessage("createclaimsuccess", player)+" - "+claimId).color(NamedTextColor.GREEN));

                                return true;
                        } else{
                            player.sendMessage(Component.text(MessageUtils.getMessage("Perm1", player)).color(NamedTextColor.RED));
                            return true;
                        }
                    } else{
                        player.sendMessage(MessageUtils.getMessage("joinclaimerror4", player));
                        return true;
                    }
                }
            return false;
    }

    private void createBook(Player player,String url,Claim claim){
        Component bookTitle = Component.text(MessageUtils.getMessage("joinclaimBookTitle", player));
        Component bookAuthor = Component.text("BTE Brasil");
        List<Component> bookPages = new ArrayList<>();
        MessageService messageService = new MessageService();
    
        Component pageOne = Component.text().append(Component.text(MessageUtils.getMessage("joinclaimbookIntro1", player))
        .append(Component.newline()).append(Component.newline()).append(Component.text(MessageUtils.getMessage("joinclaimbookIntro2", player)+claim.getClaim())
        .append(Component.newline()).append(Component.newline()).append(messageService.getMessageWithURL(url)))).build();

        Component pageTwo = Component.text().append(Component.text(MessageUtils.getMessage("joinclaiminfo2", player))).build();
        Component pageThree = Component.text().append(Component.text(MessageUtils.getMessage("joinclaiminfo3", player))).build();
        Component pageFour = Component.text().append(Component.text(MessageUtils.getMessage("joinclaiminfo4", player))).build();
        Component pageFive = Component.text().append(Component.text(MessageUtils.getMessage("joinclaiminfo5", player))).build();

        Component pageSix = Component.text().append(Component.text(MessageUtils.getMessage("joinclaimbookEnding1", player))
        .append(Component.newline()).append(Component.newline()).append(Component.text(MessageUtils.getMessage("joinclaimbookEnding2", player))
        .append(Component.newline()).append(Component.text(MessageUtils.getMessage("joinclaimbookEnding3", player)))
        .append(Component.newline()).append(Component.text(MessageUtils.getMessage("joinclaimbookEnding4", player))))).build();
    
        bookPages.add(pageOne);
        bookPages.add(pageTwo);
        bookPages.add(pageThree);
        bookPages.add(pageFour);
        bookPages.add(pageFive);
        bookPages.add(pageSix);

        Book myBook = Book.book(bookTitle, bookAuthor, bookPages);
        player.openBook(myBook);
    
        ItemStack bookItem = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta bookMeta = (BookMeta) bookItem.getItemMeta();
    
        if (bookMeta != null) {
            bookMeta.pages(bookPages);
            
            String titleText = PlainTextComponentSerializer.plainText().serialize(bookTitle);
            String authorText = PlainTextComponentSerializer.plainText().serialize(bookAuthor);
    
            bookMeta.setTitle(titleText);
            bookMeta.setAuthor(authorText);
    
            bookItem.setItemMeta(bookMeta);
    
            player.getInventory().addItem(bookItem);
        }
    }
    
}
