package bms.kotlinrxjavaretrofitrecyclerview

import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface ArticlesApiClient {

    //get list of articles as observables
    @GET("posts")
    fun getArticles(): Observable<List<Article>>

    //get one article by id
    @GET("posts/{id}")
    fun getArticle(@Path("id") id: Int): Observable<Article>

    //Add new article
    @Headers("Content-Type: application/json;charset=utf-8")
    @POST("posts")
    fun addArticle(@Body article: Article):Observable<Article>

    //creation of retrofit builder instance
    companion object {
        fun create():ArticlesApiClient{
            val retrofit = Retrofit.Builder()
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl("https://jsonplaceholder.typicode.com/")
                    .build()

            return retrofit.create(ArticlesApiClient::class.java)
        }
    }


}