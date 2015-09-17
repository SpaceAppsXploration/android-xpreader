/*
 * Copyright 2014-2015 Project Chronos and Pramantha Ltd
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
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

                    // Parse String[] into List<Keywords>
                    return (TypeAdapter<T>) new TypeAdapter<List<Keyword>>() {
                        @Override
                        public void write(JsonWriter out, List<Keyword> value) throws IOException {
                            // TODO: maybe useful?
                        }

                        @Override
                        public List<Keyword> read(JsonReader jsonReader) throws IOException {
                            // Create Keyword list
                            List<Keyword> keywordList = new ArrayList<>();

                            // Read array until there is a string
                            jsonReader.beginArray();
                            while (jsonReader.hasNext()) {
                                // Get the string
                                String keywordString = jsonReader.nextString();
                                // Create and set keyword to the new Keyword Object
                                Keyword keyword = new Keyword();
                                keyword.setKeyword(keywordString);
                                // Add the newly created object into list
                                keywordList.add(keyword);
                            }
                            // Close the closure of array and return the list
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


