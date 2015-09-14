package uk.projectchronos.xplorationreader.api;

import retrofit.Call;
import retrofit.http.GET;
import uk.projectchronos.xplorationreader.model.ResponseArticlesList;

public interface ProjectChronosService {

    @GET("visualize/articles/?api=true")
    Call<ResponseArticlesList> articles();
}