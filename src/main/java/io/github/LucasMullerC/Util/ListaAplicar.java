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

import io.github.LucasMullerC.Objetos.Aplicantes;

public class ListaAplicar {
	private File storageFile;
	private ArrayList<Aplicantes> values;
	private Aplicantes G;

	public ListaAplicar(File file) {
		this.storageFile = file;
		this.values = new ArrayList<Aplicantes>();
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
				if (this.Contains(new Aplicantes(line)) == false) {
					switch (cont) {
						case 0:
							G = new Aplicantes(line);
							cont++;
							break;
						case 1:
							G.setDiscord(line);
							cont++;
							break;
						case 2:
							G.setZona(line);
							cont++;
							break;
						case 3:
							G.setSeccao(line);
							cont++;
							break;
						case 4:
							G.setDeadline(line);
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
			for (Aplicantes value : this.values) {
				out.write(value.getUUID());
				out.newLine();
				out.write(String.valueOf(value.getDiscord()));
				out.newLine();
				out.write(String.valueOf(value.getZona()));
				out.newLine();
				out.write(value.getSeccao());
				out.newLine();
				out.write(value.getDeadLine());
				out.newLine();
			}
			out.close();
			stream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean Contains(Aplicantes value) {
		return this.values.contains(value);
	}

	public void add(Aplicantes value) {
		if (this.Contains(value) == false) {
			this.values.add(value);
		}
	}

	public void remove(Aplicantes value) {
		this.values.remove(value);
	}

	public ArrayList<Aplicantes> getValues() {
		return this.values;
	}

}
