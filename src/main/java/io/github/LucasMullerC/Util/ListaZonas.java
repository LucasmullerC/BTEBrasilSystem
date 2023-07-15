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

import io.github.LucasMullerC.BTEBrasilSystem.Sistemas;
import io.github.LucasMullerC.Objetos.Zonas;

public class ListaZonas {
	private File storageFile;
	private ArrayList<Zonas> values;
	private Zonas G;
	Sistemas sistemas = new Sistemas();

	public ListaZonas(File file) {
		this.storageFile = file;
		this.values = new ArrayList<Zonas>();
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
				if (this.Contains(new Zonas(line)) == false) {
					switch (cont) {
						case 0:
							G = new Zonas(line);
							cont++;
							break;
						case 1:
							G.setNome(line);
							cont++;
							break;
						case 2:
							G.setOcupado(Boolean.parseBoolean(line));
							cont++;
							break;
						case 3:
							G.seta(Boolean.parseBoolean(line));
							cont++;
							break;
						case 4:
							G.setb(Boolean.parseBoolean(line));
							cont++;
							break;
						case 5:
							G.setc(Boolean.parseBoolean(line));
							cont++;
							break;
						case 7:
							G.setla(sistemas.getLocationString(line));
							cont++;
							break;
						case 8:
							G.setlb(sistemas.getLocationString(line));
							cont++;
							break;
						case 9:
							G.setlc(sistemas.getLocationString(line));
							cont++;
							break;
						case 10:
							G.setld(sistemas.getLocationString(line));
							cont++;
							values.add(G);
							cont = 0;
							break;
						case 6:
							G.setd(Boolean.parseBoolean(line));
							cont++;
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
			for (Zonas value : this.values) {
				out.write(value.getZona());
				out.newLine();
				out.write(value.getNome());
				out.newLine();
				out.write(String.valueOf(value.getOcupado()));
				out.newLine();
				out.write(String.valueOf(value.geta()));
				out.newLine();
				out.write(String.valueOf(value.getb()));
				out.newLine();
				out.write(String.valueOf(value.getc()));
				out.newLine();
				out.write(String.valueOf(value.getd()));
				out.newLine();
				out.write(sistemas.getStringLocation(value.getla()));
				out.newLine();
				out.write(sistemas.getStringLocation(value.getlb()));
				out.newLine();
				out.write(sistemas.getStringLocation(value.getlc()));
				out.newLine();
				out.write(sistemas.getStringLocation(value.getld()));
				out.newLine();
			}
			out.close();
			stream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean Contains(Zonas value) {
		return this.values.contains(value);
	}

	public void add(Zonas value) {
		if (this.Contains(value) == false) {
			this.values.add(value);
		}
	}

	public void remove(Zonas value) {
		this.values.remove(value);
	}

	public ArrayList<Zonas> getValues() {
		return this.values;
	}

}
