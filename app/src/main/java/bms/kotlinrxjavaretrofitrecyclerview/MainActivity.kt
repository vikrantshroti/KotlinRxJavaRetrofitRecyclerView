package bms.kotlinrxjavaretrofitrecyclerview

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    //retrofit builder instance
    val client by lazy { ArticlesApiClient.create() }

    //disposable for web service consumption
    var disposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //show all articles
        showArticles()

        // show article of id 1
        //showArticle(1)

        //show article of custom data
        // val article = Article(1,101, "Test Article", "Have fun posting")
        // postArticle(article)
    }

    //get list of articles
    private fun showArticles() {
        disposable = client.getArticles()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result -> setUpRecyclerView(result) },
                        { error -> Log.e("ERROR", "" + error) }
                )
    }

    //show single article of given id
    private fun showArticle(id:Int){
        disposable = client.getArticle(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {result->Log.v("ARTICLE ID ${id}",""+result)},
                        {error->Log.e("ERROR",error.message)}
                )
    }

    //post article with model (userId,id,title,body)
    private fun postArticle(article:Article){
        disposable = client.addArticle(article)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {result->Log.v("POSTED ARTICLE","" + article)},
                        {error-> Log.e("ERROR",error.message)}
                )
    }

    //helper method for recycler view init
    fun setUpRecyclerView(articleList:List<Article>){
        articles_recycler.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        articles_recycler.layoutManager = layoutManager
        articles_recycler.adapter= ArticleAdapter(articleList){
            Log.v("Article",it.id.toString())
        }
    }

    //disposing disposable to avoid memory leak
    override fun onPause() {
        super.onPause()
        disposable?.dispose()
    }


}
