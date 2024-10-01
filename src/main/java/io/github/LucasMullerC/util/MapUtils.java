package io.github.LucasMullerC.util;

import java.io.File;
import io.github.LucasMullerC.model.Claim;
import io.github.LucasMullerC.service.claim.ClaimService;
import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.io.*;

public class MapUtils {
    private File storageFile;
    private ArrayList<Claim> values;

    public MapUtils(File file) {
        this.storageFile = file;
        ClaimService claimService = new ClaimService();
        this.values = claimService.getClaimList();
        if (!this.storageFile.exists()) {
            try {
                this.storageFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void save() {
        String stats = "T";

        try {
            FileWriter stream = new FileWriter(this.storageFile);
            BufferedWriter out = new BufferedWriter(stream);

            String[] doc = getDocument().split(";");
            for (int i = 0; i < doc.length; i++) {
                out.write(doc[i]);
                out.newLine();
            }
            do {
                out.write("<Folder>");
                out.newLine();
                if (stats.equals("T")) {
                    out.write("<name>Claims em Construção</name>");
                    out.newLine();
                    stats = "F";
                } else {
                    out.write("<name>Claims Completos</name>");
                    out.newLine();
                    stats = "T";
                }
                for (Claim value : this.values) {
                    if (value.getStatus().equals(stats)) {
                        out.write("<Placemark>");
                        out.newLine();
                        out.write("<name>" + value.getName() + "</name>");
                        out.newLine();
                        String parts = getParticipants(value);
                        String estado = "";
                        if (value.getStatus().equals("T")) {
                            out.write("<styleUrl>#poly-0F9D58-2401-128-nodesc</styleUrl>");
                            estado = "Contruído ";
                        } else {
                            out.write("<styleUrl>#poly-0288D1-2601-112-nodesc</styleUrl>");
                            estado = "Em Contrução ";
                        }
                        out.newLine();
                        String img = getImgs(value);
                        out.write("<description><![CDATA[" + img + " ID: " + value.getClaim() + "\r\n Construções: "
                                + value.getBuilds() + "\r\n "
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
                        String pontos = getCoordinates(value);
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
                }
                out.write("</Folder>");
            } while (stats.equals("F"));
            out.write("</Document>");
            out.newLine();
            out.write("</kml>");
            out.close();
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getImgs(Claim claim) {
        if (claim.getImage().equals("nulo")) {
            return "";
        } else {
            String[] Parts = claim.getImage().split(",");
            String imgs = "nulo";
            for (int i = 0; i < Parts.length; i++) {
                if (i == 0) {
                    imgs = "<img src='" + Parts[i] + " height='200' width='auto' />";
                } else {
                    imgs += "<br><img src='" + Parts[i] + " height='200' width='auto' />";
                }
            }
            return imgs;
        }
    }

    private static String getParticipants(Claim claim) {
        if (claim.getParticipants().equals("nulo")) {
            OfflinePlayer pt = Bukkit.getOfflinePlayer(UUID.fromString(claim.getPlayer()));
            return pt.getName();
        } else {
            OfflinePlayer lider = Bukkit.getOfflinePlayer(UUID.fromString(claim.getPlayer()));
            String[] Parts = claim.getParticipants().split(",");
            String Participants = lider.getName();
            for (int i = 0; i < Parts.length; i++) {
                OfflinePlayer pt = Bukkit.getOfflinePlayer(UUID.fromString(Parts[i]));
                Participants += "," + pt.getName();
            }
            return Participants;
        }
    }

    private static String getCoordinates(Claim claim) {
        String[] ary = claim.getPoints().split(",");
        String pontos = "";
        for (int i = 0; i < (ary.length - 1); i += 2) {
            double[] coords = RegionUtils.toGeo(Integer.parseInt(ary[i].split("\\.")[0]),
                    Integer.parseInt(ary[i + 1].split("\\.")[0]));
            pontos += String.valueOf(coords[0]) + "," + String.valueOf(coords[1]) + ",0 ";
        }
        return pontos;
    }

    private static String getDocument() {
        return "<?xml version='1.0' encoding='UTF-8'?>;<kml xmlns='http://www.opengis.net/kml/2.2'>;<Document>;<name>Claims</name>;<Style id='poly-0288D1-2601-112-nodesc-normal'>;<LineStyle> <color>ffd18802</color>;<width>2.601</width>;</LineStyle>;<PolyStyle>;<color>70d18802</color>;<fill>1</fill>;<outline>1</outline>;</PolyStyle>;<BalloonStyle>;<text><![CDATA[<h3>$[name]</h3>]]></text>;</BalloonStyle>;</Style>;<Style id='poly-0288D1-2601-112-nodesc-highlight'>;<LineStyle>;<color>ffd18802</color>;<width>3.9015</width>;</LineStyle>;<PolyStyle>;<color>70d18802</color>;<fill>1</fill>;<outline>1</outline>;</PolyStyle>;<BalloonStyle>;<text><![CDATA[<h3>$[name]</h3>]]></text>;</BalloonStyle>;</Style>;<StyleMap id='poly-0288D1-2601-112-nodesc'>;<Pair>;<key>normal</key>;<styleUrl>#poly-0288D1-2601-112-nodesc-normal</styleUrl>;</Pair>;<Pair>;<key>highlight</key>;<styleUrl>#poly-0288D1-2601-112-nodesc-highlight</styleUrl>;</Pair>;</StyleMap>;<Style id='poly-0F9D58-2401-128-nodesc-normal'>;<LineStyle>;<color>ff589d0f</color>;<width>2.401</width>;</LineStyle>;<PolyStyle>;<color>80589d0f</color>;<fill>1</fill>;<outline>1</outline>;</PolyStyle>;<BalloonStyle>;<text><![CDATA[<h3>$[name]</h3>]]></text>;</BalloonStyle>;</Style>;<Style id='poly-0F9D58-2401-128-nodesc-highlight'>;<LineStyle>;<color>ff589d0f</color>;<width>3.6015</width>;</LineStyle>;<PolyStyle>;<color>80589d0f</color>;<fill>1</fill>;<outline>1</outline>;</PolyStyle>;<BalloonStyle>;<text><![CDATA[<h3>$[name]</h3>]]></text>;</BalloonStyle>;</Style>;<StyleMap id='poly-0F9D58-2401-128-nodesc'>;<Pair>;<key>normal</key>;<styleUrl>#poly-0F9D58-2401-128-nodesc-normal</styleUrl>;</Pair>;<Pair>;<key>highlight</key>;<styleUrl>#poly-0F9D58-2401-128-nodesc-highlight</styleUrl>;</Pair>;</StyleMap>";
    }
}
