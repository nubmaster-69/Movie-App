import com.hisu.movieapp.BuildConfig
import com.hisu.movieapp.model.MovieDetail
import com.hisu.movieapp.model.MoviePreviewResult
import com.hisu.movieapp.model.MoviesResponse
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap
import java.util.concurrent.TimeUnit

/**
 *https://api.themoviedb.org/3/trending/movie/day?api_key=<My_API_KEY>
 */

private val requestInterceptor = Interceptor { chain ->

    val url = chain.request().url().newBuilder()
        .addQueryParameter("api_key", BuildConfig.API_KEY)
        .build()

    val request = chain.request().newBuilder().url(url).build()

    return@Interceptor chain.proceed(request)
}

private val okHttpClient = OkHttpClient.Builder()
    .addInterceptor(requestInterceptor)
    .connectTimeout(60, TimeUnit.SECONDS).build()

private val retrofitBuilder = Retrofit.Builder()
    .baseUrl(BuildConfig.BASE_URL)
    .client(okHttpClient)
    .addConverterFactory(GsonConverterFactory.create())
    .build()

object API {
    val apiService: ApiService by lazy {
        retrofitBuilder.create(ApiService::class.java)
    }
}

interface ApiService {
    //https://api.themoviedb.org/3/movie/popular?api_key=<>&language=en-US&page=1
    @GET("movie/popular")
    suspend fun getPopularMovies(@QueryMap queries: Map<String, String>): Response<Any>

    // https://api.themoviedb.org/3/movie/555604?api_key=<>&language=en-US
    @GET("movie/{movie_id}")
    suspend fun getMovieDetail(
        @Path("movie_id") movie_id: String, @QueryMap queries: Map<String, String>
    ): Response<MovieDetail>
}