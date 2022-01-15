package io.github.LucasMullerC.Util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import io.github.LucasMullerC.Objetos.Builders;

public class ListaBuilders {
    private File storageFile;
	private ArrayList<Builders> values;
	private Builders G;

    public ListaBuilders(File file) {
		this.storageFile = file;
		this.values = new ArrayList<Builders>();
		if (this.storageFile.exists() == false) {
			try {
				this.storageFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void load() {
		try {
			DataInputStream input = new DataInputStream(new FileInputStream(this.storageFile));
			BufferedReader reader = new BufferedReader(new InputStreamReader(input));
			String line;
			while ((line = reader.readLine()) != null) {
				if (this.Contains(new Builders(line)) == false) {
                    String[] Oarea = line.split(";");
                    G = new Builders(Oarea[0]);
                    G.setDiscord(Oarea[1]);
                    G.setTier(Integer.parseInt(Oarea[2]));
                    G.setPontos(Double.parseDouble(Oarea[3]));
                    G.setBuilds(Integer.parseInt(Oarea[4]));
                    G.setAwards(Oarea[5]);
					G.setDestaque(Oarea[6]);
					values.add(G);
				}
			}
			reader.close();
			input.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void save() {
		try {
			FileWriter stream = new FileWriter(this.storageFile);
			BufferedWriter out = new BufferedWriter(stream);
			for (Builders value : this.values) {
                out.write(value.getUUID()+";"+value.getDiscord()+";"+value.getTier().toString()+";"+String.valueOf(value.getPontos())+";"+value.getBuilds().toString()+";"+value.getAwards()+";"+value.getDestaque());
                out.newLine();
			}
			out.close();
			stream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean Contains(Builders value) {
		return this.values.contains(value);
	}

	public void add(Builders value) {
		if (this.Contains(value) == false) {
			this.values.add(value);
		}
	}

	public void remove(Builders value) {
		this.values.remove(value);
	}

	public ArrayList<Builders> getValues() {
		return this.values;
	}

}
