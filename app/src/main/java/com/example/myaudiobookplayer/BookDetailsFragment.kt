package com.example.myaudiobookplayer



import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.squareup.picasso.Picasso
import edu.temple.audlibplayer.PlayerService
class BookDetailsFragment : Fragment() {

    private lateinit var tvClickedBookTitle: TextView
    private lateinit var tvClickedBookAuthor: TextView
    private lateinit var ivBookCover: ImageView
    private lateinit var book:Book;
    private val viewModel: AppViewModel by activityViewModels()

    //2
    companion object {

        fun newInstance(book:Book): BookDetailsFragment {
            val fragmentDetails = BookDetailsFragment();
            fragmentDetails.book = book
            return fragmentDetails;
        }
        fun newInstance(): BookDetailsFragment {
            val fragmentDetails = BookDetailsFragment();
            return fragmentDetails;
        }
    }

    //3
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view =  inflater.inflate(R.layout.frag_book_details, container, false)
        findView(view)
        return view;
    }

    private fun findView(view: View){
        tvClickedBookTitle = view.findViewById(R.id.tvClickedBookTitle)
        tvClickedBookAuthor = view.findViewById(R.id.tvClickedBookAuthor)
        ivBookCover = view.findViewById(R.id.ivBookCover)

        initData();
    }

    private fun initData(){
        viewModel.selectedBook.observe(viewLifecycleOwner) { book ->
            setBookDate(book)
        }

        if(::book.isInitialized){
            setBookDate(book)
        }

    }

    private fun setBookDate(book: Book){
        tvClickedBookTitle.text = book.title
        tvClickedBookAuthor.text = book.author
        var bookUrl:String = book.cover_url.replace("\\/","");
        Picasso.get().load(bookUrl).into(ivBookCover);
    }


}