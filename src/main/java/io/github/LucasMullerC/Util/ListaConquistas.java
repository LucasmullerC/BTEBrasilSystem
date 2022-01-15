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

import io.github.LucasMullerC.Objetos.Conquistas;

public class ListaConquistas {
    private File storageFile;
	private ArrayList<Conquistas> values;
	private Conquistas G;

    public ListaConquistas(File file) {
		this.storageFile = file;
		this.values = new ArrayList<Conquistas>();
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
				if (this.Contains(new Conquistas(line)) == false) {
                    String[] Oarea = line.split(";");
                    G = new Conquistas(Oarea[0]);
                    G.setNome(Oarea[1]);
                    G.setURL(Oarea[2]);
                    G.setPontos(Double.parseDouble(Oarea[3]));
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
			for (Conquistas value : this.values) {
                out.write(value.getID()+";"+value.getNome()+";"+value.getURL().toString()+";"+String.valueOf(value.getPontos()));
                out.newLine();
			}
			out.close();
			stream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean Contains(Conquistas value) {
		return this.values.contains(value);
	}

	public void add(Conquistas value) {
		if (this.Contains(value) == false) {
			this.values.add(value);
		}
	}

	public void remove(Conquistas value) {
		this.values.remove(value);
	}

	public ArrayList<Conquistas> getValues() {
		return this.values;
	}

}

