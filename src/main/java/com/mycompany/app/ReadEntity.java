package com.mycompany.app;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

public class ReadEntity {

    public List<HomeControllerEntry> readJsonStream(final InputStream in) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        try {
            return readEntityArray(reader);
        } finally {
            reader.close();
        }
    }

    public List<HomeControllerEntry> readEntityArray(final JsonReader reader) throws IOException {
        List<HomeControllerEntry> foggerEntity = new ArrayList<HomeControllerEntry>();

        reader.beginArray();
        while (reader.hasNext()) {
            foggerEntity.add(readEntity(reader));
        }
        reader.endArray();
        return foggerEntity;
    }

    public HomeControllerEntry readEntity(final JsonReader reader) throws IOException {
        String entity = null;
        FoggerData fields = null;
        Tags tags = null;

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("entity")) {
                entity = reader.nextString();
            } else if (name.equals("fields")) {
                fields = readFoggerData(reader);
            } else if (name.equals("tags")) {
                tags = readTags(reader);
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return null;// new HomeControllerEntry(entity, fields, tags);
    }

    public FoggerData readFoggerData(final JsonReader reader) throws IOException {

        int smpvNum = -1;
        int stringNum = -1;
        Measurements measurements = null;

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("SMPV_num")) {
                smpvNum = reader.nextInt();
            } else if (name.equals("String_num")) {
                stringNum = reader.nextInt();
            } else if (name.equals("measurements")) {
                measurements = readMeasurements(reader);
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return new Smpv(smpvNum, stringNum, measurements);
    }

    public Tags readTags(final JsonReader reader) throws IOException {

        String type = null;

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("type")) {
                type = reader.nextString();
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();

        return new Tags(type);
    }

    public Measurements readMeasurements(final JsonReader reader) throws IOException {

        float E = -1f;
        CmpVal Ich = null;
        CmpVal Kch = null;
        CmpVal Pch = null;
        CmpVal Temp = null;
        CmpVal Uch = null;

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("E")) {
                E = (float) reader.nextDouble();
            } else if (name.equals("Ich")) {
                Ich = readCmpVal(reader);
            } else if (name.equals("Kch")) {
                Kch = readCmpVal(reader);
            } else if (name.equals("Pch")) {
                Pch = readCmpVal(reader);
            } else if (name.equals("Temp")) {
                Temp = readCmpVal(reader);
            } else if (name.equals("Uch")) {
                Uch = readCmpVal(reader);
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return new Measurements(E, Ich, Kch, Pch, Temp, Uch);
    }

    public CmpVal readCmpVal(final JsonReader reader) throws IOException {
        float v = -1f;
        int q = -1;

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("v")) {
                v = (float) reader.nextDouble();
            } else if (name.equals("q")) {
                q = reader.nextInt();
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();

        return new CmpVal(v, q);
    }

    public String serialize(final List<HomeControllerEntry> foggerEntityList) {
        Gson gson = new Gson();
        return gson.toJson(foggerEntityList);
    }

}
