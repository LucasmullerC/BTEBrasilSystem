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

import io.github.LucasMullerC.Objetos.Jogadores;

public class ListaJogadores {
	private File storageFile;
	private ArrayList<Jogadores> values;
	private Jogadores G;

	public ListaJogadores(File file) {
		this.storageFile = file;
		this.values = new ArrayList<Jogadores>();
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
			int cont = 0;
			while ((line = reader.readLine()) != null) {
				if (this.Contains(new Jogadores(line)) == false) {
					switch (cont) {
					case 0:
						G = new Jogadores(line);
						cont++;
						break;
					case 1:
						G.setTime(line);
						cont++;
						break;
					case 2:
						G.setDiscord(line);
						cont++;
						break;
					case 3:
						G.setCargo(Boolean.parseBoolean(line));
						cont++;
						values.add(G);
						cont = 0;
						break;
					default:
						break;
					}
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
			for (Jogadores value : this.values) {
				out.write(value.getUUID());
				out.newLine();
				out.write(value.getTime());
				out.newLine();
				out.write(String.valueOf(value.getDiscord()));
				out.newLine();
				out.write(String.valueOf(value.getCargo()));
				out.newLine();
			}
			out.close();
			stream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean Contains(Jogadores value) {
		return this.values.contains(value);
	}

	public void add(Jogadores value) {
		if (this.Contains(value) == false) {
			this.values.add(value);
		}
	}

	public void remove(Jogadores value) {
		this.values.remove(value);
	}

	public ArrayList<Jogadores> getValues() {
		return this.values;
	}

}
