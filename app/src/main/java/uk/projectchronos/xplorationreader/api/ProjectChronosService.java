package uk.projectchronos.xplorationreader.api;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;
import uk.projectchronos.xplorationreader.model.ResponseArticlesList;

/**
 * Interface with all API call to ProjectChronos service.
 */
public interface ProjectChronosService {

    @GET("visualize/articles/?api=true")
    Call<ResponseArticlesList> getArticles(@Query("bookmark") String bookmark);
}