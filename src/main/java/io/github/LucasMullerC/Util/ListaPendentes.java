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

import io.github.LucasMullerC.Objetos.Pendentes;

public class ListaPendentes {
	private File storageFile;
	private ArrayList<Pendentes> values;
	private Pendentes G;

	public ListaPendentes(File file) {
		this.storageFile = file;
		this.values = new ArrayList<Pendentes>();
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
				if (this.Contains(new Pendentes(line)) == false) {
					String[] Oarea = line.split(";");
					G = new Pendentes(Oarea[0]);
					G.setArea(Oarea[1]);
					G.setApp(Boolean.parseBoolean(Oarea[2]));
					G.setBuilds(Oarea[3]);
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
			for (Pendentes value : this.values) {
				out.write(value.getUUID() + ";" + value.getArea() + ";" + String.valueOf(value.getApp()) + ";"
						+ value.getBuilds());
				out.newLine();
			}
			out.close();
			stream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean Contains(Pendentes value) {
		return this.values.contains(value);
	}

	public void add(Pendentes value) {
		if (this.Contains(value) == false) {
			this.values.add(value);
		}
	}

	public void remove(Pendentes value) {
		this.values.remove(value);
	}

	public ArrayList<Pendentes> getValues() {
		return this.values;
	}

}
