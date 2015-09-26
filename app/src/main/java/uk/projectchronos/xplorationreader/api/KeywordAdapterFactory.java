/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Claudio Pastorini
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package uk.projectchronos.xplorationreader.api;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import uk.projectchronos.xplorationreader.model.Keyword;

/**
 * Keyword Adapter that overrides the parsing of String[] in order to parse it into a List<Keyword>.
 */
public class KeywordAdapterFactory implements TypeAdapterFactory {

    @SuppressWarnings("unchecked")
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
        // If is a String[] parse into List<Keywords>
        if (List.class.isAssignableFrom(typeToken.getRawType())) {

            Type type = typeToken.getType();
            if (type instanceof ParameterizedType) {

                Type[] instantiated = ((ParameterizedType) type).getActualTypeArguments();
                if (Keyword.class.isAssignableFrom(TypeToken.get(instantiated[0]).getRawType())) {

                    // Parses String[] into List<Keywords>
                    return (TypeAdapter<T>) new TypeAdapter<List<Keyword>>() {
                        @Override
                        public void write(JsonWriter out, List<Keyword> value) throws IOException {
                            // TODO: maybe useful?
                        }

                        @Override
                        public List<Keyword> read(JsonReader jsonReader) throws IOException {

                            // Creates Keyword list
                            List<Keyword> keywordList = new ArrayList<>();

                            // Reads array until there is a string
                            jsonReader.beginArray();

                            while (jsonReader.hasNext()) {
                                // Reads object
                                jsonReader.beginObject();

                                String relatedUrls = null;
                                String slug = null;
                                String value = null;

                                // While has something to parse..
                                while (jsonReader.hasNext()) {
                                    switch (jsonReader.nextName()) {
                                        case "related_urls":
                                            relatedUrls = jsonReader.nextString();
                                            break;
                                        case "slug":
                                            slug = jsonReader.nextString();
                                            break;
                                        case "value":
                                            value = jsonReader.nextString();
                                            break;
                                        default:
                                            jsonReader.skipValue();
                                            break;
                                    }
                                }

                                // Create keyword and set all data parsed
                                Keyword keyword = new Keyword();
                                keyword.setRelatedUrls(relatedUrls);
                                keyword.setSlug(slug);
                                keyword.setValue(value);
                                // Add the newly created object into list
                                keywordList.add(keyword);

                                // Closes object
                                jsonReader.endObject();
                            }
                            // Closes the array
                            jsonReader.endArray();

                            return keywordList;
                        }
                    };
                }
            }
        }

        // Parse in common way
        return null;
    }
}