package com.example.myaudiobookplayer


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myaudiobookplayer.database.entity.Book
import com.example.myaudiobookplayer.database.repository.BookRepository

class BookListFragment  : Fragment(), BookListAdapter.ClickedItem {

    private lateinit var booklist: ArrayList<Book>
    private lateinit var rvBooks: RecyclerView
    private lateinit var bookListAdapter: BookListAdapter;
    private val viewModel: AppViewModel by activityViewModels()

    companion object {
        fun newInstance(booklist: ArrayList<Book>): BookListFragment {
            val fragmentList = BookListFragment();
            fragmentList.booklist = booklist
            return fragmentList;
        }
    }
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.frag_book_list, container, false)
        rvBooks = view.findViewById(R.id.recyclerView);
        initData();
        return view;
    }

    private fun initData(){
        bookListAdapter = BookListAdapter(this)
        rvBooks.layoutManager = LinearLayoutManager(context);
        rvBooks.setHasFixedSize(true)

        bookListAdapter = BookListAdapter(this);
        rvBooks.adapter = bookListAdapter

        if(::booklist.isInitialized) {
            bookListAdapter.setData(booklist)
        }
    }

    override fun clickedItem(book: Book) {
        viewModel.selectedBook(book)
    }


}