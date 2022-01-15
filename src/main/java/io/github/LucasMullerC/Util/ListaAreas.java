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

import io.github.LucasMullerC.Objetos.Areas;

public class ListaAreas {
    private File storageFile;
	private ArrayList<Areas> values;
	private Areas G;

    public ListaAreas(File file) {
		this.storageFile = file;
		this.values = new ArrayList<Areas>();
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
				if (this.Contains(new Areas(line)) == false) {
                    String[] Oarea = line.split(";");
                    G = new Areas(Oarea[0]);
                    G.setNome(Oarea[1]);
                    G.setPontos(Oarea[2]);
                    G.setPlayer(Oarea[3]);
                    G.setImgs(Oarea[4]);
                    G.setStatus(Oarea[5]);
					G.setParticipantes(Oarea[6]);
					G.setBuilds(Integer.parseInt(Oarea[7]));
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
			for (Areas value : this.values) {
                out.write(value.getClaim()+";"+value.getNome()+";"+value.getPontos()+";"+value.getPlayer()+";"+value.getImgs()+";"+value.getStatus()+";"+value.getParticipantes()+";"+value.getBuilds().toString());
                out.newLine();
			}
			out.close();
			stream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean Contains(Areas value) {
		return this.values.contains(value);
	}

	public void add(Areas value) {
		if (this.Contains(value) == false) {
			this.values.add(value);
		}
	}

	public void remove(Areas value) {
		this.values.remove(value);
	}

	public ArrayList<Areas> getValues() {
		return this.values;
	}

}