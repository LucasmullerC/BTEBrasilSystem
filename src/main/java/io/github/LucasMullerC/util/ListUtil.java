package io.github.LucasMullerC.util;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public class ListUtil<T> {
    private File storageFile;
    private ArrayList<T> values;

    public ListUtil(File file) {
        this.storageFile = file;
        this.values = new ArrayList<>();
        if (!this.storageFile.exists()) {
            try {
                this.storageFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void load(Class<T> clazz) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(this.storageFile));
            String line;
            Field[] fields = clazz.getDeclaredFields();
            T obj = null;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                for (int i = 0; i < parts.length && i < fields.length; i++) {
                    Field field = fields[i];
                    field.setAccessible(true);

                    if(i == 0){
                        obj = clazz.getDeclaredConstructor().newInstance(line);
                    }
                    if(obj != null){
                        if (field.getType() == String.class) {
                            field.set(obj, parts[i]);
                        } else if (field.getType() == int.class) {
                            field.set(obj, Integer.parseInt(parts[i]));
                        } else if (field.getType() == boolean.class) {
                            field.set(obj, Boolean.parseBoolean(parts[i]));
                        }
                    }
                }
                values.add(obj);
            }
            reader.close();
        } catch (IOException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    public void save() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(this.storageFile));
            for (T value : this.values) {
                for (Field field : value.getClass().getDeclaredFields()) {
                    field.setAccessible(true);
                    writer.write(field.get(value).toString());
                    writer.write(";");
                }
                writer.newLine();
            }
            writer.close();
        } catch (IOException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public boolean contains(T value) {
        return this.values.contains(value);
    }

    public void add(T value) {
        if (!this.contains(value)) {
            this.values.add(value);
        }
    }

    public void remove(T value) {
        this.values.remove(value);
    }

    public ArrayList<T> getValues() {
        return this.values;
    }
}
