package io.github.LucasMullerC.DiscordBot.comandos;

import java.util.List;

import github.scarsz.discordsrv.dependencies.jda.api.entities.Member;
import github.scarsz.discordsrv.dependencies.jda.api.entities.Message;
import github.scarsz.discordsrv.dependencies.jda.api.entities.MessageChannel;
import github.scarsz.discordsrv.dependencies.jda.api.entities.MessageEmbed;
import github.scarsz.discordsrv.dependencies.jda.api.entities.Role;
import github.scarsz.discordsrv.dependencies.jda.api.entities.User;
import github.scarsz.discordsrv.dependencies.jda.api.entities.MessageEmbed.Footer;
import github.scarsz.discordsrv.dependencies.jda.api.entities.MessageEmbed.ImageInfo;
import github.scarsz.discordsrv.dependencies.jda.api.entities.MessageEmbed.Thumbnail;
import github.scarsz.discordsrv.util.DiscordUtil;
import io.github.LucasMullerC.BTEBrasilSystem.GerenciarListas;
import io.github.LucasMullerC.BTEBrasilSystem.Sistemas;
import io.github.LucasMullerC.Objetos.Builders;
import io.github.LucasMullerC.Objetos.Conquistas;
import io.github.LucasMullerC.Util.Mensagens;

public class DiscordPerfil {

    public DiscordPerfil(Message msg) {
        MessageChannel channel = msg.getChannel();
        List<User> Ulist = msg.getMentionedUsers();
        String[] cmd = msg.getContentRaw().split("\\s+");
        Builders B = null;
        User u = null;
        if(Ulist.size() >0){
            u = Ulist.get(0);
            B = GerenciarListas.getBuilderDiscord(u.getId());
        }
        else if(cmd.length > 1){
            u = DiscordUtil.getUserById(cmd[1]);
            if(u != null){
                B = GerenciarListas.getBuilderDiscord(cmd[1]);
            }
        }
        else{
            u = msg.getAuthor();
            B = GerenciarListas.getBuilderDiscord(msg.getAuthor().getId());
        }
        if(B == null){
            channel.sendMessage(Mensagens.PerfilNotBuilderDiscord).queue();
        }
        else{
            Member M = msg.getMember();
            List<Role> roles = M.getRoles();
            Boolean Vef = false;
            for(int i = 0;i<roles.size();i++){
                Role r =roles.get(i);
                if(r.getId().equals("716735505840209950")){
                    Vef = true;
                    break;
                }
            }
        String qtdAreas = String.valueOf(GerenciarListas.getAreaQtdByPlayerNum(B.getUUID()));
        String qtdCompletos = String.valueOf(GerenciarListas.getAreaCompletaQtdByPlayerNum(B.getUUID()));
        String NextLvl = Sistemas.ForNextLvl(B.getUUID(), Vef);

        Thumbnail thumb = new Thumbnail(u.getAvatarUrl(), null, 100, 100); // thumb
        Footer ft = new Footer("!conquistas Para ver todas suas conquistas.", null, null);
        ImageInfo img = null;
        Boolean desc = false;
        if(!B.getDestaque().equals("nulo")){
            Conquistas C = GerenciarListas.getConquistaPos(B.getDestaque());
            img = new ImageInfo(C.getURL(), null, 105, 30);
            desc = true;
        }
        String pts = String.valueOf(B.getPontos());
        pts = pts.substring(0, pts.indexOf(".")+2);
        MessageEmbed emb2 = new MessageEmbed(null, Mensagens.PerfilDiscordTitle(u.getName()),
                Mensagens.PerfilDiscordBody(String.valueOf(B.getTier()), String.valueOf(B.getBuilds()), qtdAreas,
                        qtdCompletos, pts,desc,NextLvl),
                null, null, 52224, thumb, null, null, null, ft,
                img, null);

        channel.sendMessage(emb2).queue();
        }
    }
}
