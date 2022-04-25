package com.example.myaudiobookplayer


import android.app.SearchManager
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.content.res.Configuration
import android.os.*
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.myaudiobookplayer.database.entity.Book
import com.example.myaudiobookplayer.database.repository.BookRepository
import edu.temple.audlibplayer.PlayerService
import java.io.File


class MainActivity : AppCompatActivity() {
    private lateinit var fm: FragmentManager;
    private lateinit var btnSearch: Button;
    private lateinit var progressBar: ProgressBar;
    private lateinit var bookList: BookList;
    private lateinit var book: Book;
    private lateinit var viewModel: AppViewModel;

    private  var playerService: PlayerService.MediaControlBinder? = null
    var serviceBound = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initDate();
        handleIntent(intent)
    }


    private fun initDate(){


        viewModel =  ViewModelProviders.of(this, AppViewModelFactory(this.application))
            .get(AppViewModel::class.java)


        btnSearch = findViewById(R.id.btnSearch)
        progressBar = findViewById(R.id.progress_bar)
        bookList =  BookList();

        clickListener()

        fm = supportFragmentManager

        bindService(
            Intent(this, PlayerService::class.java), connection,
            BIND_AUTO_CREATE
        )

        val orientation = resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_PORTRAIT){
            displayFragments(1);
        }
        else {
            displayFragments(2);
        }
        displayControlFragment(ControlFragment.newInstance())


        handleViewModel();

    }

    private fun handleViewModel(){
        val orientation = resources.configuration.orientation

        viewModel.selectedBook.observe(this) { book ->
            this.book = book
            if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                loadBookListFragment(BookDetailsFragment.newInstance(book));
            }
        }

        viewModel.searchBookMessage.observe(this){
            if(it != null){
                handleProgressBar(false)
                showMessage(it)
            }
        }

        viewModel.booksResponse.observe(this){
            handleProgressBar(false)
            setBookCollections(it)
        }

        viewModel.getGetAllBooks()!!.observe(this){
            handleProgressBar(false)
            setBookCollections(it)
        }


        viewModel.startPlay.observe(this){
            if(it != null && playerService!= null ) {
                progressBar.visibility = View.VISIBLE
                if (book.localPath != null && book.localPath != ""){
                    Log.e(" local ", " ==> "+book.localPath)
                    Log.e(" currentPosition ", " ==> "+book.currentPosition)
                    playerService!!.play(File(
                        Environment.getExternalStorageDirectory().toString() + "/Documents/"+book.localPath!!+".mp3"),book.currentPosition)
                }else{
                    Log.e(" stream ", " ==> "+book.localPath)
                    playerService!!.play(it.id)
                }
                playerService!!.setProgressHandler(handler)
            }

        }

        viewModel.stopPlaying.observe(this){
            if(it != null && playerService!= null ) {
                progressBar.visibility = View.GONE

                playerService!!.stop()
            }
        }

        viewModel.pausePlay.observe(this){
            if(it != null && playerService!= null ) {
                progressBar.visibility = View.GONE
                playerService!!.pause()
            }
        }

        viewModel.seekBarPlay.observe(this){
            if(it != null) {
                seekBar(it)
            }
        }



    }



    private var handler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            if(msg?.obj != null) {
                progressBar.visibility = View.GONE
                var bookProgress: PlayerService.BookProgress = msg.obj as PlayerService.BookProgress
                handleProgress(bookProgress)
            }
        }
    }

    private fun handleProgress( bookProgress: PlayerService.BookProgress){
        var progressPercentage = (100 * (bookProgress.progress)) / book.duration
        viewModel.setProgressBar(progressPercentage)

    }

    private fun seekBar(progress: Int){
        if(playerService != null)
            playerService!!.seekTo(progress)

    }


    private fun handleProgressBar(show: Boolean){
        if(show){
            progressBar.visibility = View.VISIBLE
        }else{
            progressBar.visibility = View.GONE

        }

    }

    private fun clickListener(){

        btnSearch.setOnClickListener {
            onSearchRequested();
        }

    }

    private fun displayControlFragment(fragment: Fragment){
        loadControlFragment(fragment);
    }

    private fun displayFragments(type: Int){

        if (type == 1) {
            loadBookListFragment(BookListFragment.newInstance(bookList.getBookArray()))
        } else {
            loadBookListFragment(BookListFragment.newInstance(bookList.getBookArray()));
            loadBookDetailFragment();
        }
    }

    private fun loadControlFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frag_control, fragment,"control")
        transaction.addToBackStack(null)
        transaction.commit()

    }

    private fun loadBookListFragment(fragment: Fragment) {
        // load fragment
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.book_lists, fragment,"list")
        transaction.addToBackStack(null)
        transaction.commit()

    }

    private fun loadBookDetailFragment() {
        // load fragment
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.book_details, BookDetailsFragment.newInstance(),"detail")
        transaction.addToBackStack(null)
        transaction.commit()

    }


    private fun setBookCollections(books: List<Book>){


        var list =  books;
        bookList.setBookArray(ArrayList(list))
        displayFragments(1);
    }

    class BookList{

        private  var bookArray: ArrayList<Book> = arrayListOf();

        fun setBookArray(bookArray: ArrayList<Book>){
            this.bookArray = bookArray
        }

        fun getBookArray():ArrayList<Book>{
            return bookArray;
        }

        fun addBook(book: Book){
            this.bookArray.toMutableList().add(book)
        }

        fun removeBook(book: Book){
            this.bookArray.toMutableList().remove(book)
        }

        fun getBook(position: Int): Book {
            return this.bookArray.toMutableList()[position]
        }


        fun getBookSize():Int{
            return this.bookArray.toMutableList().size
        }


    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleIntent(intent)
    }



    private fun handleIntent(intent: Intent) {
        if (Intent.ACTION_SEARCH == intent.action) {
            intent.getStringExtra(SearchManager.QUERY)?.also { query ->
                handleProgressBar(true)
                viewModel.searchBook(query)
            }
        }
    }

    private fun showMessage(message: String){
        Toast.makeText(this@MainActivity,message,Toast.LENGTH_LONG).show()

    }

    override fun onResume() {
        super.onResume()
        bindService(
            Intent(this, PlayerService::class.java), connection,
            BIND_AUTO_CREATE
        )
    }

    override fun onPause() {
        super.onPause()
    }

    private val connection = object: ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            playerService = (service as PlayerService.MediaControlBinder)
        }

        override fun onServiceDisconnected(arg0: ComponentName) {

        }
    }


}