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

import retrofit.Response;
import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;
import uk.projectchronos.xplorationreader.models.ResponseArticlesList;
import uk.projectchronos.xplorationreader.models.ResponseIndexerList;
import uk.projectchronos.xplorationreader.models.ResponseKeywordsList;

/**
 * Interface with all API call to ProjectChronos service.
 */
public interface ProjectChronosService {
    @GET
    Observable<Response<ResponseArticlesList>> getArticles(@Query("bookmark") String bookmark);

    @GET("keywords/filterby")
    Observable<Response<ResponseKeywordsList>> getKeywords(@Query("url") String url);

    @GET("filterby")
    Observable<Response<ResponseArticlesList>> getArticlesBy(@Query("type") String type, @Query("keyword") String keyword, @Query("bookmark") String bookmark);

    @GET("indexer/")
    Observable<Response<ResponseIndexerList>> getIndexer();
}