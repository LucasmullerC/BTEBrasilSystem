package io.github.LucasMullerC.Util;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import io.github.LucasMullerC.BTEBrasilSystem.GerenciarListas;
import io.github.LucasMullerC.BTEBrasilSystem.Sistemas;
import io.github.LucasMullerC.Objetos.Areas;
import java.util.ArrayList;
import java.io.File;

public class GerarMapa {
    private File storageFile;
    private ArrayList<Areas> values;

    public GerarMapa(final File file) {
        this.storageFile = file;
        this.values = GerenciarListas.areas.getValues();
        if (!this.storageFile.exists()) {
            try {
                this.storageFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void save() {
        try {
            FileWriter stream = new FileWriter(this.storageFile);
            BufferedWriter out = new BufferedWriter(stream);

            String[] doc = CriarDocumento().split(";");
            for (int i = 0; i < doc.length; i++) {
                out.write(doc[i]);
                out.newLine();
            }
            for (Areas value : this.values) {
                out.write("<Placemark>");
                out.newLine();
                out.write("<name>" + value.getNome() + "</name>");
                out.newLine();
                String parts = getParticipantes(value);
                String estado = "";
                if (value.getStatus().equals("T")) {
                    out.write("<styleUrl>#poly-0F9D58-2401-128-nodesc</styleUrl>");
                    estado = "Contruído ";
                } else {
                    out.write("<styleUrl>#poly-0288D1-2601-112-nodesc</styleUrl>");
                    estado = "Em Contrução ";
                }
                out.newLine();
                String img=getImgs(value);
                out.write("<description><![CDATA["+img+" ID: " + value.getClaim() + "\r\n Construções: " + value.getBuilds() + "\r\n "
                        + estado + " por: " + parts + "]]></description>");
                out.newLine();
                out.write("<Polygon>");
                out.newLine();
                out.write("<tessellate>1</tessellate>");
                out.newLine();
                out.write("<outerBoundaryIs>");
                out.newLine();
                out.write("<LinearRing>");
                out.newLine();
                out.write("<coordinates>");
                out.newLine();
                String pontos = GerarCoordenadas(value);
                out.write(pontos);
                out.newLine();
                out.write("</coordinates>");
                out.newLine();
                out.write("</LinearRing>");
                out.newLine();
                out.write("</outerBoundaryIs>");
                out.newLine();
                out.write("</Polygon>");
                out.newLine();
                out.write("</Placemark>");
                out.newLine();
            }
            out.write("</Document>");
            out.newLine();
            out.write("</kml>");
            out.close();
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static String getImgs(Areas A){
        if (A.getImgs().equals("nulo")) {
            return "";
        }
        else{
            String[] Parts = A.getImgs().split(",");
            String imgs = "nulo";
            for (int i = 0; i < Parts.length; i++) {
                if(i == 0){
                    imgs = "<img src='"+Parts[i]+" height='200' width='auto' />";
                }
                else{
                    imgs += "<br><img src='"+Parts[i]+" height='200' width='auto' />";
                }
            }
            return imgs;
        }
    }
    private static String getParticipantes(Areas A) {
        if (A.getParticipantes().equals("nulo")) {
            OfflinePlayer pt = Bukkit.getOfflinePlayer(UUID.fromString(A.getPlayer()));
            return pt.getName();
        }
        else{
            OfflinePlayer lider = Bukkit.getOfflinePlayer(UUID.fromString(A.getPlayer()));
            String[] Parts = A.getParticipantes().split(",");
            String Participantes = lider.getName();
            for (int i = 0; i < Parts.length; i++) {
                OfflinePlayer pt = Bukkit.getOfflinePlayer(UUID.fromString(Parts[i]));
                Participantes += "," + pt.getName();
            }
            return Participantes;
        }
    }

    private static String GerarCoordenadas(Areas A) {
        String[] ary = A.getPontos().split(",");
        String pontos = "";
        for (int i = 0; i < (ary.length - 1); i += 2) {
            double[] coords = Sistemas.toGeo(Integer.parseInt(ary[i].split("\\.")[0]),
                    Integer.parseInt(ary[i + 1].split("\\.")[0]));
            pontos += String.valueOf(coords[0]) + "," + String.valueOf(coords[1]) + ",0 ";
        }
        return pontos;
    }

    private static String CriarDocumento() {
        return "<?xml version='1.0' encoding='UTF-8'?>;<kml xmlns='http://www.opengis.net/kml/2.2'>;<Document>;<name>Claims</name>;<Style id='poly-0288D1-2601-112-nodesc-normal'>;<LineStyle> <color>ffd18802</color>;<width>2.601</width>;</LineStyle>;<PolyStyle>;<color>70d18802</color>;<fill>1</fill>;<outline>1</outline>;</PolyStyle>;<BalloonStyle>;<text><![CDATA[<h3>$[name]</h3>]]></text>;</BalloonStyle>;</Style>;<Style id='poly-0288D1-2601-112-nodesc-highlight'>;<LineStyle>;<color>ffd18802</color>;<width>3.9015</width>;</LineStyle>;<PolyStyle>;<color>70d18802</color>;<fill>1</fill>;<outline>1</outline>;</PolyStyle>;<BalloonStyle>;<text><![CDATA[<h3>$[name]</h3>]]></text>;</BalloonStyle>;</Style>;<StyleMap id='poly-0288D1-2601-112-nodesc'>;<Pair>;<key>normal</key>;<styleUrl>#poly-0288D1-2601-112-nodesc-normal</styleUrl>;</Pair>;<Pair>;<key>highlight</key>;<styleUrl>#poly-0288D1-2601-112-nodesc-highlight</styleUrl>;</Pair>;</StyleMap>;<Style id='poly-0F9D58-2401-128-nodesc-normal'>;<LineStyle>;<color>ff589d0f</color>;<width>2.401</width>;</LineStyle>;<PolyStyle>;<color>80589d0f</color>;<fill>1</fill>;<outline>1</outline>;</PolyStyle>;<BalloonStyle>;<text><![CDATA[<h3>$[name]</h3>]]></text>;</BalloonStyle>;</Style>;<Style id='poly-0F9D58-2401-128-nodesc-highlight'>;<LineStyle>;<color>ff589d0f</color>;<width>3.6015</width>;</LineStyle>;<PolyStyle>;<color>80589d0f</color>;<fill>1</fill>;<outline>1</outline>;</PolyStyle>;<BalloonStyle>;<text><![CDATA[<h3>$[name]</h3>]]></text>;</BalloonStyle>;</Style>;<StyleMap id='poly-0F9D58-2401-128-nodesc'>;<Pair>;<key>normal</key>;<styleUrl>#poly-0F9D58-2401-128-nodesc-normal</styleUrl>;</Pair>;<Pair>;<key>highlight</key>;<styleUrl>#poly-0F9D58-2401-128-nodesc-highlight</styleUrl>;</Pair>;</StyleMap>";
    }
}