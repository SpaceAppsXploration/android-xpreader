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

package uk.projectchronos.xplorationreader.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 *
 */
public class ResponseIndexerList {

    /**
     *
     */
    @Expose
    private List<Keyword> indexed;

    /**
     *
     */
    @Expose
    @SerializedName("n_indexed")
    private int count;

    /**
     * @return
     */
    public List<Keyword> getIndexed() {
        return indexed;
    }

    /**
     * @param indexed
     */
    public void setIndexed(List<Keyword> indexed) {
        this.indexed = indexed;
    }

    /**
     * @return
     */
    public int getCount() {
        return count;
    }

    /**
     * @param count
     */
    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "ResponseIndexerList{" +
                "indexed=" + indexed +
                ", count=" + count +
                '}';
    }
}