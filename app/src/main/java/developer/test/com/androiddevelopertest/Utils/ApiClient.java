package developer.test.com.androiddevelopertest.Utils;


import developer.test.com.androiddevelopertest.ResponseClasses.Repos;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiClient {

    @GET("search/repositories")
    public Call<Repos> searchForRepos(@Query("q") String input,@Query("per_page") Integer per_page,@Query("page") Integer page);
}
